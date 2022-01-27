package com.julong.longtech.menuhcm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.google.android.material.textfield.TextInputLayout;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApelPagiAdapter extends RecyclerView.Adapter<ApelPagiAdapter.MaterialHolder> {

    // List to store all the contact details
    public static List<ApelPagiList> empList;
    private Context mContext;
    LayoutInflater layoutInflater;
    String latAbsen, longAbsen;

    // Counstructor for the Class
    public ApelPagiAdapter(List empList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.empList = empList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.apelpagi_adapter, parent, false);
        return new MaterialHolder(view);
    }

    @Override
    public int getItemCount() {
        return empList == null? 0: empList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MaterialHolder holder, final int position) {
        final ApelPagiList empBriefing = empList.get(position);

        holder.setEmployee(empBriefing.getEmployeeName());

        // Text watcher, important to get adapter value same as edittext value
        holder.setPosition(empBriefing.getPositionName(), empBriefing.getVehicleCode());

        // Set kehadiran employee to KJ
        holder.btnMasuk.setOnClickListener(view -> holder.absenAnggotaMasuk(empBriefing.getEmployeeCode(),
                empBriefing.getEmployeeName(), empBriefing.getPositionCode(), empBriefing.getVehicleCode()));

        // Set kehadiran employee yang tdk masuk
        holder.btnTidakMasuk.setOnClickListener(view -> holder.absenAnggotaTdkMasuk(empBriefing.getEmployeeCode(),
                empBriefing.getEmployeeName(), empBriefing.getPositionCode(), empBriefing.getVehicleCode()));

        holder.setCheckedEmployee(empBriefing.getEmployeeCode());

        holder.tvKeteranganAbsen.setText(empBriefing.getAbsenValue());

    }

    // This class that helps to populate data to the view
    public class MaterialHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;
        DialogHelper dlgHelper;

        private TextView tvEmployee, tvPosition, tvKeteranganAbsen;
        private Button btnMasuk, btnTidakMasuk;
        private ImageView imgViewAbsenIndicator;

        public MaterialHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            dlgHelper = new DialogHelper(mContext);

            tvEmployee = (TextView) itemView.findViewById(R.id.tvEmpApelPagiLV);
            tvPosition = (TextView) itemView.findViewById(R.id.tvJabatanApelPagiLv);
            tvKeteranganAbsen = (TextView) itemView.findViewById(R.id.tvTipeKehadiranLvApel);
            btnMasuk = (Button) itemView.findViewById(R.id.btnMasukLvApel);
            btnTidakMasuk = (Button) itemView.findViewById(R.id.btnTdkHadirLvApel);
            imgViewAbsenIndicator = (ImageView) itemView.findViewById(R.id.imgViewAdapterApel);

        }

        public void setEmployee(String employee) {
            if (employee.contains(" ")) {
                int indexEnd = employee.indexOf(" ");
                tvEmployee.setText(employee.substring(0, indexEnd));
            } else {
                tvEmployee.setText(employee);
            }

        }

        public void setPosition(String positionName, String vehicleCode) {
            tvPosition.setText(positionName + " " + vehicleCode);
        }

        public void absenAnggotaMasuk(String employeeCode, String employeeName,
                                      String positionCode, String vehicleCode) {
            getLocation();

            dbhelper.insert_briefing_anggota(employeeCode,
                    positionCode, vehicleCode, "BRIEFING", "KJ", null, latAbsen, longAbsen);
            notifyDataSetChanged();
        }

        public void absenAnggotaTdkMasuk(String empCode, String empName,
                                      String positionCode, String vehicleCode) {
            // Show dialog tidak hadir
            getLocation();
            dlgHelper.showDlgBriefingTdkHadir(empCode, empName, positionCode,
                    vehicleCode, latAbsen, longAbsen);
        }

        public void setCheckedEmployee(String employeeCode) {

            empList.get(getAdapterPosition()).setAbsenValue(dbhelper.get_infokehadiran_apel(employeeCode));

            if (empList.get(getAdapterPosition()).getAbsenValue().equals("Kerja")) {
                imgViewAbsenIndicator.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.coloran));
            }
            else if (empList.get(getAdapterPosition()).getAbsenValue().equals("N/A")) {
                imgViewAbsenIndicator.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.silver));
            } else {
                imgViewAbsenIndicator.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.lightcoral));
            }

        }

    }

    public void getLocation() { // Get user current coordinate
        GPSTracker gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latAbsen = String.valueOf(latitude);
            longAbsen = String.valueOf(longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
}
