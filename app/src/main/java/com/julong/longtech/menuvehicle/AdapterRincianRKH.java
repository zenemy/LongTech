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

public class AdapterRincianRKH extends ArrayAdapter<ListParamRincianRKH> {

    private List<ListParamRincianRKH> rincianRkhParams;
    private final Activity context;
    public static TextView data;
    DatabaseHelper dbhelper;

    public AdapterRincianRKH(Activity context, int list, List<ListParamRincianRKH> rincianRkhParams) {
        super(context, R.layout.item_lvdetailrkh, rincianRkhParams);
        this.context = context;
        this.rincianRkhParams = rincianRkhParams;
    }

    static class ViewHolder {
        protected LinearLayout layoutLvDetailRKH;
        protected TextView tvJenisMuatanRincianRKH, tvSatuanKerjaRincianRKH, tvLokasiKerjaRincianRKH, tvKegiatanKerjaRincianRKH, tvWaktuKerjaRincianRKH, tvKMHMKerjaRincianRKH, tvPrestasiKerjaRincianRKH;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dbhelper = new DatabaseHelper(getContext());
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.item_lvdetailrkh, null);

            ViewHolder viewHolder = new AdapterRincianRKH.ViewHolder();
            viewHolder.layoutLvDetailRKH = (LinearLayout) convertView.findViewById(R.id.layoutItemLvRincianRKH);
            viewHolder.tvLokasiKerjaRincianRKH = (TextView) convertView.findViewById(R.id.tvLokasiKerjaRincianRKH);
            viewHolder.tvKegiatanKerjaRincianRKH = (TextView) convertView.findViewById(R.id.tvKegiatanKerjaRincianRKH);
            viewHolder.tvWaktuKerjaRincianRKH = (TextView) convertView.findViewById(R.id.tvWaktuKerjaRincianRKH);
            viewHolder.tvKMHMKerjaRincianRKH = (TextView) convertView.findViewById(R.id.tvKMHMKerjaRincianRKH);
            viewHolder.tvPrestasiKerjaRincianRKH = (TextView) convertView.findViewById(R.id.tvPrestasiKerjaRincianRKH);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvLokasiKerjaRincianRKH, viewHolder.tvLokasiKerjaRincianRKH);
            convertView.setTag(R.id.tvKegiatanKerjaRincianRKH, viewHolder.tvKegiatanKerjaRincianRKH);
            convertView.setTag(R.id.tvWaktuKerjaRincianRKH, viewHolder.tvWaktuKerjaRincianRKH);
            convertView.setTag(R.id.tvKMHMKerjaRincianRKH, viewHolder.tvKMHMKerjaRincianRKH);
            convertView.setTag(R.id.tvPrestasiKerjaRincianRKH, viewHolder.tvPrestasiKerjaRincianRKH);

        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvLokasiKerjaRincianRKH.setText(dbhelper.get_singlelokasi(rincianRkhParams.get(position).getLokasi()));
        viewHolder.tvKegiatanKerjaRincianRKH.setText(dbhelper.get_singlekegiatan(rincianRkhParams.get(position).getKegiatan()));
        viewHolder.tvWaktuKerjaRincianRKH.setText(rincianRkhParams.get(position).getWaktu());
        viewHolder.tvKMHMKerjaRincianRKH.setText(rincianRkhParams.get(position).getKilometer());
        viewHolder.tvPrestasiKerjaRincianRKH.setText(rincianRkhParams.get(position).getTargetKerja() + "  " + rincianRkhParams.get(position).getSatuanMuatan());

        return convertView;
    }

}