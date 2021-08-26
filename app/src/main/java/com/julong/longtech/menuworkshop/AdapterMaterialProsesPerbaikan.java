package com.julong.longtech.menuworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.List;

public class AdapterMaterialProsesPerbaikan extends RecyclerView.Adapter<AdapterMaterialProsesPerbaikan.MaterialHolder> {

    // List to store all the contact details
    private List<ListMaterialProsesPerbaikan> materialList;
    private Context mContext;

    // Counstructor for the Class
    public AdapterMaterialProsesPerbaikan(List materialList, Context context) {
        this.materialList = materialList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.listview_material, parent, false);
        return new MaterialHolder(view);
    }

    @Override
    public int getItemCount() {
        return materialList == null? 0: materialList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MaterialHolder holder, final int position) {
        final ListMaterialProsesPerbaikan materialServices = materialList.get(position);

        // Set the data to the views here
        holder.setMaterialName(materialServices.getMaterialName());
        holder.setMaterialUOM(materialServices.getUnitMeasure());

    }

    // This class that helps to populate data to the view
    public class MaterialHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextView tvMaterialName;
        private EditText etQtyMaterial;
        private CheckBox checkBoxMaterial;

        public MaterialHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            tvMaterialName = (TextView) itemView.findViewById(R.id.tvMaterialLvServiceProcess);
            checkBoxMaterial = (CheckBox) itemView.findViewById(R.id.cbLvMaterialLvServiceProcess);
            etQtyMaterial = (EditText) itemView.findViewById(R.id.etQtyMaterialLvServiceProcess);

        }

        public void setMaterialName(String materialName) {
            tvMaterialName.setText(materialName);
        }

        public void setMaterialUOM(String materialUOM) {
            etQtyMaterial.setHint(materialUOM);
        }
    }
}
