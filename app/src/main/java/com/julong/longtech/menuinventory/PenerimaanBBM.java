package com.julong.longtech.menuinventory;

import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PenerimaanBBM extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedVehicle, nodocTerimaBBM, selectedGudang;

    AutoCompleteTextView acVehicle, acGudangTerimaBBM;
    EditText etTodayDate, etLiterBBM, etCatatanBBM, etKurirTerimaBBM;
    Button btnBackTerimaBBM;

    List<String> listVehicle, listGudangName, listGudangCode;
    ArrayAdapter<String> adapterVehicle, adapterGudang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penerimaan_bbm);

        dbhelper = new DatabaseHelper(this);

        acVehicle = findViewById(R.id.acUnitTerimaBBM);
        etKurirTerimaBBM = findViewById(R.id.etKurirTerimaBBM);
        etLiterBBM = findViewById(R.id.etJumlahTerimaBBM);
        etCatatanBBM = findViewById(R.id.etNoteTerimaBBM);
        btnBackTerimaBBM = findViewById(R.id.btnBackTerimaBBM);
        etTodayDate = findViewById(R.id.etTodayDateTerimaBBM);
        acGudangTerimaBBM = findViewById(R.id.acGudangTerimaBBM);

        String todayDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        etTodayDate.setText(todayDate);

        btnBackTerimaBBM.setOnClickListener(view -> onBackPressed());

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicle.setAdapter(adapterVehicle);

        listGudangCode = dbhelper.get_gudangmd(0);
        listGudangName = dbhelper.get_gudangmd(1);
        adapterGudang = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listGudangName);
        acGudangTerimaBBM.setAdapter(adapterGudang);

        acVehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PenerimaanBBM.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicle.getWindowToken(), 0);
            }
        });

        acGudangTerimaBBM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedGudang = listGudangCode.get(position);
            }
        });

    }

    public void submitPenerimaanBBM(View v) {
        ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicle;

        if (TextUtils.isEmpty(etKurirTerimaBBM.getText().toString().trim())
                || selectedVehicle == null
                || TextUtils.isEmpty(etLiterBBM.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi data").setConfirmText("OK").show();
        }
        else if (vehicleOutput.indexOf(acVehicle.getText().toString()) == -1) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Kendaraan Salah").setConfirmText("OK").show();
        }
        else {
            nodocTerimaBBM = dbhelper.get_tbl_username(0) + "/RCVBBM/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

            dbhelper.insert_penerimaanbbm(nodocTerimaBBM, selectedVehicle, null, etLiterBBM.getText().toString(),
                    etKurirTerimaBBM.getText().toString(), etCatatanBBM.getText().toString());

            SweetAlertDialog dlgFinish = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            dlgFinish.setCancelable(false);
            dlgFinish.setTitleText("Berhasil Simpan");
            dlgFinish.setConfirmClickListener(sweetAlertDialog -> onBackPressed());
            dlgFinish.setConfirmText("OK").show();
        }
    }

    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}