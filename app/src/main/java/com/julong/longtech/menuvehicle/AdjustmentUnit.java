package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdjustmentUnit extends AppCompatActivity {

    String[] arrayPilihanMenu = {"Perintah Operasi Unit", "Ganti Driver / Operator", "Status Standby Unit", "Adjusment KM/HM", "Status Unit Breakdown"};
    String codeOptionMenu;
    private List<String> listVehicleAdjustmentUni, listEmployeeAdjustmentUnit;
    ArrayAdapter<String> adapterMenuOption, adapterVehicleAdjustmentUnit, adapterEmployeeAdjustmentUnit;
    DatabaseHelper dbHelper;
    private KeyListener keyListenerAcDriver;

    TextInputLayout layoutKMHMVehicle;
    AutoCompleteTextView acPilihanMenu, acVehicleAdjustmentUnit, acVehicleStatus, acDriver;
    EditText todayDateAdjustmentUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_unit);

        dbHelper = new DatabaseHelper(this);

        acPilihanMenu = findViewById(R.id.acMenuOptionAdjustmentUnit);
        todayDateAdjustmentUnit = findViewById(R.id.todayDateAdjustmentUnit);
        acVehicleAdjustmentUnit = findViewById(R.id.acKendaraanKerjaGantiDriver);
        layoutKMHMVehicle = findViewById(R.id.layoutKMHMVehicle);
        acDriver = findViewById(R.id.acNamaSupirGantiDriver);
        keyListenerAcDriver = acDriver.getKeyListener();

        acPilihanMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterMenuOption = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, arrayPilihanMenu);
                acPilihanMenu.setAdapter(adapterMenuOption);
                acPilihanMenu.showDropDown();
            }
        });

        acPilihanMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (selection.equals("Ganti Driver / Operator")) {
                    codeOptionMenu = "CHANGE";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    listEmployeeAdjustmentUnit = dbHelper.get_employee();
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinnerlist, R.id.spinnerItem, listEmployeeAdjustmentUnit);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
                    acDriver.setKeyListener(keyListenerAcDriver);
                }
                else if (selection.equals("Perintah Operasi Unit")) {
                    codeOptionMenu = "RUN";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    acDriver.setKeyListener(null);
                    acDriver.setAdapter(null);
                    acDriver.setText(null);
                }
                else if (selection.equals("Adjusment KM/HM")) {
                    codeOptionMenu = "KM/HM";
                    layoutKMHMVehicle.setVisibility(View.VISIBLE);
                    acDriver.setKeyListener(null);
                    acDriver.setAdapter(null);
                    acDriver.setText(null);
                }
                else if (selection.equals("Status Unit Breakdown")) {
                    codeOptionMenu = "BREAKDOWN";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    acDriver.setKeyListener(null);
                    acDriver.setAdapter(null);
                    acDriver.setText(null);
                }
                else {
                    codeOptionMenu = "STANDBY";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    acDriver.setKeyListener(null);
                    acDriver.setAdapter(null);
                    acDriver.setText(null);
                }
            }
        });

        listVehicleAdjustmentUni = dbHelper.get_vehiclemasterdata();
        adapterVehicleAdjustmentUnit = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleAdjustmentUni);
        acVehicleAdjustmentUnit.setAdapter(adapterVehicleAdjustmentUnit);

        todayDateAdjustmentUnit.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

    }
}