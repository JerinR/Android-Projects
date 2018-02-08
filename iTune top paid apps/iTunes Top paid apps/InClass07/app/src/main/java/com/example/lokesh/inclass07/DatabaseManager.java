package com.example.lokesh.inclass07;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class DatabaseManager {
    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private itunesAppsDAO itunesAppsDAO;

    public DatabaseManager(Context mContext){
        this.mContext = mContext;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        itunesAppsDAO = new itunesAppsDAO(db);
    }

    public void close(){
        if (db!=null){
            db.close();
        }
    }
    public itunesAppsDAO getItunesAppsDAO(){
        return this.itunesAppsDAO;
    }

    public long saveApp(itunesApp itunesApp){
        return this.itunesAppsDAO.saveApp(itunesApp);
    }

    public boolean deleteApp(itunesApp itunesApp){
        return this.itunesAppsDAO.deleteApp(itunesApp);
    }

    public ArrayList<itunesApp> getAllApps(){
        return this.itunesAppsDAO.getAllApps();
    }
}
