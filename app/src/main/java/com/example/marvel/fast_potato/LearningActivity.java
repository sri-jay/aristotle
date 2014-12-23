package com.example.marvel.fast_potato;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class LearningActivity extends FragmentActivity {

    private TextView knAdvert = null;
    private ProgressBar pathProgress = null;

    private TextView knTitle = null;
    private TextView knContent = null;
    private RadioGroup ansGroup= null;
    private RadioButton[] options = null;

    private Button getNextTask = null;

    private Knowledge k = null;

    FragmentTransaction fragmentLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the view
        setContentView(R.layout.activity_learning);
        bindViewsToVariables();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.learning, menu);
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

    public void bindViewsToVariables() {
        knAdvert = (TextView) findViewById(R.id.knowledgeAdvert);
        pathProgress = (ProgressBar) findViewById(R.id.pathProgress);

        knTitle = (TextView) findViewById(R.id.knowledgeTitle);
        knContent = (TextView) findViewById(R.id.knowledgeContent);

        ansGroup = (RadioGroup) findViewById(R.id.ansGroup);

        options = new RadioButton[7];

        options[0] = (RadioButton) findViewById(R.id.option_a);
        options[1] = (RadioButton) findViewById(R.id.option_b);
        options[2] = (RadioButton) findViewById(R.id.option_c);
        options[3] = (RadioButton) findViewById(R.id.option_d);
        options[4] = (RadioButton) findViewById(R.id.option_e);
        options[5] = (RadioButton) findViewById(R.id.option_f);
        options[6] = (RadioButton) findViewById(R.id.option_g);

        getNextTask = (Button) findViewById(R.id.getNextTask);
    }

    public void setOnClickListeners() {
        getNextTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    k = new GetKnowledge().execute().get();
                    updateViews();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateViews() {
        if(k.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT) ) {

        }
        else if(k.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {

        }
    }
}
