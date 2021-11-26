package com.julong.longtech.menureport;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.R;
import com.julong.longtech.menuhistory.ListHistoryRKH;
import com.julong.longtech.menuinventory.PengeluaranBBM;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterReportMintaBBM extends RecyclerView.Adapter<AdapterReportMintaBBM.HolderBBM> {

    // List to store all the contact details
    private List<ListReportMintaBBM> historyPlans;
    private Context mContext;

    // Counstructor for the Class
    public AdapterReportMintaBBM(List historyPlans, Context context) {
        this.historyPlans = historyPlans;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public HolderBBM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_report_mintabbm, parent, false);
        return new HolderBBM(view);
    }

    @Override
    public int getItemCount() {
        return historyPlans == null? 0: historyPlans.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull HolderBBM holderMinyak, final int position) {
        final ListReportMintaBBM minyakReports = historyPlans.get(position);

        // Set the data to the views here
        holderMinyak.setEmployee(minyakReports.getEmployeeName());
        holderMinyak.setTime(minyakReports.getWaktuMintaBBM());
        holderMinyak.setVehicle(minyakReports.getVehicleCode());
        holderMinyak.setLiterBBM(minyakReports.getJumlahLiter());

        holderMinyak.layoutMintaBBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PengeluaranBBM.class);
                intent.putExtra("requester", minyakReports.getEmployeeName());
                intent.putExtra("literjml", minyakReports.getJumlahLiter());
                intent.putExtra("unitcode", minyakReports.getVehicleCode());
                mContext.startActivity(intent);
            }
        });
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class HolderBBM extends RecyclerView.ViewHolder {

        private CardView layoutMintaBBM;
        private TextView tvWaktuMintaBBM, tvUnit, tvEmployeeName, tvJumlahLiter;

        public HolderBBM(View itemView) {
            super(itemView);

            tvUnit = itemView.findViewById(R.id.tvVehicleReportMintaBBM);
            tvJumlahLiter = itemView.findViewById(R.id.tvInfoLiterReportBBM);
            layoutMintaBBM = itemView.findViewById(R.id.layoutReportMintaBBM);
            tvWaktuMintaBBM = itemView.findViewById(R.id.tvTimeReportMintaBBM);
            tvEmployeeName = itemView.findViewById(R.id.tvEmpNameReportMintaBBM);
        }

        public void setVehicle(String vehicleCode) {
            tvUnit.setText(vehicleCode);
        }

        public void setTime(String timeInput) {
            tvWaktuMintaBBM.setText(timeInput);
        }

        public void setEmployee(String employeeName) {
            tvEmployeeName.setText(employeeName);
        }

        public void setLiterBBM(Integer literBBM) {
            tvJumlahLiter.setText(literBBM + " Liter");
        }

    }
}
