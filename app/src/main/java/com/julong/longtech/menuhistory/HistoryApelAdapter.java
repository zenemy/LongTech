package com.julong.longtech.menuhistory;

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

public class HistoryApelAdapter extends RecyclerView.Adapter<HistoryApelAdapter.ApelHolder> {

    // List to store all the contact details
    private List<ListHistoryApel> apelList;
    private Context mContext;

    // Counstructor for the Class
    public HistoryApelAdapter(List apelList, Context context) {
        this.apelList = apelList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ApelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_historyapelpagi, parent, false);
        return new ApelHolder(view);
    }

    @Override
    public int getItemCount() {
        return apelList == null? 0: apelList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ApelHolder holder, final int position) {
        final ListHistoryApel apelHistories = apelList.get(position);

        // Set the data to the views here
        holder.setDocumentNumber(apelHistories.getDocumentNumber());
        holder.setApelDate(apelHistories.getTglApel());
        holder.setApelTime(apelHistories.getWaktuApel());
        holder.setEmpName(apelHistories.getEmployeeName());
        holder.setEmpPosition(apelHistories.getEmpPosition());
        holder.setUploaded(apelHistories.getIsUploaded());
        holder.setKehadiranEmp(apelHistories.getKehadiranEmp());

        if (apelHistories.getFotoApel() != null) {
            holder.setApelImg(apelHistories.getFotoApel());
        }

        if (apelHistories.getMetodeAbsen() != null) {
            holder.setMetodeAbsen(apelHistories.getMetodeAbsen());
        }

    }

    // This class that helps to populate data to the view
    public class ApelHolder extends RecyclerView.ViewHolder {

        private TextView tvNoDoc, tvTglHistory, tvTimeHistory, tvEmpNameApel,
                tvPositionApel, tvKehadiranApel, tvMetodeAbsen;

        private ImageView imgViewApel, imgUploaded;

        public ApelHolder(View itemView) {
            super(itemView);

            tvNoDoc = (TextView) itemView.findViewById(R.id.tvNodocHistoryApel);
            tvTglHistory = (TextView) itemView.findViewById(R.id.tvHistoryDateApel);
            tvTimeHistory = (TextView) itemView.findViewById(R.id.tvHistoryTimeApel);
            tvEmpNameApel = (TextView) itemView.findViewById(R.id.tvEmpApelHistory);
            tvPositionApel = (TextView) itemView.findViewById(R.id.tvPositionApelHistory);
            tvKehadiranApel = (TextView) itemView.findViewById(R.id.tvKehadiranApelHistory);
            tvMetodeAbsen = (TextView) itemView.findViewById(R.id.tvMetodeAbsenApelHistory);
            imgViewApel = (ImageView) itemView.findViewById(R.id.imgHistoryApelPagi);
            imgUploaded = (ImageView) itemView.findViewById(R.id.imgUploadHistoryApel);

        }

        public void setDocumentNumber(String nodoc) {
            tvNoDoc.setText(nodoc);
        }

        public void setApelDate(String apelDate) {
            tvTglHistory.setText(apelDate);
        }

        public void setApelTime(String apelTime) {
            tvTimeHistory.setText(apelTime);
        }

        public void setEmpName(String empname) {
            tvEmpNameApel.setText(empname);
        }

        public void setEmpPosition(String empPosition) {
            tvPositionApel.setText(empPosition);
        }

        public void setKehadiranEmp(String kehadiranEmp) {
            tvKehadiranApel.setText(kehadiranEmp);
        }

        public void setMetodeAbsen(String metodeAbsen) {
            if (metodeAbsen != null) {
                tvMetodeAbsen.setText("(" + metodeAbsen + ")");
            }
        }

        public void setApelImg(byte[] fotoApel) {
            try {
                if (fotoApel != null) {
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(fotoApel, 0, fotoApel.length);
                    imgViewApel.setBackground(null);
                    imgViewApel.setImageBitmap(compressedBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
