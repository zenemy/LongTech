package com.julong.longtech.menureport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.R;
import com.julong.longtech.menuhistory.ListHistoryRKH;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterReportRKH extends RecyclerView.Adapter<AdapterReportRKH.HolderRKH> {

    // List to store all the contact details
    private List<ListHistoryRKH> historyPlans;
    private Context mContext;

    // Counstructor for the Class
    public AdapterReportRKH(List historyPlans, Context context) {
        this.historyPlans = historyPlans;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public HolderRKH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_historyrkh, parent, false);
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
        holderRKH.setWorkDate(historyDailyPlans.getInputTime());
        holderRKH.setVehicle(historyDailyPlans.getUnitCode());
        holderRKH.setActivity(historyDailyPlans.getActivityName());
        holderRKH.setLocation(historyDailyPlans.getDivision(), historyDailyPlans.getBlokCode());
        holderRKH.setTargetKerja(historyDailyPlans.getTargetKerja(), historyDailyPlans.getSatuanKerja());
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class HolderRKH extends RecyclerView.ViewHolder {

        private TextView tvTglPelaksanaan, tvUnitRKH, tvActivityName, tvLocationName, tvTargetKerja;

        public HolderRKH(View itemView) {
            super(itemView);

            tvTglPelaksanaan = itemView.findViewById(R.id.tvDateReportRKH);
            tvUnitRKH = itemView.findViewById(R.id.tvVehicleReportRKH);
            tvActivityName = itemView.findViewById(R.id.tvActivityReportRKH);
            tvLocationName = itemView.findViewById(R.id.tvLocationReportRKH);
            tvTargetKerja = itemView.findViewById(R.id.tvTargetKerjaReportRKH);

        }

        public void setWorkDate(String workDate) {
            tvTglPelaksanaan.setText(workDate);
        }

        public void setVehicle(String vehicle) {
            tvUnitRKH.setText(vehicle);
        }

        public void setActivity(String activityName) {
            tvActivityName.setText(activityName);
        }

        public void setLocation(String division, String blokcode) {
            tvLocationName.setText(division + " (" + blokcode + ")");
        }

        public void setTargetKerja(String targetKerja, String unitOfMeasure) {
            tvTargetKerja.setText("Target " + targetKerja + " " + unitOfMeasure);
        }

    }
}
