package com.julong.longtech.menuvehicle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.julong.longtech.R;
import com.julong.longtech.DatabaseHelper;

import java.util.Date;
import java.util.List;

public class AdapterKoordinatGIS extends ArrayAdapter<ListParamGIS> {

    private List<ListParamGIS> gisParams;
    private final Activity context;
    public static TextView data;
    DatabaseHelper dbhelper;

    public AdapterKoordinatGIS(Activity context, int list, List<ListParamGIS> gisParams) {
        super(context, R.layout.listview_koordinatgis, gisParams);
        this.context = context;
        this.gisParams = gisParams;
    }

    static class ViewHolder {
        protected TextView tvLatitude, tvLongitude, tvTime;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dbhelper = new DatabaseHelper(getContext());
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.listview_koordinatgis, null);

            ViewHolder viewHolder = new AdapterKoordinatGIS.ViewHolder();
            viewHolder.tvLatitude = (TextView) convertView.findViewById(R.id.tvLatListViewGIS);
            viewHolder.tvLongitude = (TextView) convertView.findViewById(R.id.tvLongListViewGIS);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTimeListViewGIS);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvLatListViewGIS, viewHolder.tvLatitude);
            convertView.setTag(R.id.tvLongListViewGIS, viewHolder.tvLongitude);
            convertView.setTag(R.id.tvTimeListViewGIS, viewHolder.tvTime);

        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvLatitude.setText(gisParams.get(position).getLatKoordinat());
        viewHolder.tvLongitude.setText(gisParams.get(position).getLongKoordinat());
        viewHolder.tvTime.setText(gisParams.get(position).getTagTime());

        return convertView;
    }

}