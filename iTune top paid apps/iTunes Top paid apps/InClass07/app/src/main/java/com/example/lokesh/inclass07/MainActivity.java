package com.example.lokesh.inclass07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements GetDataAsyncTask.LoadData{
    ListView appsListView;
    ArrayList<itunesApp> appsList;
    ArrayList<itunesApp> ascList;
    ArrayList<itunesApp> dataItunesApps;
    ArrayList<itunesApp> newSet;
    boolean orderChoice;
    ProgressDialog progressDialog = null;
    DatabaseManager databaseDataManager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase("MyApps.db");


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        databaseDataManager = new DatabaseManager(this);
        appsListView = (ListView)findViewById(R.id.listView);
        final Switch aSwitch = (Switch)findViewById(R.id.orderSwitch);

        mRecyclerView = (RecyclerView) findViewById(R.id.filteredList);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        dataItunesApps = databaseDataManager.getAllApps();;

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Collections.sort(ascList, new Comparator<itunesApp>() {
                        @Override
                        public int compare(itunesApp o1, itunesApp o2) {
                            float o1Price = Float.parseFloat(extractPrice(o1.getPrice()));
                            float o2Price = Float.parseFloat(extractPrice(o2.getPrice()));
                            return (int) (o1Price - o2Price);
                        }
                    });
                    aSwitch.setText("Ascending");
                }else {
                    Collections.reverse(ascList);
                    aSwitch.setText("Descending");
                }
                updateViews();
            }
        });
        new GetDataAsyncTask(this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");

        findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetDataAsyncTask(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
            }
        });
    }

    @Override
    public void setApps(ArrayList<itunesApp> itunesApps) {
        progressDialog.dismiss();
        appsList = itunesApps;
        ascList = itunesApps;
        ArrayList<itunesApp> recy = databaseDataManager.getAllApps();
        ArrayList<itunesApp> newlist;
        Collections.sort(ascList, new Comparator<itunesApp>() {
            @Override
            public int compare(itunesApp o1, itunesApp o2) {

                float o1Price = Float.parseFloat(extractPrice(o1.getPrice()));
                float o2Price = Float.parseFloat(extractPrice(o2.getPrice()));
                return (int) (o1Price - o2Price);
            }
        });
        updateViews();
    }

    void setAdapterForAppsList(ArrayList<itunesApp> list){
        AppsListAdapter feedAdapter = new AppsListAdapter(this,R.layout.list_item,list);

        appsListView.setAdapter(feedAdapter);
        appsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                itunesApp itunesApp = ascList.get(i);
                databaseDataManager.saveApp(itunesApp);
                dataItunesApps = databaseDataManager.getAllApps();
                updateViews();
                boolean found = false;
                return false;
            }
        });
    }

    void setRecyclerListAdapter(ArrayList<itunesApp> itunesApps){
        mRecyclerView.setVisibility(View.VISIBLE);
        RecyclerViewAdapter filterViewAdapter= new RecyclerViewAdapter(itunesApps,MainActivity.this);
        mRecyclerView.setAdapter(filterViewAdapter);
    }

    class AppsSort implements Comparator<itunesApp> {
        @Override
        public int compare(itunesApp a1, itunesApp a2) {
            return a1.getTitle().compareTo(a2.getTitle());
        }
    }

    void updateViews(){
        newSet = new ArrayList<itunesApp>();
        mRecyclerView = (RecyclerView) findViewById(R.id.filteredList);
        for (itunesApp itunesApp1 : ascList){
            for (itunesApp itunesApp2 : dataItunesApps){
                if (itunesApp1.getTitle().equalsIgnoreCase(itunesApp2.getTitle())){

                }else {
                    newSet.add(itunesApp1);
                    break;
                }
            }
        }
        if (dataItunesApps.size()>0){
            mRecyclerView.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.textViewMSG)).setVisibility(View.INVISIBLE);
        }

        if (newSet.size()==0){
            setAdapterForAppsList(ascList);
        }else {
            setAdapterForAppsList(newSet);
        }

        setRecyclerListAdapter(dataItunesApps);

    }


    public void getAndSet() {
        Log.d("Demo","set");
        dataItunesApps = databaseDataManager.getAllApps();
        updateViews();
    }

    public String extractPrice(String s){
        return s.substring(1);
    }
}
