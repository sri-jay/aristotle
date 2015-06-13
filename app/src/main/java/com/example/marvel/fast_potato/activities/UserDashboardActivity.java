package com.example.marvel.fast_potato.activities;

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

import com.example.marvel.fast_potato.database.EulerDB;
import com.example.marvel.fast_potato.knowledge.Knowledge;
import com.example.marvel.fast_potato.R;

import java.util.Map;

public class UserDashboardActivity extends Activity {

    private TextView welcomeMessage = null;
    private TextView apiKeyView = null;
    private TextView deviceKeyView  = null;

    private Typeface tf = null;
    private Button startQuiz = null;
    private Button registerCourse = null;

    private Knowledge quiz = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_dashboard);

        bindViews();
        loadViews();

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewQuiz();
            }
        });
        registerCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistrationDash();
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
        Intent quizIntent = new Intent(getApplicationContext(), LearningActivity.class);
        startActivity(quizIntent);
        //finish();
    }

    void openRegistrationDash() {
        Intent intent = new Intent(getApplicationContext(), RegisterCourseActivity.class);
        startActivity(intent);
    }

    void bindViews() {
        startQuiz = (Button) findViewById(R.id.takeQuiz);
        registerCourse = (Button) findViewById(R.id.regisiter_for_courses);

        welcomeMessage = (TextView) findViewById(R.id.userDashWelcome);
        tf = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        welcomeMessage.setTypeface(tf);

        apiKeyView = (TextView) findViewById(R.id.apiKeyView);
        deviceKeyView = (TextView) findViewById(R.id.deviceKeyView);
    }

    void loadViews() {
        EulerDB db = new EulerDB(getApplicationContext());
        Map<String, String[]> userData = db.getUserData();

        welcomeMessage.setText("Welcome, "+userData.get("name")[0]);
        apiKeyView.setText(userData.get("keyData")[0]);
        deviceKeyView.setText(userData.get("keyData")[1]);
    }
}
