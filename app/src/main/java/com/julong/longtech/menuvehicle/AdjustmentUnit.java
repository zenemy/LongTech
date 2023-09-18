package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdjustmentUnit extends AppCompatActivity {

    ArrayAdapter<String> adapterMenuOption, adapterVehicleAdjustmentUnit;
    List<String> listVehicleAdj, listStatusCode, listStatusDesc;
    String selectedCodeOptionMenu, selectedVehicle, nodocAdjustment, unitKm;

    DatabaseHelper dbHelper;

    TextInputLayout inputLayoutKM;
    Button btnSubmitAdjustment, btnBackAdjustment;
    AutoCompleteTextView acPilihanMenu, acVehicleAdjustmentUnit;
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
        btnSubmitAdjustment = findViewById(R.id.btnSubmitAdjustmentVehicle);
        btnBackAdjustment = findViewById(R.id.btnBackAdjustmentVehicle);
        etNoteAdjustment = findViewById(R.id.etNoteAdjustmentUnit);
        etAdjustmentKMHM = findViewById(R.id.etAdjustmentKMHM);

        populateDropdownData();

        // Select work date
        acPilihanMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCodeOptionMenu = listStatusCode.get(position);

                if (selectedCodeOptionMenu.equals("KM/HM")) {
                    inputLayoutKM.setVisibility(View.VISIBLE);
                } else {
                    inputLayoutKM.setVisibility(View.GONE);
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

        btnBackAdjustment.setOnClickListener(view -> onBackPressed());

        btnSubmitAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicleAdj;

                if (selectedCodeOptionMenu == null) {
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
                } else if (vehicleOutput.indexOf(acVehicleAdjustmentUnit.getText().toString()) == -1) {
                    Snackbar.make(view, "Kendaraan / Driver salah", Snackbar.LENGTH_LONG).setAnchorView(findViewById(android.R.id.content))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).setAction("OKAY", snackview -> {
                                acVehicleAdjustmentUnit.requestFocus();
                            }).show();
                } else {

                    unitKm = etAdjustmentKMHM.getText().toString();

                    nodocAdjustment = dbHelper.get_tbl_username(0) + "/RUNVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

                    dbHelper.insert_adjustmentunit(nodocAdjustment, selectedCodeOptionMenu, selectedVehicle,
                            dbHelper.get_tbl_username(0), etNoteAdjustment.getText().toString(), unitKm);

                    new SweetAlertDialog(AdjustmentUnit.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Adjustment Unit")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            acPilihanMenu.setText(null);
                            acVehicleAdjustmentUnit.setText(null);
                            etNoteAdjustment.setText(null);
                            selectedCodeOptionMenu = null;
                            selectedVehicle = null;

                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        }).setConfirmText("OK").show();
                }
            }
        });

    }

    public void populateDropdownData() {

        // Populate Stock Out Type
        acPilihanMenu.setKeyListener(null);
        listStatusCode = dbHelper.get_list_adjustmenu(0);
        listStatusDesc = dbHelper.get_list_adjustmenu(1);
        adapterMenuOption = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listStatusDesc);
        acPilihanMenu.setAdapter(adapterMenuOption);

        // Populate Vehicle
        listVehicleAdj = dbHelper.get_vehiclemasterdata();
        adapterVehicleAdjustmentUnit = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleAdj);
        acVehicleAdjustmentUnit.setAdapter(adapterVehicleAdjustmentUnit);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}