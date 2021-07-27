package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdjustmentUnit extends AppCompatActivity {

    String[] arrayPilihanMenu = {"Perintah Operasi Unit", "Ganti Driver / Operator", "Status Standby Unit", "Adjusment KM/HM", "Status Unit Breakdown"};
    String codeOptionMenu, selectedVehicle, selectedEmp, nodocAdjustment, dateSaveDatabase, unitKm;
    private List<String> listVehicleName, listVehicleCode, listEmployeeName, listEmployeeCode;
    ArrayAdapter<String> adapterMenuOption, adapterVehicleAdjustmentUnit, adapterEmployeeAdjustmentUnit;
    DatabaseHelper dbHelper;
    private KeyListener keyListenerAcDriver;

    TextInputLayout inputLayoutDecimalKM;
    LinearLayout layoutKMHMVehicle;
    Button btnSubmitAdjustment, btnBackAdjustment;
    AutoCompleteTextView acPilihanMenu, acVehicleAdjustmentUnit, acDriver;
    EditText todayDateAdjustmentUnit, etPelaksanaanTgl, etNoteAdjustment, etAdjustmentKMHM, etAdjustmentDecimalKMHM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_unit);

        dbHelper = new DatabaseHelper(this);

        acPilihanMenu = findViewById(R.id.acMenuOptionAdjustmentUnit);
        todayDateAdjustmentUnit = findViewById(R.id.todayDateAdjustmentUnit);
        acVehicleAdjustmentUnit = findViewById(R.id.acKendaraanKerjaGantiDriver);
        layoutKMHMVehicle = findViewById(R.id.layoutKMHMVehicle);
        inputLayoutDecimalKM = findViewById(R.id.inputLayoutAdjustmentDecimalKM);
        acDriver = findViewById(R.id.acNamaSupirGantiDriver);
        btnSubmitAdjustment = findViewById(R.id.btnSubmitAdjustmentVehicle);
        btnBackAdjustment = findViewById(R.id.btnBackAdjustmentVehicle);
        etPelaksanaanTgl = findViewById(R.id.etPelaksanaanTglGantiDriver);
        etNoteAdjustment = findViewById(R.id.etNoteAdjustmentUnit);
        etAdjustmentKMHM = findViewById(R.id.etAdjustmentKMHM);
        etAdjustmentDecimalKMHM = findViewById(R.id.etAdjustmentDecimalKMHM);
        keyListenerAcDriver = acDriver.getKeyListener();

        todayDateAdjustmentUnit.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date()));
        adapterMenuOption = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, arrayPilihanMenu);
        acPilihanMenu.setAdapter(adapterMenuOption);

        listVehicleCode = dbHelper.get_vehiclemasterdata(0);
        listVehicleName = dbHelper.get_vehiclemasterdata(1);
        adapterVehicleAdjustmentUnit = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleName);
        acVehicleAdjustmentUnit.setAdapter(adapterVehicleAdjustmentUnit);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        etPelaksanaanTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), datePicker.toString());

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        // Get the offset from our timezone and UTC.
                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        // It will be negative, so that's the -1
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        // Create a date format, then a date object with our offset
                        SimpleDateFormat simpleFormatView = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                        SimpleDateFormat formatSave = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date date = new Date(selection + offsetFromUTC);
                        dateSaveDatabase = formatSave.format(date);

                        etPelaksanaanTgl.setText(simpleFormatView.format(date));
                    }
                });
            }
        });

        acPilihanMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (selection.equals("Ganti Driver / Operator")) {
                    codeOptionMenu = "CHANGE";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    listEmployeeCode = dbHelper.get_employee(0);
                    listEmployeeName = dbHelper.get_employee(1);
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeName);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
                }
                else if (selection.equals("Perintah Operasi Unit")) {
                    codeOptionMenu = "RUN";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    listEmployeeCode = dbHelper.get_employee(0);
                    listEmployeeName = dbHelper.get_employee(1);
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeName);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
                else if (selection.equals("Adjusment KM/HM")) {
                    codeOptionMenu = "KM/HM";
                    layoutKMHMVehicle.setVisibility(View.VISIBLE);
                    listEmployeeCode = dbHelper.get_employee(0);
                    listEmployeeName = dbHelper.get_employee(1);
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeName);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
                else if (selection.equals("Status Unit Breakdown")) {
                    codeOptionMenu = "BREAKDOWN";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    listEmployeeCode = dbHelper.get_employee(0);
                    listEmployeeName = dbHelper.get_employee(1);
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeName);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
                else {
                    codeOptionMenu = "STANDBY";
                    layoutKMHMVehicle.setVisibility(View.GONE);
                    listEmployeeCode = dbHelper.get_employee(0);
                    listEmployeeName = dbHelper.get_employee(1);
                    adapterEmployeeAdjustmentUnit = new ArrayAdapter<String>(AdjustmentUnit.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeName);
                    acDriver.setAdapter(adapterEmployeeAdjustmentUnit);
//                    acDriver.setKeyListener(keyListenerAcDriver);
//                    acDriver.setKeyListener(null);
//                    acDriver.setAdapter(null);
//                    acDriver.setText(null);
                }
            }
        });

        acVehicleAdjustmentUnit.setOnItemClickListener((adapterView, view, position, l) -> selectedVehicle = listVehicleCode.get(position));
        acDriver.setOnItemClickListener((adapterView, view, position, l) -> selectedEmp = listEmployeeCode.get(position));

        btnBackAdjustment.setOnClickListener(view -> finish());

        btnSubmitAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.ERROR_TYPE).setContentText("Pilih Unit!").setConfirmText("OK").show();
                }
                else if (selectedEmp == null) {
                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.ERROR_TYPE).setContentText("Pilih Karyawan!").setConfirmText("OK").show();
                }
                else {
                    unitKm = etAdjustmentKMHM.getText().toString() + "," + etAdjustmentDecimalKMHM.getText().toString();
                    nodocAdjustment = dbHelper.get_tbl_username(0) + "/RUNVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
                    dbHelper.insert_adjustmentunit(nodocAdjustment, dateSaveDatabase, codeOptionMenu, selectedVehicle, selectedEmp, etNoteAdjustment.getText().toString(), unitKm);

                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Adjustment Unit")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                acPilihanMenu.setText(null);
                                etPelaksanaanTgl.setText(null);
                                acDriver.setText(null);
                                acVehicleAdjustmentUnit.setText(null);
                                etNoteAdjustment.setText(null);
                                codeOptionMenu = null;
                                selectedEmp = null;
                                selectedVehicle = null;
                            }).setConfirmText("OK").show();
                }
            }
        });

    }
}