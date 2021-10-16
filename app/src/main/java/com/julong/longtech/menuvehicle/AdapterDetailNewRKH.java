package com.julong.longtech.menuvehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDetailNewRKH extends RecyclerView.Adapter<AdapterDetailNewRKH.WorkDetailHolder> {
    // List to store all the contact details
    public static List<ListDetailNewRKH> workDetailList;
    private Context mContext;
    LayoutInflater layoutInflater;

    // Counstructor for the Class
    public AdapterDetailNewRKH(List workDetailList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.workDetailList = workDetailList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public WorkDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_workdetail_newrkh, parent, false);
        return new WorkDetailHolder(view);
    }

    @Override
    public int getItemCount() {
        return workDetailList == null? 0: workDetailList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull AdapterDetailNewRKH.WorkDetailHolder holder, final int position) {
        final ListDetailNewRKH workDetailPlans = workDetailList.get(position);

        holder.setLokasi(workDetailPlans.getDivision(), workDetailPlans.getLokasiKerja());
        holder.setAktivitas(workDetailPlans.getKegiatanKerja());
        holder.setTarget(workDetailPlans.getTargetKerja(), workDetailPlans.getSatuanKerja());


    }

    // This class that helps to populate data to the view
    public class WorkDetailHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextView tvlokasi, tvaktifitas, tvtarget;

        public WorkDetailHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            tvlokasi = (TextView) itemView.findViewById(R.id.tvNewLokasiRKH);
            tvaktifitas = (TextView) itemView.findViewById(R.id.tvNewActivityRKH);
            tvtarget = (TextView) itemView.findViewById(R.id.tvNewTargetKerjaRKH);

        }

        public void setLokasi(String division, String setLokasi) {
            tvlokasi.setText(division + " (" + setLokasi + ")");
        }

        public void setAktivitas(String setAktifitas) {
            tvaktifitas.setText(setAktifitas);
        }

        public void setTarget(String setTarget, String satuanKerja) {
            tvtarget.setText(setTarget + " " + satuanKerja);
        }
    }
}
