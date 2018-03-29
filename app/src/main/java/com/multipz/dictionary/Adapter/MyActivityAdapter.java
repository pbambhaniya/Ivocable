package com.multipz.dictionary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.Model.MyActivityModel;
import com.multipz.dictionary.R;

import java.util.ArrayList;

/**
 * Created by Admin on 20-07-2017.
 */

public class MyActivityAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyActivityModel> list = new ArrayList<MyActivityModel>();
    MyActivityModel data;

    public MyActivityAdapter(Context context, ArrayList<MyActivityModel> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        data = list.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View layoutview = inflater.inflate(R.layout.row_my_activity, parent, false);

        TextView txt_news = (TextView) layoutview.findViewById(R.id.txt_name);
        ImageView img_rejected = (ImageView) layoutview.findViewById(R.id.txt_rejected_red);
        ImageView img_approved = (ImageView) layoutview.findViewById(R.id.txt_approved_green);
        ImageView img_pending = (ImageView) layoutview.findViewById(R.id.txt_pending_orange);

        txt_news.setText(data.getDef_title());

        if(data.getIs_word_status().contentEquals("PENDING")){
            img_pending.setVisibility(View.VISIBLE);
        }else if(data.getIs_word_status().contentEquals("APPROVED")){
            img_approved.setVisibility(View.VISIBLE);
        }else if(data.getIs_word_status().contentEquals("REJECTED")){
            img_rejected.setVisibility(View.VISIBLE);
        }
//        img_red.setVisibility(View.VISIBLE);
//        img_green.setVisibility(View.GONE);


        return layoutview;
    }
}
