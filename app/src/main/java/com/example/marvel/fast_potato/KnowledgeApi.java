package com.example.marvel.fast_potato;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sriduth_2 on 20-12-2014.
 * Fetches questions from the database.
 */

public class KnowledgeApi {
    static class GetKnowledge extends AsyncTask<String, String, Knowledge> {

        private ProgressDialog pd = null;
        private LearningActivity activityContext = null;

        private final String apiUrl = "https://droid-api.herokuapp.com/getNextItemInPath";

        GetKnowledge(Activity callerActivityContext) {
            activityContext = (LearningActivity) callerActivityContext;
            pd = new ProgressDialog(callerActivityContext);
            pd.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Loading..");
            pd.show();
        }

        @Override
        protected Knowledge doInBackground(String... string) {

            publishProgress("Starting download.");
            HttpClient myDevice = new DefaultHttpClient();
            HttpPost request = new HttpPost(apiUrl);

            List<NameValuePair> postData = new ArrayList<NameValuePair>();

            Map<String, String[]> deviceData = new EulerDB(activityContext).getUserData();
            postData.add(new BasicNameValuePair("DEVICE_ID", deviceData.get("keyData")[1]));

            Knowledge knowledge = null;
            try {
                request.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
                publishProgress("Downloading...");
                String jsonData = EntityUtils.toString(myDevice.execute(request).getEntity());

                Log.d("JSON DATA : ", jsonData);
                JSONObject json = new JSONObject(jsonData);
                publishProgress("Finished Download");
                String id = json.getString("ID");
                String progress = json.getString("PATH_PROGRESS");

                if (json.getString("TYPE").equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
                    String statement = json.getString("STATEMENT");
                    String[] qoptions = {json.getString("OPTION_A"), json.getString("OPTION_B"), json.getString("OPTION_C")};
                    knowledge = new QuestionUnit(id, statement, null, qoptions, progress);
                } else if (json.getString("TYPE").equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT)) {
                    String title = json.getString("TITLE");
                    String content = json.getString("CONTENT");
                    knowledge = new KnowledgeUnit(id, content, title, progress);
                }

                new EulerDB(activityContext).updateSequence(json.getString("SEQUENCE"));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return knowledge;
        }

        @Override
        protected void onPostExecute(Knowledge knowledge) {
            activityContext.knowledge = knowledge;
            pd.setMessage("Action Type : " + knowledge.getKnowledgeType());
            activityContext.setActivityMode();
            activityContext.updateViews();
            pd.setMessage("Loading Views..");
            pd.hide();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pd.setMessage(values[0]);
        }
    }

    static class SaveProgress extends AsyncTask<String, String, String> {

        private final String apiUrl = "https://droid-api.herokuapp.com/recordResponse";
        private LearningActivity activityContext = null;
        private Knowledge knowledge = null;

        SaveProgress(LearningActivity activity) {
            activityContext = activity;
            knowledge = activityContext.knowledge;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(apiUrl);

            List<NameValuePair> postData = new ArrayList<NameValuePair>();
            Map<String, String[]> deviceData = new EulerDB(activityContext).getUserData();

            postData.add(new BasicNameValuePair("RESPONSE_FOR", knowledge.getKnowledgeType()));
            postData.add(new BasicNameValuePair("DEVICE_ID", deviceData.get("keyData")[1]));
            postData.add(new BasicNameValuePair("COURSE_ID", "GK214"));
            postData.add(new BasicNameValuePair("RESPONSE", "TEST_RESPONSE"));
            postData.add(new BasicNameValuePair("SEQUENCE", new EulerDB(activityContext).getSequence()));

            Log.d("", knowledge.getKnowledgeType());
            Log.d("", deviceData.get("keyData")[1]);
            Log.d("", "GK14");
            Log.d("", "TEST");
            Log.d("", new EulerDB(activityContext).getSequence());
            try {
                post.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
                client.execute(post).getEntity();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}


