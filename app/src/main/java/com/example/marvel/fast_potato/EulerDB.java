package com.example.marvel.fast_potato;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Sriduth Jayhari
 */
public class EulerDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eulerApplicationDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private final String[] INIT_QUERIES = {
            "CREATE TABLE api_information (api_key text, device_key text, user_org text);",
            "CREATE TABLE learning_path (path_status text);",
            "CREATE TABLE application_logging (desc text, data text);",
            "CREATE TABLE user_log (in_time text, out_time text);",
            "CREATE TABLE quiz_log (quiz_hash text, start_time text, end_time text);",
            "CREATE TABLE user_info (first_name text, last_name text);"
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

    public void initAndSaveApiData(String api_key, String device_key, String user_org) {
        final String query = "INSERT INTO api_information VALUES(\'"+api_key+"\',\'"+device_key+"\',\'"+user_org+"\');";
        SQLiteDatabase thisDatabase = getWritableDatabase();
        thisDatabase.execSQL(query);
    }

    public void saveUserData(String firstName, String lastName) {
        final String Query = "INSERT INTO user_info VALUES(\'"+firstName+"\',\'"+lastName+"\');";
        SQLiteDatabase thisDatabase = getWritableDatabase();
        thisDatabase.execSQL(Query);
    }

    public Map<String , String[]> getUserData() {
        Map<String, String[]> data = new TreeMap<String, String[]>();

        final String userDetailQuery = "SELECT * FROM user_info";
        final String userKeysQuery = "SELECT * FROM api_information";

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(userDetailQuery, null);
        c.moveToFirst();

        data.put("name", new String[] {c.getString(0), c.getString(1)});

        c = db.rawQuery(userKeysQuery, null);
        c.moveToFirst();

        data.put("keyData", new String[] {c.getString(0), c.getString(1)});
        c.close();
        return data;
    }
}