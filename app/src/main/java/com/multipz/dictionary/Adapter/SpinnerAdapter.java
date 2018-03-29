package com.multipz.dictionary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.multipz.dictionary.Model.SpinnerModel;
import com.multipz.dictionary.R;

import java.util.ArrayList;

/**
 * Created by Admin on 19-07-2017.
 */

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<SpinnerModel> list = new ArrayList<SpinnerModel>();
    SpinnerModel data;

    public SpinnerAdapter(Context context, ArrayList<SpinnerModel> list) {
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
        View layoutview = inflater.inflate(R.layout.spinner, parent, false);

        TextView txt_news = (TextView) layoutview.findViewById(R.id.Spinner_title);

        txt_news.setText(data.getType());

        return layoutview;
    }

}
