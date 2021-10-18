package com.julong.longtech.menuvehicle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;

import java.util.ArrayList;
import java.util.List;

public class AdapterRKH extends RecyclerView.Adapter<AdapterRKH.operatorHolder> {

    // List to store all the contact details
    private final List<ListOperatorNewRKH> operatorList;
    private final Context mContext;

    // Counstructor for the Class
    public AdapterRKH(List operatorList, Context context) {
        this.operatorList = operatorList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public operatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_mekanik, parent, false);
        return new operatorHolder(view);
    }

    @Override
    public int getItemCount() {
        return operatorList == null? 0: operatorList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull operatorHolder holder, final int position) {
        final ListOperatorNewRKH operatorServices = operatorList.get(position);

        // Set the data to the views here
        holder.setOperatorName(operatorServices.getEmployeeName());
        holder.setOperatorCode(operatorServices.getEmployeeCode());

        holder.checkBoxLvRKH.setOnClickListener(view -> {
            if (TextUtils.isEmpty(NewMethodRKH.acKondisiUnitNewRKH.getText().toString().trim())) {
                Toast.makeText(mContext, "Pilih Kondisi Unit", Toast.LENGTH_LONG).show();
                holder.checkBoxLvRKH.setChecked(false);
            } else {
                holder.setCheckboxOperator(operatorServices.getEmployeeCode(), NewMethodRKH.selectedVehicle);
            }
        });

        holder.layoutItemLvRKH.setOnClickListener(view -> {
            if (TextUtils.isEmpty(NewMethodRKH.acKondisiUnitNewRKH.getText().toString().trim())) {
                Toast.makeText(mContext, "Pilih Kondisi Unit", Toast.LENGTH_LONG).show();
                holder.checkBoxLvRKH.setChecked(false);
            } else {
                holder.setCheckedLayout(operatorServices.getEmployeeCode(), NewMethodRKH.selectedVehicle);
            }
        });

        holder.getCheckedValue(operatorServices.getEmployeeCode());
    }

    // This class that helps to populate data to the view
    public class operatorHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextView tvOperatorName, tvOperatorCode;
        private ConstraintLayout layoutItemLvRKH;
        private CheckBox checkBoxLvRKH;
        private int checkedOperatorValue;

        public operatorHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            tvOperatorName = itemView.findViewById(R.id.tvMekanikLvMintaService);
            tvOperatorCode = itemView.findViewById(R.id.tvMekanikCodeMintaService);
            layoutItemLvRKH = itemView.findViewById(R.id.layoutLvMekanik);
            checkBoxLvRKH = itemView.findViewById(R.id.checkBoxMekanikLv);
        }

        public void setOperatorName(String mekanikName) {
            tvOperatorName.setText(mekanikName);
        }

        public void setOperatorCode(String operatorCode) {
            tvOperatorCode.setText(operatorCode);
        }

        public void setCheckboxOperator(String operatorCode,String unitCode) {
            if (checkBoxLvRKH.isChecked()) {
                dbhelper.updateCheckedOperator(operatorCode, NewMethodRKH.rkhWorkdate, unitCode);
                checkBoxLvRKH.setChecked(true);
                notifyDataSetChanged();
            }
            else {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("DELETE FROM tr_02 WHERE datatype = 'RKHVH' AND itemdata = 'DETAIL2' AND text1 = '" + operatorCode + "'" +
                        "AND text2 = '" + unitCode + "' AND uploaded IS NULL");
                checkBoxLvRKH.setChecked(false);
                checkedOperatorValue = dbhelper.getCheckRKH(operatorCode);
                notifyDataSetChanged();
            }

        }


        public void getCheckedValue(String operatorCode) {
            int getCheckedOperator = dbhelper.getCheckRKH(operatorCode);
            Log.d("cukvale", operatorCode +  " " + getCheckedOperator);
            if (getCheckedOperator > 0) {
                checkBoxLvRKH.setChecked(true);
            } else if (getCheckedOperator == 0) {
                checkBoxLvRKH.setChecked(false);
            }
        }

        public void setCheckedLayout (String operatorCode, String unitCode) {
            if (checkBoxLvRKH.isChecked()) {
                checkBoxLvRKH.setChecked(false);
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("DELETE FROM tr_02 WHERE datatype = 'RKHVH' AND itemdata = 'DETAIL2' AND text1 = '" + operatorCode + "'" +
                        "AND text2 = '" + unitCode + "' AND uploaded IS NULL");
                checkBoxLvRKH.setChecked(false);
                checkedOperatorValue = dbhelper.getCheckRKH(operatorCode);
                notifyDataSetChanged();
            }
            else {
                checkBoxLvRKH.setChecked(true);
                dbhelper.updateCheckedOperator(operatorCode, NewMethodRKH.rkhWorkdate, unitCode);
                notifyDataSetChanged();
            }
        }
    }
}
