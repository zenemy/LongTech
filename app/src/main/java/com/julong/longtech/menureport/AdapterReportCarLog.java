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

public class AdapterReportCarLog extends RecyclerView.Adapter<AdapterReportCarLog.CarLogReportHolder> {

    // List to store all the contact details
    private List<ListReportCarLog> carLogList;
    private Context mContext;

    // Counstructor for the Class
    public AdapterReportCarLog(List carLogList, Context context) {
        this.carLogList = carLogList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public CarLogReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_reportcarlog, parent, false);
        return new CarLogReportHolder(view);
    }

    @Override
    public int getItemCount() {
        return carLogList == null? 0: carLogList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull CarLogReportHolder holder, final int position) {
        final ListReportCarLog carLogReports = carLogList.get(position);

        // Set the data to the views here
        holder.setDocumentNumber(carLogReports.getDocumentNumber());
        holder.setVehicle(carLogReports.getUnitCarLog());
        holder.setDate(carLogReports.getTglCarLog(), carLogReports.getWaktuCarLog());
        holder.setEmpName(carLogReports.getFirstName(), carLogReports.getLastName());
        holder.setCarLogResult(carLogReports.getHasilPekerjaan(), carLogReports.getSatuanPekerjaan());
        holder.setActivityLoad(carLogReports.getActivityLog());
        holder.setCarKMHM(carLogReports.getKmAwal(), carLogReports.getKmAkhir());
    }

    // This class that helps to populate data to the view
    public class CarLogReportHolder extends RecyclerView.ViewHolder {

        private TextView tvNoDoc, tvTglCarLog, tvVehicle, tvFirstName, tvLastName,
                tvCarKMHM, tvActivityLoad, tvCarLogResult;

        public CarLogReportHolder(View itemView) {
            super(itemView);

            tvNoDoc = (TextView) itemView.findViewById(R.id.tvNodocReportCarLog);
            tvTglCarLog = (TextView) itemView.findViewById(R.id.tvDateReportCarLog);
            tvVehicle = (TextView) itemView.findViewById(R.id.tvUnitReportCarLog);
            tvFirstName = (TextView) itemView.findViewById(R.id.tvFirstNameReportCarLog);
            tvLastName = (TextView) itemView.findViewById(R.id.tvLastNameReportCarLog);
            tvCarKMHM = (TextView) itemView.findViewById(R.id.tvReportKMHMCarLog);
            tvActivityLoad = (TextView) itemView.findViewById(R.id.tvLoadCategoryCarLogReport);
            tvCarLogResult = (TextView) itemView.findViewById(R.id.tvHasilReportCarLog);

        }

        public void setDocumentNumber(String nodoc) {
            tvNoDoc.setText(nodoc);
        }

        public void setVehicle(String unitCode) {
            tvVehicle.setText(unitCode);
        }

        public void setDate(String date, String time) {
            tvTglCarLog.setText(date + " | " + time);
        }

        public void setEmpName(String firstname, String lastname) {
            tvFirstName.setText(firstname);

            if (lastname.equals(firstname)) {
                tvLastName.setVisibility(View.GONE);
            } else {
                tvLastName.setText(lastname);
            }
        }

        public void setActivityLoad(String activityLoad) {
            tvActivityLoad.setText(activityLoad);
        }

        public void setCarLogResult(String totalResult, String unitOfMeasure) {
            tvCarLogResult.setText(totalResult + " (" + unitOfMeasure + ")");
        }

        public void setCarKMHM(String awalKMHM, String akhirKHM) {
            tvCarKMHM.setText(awalKMHM + " — " + akhirKHM);
        }

    }
}
