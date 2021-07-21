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

public class AdapterRKH extends ArrayAdapter<ListParamRKH> {

    private List<ListParamRKH> uploadParams;
    private final Activity context;
    public static TextView data;
    DatabaseHelper dbhelper;
    public static CheckBox checkBox;

    public AdapterRKH(Activity context, int download_list, List<ListParamRKH> uploadParams) {
        super(context, R.layout.upload_list, uploadParams);
        this.context = context;
        this.uploadParams = uploadParams;
        this.uploadParams = uploadParams;
    }

    static class ViewHolder {
        protected LinearLayout layoutItemLvRKH;
        protected TextView tvVehicleNameLvRKH, tvShiftLvRKH, tvDriverNameLvRKH, tvHelper1NameLvRKH, tvHelper2NameLvRKH, tvBensinLvRKH;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dbhelper = new DatabaseHelper(getContext());
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.item_lvrkh, null);

            ViewHolder viewHolder = new AdapterRKH.ViewHolder();
            viewHolder.layoutItemLvRKH = (LinearLayout) convertView.findViewById(R.id.layoutItemLvRKH);
            viewHolder.tvVehicleNameLvRKH = (TextView) convertView.findViewById(R.id.tvVehicleNameLvRKH);
            viewHolder.tvDriverNameLvRKH = (TextView) convertView.findViewById(R.id.tvDriverNameLvRKH);
            viewHolder.tvHelper1NameLvRKH = (TextView) convertView.findViewById(R.id.tvHelper1NameLvRKH);
            viewHolder.tvHelper2NameLvRKH = (TextView) convertView.findViewById(R.id.tvHelper2NameLvRKH);
            viewHolder.tvBensinLvRKH = (TextView) convertView.findViewById(R.id.tvKebutuhanBBM);
            viewHolder.tvShiftLvRKH = (TextView) convertView.findViewById(R.id.tvShiftLvRKH);

            viewHolder.layoutItemLvRKH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), InputRincianRKH.class);
                    intent.putExtra("nodoc", RencanaKerjaHarian.nodocRKH);
                    intent.putExtra("vehicle", viewHolder.tvVehicleNameLvRKH.getText().toString());
                    intent.putExtra("shiftkerja", viewHolder.tvShiftLvRKH.getText().toString());
                    context.startActivity(intent);
                }
            });

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvVehicleNameLvRKH, viewHolder.tvVehicleNameLvRKH);
            convertView.setTag(R.id.tvDriverNameLvRKH, viewHolder.tvDriverNameLvRKH);
            convertView.setTag(R.id.tvHelper1NameLvRKH, viewHolder.tvHelper1NameLvRKH);
            convertView.setTag(R.id.tvHelper2NameLvRKH, viewHolder.tvHelper2NameLvRKH);
            convertView.setTag(R.id.tvKebutuhanBBM, viewHolder.tvBensinLvRKH);
            convertView.setTag(R.id.tvShiftLvRKH, viewHolder.tvShiftLvRKH);

        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvVehicleNameLvRKH.setText(uploadParams.get(position).getVehiclename());
        viewHolder.tvShiftLvRKH.setText("(" + uploadParams.get(position).getShiftkerja() + ")");
        viewHolder.tvDriverNameLvRKH.setText(dbhelper.get_empname(uploadParams.get(position).getDrivername()));
        viewHolder.tvHelper1NameLvRKH.setText(dbhelper.get_empname(uploadParams.get(position).getHelper1Name()));
        viewHolder.tvHelper2NameLvRKH.setText(dbhelper.get_empname(uploadParams.get(position).getHelper2Name()));
        viewHolder.tvBensinLvRKH.setText("BBM " + uploadParams.get(position).getKebutuhanBBM() + " Liter");

        return convertView;
    }

}