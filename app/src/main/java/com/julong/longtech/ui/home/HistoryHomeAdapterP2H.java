package com.julong.longtech.ui.home;

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

public class HistoryHomeAdapterP2H extends RecyclerView.Adapter<HistoryHomeAdapterP2H.HolderP2H> {

    // List to store all the contact details
    private List<ListHistoryHomeP2H> historyP2H;
    private Context mContext;

    // Counstructor for the Class
    public HistoryHomeAdapterP2H(List historyP2H, Context context) {
        this.historyP2H = historyP2H;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public HolderP2H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.fragment_historyp2h, parent, false);
        return new HolderP2H(view);
    }

    @Override
    public int getItemCount() {
        return historyP2H == null? 0: historyP2H.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull HolderP2H holderP2H, final int position) {
        final ListHistoryHomeP2H historyItemChecks = historyP2H.get(position);

        holderP2H.setVehicle(historyItemChecks.getVehicleName());
        holderP2H.setItemsP2H(historyItemChecks.getItemsP2H());
        holderP2H.setNoteP2H(historyItemChecks.getNotes());

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class HolderP2H extends RecyclerView.ViewHolder {

        private TextView  tvUnitP2H, tvItemP2H, tvNotesP2H;

        public HolderP2H(View itemView) {
            super(itemView);

            tvUnitP2H = itemView.findViewById(R.id.tvUnitLvHistoryP2H);
            tvItemP2H = itemView.findViewById(R.id.tvKegiatanLvHistoryP2H);
            tvNotesP2H = itemView.findViewById(R.id.tvNoteLvHistoryP2H);
        }

        public void setVehicle(String vehicleCode) {
            tvUnitP2H.setText(vehicleCode);
        }

        public void setItemsP2H(String itemsP2H) {
            tvItemP2H.setText(itemsP2H);
        }

        public void setNoteP2H(String noteP2H) {
            if (noteP2H.length() > 0) {
                tvNotesP2H.setVisibility(View.VISIBLE);
                tvNotesP2H.setText(noteP2H);
            }
        }

    }
}
