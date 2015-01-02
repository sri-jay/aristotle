package com.example.marvel.fast_potato;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Activity {

    private Spinner orgSpinner = null;
    private EditText firstName = null;
    private EditText lastName = null;
    private EditText phoneNum = null;

    private Button doRegister = null;

    private Map<String, String> orgData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        bindViews();
        setOnClickListeners();

        try {
            orgData = new RegisterUserDevice.GetOrganizations().execute().get();
            List<String> orgNames = new ArrayList<String>();

            for(Map.Entry<String, String> ent : orgData.entrySet()) {
                orgNames.add(ent.getKey());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, orgNames);
            orgSpinner.setAdapter(spinnerAdapter);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bindViews() {
        orgSpinner = (Spinner) findViewById(R.id.orgSelect);
        firstName = (EditText) findViewById(R.id.fNameField);
        lastName = (EditText) findViewById(R.id.lNameField);
        phoneNum = (EditText) findViewById(R.id.pNumField);
        doRegister = (Button) findViewById(R.id.registerUserButton);

        TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        if(mPhoneNumber != null)
            phoneNum.setText(mPhoneNumber);
    }

    public void setOnClickListeners() {
        doRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    public void registerUser() {
        try {
            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String clientId = orgData.get(orgSpinner.getSelectedItem().toString());
            String phoneNumber = phoneNum.getText().toString();

            new RegisterUserDevice.RegisterDevice(this).execute(clientId, fName, lName, phoneNumber);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
