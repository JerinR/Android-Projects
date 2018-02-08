package com.example.lokesh.inclass07;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "MyApps.db";
    static final int DB_VERSION = 1;

    public DatabaseOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        itunesAppsTable.onCreate(db);
        Log.d("demo", "onCreate: "+db.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        itunesAppsTable.onUpgrade(db,oldVersion,newVersion);
    }
}

