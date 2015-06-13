package com.example.marvel.fast_potato.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marvel.fast_potato.knowledge.KnowledgeApi;
import com.example.marvel.fast_potato.R;


public class RegisterCourseActivity extends Activity {

    private TextView regAdvert = null;
    private ListView courseList = null;
    private Typeface tf = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_course);
        regAdvert = (TextView) findViewById(R.id.courseRegAdvert);
        courseList = (ListView) findViewById(R.id.coursesView);

        tf = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");

        regAdvert.setTypeface(tf);
        try {
            new KnowledgeApi.GetAllCourses(this).execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadCourses(RegisterCourseActivity activity, String[] data) {
        courseList.setAdapter(new CourseListAdapter(activity.getApplicationContext(), data));
    }


    class CourseListAdapter extends ArrayAdapter<String>{
        private Context context = null;
        private String[] values = null;

        public CourseListAdapter(Context ctxt, String[] vals) {
            super(ctxt, R.layout.course_reg_list_iew, vals);
            context = ctxt;
            values = vals;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.course_reg_list_iew, parent, false);

            TextView main = (TextView) row.findViewById(R.id.advert);
            TextView sub = (TextView) row.findViewById(R.id.label);

            String[] data = values[position].split(";");

            main.setText(data[0]);
            sub.setText(data[1]);

            return row;
        }
    }
}
