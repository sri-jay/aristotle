package com.example.marvel.fast_potato;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UserDashboardActivity extends Activity {

    TextView welcomeMessage = null;

    Typeface tf = null;
    Button startQuiz = null;

    Knowledge quiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_dashboard);
        startQuiz = (Button) findViewById(R.id.takeQuiz);

        welcomeMessage = (TextView) findViewById(R.id.userDashWelcome);
        tf = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");

        welcomeMessage.setTypeface(tf);

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewQuiz();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_dashboard, menu);
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

    void startNewQuiz() {
        Intent quizIntent = new Intent(getApplicationContext(), null);
        startActivity(quizIntent);
        finish();
    }
}
