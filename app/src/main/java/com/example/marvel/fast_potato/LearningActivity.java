package com.example.marvel.fast_potato;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import android.widget.Toast;

public class LearningActivity extends Activity {

    private final static boolean QUIZ_MODE = true;
    private final static boolean UNIT_MODE = false;

    private Typeface tf = null;

    private TextView knAdvert = null;
    private ProgressBar pathProgress = null;

    private TextView knTitle = null;
    private TextView knContent = null;
    private RadioGroup ansGroup= null;
    private RadioButton[] options = null;

    private Button getNextTask = null;

    private boolean activityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the view
        setContentView(R.layout.activity_learning);
        bindViewsToVariables();
        setOnClickListeners();
        init();

        // Set styles
        tf = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        knAdvert.setTypeface(tf);
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
        knContent.setMovementMethod(new ScrollingMovementMethod());

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
                init();
            }
        });
    }

    public void init() {
        try{
            new GetKnowledge(this).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActivityMode(Knowledge k) {
        if(k.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION))
            activityMode = QUIZ_MODE;
        else if(k.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT))
            activityMode = UNIT_MODE;
    }
    public void updateViews(Knowledge k) {
        if(k.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT) ) {
            setUnitView(k);
        }
        else if(k.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
            setQuestionView(k);
        }
    }

    public void setQuestionView(Knowledge k) {
        ansGroup.setVisibility(View.VISIBLE);
        String questionTitle = k.getKnowledgeTitle();
        Log.d("setQuestionView()", k.getKnowledgeTitle());
        String[] questionOptions = (String[]) k.getKnowledgeContent();

        knAdvert.setText("Pop Quiz!");
        Toast.makeText(getApplicationContext(),k.getUniqueHash(),Toast.LENGTH_SHORT).show();
        knTitle.setText(questionTitle);

        StringBuilder sb = new StringBuilder();
        sb.append("Option A : ").append(questionOptions[0]).append("\n\nOption B : ").append(questionOptions[1]).append("\n\nOption C : ").append(questionOptions[2]);

        knContent.setText(sb.toString());
    }

    public void setUnitView(Knowledge k) {
        ansGroup.setVisibility(View.GONE);
        String unitTitle = k.getKnowledgeTitle();
        String unitKnowledge = (String) k.getKnowledgeContent();

        knAdvert.setText("Here is a small topic!");
        Toast.makeText(getApplicationContext(),k.getUniqueHash(),Toast.LENGTH_SHORT).show();
        knTitle.setText(unitTitle);
        knContent.setText(unitKnowledge);
    }

    public void saveAnswer() {
        int choice = ansGroup.getCheckedRadioButtonId();
        if(choice == -1) {
            Toast.makeText(getApplicationContext(), "Select a choice please!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
