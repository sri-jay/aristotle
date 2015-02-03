package com.example.marvel.fast_potato;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Sriduth_2 on 20-12-2014.
 * Fetches questions from the database.
 */

//Has 2 classes:
//  * One to get Information from the api,
//  * One to submit information to the api.
//
public class KnowledgeApi {
    static class GetKnowledge extends AsyncTask<String, String, Knowledge> {

        private ProgressDialog pd = null;
        private LearningActivity activityContext = null;

        private final String apiUrl = "https://droid-api-staging.herokuapp.com/getNextItemInPath";

        private boolean save = false;

        GetKnowledge(LearningActivity callerActivityContext, boolean chainWIthSave) {
            activityContext = (LearningActivity) callerActivityContext;
            pd = new ProgressDialog(callerActivityContext);
            pd.setCanceledOnTouchOutside(false);
            save = chainWIthSave;
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
            postData.add(new BasicNameValuePair("COURSE_ID", "GK214"));
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
                    if(json.getString("MEDIA_TYPE").equals("image"))
                        knowledge = new QuestionUnit(id, statement, null, qoptions, progress, BitmapFactory.decodeStream(new java.net.URL(json.getString("MEDIA_URL")).openStream()), "image");
                    else
                        knowledge = new QuestionUnit(id, statement, null, qoptions, progress, json.getString("MEDIA_URL"), "video");
                }

                else if (json.getString("TYPE").equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT)) {
                    String title = json.getString("TITLE");
                    String content = json.getString("CONTENT");
                    if(json.getString("MEDIA_TYPE").equals("image"))
                        knowledge = new KnowledgeUnit(id, content, title, progress, BitmapFactory.decodeStream(new java.net.URL(json.getString("MEDIA_URL")).openStream()), "image");
                    else
                        knowledge = new KnowledgeUnit(id, content, title, progress, json.get("MEDIA_URL"), "video");
                }

                new EulerDB(activityContext).updateSequence(json.getString("SEQUENCE"));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return knowledge;
        }

        @Override
        protected void onPostExecute(Knowledge knowledge) {
            try {
                if(knowledge == null)
                    System.out.println("Null!");

                activityContext.knowledge = knowledge;
                pd.setMessage("Action Type : " + knowledge.getKnowledgeType());
                activityContext.setActivityMode();
                activityContext.updateViews();
                activityContext.setProgress();
                activityContext.showPopup();

                pd.setMessage("Loading Views..");
                pd.hide();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pd.setMessage(values[0]);
        }
    }

    static class SaveProgress extends AsyncTask<String, String, String> {

        private final String apiUrl = "https://droid-api-staging.herokuapp.com/recordResponse";

        private ProgressDialog pd = null;
        private LearningActivity activityContext = null;

        private Knowledge knowledge = null;

        SaveProgress(LearningActivity activity) {
            activityContext = activity;
            knowledge = activityContext.knowledge;
            pd = new ProgressDialog(activityContext);
            pd.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Saving Progress");
            pd.show();
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
            postData.add(new BasicNameValuePair("RESPONSE", strings[0]));
            postData.add(new BasicNameValuePair("SEQUENCE", new EulerDB(activityContext).getSequence()));
            postData.add(new BasicNameValuePair("DURATION", knowledge.getTelemetrics().getDuration()));

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
            pd.setMessage("Done!");
            pd.dismiss();
        }
    }

    static class GetAllCourses extends AsyncTask<String, String, String[]>{
        private final String apiUrl = "https://droid-api-staging.herokuapp.com/getAllCourses";
        private ProgressDialog pd = null;

        private RegisterCourse activity = null;

        GetAllCourses(RegisterCourse caller){
            activity =  caller;
            pd = new ProgressDialog(activity);
            pd.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading Courses");
            pd.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            HttpClient device = new DefaultHttpClient();
            HttpPost request = new HttpPost(apiUrl);

            Map<String, String[]> deviceData = new EulerDB(activity).getUserData();
            List<NameValuePair> postData = new ArrayList<NameValuePair>();
            postData.add(new BasicNameValuePair("DEVICE_KEY", deviceData.get("keyData")[1]));

            String[] data = null;

            try {
                request.setEntity(new UrlEncodedFormEntity(postData,"UTF-8"));
                String jsonAsString = EntityUtils.toString(device.execute(request).getEntity());
                JSONObject json = new JSONObject(jsonAsString);

                data = new String[json.length()];
                Log.d("JSON STRING: ", jsonAsString);

                Iterator<String> keys = json.keys();

                for(int i=0;keys.hasNext();i++){
                    String key = (String) keys.next();
                    data[i] = key+";"+json.getString(key);
                }
            }

            catch (Exception e){
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String[] data) {
            super.onPostExecute(data);
            activity.loadCourses(activity, data);
            pd.dismiss();
        }
    }
}


