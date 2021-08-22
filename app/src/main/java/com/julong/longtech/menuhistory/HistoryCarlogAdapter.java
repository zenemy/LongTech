package com.julong.longtech.menuhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.List;

public class HistoryCarlogAdapter extends ArrayAdapter<ListHistoryCarLog> {

    //storing all the names in the list
    private List<ListHistoryCarLog> listHistoryCarLogs;
    DatabaseHelper dbhelper;

    private Context context;

    //constructor
    public HistoryCarlogAdapter(Context context, int resource, List<ListHistoryCarLog> listHistoryCarLogs) {
        super(context, resource, listHistoryCarLogs);
        this.context = context;
        this.listHistoryCarLogs = listHistoryCarLogs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.listview_historycarlog, null, true);
        final TextView tvNoDoc = (TextView) listViewItem.findViewById(R.id.tvNodocHistoryCarLog);
        final TextView tvTglHistory = (TextView) listViewItem.findViewById(R.id.tvHistoryDateCarLog);
        final TextView tvUnitCarLog = (TextView) listViewItem.findViewById(R.id.tvHistoryVehicleCarLog);
        final TextView tvKmCarLog = (TextView) listViewItem.findViewById(R.id.tvHistoryKMHMCarLog);
        final TextView tvLoadType = (TextView) listViewItem.findViewById(R.id.tvHistoryMuatanCarLog);
        final TextView tvHasilHistory = (TextView) listViewItem.findViewById(R.id.tvHasilHistoryCarLog);
        final TextView tvSatuanHasil = (TextView) listViewItem.findViewById(R.id.tvSatuanHistoryCarLog);
        final ImageView imgUploaded = (ImageView) listViewItem.findViewById(R.id.imgUploadHistoryCarLog);
        final ListHistoryCarLog historyCarLog = listHistoryCarLogs.get(position);

        tvNoDoc.setText(historyCarLog.getDocumentNumber());
        tvTglHistory.setText(historyCarLog.getWaktuAwal());
        tvUnitCarLog.setText(historyCarLog.getUnitCarLog());
        tvKmCarLog.setText(historyCarLog.getKmAwal() + " — " + historyCarLog.getKmAkhir());
        tvLoadType.setText(historyCarLog.getJenisMuatan() + " (" + historyCarLog.getKategoriMuatan() + ")");
        tvHasilHistory.setText(historyCarLog.getHasilPekerjaan());
        tvSatuanHasil.setText(historyCarLog.getSatuanPekerjaan());

        if (historyCarLog.getIsUploaded() == 0 ) {
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else {
            imgUploaded.setImageResource(R.drawable.bluetick);
        }

        return listViewItem;
    }

}
