package com.julong.longtech.menureport;

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

public class AdapterReportP2H extends RecyclerView.Adapter<AdapterReportP2H.HolderReportP2H> {

    // List to store all the contact details
    private List<ListReportP2H> listReportP2H;
    private Context mContext;

    // Counstructor for the Class
    public AdapterReportP2H(List listReportP2H, Context context) {
        this.listReportP2H = listReportP2H;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public HolderReportP2H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_reportp2h, parent, false);
        return new HolderReportP2H(view);
    }

    @Override
    public int getItemCount() {
        return listReportP2H == null? 0: listReportP2H.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull HolderReportP2H holder, final int position) {
        final ListReportP2H reportP2H = listReportP2H.get(position);

        // Set the data to the views here
        holder.setDocumentNumber(reportP2H.getDocumentNumber());
        holder.setVehicle(reportP2H.getVehicleCode());
        holder.setDate(reportP2H.getTglP2H());
        holder.setEmpName(reportP2H.getEmployeeName());
        holder.setActivityP2H(reportP2H.getItemP2H());
        holder.setNoteP2H(reportP2H.getNoteP2H());
    }

    // This class that helps to populate data to the view
    public class HolderReportP2H extends RecyclerView.ViewHolder {

        private TextView tvNoDoc, tvTglP2H, tvVehicle, tvDriver, tvActivity, tvNoteP2H;

        public HolderReportP2H(View itemView) {
            super(itemView);

            tvNoDoc = (TextView) itemView.findViewById(R.id.tvNodocReportP2H);
            tvTglP2H = (TextView) itemView.findViewById(R.id.tvInputDateReportP2H);
            tvVehicle = (TextView) itemView.findViewById(R.id.tvVehicleReportP2H);
            tvDriver = (TextView) itemView.findViewById(R.id.tvDriverReportP2H);
            tvActivity = (TextView) itemView.findViewById(R.id.tvKegiatanReportP2H);
            tvNoteP2H = (TextView) itemView.findViewById(R.id.tvNoteReportP2H);

        }

        public void setDocumentNumber(String nodoc) {
            tvNoDoc.setText(nodoc);
        }

        public void setVehicle(String unitCode) {
            tvVehicle.setText(unitCode);
        }

        public void setDate(String date) {
            tvTglP2H.setText(date);
        }

        public void setEmpName(String empname) {
            tvDriver.setText(empname);
        }

        public void setActivityP2H(String activityP2H) {
            tvActivity.setText(activityP2H);
        }

        public void setNoteP2H(String noteP2H) {
            if (noteP2H.length() > 0) {
                tvNoteP2H.setVisibility(View.VISIBLE);
                tvNoteP2H.setText(noteP2H);
            }

        }


    }
}
