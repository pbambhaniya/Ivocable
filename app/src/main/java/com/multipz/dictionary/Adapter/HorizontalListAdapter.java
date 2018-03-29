package com.multipz.dictionary.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.multipz.dictionary.R;

import java.util.ArrayList;
import java.util.Arrays;

public class HorizontalListAdapter extends BaseAdapter {
    Context mContext;
    int currentPos = 0;
    public static String selected_word="A";
    LayoutInflater inflater;
    //    private int[] listview_images = {R.drawable.india,R.drawable.bangladesh,R.drawable.china,R.drawable.indonesia,R.drawable.finland,R.drawable.great_britain,R.drawable.iceland,R.drawable.ireland};
    private String[] listview_names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private ArrayList<String> array_sort;
    private ArrayList<Integer> image_sort;

    public HorizontalListAdapter(Context context) {
        this.mContext = context;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        array_sort = new ArrayList<String>(Arrays.asList(listview_names));

        //selected_word = "A";

//        image_sort=new ArrayList<Integer>();
//        for (int index = 0; index < listview_images.length; index++)
//        {
//            image_sort.add(listview_images[index]);
//        }
    }

    @Override
    public int getCount() {
// TODO Auto-generated method stub
        return array_sort.size();
    }

    @Override
    public Object getItem(int arg0) {
// TODO Auto-generated method stub
        return array_sort.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return array_sort.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View view = convertView;
        viewHolder holder;
        if (view == null) {
            holder = new viewHolder();
            view = inflater.inflate(R.layout.row, parent, false);
            holder.txtCrewName = (TextView) view.findViewById(R.id.txt_Name);
            holder.selectitem = (TextView) view.findViewById(R.id.hover_txt);
            view.setTag(holder);
        }
        holder = (viewHolder) view.getTag();
        holder.txtCrewName.setText(array_sort.get(position));

        if (array_sort.get(position).contentEquals(selected_word)) {
            //view.setBackground();
            Log.e("Notify SElected", selected_word+"");
            holder.selectitem.setVisibility(View.VISIBLE);
            holder.txtCrewName.setTextColor(Color.WHITE);
        }else{
            holder.selectitem.setVisibility(View.GONE);
            holder.txtCrewName.setTextColor(Color.parseColor("#087a8a"));
        }

        return view;
    }

    public class viewHolder {
        ImageView imgCrewMember;
        TextView txtCrewName, selectitem;
    }

    public void notifyme(int pos) {
        currentPos = pos;
        Log.e("Notify", pos+"");
        selected_word=array_sort.get(pos);
        this.notifyDataSetChanged();
    }

}