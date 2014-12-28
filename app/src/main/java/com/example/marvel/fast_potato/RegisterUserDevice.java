package com.example.marvel.fast_potato;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author Sriduth Jayhari
 * This class provides functions to register the user.
 */
public class RegisterUserDevice {
    public static final String REGISTER_SUCCESS = "REGISTRATION_SUCCEEDED";

    static class GetOrganizations extends AsyncTask<String, String , Map<String, String> > {

        private final String apiUrl = "https://droid-api.herokuapp.com/getAllOrg";
        @Override
        protected Map<String, String> doInBackground(String... strings) {
            HttpClient thisDevice = new DefaultHttpClient();
            HttpGet req = new HttpGet(apiUrl);

            Map<String, String> data = new TreeMap<String, String>();

            try {
                String jsonAsString = EntityUtils.toString(thisDevice.execute(req).getEntity());
                JSONObject jsonData = new JSONObject(jsonAsString);

                Iterator it = jsonData.keys();

                while(it.hasNext()){
                    String key = (String) it.next();
                    data.put(jsonData.getString(key), key);
                }
            }

            catch (Exception e){
                e.printStackTrace();
            }
            return data;
        }
    }

    static class RegisterDevice extends AsyncTask<String, String, Map<String, String> > {

        private final String registerApiUrl = "https://droid-api.herokuapp.com/registerDevice";

        @Override
        protected Map<String, String> doInBackground(String... data) {

            Map<String, String> regData = new HashMap<String, String>();

            HttpClient myDevice = new DefaultHttpClient();
            HttpPost request = new HttpPost(registerApiUrl);

            List<NameValuePair> postData = new ArrayList<NameValuePair>();
            String uniqueHash = UUID.randomUUID().toString();

            Log.d("--reg", data[0]);
            Log.d("--reg", data[1]);
            Log.d("--reg", data[2]);

            postData.add(new BasicNameValuePair("DEVICE_ID", uniqueHash));
            postData.add(new BasicNameValuePair("CLIENT_ID", data[0]));
            postData.add(new BasicNameValuePair("F_NAME", data[1]));
            postData.add(new BasicNameValuePair("L_NAME", data[2]));

            try {
                request.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
                String jsonAsString = EntityUtils.toString(myDevice.execute(request).getEntity());

                JSONObject jsonData = new JSONObject(jsonAsString);

                regData.put("STATUS", jsonData.getString("STATUS"));
                regData.put("API_KEY", jsonData.getString("API_SECRET"));
                regData.put("USER_ID", uniqueHash);
                regData.put("CLIENT_ID", data[0]);
                regData.put("FIRST_NAME", data[1]);
                regData.put("LAST_NAME", data[2]);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RegisterUserDevice : ", e.getStackTrace().toString());
            }
            return regData;
        }
    }
}