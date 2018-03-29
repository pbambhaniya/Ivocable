package com.multipz.dictionary.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Model.MyActivityModel;
import com.multipz.dictionary.Model.MyApp;
import com.multipz.dictionary.R;

import java.util.ArrayList;

/**
 * Created by Admin on 24-07-2017.
 */

public class MyAppAdapter extends BaseAdapter {

    Context context;
    MyApp myApp;
    ArrayList<MyApp> list = new ArrayList<MyApp>();

    public MyAppAdapter(Context context,ArrayList<MyApp> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        myApp = list.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View layoutview = inflater.inflate(R.layout.layout_app, parent, false);

        TextView txt_title = (TextView) layoutview.findViewById(R.id.txt_title);
        TextView txt_desc = (TextView) layoutview.findViewById(R.id.txt_desc);
        TextView txt_url = (TextView) layoutview.findViewById(R.id.txt_get);
        ImageView img = (ImageView) layoutview.findViewById(R.id.img_lg);

        txt_title.setText(myApp.getTitle());
        txt_desc.setText(myApp.getDesc());
        img.setImageResource(myApp.getImage());

        txt_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + list.get(position).getUrl());
                Intent rate = new Intent(Intent.ACTION_VIEW, uri);
                rate.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    context.startActivity(rate);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + myApp.getUrl())));
                }

            }
        });

        return layoutview;
    }
}
