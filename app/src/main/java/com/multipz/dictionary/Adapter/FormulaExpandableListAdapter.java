package com.multipz.dictionary.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.multipz.dictionary.Model.MathsFormulasModel;
import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FormulaExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listHeader = new ArrayList<>();
    private HashMap<String, List<MathsFormulasModel>> listitem;

    public FormulaExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<MathsFormulasModel>> listitem) {
        this._context = context;
        this.listHeader = listDataHeader;
        this.listitem = listitem;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listitem.get(this.listHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.formulas_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.txt_formulas_item);

        txtListChild.setText(listitem.get(listHeader.get(groupPosition)).get(childPosition).getSub_type_name());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listitem.get(this.listHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this.listHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.formulas_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_formulas_name);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(listHeader.get(groupPosition));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
