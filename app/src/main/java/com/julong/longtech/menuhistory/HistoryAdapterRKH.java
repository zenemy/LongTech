package com.julong.longtech.menuhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.R;

import org.w3c.dom.Text;

import java.util.List;

public class HistoryAdapterRKH extends RecyclerView.Adapter<HistoryAdapterRKH.HolderRKH> {

    // List to store all the contact details
    private List<ListHistoryRKH> historyPlans;
    private Context mContext;

    // Counstructor for the Class
    public HistoryAdapterRKH(List historyPlans, Context context) {
        this.historyPlans = historyPlans;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public HolderRKH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.fragment_historyrkh, parent, false);
        return new HolderRKH(view);
    }

    @Override
    public int getItemCount() {
        return historyPlans == null? 0: historyPlans.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull HolderRKH holderRKH, final int position) {
        final ListHistoryRKH historyDailyPlans = historyPlans.get(position);

        // Set the data to the views here
        if (historyDailyPlans.getItemdata().equals("DETAIL2")) {
            holderRKH.layoutDriver.setVisibility(View.GONE);
            holderRKH.layoutAktifitasLokasi.setVisibility(View.GONE);
        }

        if (historyDailyPlans.getItemdata().equals("DETAIL1")) {
            holderRKH.layoutDriver.setVisibility(View.GONE);
            holderRKH.layoutAktifitasLokasi.setVisibility(View.VISIBLE);
        }

        holderRKH.setLokasi(historyDailyPlans.getDivision(), historyDailyPlans.getBlokCode());
        holderRKH.setActivity(historyDailyPlans.getActivityName());
        holderRKH.setTargetKerja(historyDailyPlans.getTargetKerja(), historyDailyPlans.getSatuanKerja());

        holderRKH.setVehicle(historyDailyPlans.getUnitCode());

        holderRKH.setInputTime(historyDailyPlans.getInputTime());

        holderRKH.setUploaded(historyDailyPlans.getIsUploaded());

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class HolderRKH extends RecyclerView.ViewHolder {

        private TextView  tvUnitRKH, tvLokasi, tvActivity, tvTargetKerja, tvInputTime,
                tvEmployeeName, tvEmployeeCode;
        private LinearLayout layoutAktifitasLokasi;
        private ConstraintLayout layoutDriver;
        private ImageView imgUploaded;

        public HolderRKH(View itemView) {
            super(itemView);

            tvUnitRKH = itemView.findViewById(R.id.tvVehicleHistoryRKH);
            tvLokasi = itemView.findViewById(R.id.tvLokLvNewRKH);
            tvActivity = itemView.findViewById(R.id.tvActivityLvNewRKH);
            tvTargetKerja = itemView.findViewById(R.id.tvTargetKerjaHistoryNewRKH);
            tvInputTime = itemView.findViewById(R.id.tvDateNewRKH);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeNameLvNewRKH);
            tvEmployeeCode = itemView.findViewById(R.id.tvEmployeeCodeLvNewRKH);
            layoutAktifitasLokasi = itemView.findViewById(R.id.layoutDetailLvHistoryRKH);
            layoutDriver = itemView.findViewById(R.id.layoutDriverLvHistoryRKH);

            imgUploaded = itemView.findViewById(R.id.imgUploadHistoryRKH);

        }

        public void setVehicle(String vehicle) {
            tvUnitRKH.setText(vehicle);
        }

        public void setTargetKerja(String TargetKerja, String SatuanKerja) {
            tvTargetKerja.setText("Target " + TargetKerja + " " + SatuanKerja);
        }

        public void setActivity(String activityName) {
            tvActivity.setText(activityName);
        }

        public void setInputTime(String InputTime) {
            tvInputTime.setText(InputTime);
        }

        public void setLokasi(String division, String setLokasi) {
            tvLokasi.setText(division + " (" + setLokasi + ")");
        }

        public void setUploaded(int isUploaded) {
            if (isUploaded == 0 ) {
                imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
            }
            else if (isUploaded == 1) {
                imgUploaded.setImageResource(R.drawable.bluetick);
            }
            else {
                imgUploaded.setVisibility(View.GONE);
            }
        }
    }
}
