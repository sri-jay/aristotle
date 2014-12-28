package com.example.marvel.fast_potato;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class HomeActivity extends Activity {

    private Typeface tf = null;
    private TextView registerAdvert = null;
    private Button homePageAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        tf = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");

        registerAdvert = (TextView) findViewById(R.id.register_advert);
        registerAdvert.setTypeface(tf);

        homePageAction = (Button) findViewById(R.id.registerButton);
        Log.d("HomeActivity", "Checking if device is registered");
        homePageAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File database = getApplicationContext().getDatabasePath("eulerApplicationDatabase.db");
                if(!database.exists()) {
                    registerDevice();
                }
                else {
                    openDash();
                }
            }
        });
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

    // This function opens the user dashboard
    public void openDash() {
        //Intent intent = new Intent(this, UserDashboardActivity.class);
        Intent intent = new Intent(this, LearningActivity.class);
        startActivity(intent);
    }

    public void registerDevice() {
        Intent regIntent = new Intent(this, RegisterActivity.class);
        startActivity(regIntent);
    }
}
