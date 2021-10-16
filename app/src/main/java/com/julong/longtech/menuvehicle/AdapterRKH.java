package com.julong.longtech.menuvehicle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        View view = layoutInflater.inflate(R.layout.item_lvrkh, parent, false);
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
        holder.setVehicleCode(operatorServices.getUnitCode());

        holder.checkBoxLvRKH.setOnClickListener(view -> {
            if (!NewMethodRKH.acKondisiUnitNewRKH.getText().toString().equals("Normal")) {
                Toast.makeText(mContext, "Unit Sedang " + NewMethodRKH.acKondisiUnitNewRKH.getText().toString(), Toast.LENGTH_LONG).show();
            } else {
                holder.setCheckboxOperator(operatorServices.getEmployeeCode(),
                        operatorServices.getUnitCode(), operatorServices.getShiftCode());
            }
        });

        holder.layoutItemLvRKH.setOnClickListener(view -> {
            if (!NewMethodRKH.acKondisiUnitNewRKH.getText().toString().equals("Normal")) {
                Toast.makeText(mContext, "Unit Sedang " + NewMethodRKH.acKondisiUnitNewRKH.getText().toString(), Toast.LENGTH_LONG).show();
            } else {
                holder.setCheckedLayout(operatorServices.getEmployeeCode(),
                        operatorServices.getUnitCode(), operatorServices.getShiftCode());
            }
        });

        holder.getCheckedValue(operatorServices.getUnitCode(), operatorServices.getEmployeeCode(), operatorServices.getShiftCode());
    }

    // This class that helps to populate data to the view
    public class operatorHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextView tvOperatorName, tvOperatorCode, tvVehicleCode;
        private LinearLayout layoutItemLvRKH;
        private CheckBox checkBoxLvRKH;
        private int checkedOperatorValue;

        public operatorHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            tvOperatorCode = itemView.findViewById(R.id.tvDriverCodeLvRKH);
            tvOperatorName = itemView.findViewById(R.id.tvDriverNameLvRKH);
            layoutItemLvRKH = itemView.findViewById(R.id.layoutItemLvRKH);
            checkBoxLvRKH = itemView.findViewById(R.id.checkBoxLvRKH);
            tvVehicleCode = itemView.findViewById(R.id.tvVehicleCodeLvRKH);

        }

        public void setOperatorName(String mekanikName) {
            tvOperatorName.setText(mekanikName);
        }

        public void setVehicleCode(String vehicleCode) {
            tvVehicleCode.setText(vehicleCode);
        }

        public void setOperatorCode(String operatorCode) {
            tvOperatorCode.setText(operatorCode);
        }

        public void setCheckboxOperator(String operatorCode,String unitCode,String shiftCode) {
            if (checkBoxLvRKH.isChecked()) {
                dbhelper.updateCheckedOperator(operatorCode, NewMethodRKH.rkhWorkdate, unitCode, shiftCode);
                checkBoxLvRKH.setChecked(true);
                notifyDataSetChanged();
            }
            else {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("DELETE FROM tr_02 WHERE datatype = 'RKHVH' AND itemdata = 'DETAIL2' AND text1 = '" + operatorCode + "'" +
                        "AND text2 = '" + unitCode + "' AND text3 = '" + shiftCode + "'  AND uploaded IS NULL");
                checkBoxLvRKH.setChecked(false);
                checkedOperatorValue = dbhelper.getCheckRKH(unitCode,shiftCode,operatorCode);
                notifyDataSetChanged();
            }

        }


        public void getCheckedValue(String unitCode, String shiftCode, String operatorCode) {
            int getCheckedOperator = dbhelper.getCheckRKH(unitCode,shiftCode,operatorCode);
            if (getCheckedOperator > 0) {
                checkBoxLvRKH.setChecked(true);
            }

        }

        public void setCheckedLayout (String operatorCode,String unitCode,String shiftCode) {
            if (checkBoxLvRKH.isChecked()) {
                checkBoxLvRKH.setChecked(false);
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                db.execSQL("DELETE FROM tr_02 WHERE datatype = 'RKHVH' AND itemdata = 'DETAIL2' AND text1 = '" + operatorCode + "'" +
                        "AND text2 = '" + unitCode + "' AND text3 = '" + shiftCode + "'  AND uploaded IS NULL");
                checkBoxLvRKH.setChecked(false);
                checkedOperatorValue = dbhelper.getCheckRKH(unitCode,shiftCode,operatorCode);
                notifyDataSetChanged();
            }
            else {
                checkBoxLvRKH.setChecked(true);
                dbhelper.updateCheckedOperator(operatorCode, NewMethodRKH.rkhWorkdate, unitCode, shiftCode);
                notifyDataSetChanged();
            }
        }
    }
}
