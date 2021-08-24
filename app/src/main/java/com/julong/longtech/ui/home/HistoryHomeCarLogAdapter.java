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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryHomeCarLogAdapter extends RecyclerView.Adapter<HistoryHomeCarLogAdapter.CarLogHolder> {

    // List to store all the contact details
    private List<ListHistoryHomeCarLog> carLogs;
    private Context mContext;

    // Counstructor for the Class
    public HistoryHomeCarLogAdapter(List carLogs, Context context) {
        this.carLogs = carLogs;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public CarLogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.fragment_carloghistory, parent, false);
        return new CarLogHolder(view);
    }

    @Override
    public int getItemCount() {
        return carLogs == null? 0: carLogs.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull CarLogHolder holder, final int position) {
        final ListHistoryHomeCarLog historyCarLogs = carLogs.get(position);

        // Set the data to the views here
        holder.setDocumentNumber(historyCarLogs.getDocumentNumber());
        holder.setLogTime(historyCarLogs.getWaktuAwal());
        holder.setVehicle(historyCarLogs.getUnitCarLog());
        holder.setKMHM(historyCarLogs.getKmAwal(), historyCarLogs.getKmAkhir());
        holder.setLoadType(historyCarLogs.getJenisMuatan(), historyCarLogs.getKategoriMuatan());
        holder.setResult(historyCarLogs.getHasilPekerjaan(), historyCarLogs.getSatuanPekerjaan());
        holder.setUploaded(historyCarLogs.getIsUploaded());

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class CarLogHolder extends RecyclerView.ViewHolder {

        private TextView tvNoDoc, tvTglHistory, tvUnitCarLog, tvKmCarLog,
        tvLoadType, tvHasilHistory, tvSatuanHasil;

        private ImageView imgUploaded;

        public CarLogHolder(View itemView) {
            super(itemView);

            tvNoDoc = (TextView) itemView.findViewById(R.id.tvNodocHistoryHomeCarLog);
            tvTglHistory = (TextView) itemView.findViewById(R.id.tvHistoryHomeDateCarLog);
            tvUnitCarLog = (TextView) itemView.findViewById(R.id.tvHistoryHomeVehicleCarLog);
            tvKmCarLog = (TextView) itemView.findViewById(R.id.tvHistoryHomeKMHMCarLog);
            tvLoadType = (TextView) itemView.findViewById(R.id.tvHistoryHomeMuatanCarLog);
            tvHasilHistory = (TextView) itemView.findViewById(R.id.tvHasilHistoryHomeCarLog);
            tvSatuanHasil = (TextView) itemView.findViewById(R.id.tvSatuanHistoryHomeCarLog);
            imgUploaded = (ImageView) itemView.findViewById(R.id.imgUploadHistoryHomeCarLog);

        }

        public void setDocumentNumber(String nodoc) {
            tvNoDoc.setText(nodoc);
        }

        public void setLogTime(String startTime) {
            tvTglHistory.setText(startTime);
        }

        public void setVehicle(String vehicle) {
            tvUnitCarLog.setText("(" + vehicle + ")");
        }

        public void setKMHM(String kmhmAwal, String kmhmAkhir) {
            tvKmCarLog.setText(kmhmAwal + " — " + kmhmAkhir);
        }

        public void setLoadType(String loadType, String loadCategory) {
            tvLoadType.setText(loadType + " (" + loadCategory + ")");
        }

        public void setResult(String hasilkerja, String satuankerja) {
            tvHasilHistory.setText(hasilkerja);
            tvSatuanHasil.setText(satuankerja);
        }

        public void setUploaded(int isUploaded) {
            if (isUploaded == 0 ) {
                imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
            } else {
                imgUploaded.setImageResource(R.drawable.bluetick);
            }
        }
    }
}
