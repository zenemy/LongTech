package com.julong.longtech;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    int v_ev_seq;
    public static TextView txtListChild, lblListHeader, txtisiListChild;
    DatabaseHelper dbhelper;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, int ev_Seq) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        v_ev_seq = ev_Seq;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    static class ViewHolder {
        protected TextView text,remarks;
        protected CheckBox checkBox;
        protected EditText address;
        protected Button notes;

    }
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String)getChild(groupPosition, childPosition);

        dbhelper = new DatabaseHelper(_context);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = infalInflater.inflate(R.layout.list_sub_menu, null);

            //convertView = infalInflater.inflate(R.layout.list_sub_menu, null); //-----
        }


        txtListChild = (TextView) convertView.findViewById(R.id.tv_Listmenu);

        //TextView txtListChild = (TextView)convertView.findViewById(R.id.tv_Listmenu);//----

        txtListChild.setText(childText);



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_menu, null);
        }

        //convertView = inflater.inflate(R.layout.list_group_menu, null);//-----


            lblListHeader = convertView.findViewById(R.id.tv_groupmenu);


            //TextView lblListHeader = convertView.findViewById(R.id.tv_groupmenu);//----
            lblListHeader.setTypeface(null, Typeface.NORMAL);
            lblListHeader.setText(headerTitle);

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