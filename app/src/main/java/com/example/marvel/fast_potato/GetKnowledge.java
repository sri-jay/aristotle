package com.example.marvel.fast_potato;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Sriduth_2 on 20-12-2014.
 * Fetches questions from the database.
 */
public class GetKnowledge extends AsyncTask<String, String, Knowledge>  {

    private static ProgressDialog pd = null;
    private LearningActivity activityContext = null;

    private final String apiUrl = "https://droid-api.herokuapp.com/initialAssessment";

    GetKnowledge(Activity callerActivityContext) {
        activityContext =(LearningActivity) callerActivityContext;
        pd = new ProgressDialog(callerActivityContext);

    }

    @Override
    protected  void onPreExecute(){
        pd.setMessage("Loading..");
        pd.show();
    }
    @Override
    protected Knowledge doInBackground(String... string) {

        publishProgress("Starting download.");
        HttpClient myDevice = new DefaultHttpClient();
        HttpPost request = new HttpPost(apiUrl);

        Knowledge knowledge = null;
        try{
            publishProgress("Downloading...");
            String jsonData = EntityUtils.toString(myDevice.execute(request).getEntity());
            JSONObject json = new JSONObject(jsonData);
            publishProgress("Finished Download");
            String id = json.getString("ID");
            String progress = json.getString("PATH_PROGRESS");

            if(json.getString("TYPE").equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
                String statement = json.getString("STATEMENT");
                String[] qoptions = {json.getString("OPTION_A"),json.getString("OPTION_B"),json.getString("OPTION_C")};
                knowledge = new QuestionUnit(id, statement, null, qoptions, progress);
            }
            else if(json.getString("TYPE").equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT)){
                String title = json.getString("TITLE");
                String content = json.getString("CONTENT");
                knowledge = new KnowledgeUnit(id, content, title, progress);
            }
        }

        catch(Exception e){
            e.printStackTrace();
        }

        return knowledge;
    }

    @Override
    protected void onPostExecute(Knowledge knowledge) {
        activityContext.setActivityMode(knowledge);
        pd.setMessage("Action Type : "+ knowledge.getKnowledgeType());
        activityContext.updateViews(knowledge);
        pd.setMessage("Loading Views..");
        pd.hide();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        pd.setMessage(values[0]);
    }
}
