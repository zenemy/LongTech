package com.julong.longtech.menuworkshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;

import java.util.ArrayList;
import java.util.List;

public class AdapterMekanikPerintahService extends RecyclerView.Adapter<AdapterMekanikPerintahService.MekanikHolder> {

    // List to store all the contact details
    private List<ListMekanikPerintahService> mekanikList;
    private Context mContext;

    // Counstructor for the Class
    public AdapterMekanikPerintahService(List mekanikList, Context context) {
        this.mekanikList = mekanikList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public MekanikHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_mekanik, parent, false);
        return new MekanikHolder(view);
    }

    @Override
    public int getItemCount() {
        return mekanikList == null? 0: mekanikList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MekanikHolder holder, final int position) {
        final ListMekanikPerintahService mekanikServices = mekanikList.get(position);

        // Set the data to the views here
        holder.setMekanikName(mekanikServices.getEmployeeName());
        holder.setMekanikCode(mekanikServices.getEmployeeCode());

        holder.layoutLvMekanik.setOnClickListener(view -> holder.setCheckedLayout(mekanikServices.getEmployeeCode()));
        holder.checkBoxMekanikLv.setOnClickListener(view -> holder.setCheckboxMekanik(mekanikServices.getEmployeeCode()));

        holder.getCheckedLayout(mekanikServices.getEmployeeCode());
    }

    // This class that helps to populate data to the view
    public class MekanikHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextView tvMekanikName, tvMekanikCode;
        private ConstraintLayout layoutLvMekanik;
        private CheckBox checkBoxMekanikLv;
        private String checkedMekanik;

        public MekanikHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            tvMekanikCode = (TextView) itemView.findViewById(R.id.tvMekanikCodeMintaService);
            tvMekanikName = (TextView) itemView.findViewById(R.id.tvMekanikLvMintaService);
            layoutLvMekanik = (ConstraintLayout) itemView.findViewById(R.id.layoutLvMekanik);
            checkBoxMekanikLv = (CheckBox) itemView.findViewById(R.id.checkBoxMekanikLv);

        }

        public void setMekanikName(String mekanikName) {
            tvMekanikName.setText(mekanikName);
        }

        public void setMekanikCode(String mekanikCode) {
            tvMekanikCode.setText(mekanikCode);
        }

        public void setCheckedLayout(String mekanikCode) {
            if (checkBoxMekanikLv.isChecked()) {
                checkBoxMekanikLv.setChecked(false);
                checkedMekanik = dbhelper.getCheckMekanik(mekanikCode);
                notifyDataSetChanged();
                if (checkedMekanik != null) {
                    dbhelper.updateCheckedMekanik(mekanikCode, "");
                    checkBoxMekanikLv.setChecked(false);
                }
            }
            else {
                checkBoxMekanikLv.setChecked(true);
                dbhelper.updateCheckedMekanik(mekanikCode, "Checked");
                notifyDataSetChanged();
            }

        }

        public void setCheckboxMekanik(String mekanikCode) {
            if (checkBoxMekanikLv.isChecked()) {
                dbhelper.updateCheckedMekanik(mekanikCode, "Checked");
                notifyDataSetChanged();
            } else {
                checkedMekanik = dbhelper.getCheckMekanik(mekanikCode);
                notifyDataSetChanged();
                if (checkedMekanik != null) {
                    dbhelper.updateCheckedMekanik(mekanikCode, "");
                    checkBoxMekanikLv.setChecked(false);
                    notifyDataSetChanged();
                }
            }
        }

        public void getCheckedLayout(String checkedMekanik) {
            String checkValue = dbhelper.getCheckMekanik(checkedMekanik);

            if (checkValue.equals("Checked")) {
               checkBoxMekanikLv.setChecked(true);
            }
            else  {
                checkBoxMekanikLv.setChecked(false);
            }
        }
    }
}
