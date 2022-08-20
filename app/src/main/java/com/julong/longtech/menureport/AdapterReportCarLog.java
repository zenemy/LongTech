package com.julong.longtech.menureport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuinventory.PengeluaranBBM;
import com.julong.longtech.menuvehicle.VerifikasiGIS;

import java.util.ArrayList;
import java.util.List;

public class AdapterReportCarLog extends RecyclerView.Adapter<AdapterReportCarLog.CarLogReportHolder> {

    // List to store all the contact details
    private List<ListReportCarLog> carLogList;
    private Context mContext;
    private DatabaseHelper dbhelper;

    int isVerified;
    String teamCode;

    ActivityResultLauncher<Intent> intentLaunchGIS;

    // Counstructor for the Class
    public AdapterReportCarLog(List carLogList, Context context, ActivityResultLauncher<Intent> intentLaunchGIS,
                               int isVerified, String teamCode) {
        this.carLogList = carLogList;
        this.mContext = context;
        this.intentLaunchGIS = intentLaunchGIS;
        this.isVerified = isVerified;
        this.teamCode = teamCode;
        dbhelper = new DatabaseHelper(context);
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
        holder.setLocation(carLogReports.getBlokCode());
        holder.setCarKMHM(carLogReports.getKmAwal(), carLogReports.getKmAkhir());

        holder.layoutCarLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerified == 1) {

                } else if (isVerified == 0) {
                    if (dbhelper.get_tbl_username(3).equals("SPV-TRS")
                            || dbhelper.get_tbl_username(2).equals("GIS")
                            || dbhelper.get_tbl_username(3).equals("GIS")) {
                        Intent intent = new Intent(mContext, VerifikasiGIS.class);
                        intent.putExtra("teamcode", teamCode);
                        intent.putExtra("nodoc", carLogReports.getDocumentNumber());
                        intent.putExtra("workdate", carLogReports.getTglCarLog());
                        intent.putExtra("unitcode", carLogReports.getUnitCarLog());
                        intent.putExtra("drivername", carLogReports.getEmployeeName());
                        intent.putExtra("drivercode", carLogReports.getEmployeeCode());
                        intent.putExtra("kegiatanunit", carLogReports.getActivityLog());
                        intent.putExtra("blokcode", carLogReports.getBlokCode());
                        intentLaunchGIS.launch(intent);
                    }
                }
            }
        });
    }

    // This class that helps to populate data to the view
    public class CarLogReportHolder extends RecyclerView.ViewHolder {

        private CardView layoutCarLog;
        private TextView tvNoDoc, tvTglCarLog, tvVehicle, tvLocation,
                tvFirstName, tvLastName, tvCarKMHM, tvActivityLoad, tvCarLogResult;

        public CarLogReportHolder(View itemView) {
            super(itemView);

            layoutCarLog = itemView.findViewById(R.id.layoutCarLogReport);
            tvNoDoc = itemView.findViewById(R.id.tvNodocReportCarLog);
            tvTglCarLog = itemView.findViewById(R.id.tvDateReportCarLog);
            tvVehicle = itemView.findViewById(R.id.tvUnitReportCarLog);
            tvFirstName = itemView.findViewById(R.id.tvFirstNameReportCarLog);
            tvLastName = itemView.findViewById(R.id.tvLastNameReportCarLog);
            tvCarKMHM = itemView.findViewById(R.id.tvReportKMHMCarLog);
            tvActivityLoad = itemView.findViewById(R.id.tvLoadCategoryCarLogReport);
            tvCarLogResult = itemView.findViewById(R.id.tvHasilReportCarLog);
            tvLocation = itemView.findViewById(R.id.tvLocationCarLogReport);

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

        public void setLocation(String blokCode) {
            tvLocation.setText("Blok " + blokCode);
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
