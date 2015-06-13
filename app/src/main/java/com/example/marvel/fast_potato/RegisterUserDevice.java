package com.example.marvel.fast_potato;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.marvel.fast_potato.activities.UserDashboardActivity;
import com.example.marvel.fast_potato.database.EulerDB;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author Sriduth Jayhari
 * This class provides functions to register the user.
 */
public class RegisterUserDevice {

    public static class GetOrganizations extends AsyncTask<String, String , Map<String, String> > {

        private final String apiUrl = "http://localhost:8080/aristotle/v1/data/getAllOrg";
        @Override
        protected Map<String, String> doInBackground(String... strings) {
            HttpClient thisDevice = new DefaultHttpClient();
            HttpGet req = new HttpGet(apiUrl);

            Map<String, String> data = new TreeMap<String, String>();

            try {
                String jsonAsString = EntityUtils.toString(thisDevice.execute(req).getEntity());
                JSONObject jsonData = new JSONObject(jsonAsString);
                JSONArray org = jsonData.getJSONArray("payload");
                Iterator it = jsonData.keys();

                for(int i=0;i<org.length();i++) {
                    JSONObject ob = org.getJSONObject(i);
                    data.put(ob.getString("clientName"), ob.getString("clientId"));
                }
            }

            catch (Exception e){
                e.printStackTrace();
            }
            return data;
        }
    }

    public static class RegisterDevice extends AsyncTask<String, String, Map<String, String> > {

        private final String registerApiUrl = "http://localhost:8080/aristotle/v1/data/registerDevice";
        private Activity callerActivity = null;
        private ProgressDialog pd = null;

        public RegisterDevice(Activity caller){
            callerActivity = caller;
            pd = new ProgressDialog(callerActivity);
            pd.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Processing..");
            pd.show();
        }

        @Override
        protected Map<String, String> doInBackground(String... data) {

            Map<String, String> regData = new HashMap<String, String>();

            HttpClient myDevice = new DefaultHttpClient();
            HttpPost request = new HttpPost(registerApiUrl);
            JSONObject payload = new JSONObject();
            String uniqueHash = UUID.randomUUID().toString();

            try {
                payload.put("deviceCode", uniqueHash);
                payload.put("clientId", data[0]);
                payload.put("firstName", data[1]);
                payload.put("lastName", data[2]);
                payload.put("phoneNumber", data[3]);

                StringEntity se = new StringEntity(payload.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);
                String jsonAsString = EntityUtils.toString(myDevice.execute(request).getEntity());

                JSONObject jsonData = new JSONObject(jsonAsString);

                regData.put("status", jsonData.getString("status"));
                regData.put("apiKey", jsonData.getString("payload"));
                regData.put("userId", uniqueHash);
                regData.put("clientId", data[0]);
                regData.put("firstName", data[1]);
                regData.put("lastName", data[2]);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RegisterUserDevice : ", e.getStackTrace().toString());
            }
            return regData;
        }

        @Override
        protected void onPostExecute(Map<String, String> regData) {
            super.onPostExecute(regData);

            pd.dismiss();

            if(regData.get("status").equals("ok")) {
                createLocalDatabase(regData.get("apiKey"), regData.get("userId"), regData.get("clientId"));
                saveUserData(regData.get("firstName"), regData.get("lastName"));

                Intent intent = new Intent(callerActivity, UserDashboardActivity.class);

                /*
                    Registration is done, open Dash and show welcome to user.
                * */
                callerActivity.startActivity(intent);
                callerActivity.finish();
            }
        }

        public void createLocalDatabase(String apiKey, String deviceKey, String userOrg) {
            EulerDB db = new EulerDB(callerActivity);
            db.initAndSaveApiData(apiKey, deviceKey, userOrg);
        }

        public void saveUserData(String firstName, String lastName) {
            EulerDB db = new EulerDB(callerActivity);
            db.saveUserData(firstName, lastName);
        }
    }
}