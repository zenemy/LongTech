package com.julong.longtech.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.UploadData;

import java.util.List;

public class AdapterHomeInfo extends ArrayAdapter<ParamListHomeInfo> {

    //storing all the names in the list
    private List<ParamListHomeInfo> listHomeInfos;
    DatabaseHelper dbhelper;

    //context object
    private Context context;
    //constructor
    public AdapterHomeInfo(Context context, List<ParamListHomeInfo> listHomeInfos) {
        super(context, R.layout.item_lvworkinfohome, listHomeInfos);
        this.context = context;
        this.listHomeInfos = listHomeInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.item_lvworkinfohome, null, true);
        final ConstraintLayout layoutLvInfo = (ConstraintLayout) listViewItem.findViewById(R.id.layoutLvInfo);
        final TextView tvWorkType = (TextView) listViewItem.findViewById(R.id.tvWorkTypeInfoHome);
        final TextView tvWorkStatus = (TextView) listViewItem.findViewById(R.id.tvWorkStatusInfoHome);
        final TextView tvTransactionTime = (TextView) listViewItem.findViewById(R.id.tvTransactionInfoTime);
        final TextView tvNoteTambahan = (TextView) listViewItem.findViewById(R.id.tvNoteTambahanLvInfo);
        final TextView tvInfoNoteTambahan = (TextView) listViewItem.findViewById(R.id.tvNoteTambahanLvInfo2);
        final ImageView imgUploaded = (ImageView) listViewItem.findViewById(R.id.imgUploadInfoHome);
        final ParamListHomeInfo listInfo = listHomeInfos.get(position);

        tvWorkType.setText(listInfo.getDatatype());

        // Keterangan tambahan
        if (listInfo.getMenucode().equals("010104")) {
            try {
                if (dbhelper.get_statuscheckinout_absmdr(listInfo.getDocumentnumber()).equals("CHECKIN")) {
                    tvNoteTambahan.setText("ABSEN MASUK");
                } else {
                    tvNoteTambahan.setText("ABSEN PULANG");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //p2h
        if (listInfo.getMenucode().equals("020202")) {
            tvNoteTambahan.setText(dbhelper.get_unitP2H_fragmentinfo(listInfo.getDocumentnumber()));
        }
        //carlog
        if (listInfo.getMenucode().equals("020203")) {
            tvNoteTambahan.setText(dbhelper.get_unitcarlog_fragmentinfo(listInfo.getDocumentnumber()));
        }
        //permintaan bbm
        if (listInfo.getMenucode().equals("030101")) {
            tvNoteTambahan.setText(dbhelper.get_permintaanbbm_fragmentinfo(listInfo.getDocumentnumber(),0));
            tvInfoNoteTambahan.setText(dbhelper.get_permintaanbbm_fragmentinfo(listInfo.getDocumentnumber(),1)+" Liter");
        }
        //penerimaanbbm
        if (listInfo.getMenucode().equals("030103")) {
            tvNoteTambahan.setText(dbhelper.get_penerimaanbbm_fragmentinfo(listInfo.getDocumentnumber(), 0));
            tvInfoNoteTambahan.setText(dbhelper.get_penerimaanbbm_fragmentinfo(listInfo.getDocumentnumber(), 1) + " Liter");
        }
        //permintaanperbaikan
        if (listInfo.getMenucode().equals("020101")) {
            tvNoteTambahan.setText(dbhelper.get_permintaanperbaikan_fragmentinfo(listInfo.getDocumentnumber(), 0));
            tvInfoNoteTambahan.setText(dbhelper.get_permintaanperbaikan_fragmentinfo(listInfo.getDocumentnumber(), 1));
        }

        tvTransactionTime.setText(listInfo.getTransactiondate());

        if (listInfo.getWorkstatus().equals("0")) {
            tvWorkStatus.setText("Pekerjaan belum diupload");
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
            layoutLvInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UploadData.class);
                    ((MainActivity) context).startActivityForResult(intent, 727);
                }
            });
        }
        else if (listInfo.getTransactiondate().equals("") && listInfo.getWorkstatus().equals("")) {
            tvWorkStatus.setText("Pekerjaan belum selesai");
            tvWorkStatus.setTextColor(Color.RED);
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else if (listInfo.getTransactiondate().length() > 0 && listInfo.getWorkstatus().equals("")) {
            tvWorkStatus.setText("Pekerjaan belum selesai");
            tvWorkStatus.setTextColor(Color.RED);
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else {
            tvWorkStatus.setText("Pekerjaan sudah diupload");
            imgUploaded.setImageResource(R.drawable.bluetick);
        }

        return listViewItem;
    }

}
