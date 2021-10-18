package com.julong.longtech.menuvehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menureport.AdapterReportCarLog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterResultCarLog extends RecyclerView.Adapter<AdapterResultCarLog.ResultDetailHolder> {
    // List to store all the contact details
    public static List<ListResultCarLog> resultDetailList;
    private Context mContext;
    LayoutInflater layoutInflater;

    // Counstructor for the Class
    public AdapterResultCarLog(List resultDetailList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.resultDetailList = resultDetailList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ResultDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_carlogresult, parent, false);
        return new ResultDetailHolder(view);
    }

    @Override
    public int getItemCount() {
        return resultDetailList == null? 0: resultDetailList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull AdapterResultCarLog.ResultDetailHolder holder, final int position) {
        final ListResultCarLog workDetailPlans = resultDetailList.get(position);

        holder.setLokasi(workDetailPlans.getDivision(), workDetailPlans.getBlokLocation());
        holder.setAktivitas(workDetailPlans.getActivityType());
        holder.setTarget(workDetailPlans.getHasilKerja(), workDetailPlans.getSatuanKerja());
        holder.setInputDate(workDetailPlans.getVehicleCode(), workDetailPlans.getInputDate());

    }

    // This class that helps to populate data to the view
    public class ResultDetailHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextView tvLokasi, tvAktifitas, tvTarget, tvInputDate;

        public ResultDetailHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            tvAktifitas = itemView.findViewById(R.id.tvActivityLvCarLog);
            tvLokasi = itemView.findViewById(R.id.tvLokasiKerjaCarLog);
            tvTarget = itemView.findViewById(R.id.tvHasilKerjaLvCarLog);
            tvInputDate = itemView.findViewById(R.id.tvInputDateLvNewCarLog);
        }

        public void setAktivitas(String setAktifitas) {
            tvAktifitas.setText(setAktifitas);
        }

        public void setLokasi(String divison, String blokLocation) {
            if (blokLocation.length() > 0) {
                tvLokasi.setText(divison + " (" + blokLocation + ")");
            } else {
                tvLokasi.setText(divison);
            }
        }

        public void setTarget(String setTarget, String satuanKerja) {
            tvTarget.setText(setTarget + " " + satuanKerja);
        }

        public void setInputDate(String unitCode, String timeInput) {
            tvInputDate.setText(unitCode + " | " + timeInput);
        }
    }
}
