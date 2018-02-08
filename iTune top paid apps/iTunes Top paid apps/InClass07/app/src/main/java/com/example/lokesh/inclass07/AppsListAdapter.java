package com.example.lokesh.inclass07;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class AppsListAdapter extends ArrayAdapter<itunesApp>{
    Context mContext;
    int mResource;
    List<itunesApp> mData;
    public AppsListAdapter(Context context, int resource, List<itunesApp> itunesApps) {
        super(context, resource, itunesApps);
        this.mContext = context;
        this.mResource = resource;
        this.mData = itunesApps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        ImageView listImage = (ImageView)convertView.findViewById(R.id.listImage);
        ImageView listImage2 = (ImageView)convertView.findViewById(R.id.listImage2);
        TextView listName = (TextView)convertView.findViewById(R.id.listName);
        TextView listPrice = (TextView)convertView.findViewById(R.id.listPrice);

        itunesApp itunesApp = mData.get(position);

        listName.setText(itunesApp.getTitle());
        listPrice.setText("Price: USD "+ itunesApp.getPrice());
        Picasso.with(mContext).load(itunesApp.getThumbUrl()).into(listImage);
        String priceString = itunesApp.getPrice();
        String subPrice = priceString.substring(1,priceString.length());
        double price = Double.parseDouble(subPrice);
        if (price>0 && price<2){
            listImage2.setImageResource(R.drawable.price_low);
        }else if (price>=2 && price<6){
            listImage2.setImageResource(R.drawable.price_medium);
        }else if (price>=6) {
            listImage2.setImageResource(R.drawable.price_high);
        }
        return convertView;
    }
}
