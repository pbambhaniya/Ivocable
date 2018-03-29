package com.multipz.dictionary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.R;

import java.util.ArrayList;

/**
 * Created by Admin on 28-06-2017.
 */

public class MathsWordAdapter extends BaseAdapter {
    Context context;
    ArrayList<MathsWordModel> list = new ArrayList<MathsWordModel>();
    MathsWordModel data;

    public MathsWordAdapter(Context context, ArrayList<MathsWordModel> list) {
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

        View layoutview = inflater.inflate(R.layout.row_item, parent, false);

        TextView txt_news = (TextView) layoutview.findViewById(R.id.txt_name);

        txt_news.setText(data.getDefinition());


        return layoutview;
    }
}