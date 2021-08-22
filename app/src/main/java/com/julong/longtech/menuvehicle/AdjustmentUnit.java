package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdjustmentUnit extends AppCompatActivity {

    String[] arrayPilihanMenu = {"Perintah Operasi Unit", "Status Standby Unit", "Adjusment KM/HM", "Status Unit Breakdown"};
    String codeOptionMenu, selectedVehicle, selectedEmp, nodocAdjustment, dateSaveDatabase, unitKm;
    private List<String> listVehicleAdj, listEmployeeAdj;
    ArrayAdapter<String> adapterMenuOption, adapterVehicleAdjustmentUnit, adapterEmployeeAdjustmentUnit;
    DatabaseHelper dbHelper;
    private KeyListener keyListenerAcDriver;

    TextInputLayout inputLayoutKM;
    Button btnSubmitAdjustment, btnBackAdjustment;
    AutoCompleteTextView acPilihanMenu, acVehicleAdjustmentUnit, acDriver;
    EditText etNoteAdjustment, etAdjustmentKMHM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_unit);

        dbHelper = new DatabaseHelper(this);

        //Declare design ID
        acPilihanMenu = findViewById(R.id.acMenuOptionAdjustmentUnit);
        acVehicleAdjustmentUnit = findViewById(R.id.acKendaraanKerjaGantiDriver);
        inputLayoutKM = findViewById(R.id.inputLayoutAdjustKMHM);
        acDriver = findViewById(R.id.acNamaSupirGantiDriver);
        btnSubmitAdjustment = findViewById(R.id.btnSubmitAdjustmentVehicle);
        btnBackAdjustment = findViewById(R.id.btnBackAdjustmentVehicle);
        etNoteAdjustment = findViewById(R.id.etNoteAdjustmentUnit);
        etAdjustmentKMHM = findViewById(R.id.etAdjustmentKMHM);
        keyListenerAcDriver = acDriver.getKeyListener();

        adapterMenuOption = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayPilihanMenu);
        acPilihanMenu.setAdapter(adapterMenuOption);

        listVehicleAdj = dbHelper.get_vehiclemasterdata();
        adapterVehicleAdjustmentUnit = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleAdj);
        acVehicleAdjustmentUnit.setAdapter(adapterVehicleAdjustmentUnit);

        // Select work date
        acPilihanMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (selection.equals("Perintah Operasi Unit")) {
                    codeOptionMenu = "RUN";
                    inputLayoutKM.setVisibility(View.GONE);
                    listEmployeeAdj = dbHelper.get_employee();
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeAdj);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
                else if (selection.equals("Adjusment KM/HM")) {
                    codeOptionMenu = "KM/HM";
                    inputLayoutKM.setVisibility(View.VISIBLE);
                    listEmployeeAdj = dbHelper.get_employee();
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeAdj);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
                else if (selection.equals("Status Unit Breakdown")) {
                    codeOptionMenu = "BREAKDOWN";
                    inputLayoutKM.setVisibility(View.GONE);
                    listEmployeeAdj = dbHelper.get_employee();
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeAdj);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
                else {
                    codeOptionMenu = "STANDBY";
                    inputLayoutKM.setVisibility(View.GONE);
                    listEmployeeAdj = dbHelper.get_employee();
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeAdj);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
            }
        });

        acVehicleAdjustmentUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbHelper.get_vehiclecodeonly((String) adapterView.getItemAtPosition(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(AdjustmentUnit.this.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleAdjustmentUnit.getWindowToken(), 0);
            }
        });

        acDriver.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedEmp = dbHelper.get_empcode(0, (String) adapterView.getItemAtPosition(position));
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(AdjustmentUnit.this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acDriver.getWindowToken(), 0);
        });

        btnBackAdjustment.setOnClickListener(view -> onBackPressed());

        btnSubmitAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicleAdj;
                ArrayList<String> driverOutput = (ArrayList<String>) listEmployeeAdj;

                if (codeOptionMenu == null) {
                    final SweetAlertDialog dlgAlert = new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.ERROR_TYPE);
                    dlgAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            acPilihanMenu.requestFocus();
                            acPilihanMenu.showDropDown();
                            dlgAlert.dismiss();
                        }
                    });
                    dlgAlert.setTitleText("Pilih Menu!").setConfirmText("OK").show();
                }
                else if (selectedVehicle == null) {
                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Pilih Unit!").setConfirmText("OK").show();
                }
                else if (selectedEmp == null) {
                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Pilih Driver!").setConfirmText("OK").show();
                }
                else if (vehicleOutput.indexOf(acVehicleAdjustmentUnit.getText().toString()) == -1 || driverOutput.indexOf(acDriver.getText().toString()) == -1) {
                    Snackbar.make(view, "Kendaraan / Driver salah", Snackbar.LENGTH_LONG).setAnchorView(findViewById(android.R.id.content))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).setAction("OKAY", snackview -> {
                                acVehicleAdjustmentUnit.requestFocus();
                                acDriver.requestFocus();
                            }).show();
                }
                else {

                    unitKm = etAdjustmentKMHM.getText().toString();

                    nodocAdjustment = dbHelper.get_tbl_username(0) + "/RUNVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

                    dbHelper.insert_adjustmentunit(nodocAdjustment, codeOptionMenu, selectedVehicle,
                            selectedEmp, etNoteAdjustment.getText().toString(), unitKm);

                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Adjustment Unit")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            acPilihanMenu.setText(null);
                            acDriver.setText(null);
                            acVehicleAdjustmentUnit.setText(null);
                            etNoteAdjustment.setText(null);
                            codeOptionMenu = null;
                            selectedEmp = null;
                            selectedVehicle = null;

                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        }).setConfirmText("OK").show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}