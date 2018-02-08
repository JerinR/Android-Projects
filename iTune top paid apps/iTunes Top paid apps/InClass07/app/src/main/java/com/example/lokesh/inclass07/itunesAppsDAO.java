package com.example.lokesh.inclass07;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class itunesAppsDAO {
    private SQLiteDatabase db;

    public itunesAppsDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long saveApp(itunesApp itunesApp) {
        ContentValues values = new ContentValues();
        values.put(itunesAppsTable.COLUMN_TITLE, itunesApp.getTitle());
        values.put(itunesAppsTable.COLUMN_PRICE, itunesApp.getPrice());
        values.put(itunesAppsTable.COLUMN_THUMB_URL, itunesApp.getThumbUrl());
        return db.insert(itunesAppsTable.TABLENAME, null, values);
    }

    public boolean deleteApp(itunesApp itunesApp) {
        return db.delete(itunesAppsTable.TABLENAME, itunesAppsTable.COLUMN_ID + "=?", new String[]{itunesApp.get_id() + ""}) > 0;
    }

    public ArrayList<itunesApp> getAllApps() {

        ArrayList<itunesApp> itunesApps = new ArrayList<itunesApp>();

        Cursor cursor = db.query(itunesAppsTable.TABLENAME, new String[]{itunesAppsTable.COLUMN_ID, itunesAppsTable.COLUMN_TITLE, itunesAppsTable.COLUMN_PRICE, itunesAppsTable.COLUMN_THUMB_URL}, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                itunesApp itunesApp = buildNoteFromCursor(cursor);
                if (itunesApp != null) {
                    itunesApps.add(itunesApp);
                }
            } while (cursor.moveToNext());

            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        return itunesApps;
    }

    private itunesApp buildNoteFromCursor(Cursor cursor) {
        itunesApp itunesApp = null;
        if (cursor != null) {
            itunesApp = new itunesApp();
            itunesApp.set_id(cursor.getLong(0));
            itunesApp.setTitle(cursor.getString(1));
            itunesApp.setPrice(cursor.getString(2));
            itunesApp.setThumbUrl(cursor.getString(3));
        }
        return itunesApp;
    }
}


