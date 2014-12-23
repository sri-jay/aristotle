package com.example.marvel.fast_potato;

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
import java.util.UUID;

/**
 * @author Sriduth Jayhari
 * This class provides functions to register the user.
 */
public class RegisterUserDevice extends AsyncTask<String, String, String[]> {

    public static final String REGISTER_SUCCESS = "REGISTRATION_SUCCEEDED";
    private final String registerApiUrl = "https://droid-api.herokuapp.com/registerDevice";

    @Override
    protected String[] doInBackground(String... data) {

        String[] returnData = new String[2];
        HttpClient myDevice = new DefaultHttpClient();
        HttpPost request = new HttpPost(registerApiUrl);

        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        String uniqueHash = UUID.randomUUID().toString();

        postData.add(
                new BasicNameValuePair(
                        "random_hash",
                        uniqueHash
                )
        );

        try {
            request.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
            String jsonAsString = EntityUtils.toString(myDevice.execute(request).getEntity());

            JSONObject jsonData= new JSONObject(jsonAsString);
            System.out.println(jsonData.getString("STATUS"));
            System.out.println(jsonData.getString("API_SECRET"));
            returnData[0] = jsonData.getString("STATUS");
            returnData[1] = jsonData.getString("API_SECRET");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("RegisterUserDevice : ",e.getStackTrace().toString());
        }
        return returnData;
    }
}
