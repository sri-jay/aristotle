package com.example.marvel.fast_potato.knowledge;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.marvel.fast_potato.activities.LearningActivity;
import com.example.marvel.fast_potato.activities.RegisterCourseActivity;
import com.example.marvel.fast_potato.database.EulerDB;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.sql.Timestamp;
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
    public static class GetKnowledge extends AsyncTask<String, String, Knowledge> {

        private ProgressDialog pd = null;
        private LearningActivity activityContext = null;

        private final String apiUrl = "http://localhost:8080/aristotle/v1/data/getNextItemInPath";

        private boolean save = false;

        public GetKnowledge(LearningActivity callerActivityContext, boolean chainWIthSave) {
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

            Knowledge knowledge = null;
            HttpClient myDevice = new DefaultHttpClient();
            HttpPost request = new HttpPost(apiUrl);

            Map<String, String[]> deviceData = new EulerDB(activityContext).getUserData();
            JSONObject payload = new JSONObject();

            try {
                payload.put("deviceId", deviceData.get("keyData")[1]);
                payload.put("courseId", "GK214");

                StringEntity se = new StringEntity(payload.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);

                publishProgress("Downloading...");
                String jsonData = EntityUtils.toString(myDevice.execute(request).getEntity());

                Log.d("JSON DATA : ", jsonData);
                JSONObject json = new JSONObject(jsonData);
                publishProgress("Finished Download");
                json = json.getJSONObject("payload");
                String id = json.getString("id");
                String progress = "12";


                if (json.getString("type").equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
                    String statement = json.getString("statement");
                    String[] qoptions = {json.getString("optionA"), json.getString("optionB"), json.getString("optionC")};
                    if(json.getString("mediaType").equals("image"))
                        knowledge = new QuestionUnit(id, statement, null, qoptions, progress, BitmapFactory.decodeStream(new java.net.URL(json.getString("mediaUrl")).openStream()), "image");
                    else
                        knowledge = new QuestionUnit(id, statement, null, qoptions, progress, json.getString("mediaUrl"), "video");
                }

                else if (json.getString("type").equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT)) {
                    String title = json.getString("title");
                    String content = json.getString("content");
                    if(json.getString("mediaType").equals("image"))
                        knowledge = new KnowledgeUnit(id, content, title, progress, BitmapFactory.decodeStream(new java.net.URL(json.getString("mediaUrl")).openStream()), "image");
                    else
                        knowledge = new KnowledgeUnit(id, content, title, progress, json.get("mediaUrl"), "video");
                }

                new EulerDB(activityContext).updateSequence(json.getString("sequence"));

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

    public static class SaveProgress extends AsyncTask<String, String, String> {

        private final String apiUrl = "http://localhost:8080/aristotle/v1/data/recordResponse";

        private ProgressDialog pd = null;
        private LearningActivity activityContext = null;

        private Knowledge knowledge = null;

        public SaveProgress(LearningActivity activity) {
            activityContext = activity;
            knowledge = activityContext.knowledge;
            pd = new ProgressDialog(activityContext);
            pd.setCanceledOnTouchOutside(true);
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

            Map<String, String[]> deviceData = new EulerDB(activityContext).getUserData();

            JSONObject payload = new JSONObject();
            try {
                payload.put("responseFor", knowledge.getKnowledgeType());
                payload.put("id", knowledge.getUniqueHash());
                payload.put("deviceId", deviceData.get("keyData")[1]);
                payload.put("courseId", "GK214");
                payload.put("response", strings[0]);
                payload.put("sequence", new EulerDB(activityContext).getSequence());
                payload.put("timestamp", new DateTime().toString());

                StringEntity se = new StringEntity(payload.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                post.setEntity(se);
                String i = EntityUtils.toString(client.execute(post).getEntity());

                System.out.println(i);
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

    public static class GetAllCourses extends AsyncTask<String, String, String[]>{
        private final String apiUrl = "http://localhost:8080/aristotle/v1/data/getAllCourses";
        private ProgressDialog pd = null;

        private RegisterCourseActivity activity = null;

        public GetAllCourses(RegisterCourseActivity caller){
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
            postData.add(new BasicNameValuePair("deviceId", deviceData.get("keyData")[1]));

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


