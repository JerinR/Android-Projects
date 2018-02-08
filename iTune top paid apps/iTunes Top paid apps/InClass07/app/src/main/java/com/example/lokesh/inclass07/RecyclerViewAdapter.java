package com.example.lokesh.inclass07;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<itunesApp> myDataSet;
    Context mContext;
    DatabaseManager databaseDataManager;

    public Context getMContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,price;
        public ImageView imageView,imageView2,imageView3;

        public RecyclerViewAdapter parent;

        public ViewHolder(View v, final RecyclerViewAdapter parent) {
            super(v);
            this.parent = parent;
            title = (TextView)v.findViewById(R.id.rName);
            price = (TextView)v.findViewById(R.id.rPrice);
            imageView = (ImageView)v.findViewById(R.id.rMainImage);
            imageView2 = (ImageView)v.findViewById(R.id.rPImage);
            imageView3 = (ImageView)v.findViewById(R.id.rDelete);
        }
    }

    public RecyclerViewAdapter(ArrayList<itunesApp> myDataSet, Context mContext) {
        this.myDataSet = myDataSet;
        this.mContext = mContext;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View savedCityView = inflater.inflate(R.layout.recycler_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(savedCityView,this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, final int position) {
        itunesApp itunesApp = myDataSet.get(position);
        holder.title.setText(itunesApp.getTitle());
        holder.price.setText(itunesApp.getPrice());

        Log.d("Title", itunesApp.getTitle());

        Picasso.with(mContext).load(itunesApp.getThumbUrl()).into(holder.imageView);

        String priceString = itunesApp.getPrice();
        String subPrice = priceString.substring(1,priceString.length());

        double price = Double.parseDouble(subPrice);
        if (price>0 && price<2){
            holder.imageView2.setImageResource(R.drawable.price_low);
        }else if (price>=2 && price<6){
            holder.imageView2.setImageResource(R.drawable.price_medium);
        }else if (price>=6) {
            holder.imageView2.setImageResource(R.drawable.price_high);
        }

        holder.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseDataManager = new DatabaseManager(mContext);
                databaseDataManager.deleteApp(myDataSet.get(position));

                MainActivity activity = (MainActivity)mContext;
                activity.getAndSet();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }
    interface LoadData
    {
        public void getAndSet();
    }
}
