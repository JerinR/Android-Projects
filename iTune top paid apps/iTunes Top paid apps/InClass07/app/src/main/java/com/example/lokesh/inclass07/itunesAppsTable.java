package com.example.lokesh.inclass07;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class itunesAppsTable {
    static final String TABLENAME = "Filter";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TITLE = "name";
    static final String COLUMN_PRICE = "price";
    static final String COLUMN_THUMB_URL = "url";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLENAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_TITLE + " text not null, ");
        sb.append(COLUMN_PRICE + " text not null, ");
        sb.append(COLUMN_THUMB_URL + " text not null);");

        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            Log.d("Exception", e.getLocalizedMessage());
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        itunesAppsTable.onCreate(db);
    }
}

