package com.example.marvel.fast_potato;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.VideoView;

public class LearningActivity extends Activity {
    private View[] views = null;

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

    private int progress = 0;

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
            new KnowledgeApi.GetKnowledge(this, false).execute();
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
                showPopup();
                return true;
            }
        });
    }

    public void init() {
        try{
            new KnowledgeApi.SaveProgress(this).execute(saveAnswer()).get();
            new KnowledgeApi.GetKnowledge(this, false).execute();
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
        knowledge.getTelemetrics().start();
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

        knowledge.getTelemetrics().stop();
        String data = "KNOWLEDGE_CONSUMED";
        if(knowledge.getKnowledgeType().equals(KnowledgeTypes.KNOWLEDGE_TYPE_QUESTION)) {
            if(choice == -1 && activityMode) {
                return "-1";
            }
            RadioButton temp = (RadioButton) findViewById(choice);
            data = temp.getText().toString();
        }
        return data;
    }

    public void setProgress() {
        String prog = knowledge.getPathProgress();
        pathProgress.setProgress(Integer.parseInt(prog));
        pathProgressAdvert.setText("Your Progress : "+prog+"%");
    }

    public void showPopup() {
        if(knowledge.getMediaType().equals("image"))
            showImagePopup();
        else
            showVideoPopup();
    }
    public void showVideoPopup() {
        Dialog videoBox = new Dialog(this);
        videoBox.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View popup = getLayoutInflater().inflate(R.layout.learning_video_popup, null);
        videoBox.setContentView(popup);

        VideoView video = (VideoView) popup.findViewById(R.id.videoPopupContent);
        video.setVideoURI(Uri.parse(knowledge.getMultimediaContent().toString()));
        video.start();
        videoBox.show();
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

        view.setOnTouchListener(new View.OnTouchListener() {
            // These matrices will be used to move and zoom image
            Matrix matrix = new Matrix();
            Matrix savedMatrix = new Matrix();

            // We can be in one of these 3 states
            static final int NONE = 0;
            static final int DRAG = 1;
            static final int ZOOM = 2;
            int mode = NONE;

            // Remember some things for zooming
            PointF start = new PointF();
            PointF mid = new PointF();
            float oldDist = 1f;
            String savedItemClicked;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Display dis = getWindowManager().getDefaultDisplay();
                ImageView image = (ImageView) view;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            // ...
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY()
                                    - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }
                ((ImageView) view).setImageMatrix(matrix);
                return true;
            }


            /** Determine the space between the first two fingers */
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return FloatMath.sqrt(x * x + y * y);
            }

            /** Calculate the mid point of the first two fingers */
            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
        });
    }
}
