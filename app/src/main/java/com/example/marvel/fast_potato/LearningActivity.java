package com.example.marvel.fast_potato;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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

    private TextView pathProgressAdvert = null;
    private ProgressBar pathProgress = null;

    private TextView knTitle = null;
    private TextView knContent = null;
    private RadioGroup ansGroup= null;
    private RadioButton[] options = null;
    private ImageView imageContent = null;

    private Button getNextTask = null;

    private boolean activityMode;

    public Knowledge knowledge = null;

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

        try{
            new KnowledgeApi.GetKnowledge(this).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Set styles
        tf = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        knAdvert.setTypeface(tf);
        pathProgressAdvert.setTypeface(tf);
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
        pathProgressAdvert = (TextView) findViewById(R.id.progressAdvert);

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

        imageContent = (ImageView) findViewById(R.id.imageContent);
        getNextTask = (Button) findViewById(R.id.getNextTask);
    }

    public void setOnClickListeners() {
        getNextTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!saveAnswer().equals("-1")) {
                    init();
                }
                else
                    Toast.makeText(getApplicationContext(), "Select a choice please!", Toast.LENGTH_SHORT).show();
            }
        });

        knContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showImagePopup();
                return true;
            }
        });
    }

    public void init() {
        try{
            new KnowledgeApi.SaveProgress(this).execute(saveAnswer()).get();
            new KnowledgeApi.GetKnowledge(this).execute();
            ansGroup.clearCheck();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActivityMode() {
        if(knowledge.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION))
            activityMode = QUIZ_MODE;
        else if(knowledge.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT))
            activityMode = UNIT_MODE;
    }
    public void updateViews() {
        if(knowledge.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_UNIT) ) {
            setUnitView();
        }
        else if(knowledge.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
            setQuestionView();
        }
    }

    public void setQuestionView() {
        ansGroup.setVisibility(View.VISIBLE);
        String questionTitle = knowledge.getKnowledgeTitle();
        Log.d("setQuestionView()", knowledge.getKnowledgeTitle());
        String[] questionOptions = (String[]) knowledge.getKnowledgeContent();

        knAdvert.setText("Pop Quiz!");
        knTitle.setText(questionTitle);

        StringBuilder sb = new StringBuilder();
        sb.append("\nOption A : ").append(questionOptions[0]).append("\n\nOption B : ").append(questionOptions[1]).append("\n\nOption C : ").append(questionOptions[2]);

        knContent.setText(sb.toString());
    }

    public void setUnitView() {
        ansGroup.setVisibility(View.GONE);
        String unitTitle = knowledge.getKnowledgeTitle();
        String unitKnowledge = (String) knowledge.getKnowledgeContent();

        knAdvert.setText("Here is a small topic!");
        knTitle.setText(unitTitle);
        knContent.setText(unitKnowledge);
    }

    public String saveAnswer() {
        int choice = ansGroup.getCheckedRadioButtonId();
        RadioButton temp = (RadioButton) findViewById(choice);

        String data = "KNOWLEDGE_CONSUMED";
        if(knowledge.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
            data = temp.getText().toString();
            if (data == null){
                return "-1";
            }
            if(choice == -1 && activityMode) {
                return "-1";
            }
        }
        return data;
    }

    public void setProgress() {
        String prog = knowledge.getPathProgress();
        pathProgress.setProgress(Integer.parseInt(prog));
        pathProgressAdvert.setText("Your Progress : "+prog+"%");
    }

    public void setImageContent() {
        imageContent.setImageBitmap((Bitmap) knowledge.getMultimediaContent());
    }

    public void showImagePopup() {
        Toast.makeText(getApplicationContext(),"! : You can view the image anytime by long clicking the screen.", Toast.LENGTH_LONG).show();

        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View popup = getLayoutInflater().inflate(R.layout.learning_image_popup, null);
        settingsDialog.setContentView(popup);


        ImageView view = (ImageView) popup.findViewById(R.id.imagePopupContent);
        view.setImageBitmap((Bitmap) knowledge.getMultimediaContent());

        settingsDialog.show();
    }
}
