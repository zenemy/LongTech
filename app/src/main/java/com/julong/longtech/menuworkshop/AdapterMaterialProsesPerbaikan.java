package com.julong.longtech.menuworkshop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMaterialProsesPerbaikan extends RecyclerView.Adapter<AdapterMaterialProsesPerbaikan.MaterialHolder> {

    // List to store all the contact details
    public static List<ListMaterialProsesPerbaikan> materialList;
    public static int totalChecked = 0;
    private Context mContext;
    LayoutInflater layoutInflater;

    // Counstructor for the Class
    public AdapterMaterialProsesPerbaikan(List materialList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.materialList = materialList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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

        holder.setMaterialName(materialServices.getMaterialName());
        holder.setMaterialUOM(materialServices.getUnitMeasure());
        holder.etQtyMaterial.setText(materialServices.getEditTextValue());

        //if true, your checkbox will be selected, else unselected
        holder.checkBoxMaterial.setChecked(materialServices.getChecked());

        holder.checkBoxMaterial.setTag(position);
        holder.checkBoxMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBoxMaterial.getTag();

                if (materialServices.getChecked()) {
                    materialList.get(pos).setChecked(false);
                } else {
                    materialList.get(pos).setChecked(true);
                }
            }
        });

        holder.checkBoxMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (materialServices.getChecked()) {
                    holder.layoutQtyMaterial.setEnabled(false);
                    holder.etQtyMaterial.setText(null);
                }
                else {
                    holder.layoutQtyMaterial.setEnabled(true);
                }
            }
        });

    }

    // This class that helps to populate data to the view
    public class MaterialHolder extends RecyclerView.ViewHolder {

        DatabaseHelper dbhelper;

        private TextInputLayout layoutQtyMaterial;
        private TextView tvMaterialName;
        private EditText etQtyMaterial;
        private CheckBox checkBoxMaterial;

        public MaterialHolder(View itemView) {
            super(itemView);

            dbhelper = new DatabaseHelper(mContext);
            layoutQtyMaterial = (TextInputLayout) itemView.findViewById(R.id.layoutQtyMaterialLvServiceProcess);
            tvMaterialName = (TextView) itemView.findViewById(R.id.tvMaterialLvServiceProcess);
            checkBoxMaterial = (CheckBox) itemView.findViewById(R.id.cbLvMaterialLvServiceProcess);
            etQtyMaterial = (EditText) itemView.findViewById(R.id.etQtyMaterialLvServiceProcess);

            etQtyMaterial.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    materialList.get(getAdapterPosition()).setEditTextValue(etQtyMaterial.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        public void setMaterialName(String materialName) {
            tvMaterialName.setText(materialName);
        }

        public void setMaterialUOM(String materialUOM) {
            etQtyMaterial.setHint(materialUOM);
        }
    }
}
