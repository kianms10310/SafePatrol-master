package com.hanict.safepatrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static java.sql.DriverManager.println;


/**
 * Created by Shin on 2017-06-23.
 */

public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final String TAG = "DatabseAdapter";
    private static final String DATABASE_NAME="";
    private static final int DATABASE_VERSION=0;
    private static final String TABLE_NAME="";

    public DatabaseAdapter(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        println("creating table [" + TABLE_NAME + "].");
        try{
            String DROP_SQL="drop table if exists " + TABLE_NAME;
            db.execSQL(DROP_SQL);
        }catch (Exception ex){
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
