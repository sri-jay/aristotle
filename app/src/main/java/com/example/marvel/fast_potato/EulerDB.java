package com.example.marvel.fast_potato;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.UUID;

/**
 * @author Sriduth Jayhari
 */
public class EulerDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eulerApplicationDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private final String[] INIT_QUERIES = {
            "CREATE TABLE api_information (api_key text, device_key text);",
            "CREATE TABLE learning_path (path_status text);",
            "CREATE TABLE application_logging (desc text, data text);",
            "CREATE TABLE user_log (in_time text, out_time text);",
            "CREATE TABLE quiz_log (quiz_hash text, start_time text, end_time text);"
    };

    public EulerDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("RegisterUserDevice", "Creating local database.");
        for(String query : INIT_QUERIES) {
            db.execSQL(query);
            Log.d("RegisterUserDevice","Creating Table -> "+query);
        }
        Log.d("RegisterUserDevice","Done creating tables.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Must override.
    }

    public void saveApiData(String key) {
        final String query = "INSERT INTO api_information VALUES("+key+","+UUID.randomUUID().toString()+" );";
        SQLiteDatabase thisDatabase = getWritableDatabase();
        thisDatabase.execSQL(query);
    }

//    public void createQuizData(Knowledge q) {
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "INSERT INTO quiz_log VALUES("+q.getQuizID()+q.getQuizStartTime()+"end-time-unset);";
//
//        db.execSQL(query);
//    }
//
//    public void finishQuiz(Knowledge q) {
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "UPDATE quiz_log SET endtime="+q.getQuizStopTime()+"WHERE quiz_hash="+q.getQuizID()+";";
//
//        db.execSQL(query);
//    }
}