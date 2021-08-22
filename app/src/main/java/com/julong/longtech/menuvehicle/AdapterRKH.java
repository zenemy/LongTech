package com.julong.longtech.menuvehicle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.julong.longtech.R;
import com.julong.longtech.DatabaseHelper;

import java.util.Date;
import java.util.List;

import static com.julong.longtech.menuvehicle.RencanaKerjaHarian.loadListViewRKH;

public class AdapterRKH extends ArrayAdapter<ListParamRKH> {

    private List<ListParamRKH> rkhParams;
    private final Context context;
    private String checkedRKH;
    DatabaseHelper dbhelper;

    public AdapterRKH(Context context, List<ListParamRKH> rkhParams) {
        super(context, R.layout.item_lvrkh, rkhParams);
        this.context = context;
        this.rkhParams = rkhParams;
    }

    static class ViewHolder {
        protected LinearLayout layoutItemLvRKH;
        protected TextView tvVehicleCodeLvRKH, tvShiftLvRKH, tvDriverNameLvRKH, tvDriverCodeLvRKH;
        protected CheckBox checkBoxLvRKH;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dbhelper = new DatabaseHelper(getContext());
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(R.layout.item_lvrkh, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.layoutItemLvRKH = convertView.findViewById(R.id.layoutItemLvRKH);
            viewHolder.tvVehicleCodeLvRKH = convertView.findViewById(R.id.tvVehicleCodeLvRKH);
            viewHolder.tvDriverNameLvRKH = convertView.findViewById(R.id.tvDriverNameLvRKH);
            viewHolder.tvShiftLvRKH = convertView.findViewById(R.id.tvShiftLvRKH);
            viewHolder.tvDriverCodeLvRKH = convertView.findViewById(R.id.tvDriverCodeLvRKH);
            viewHolder.checkBoxLvRKH = convertView.findViewById(R.id.checkBoxLvRKH);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvVehicleCodeLvRKH, viewHolder.tvVehicleCodeLvRKH);
            convertView.setTag(R.id.tvDriverNameLvRKH, viewHolder.tvDriverNameLvRKH);
            convertView.setTag(R.id.tvShiftLvRKH, viewHolder.tvShiftLvRKH);
            convertView.setTag(R.id.tvDriverCodeLvRKH, viewHolder.tvDriverCodeLvRKH);
            convertView.setTag(R.id.checkBoxLvRKH, viewHolder.checkBoxLvRKH);

        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvVehicleCodeLvRKH.setText(rkhParams.get(position).getVehicleCode());
        viewHolder.tvShiftLvRKH.setText(rkhParams.get(position).getShiftkerja());
        viewHolder.tvDriverNameLvRKH.setText(dbhelper.get_empname(rkhParams.get(position).getDrivername()));
        viewHolder.tvDriverCodeLvRKH.setText(rkhParams.get(position).getDrivername());

        viewHolder.checkBoxLvRKH.setTag(position);
        viewHolder.checkBoxLvRKH.setChecked(rkhParams.get(position).isChecked());

        if (RencanaKerjaHarian.selectedLokasi != null && RencanaKerjaHarian.selectedKegiatan != null) {
            viewHolder.checkBoxLvRKH.setVisibility(View.VISIBLE);
        }

        viewHolder.layoutItemLvRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RencanaKerjaHarian.selectedLokasi == null && RencanaKerjaHarian.selectedKegiatan == null) {
                    Snackbar.make(v, "Harap pilih lokasi dan kegiatan", Snackbar.LENGTH_LONG).setAnchorView(((RencanaKerjaHarian) context).layoutBtnRKH)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }
                else {
                    if (viewHolder.checkBoxLvRKH.isChecked()) {
                        viewHolder.checkBoxLvRKH.setChecked(false);
                        checkedRKH = dbhelper.getCheckRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                                rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername());
                        RencanaKerjaHarian.btnRefreshRKH.performClick();
                        if (checkedRKH != null) {
                            dbhelper.update_checkedRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                                    rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername(),
                                    null, null, "");
                            viewHolder.checkBoxLvRKH.setChecked(false);
                            RencanaKerjaHarian.btnRefreshRKH.performClick();
                        }
                    }
                    else {
                        viewHolder.checkBoxLvRKH.setChecked(true);
                        dbhelper.update_checkedRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                                rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername(),
                                RencanaKerjaHarian.selectedLokasi,RencanaKerjaHarian.selectedKegiatan,"Checked");
                        RencanaKerjaHarian.btnRefreshRKH.performClick();
                    }
                }

            }
        });

        viewHolder.checkBoxLvRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBoxLvRKH.isChecked()) {
                    dbhelper.update_checkedRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                            rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername(),
                            RencanaKerjaHarian.selectedLokasi,RencanaKerjaHarian.selectedKegiatan,"Checked");
                    RencanaKerjaHarian.btnRefreshRKH.performClick();
                } else {
                    checkedRKH = dbhelper.getCheckRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                            rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername());
                    RencanaKerjaHarian.btnRefreshRKH.performClick();
                    if (checkedRKH != null) {
                        dbhelper.update_checkedRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                                rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername(),
                                null, null, "");
                        viewHolder.checkBoxLvRKH.setChecked(false);
                        RencanaKerjaHarian.btnRefreshRKH.performClick();
                    }
                }
            }
        });


        String checkRKH = dbhelper.getCheckRKH(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                rkhParams.get(position).getShiftkerja(),rkhParams.get(position).getDrivername());

        if (checkRKH.equals("Checked")) {
            viewHolder.checkBoxLvRKH.setChecked(true);
        }
        else  {
            viewHolder.checkBoxLvRKH.setChecked(false);

        }

        return convertView;
    }

}