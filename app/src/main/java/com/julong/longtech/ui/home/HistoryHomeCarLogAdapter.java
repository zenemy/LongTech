package com.julong.longtech.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.List;

public class HistoryHomeCarLogAdapter extends ArrayAdapter<ListHistoryHomeCarLog> {

    //storing all the names in the list
    private List<ListHistoryHomeCarLog> listHistoryCarLogs;
    DatabaseHelper dbhelper;

    private Context context;

    //constructor
    public HistoryHomeCarLogAdapter(Context context, int resource, List<ListHistoryHomeCarLog> listHistoryCarLogs) {
        super(context, resource, listHistoryCarLogs);
        this.context = context;
        this.listHistoryCarLogs = listHistoryCarLogs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.fragment_carloghistory, null, true);
        final TextView tvNoDoc = (TextView) listViewItem.findViewById(R.id.tvNodocHistoryHomeCarLog);
        final TextView tvTglHistory = (TextView) listViewItem.findViewById(R.id.tvHistoryHomeDateCarLog);
        final TextView tvUnitCarLog = (TextView) listViewItem.findViewById(R.id.tvHistoryHomeVehicleCarLog);
        final TextView tvKmCarLog = (TextView) listViewItem.findViewById(R.id.tvHistoryHomeKMHMCarLog);
        final TextView tvLoadType = (TextView) listViewItem.findViewById(R.id.tvHistoryHomeMuatanCarLog);
        final TextView tvHasilHistory = (TextView) listViewItem.findViewById(R.id.tvHasilHistoryHomeCarLog);
        final TextView tvSatuanHasil = (TextView) listViewItem.findViewById(R.id.tvSatuanHistoryHomeCarLog);
        final ImageView imgUploaded = (ImageView) listViewItem.findViewById(R.id.imgUploadHistoryHomeCarLog);
        final ListHistoryHomeCarLog historyCarLog = listHistoryCarLogs.get(position);

        tvNoDoc.setText(historyCarLog.getDocumentNumber());
        tvTglHistory.setText(historyCarLog.getWaktuAwal() + " — " + historyCarLog.getWaktuAkhir());
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
