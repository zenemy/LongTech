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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PengeluaranBBM extends AppCompatActivity {

    String selectedVehicle, nodocKeluarBBM;

    DatabaseHelper dbhelper;
    EditText etTodayDate, etLiterBBM, etNoteKeluarBBM;
    AutoCompleteTextView acVehicleKeluarBBM;
    Button btnBackKeluarBBM;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran_bbm);

        dbhelper = new DatabaseHelper(this);

        etTodayDate = findViewById(R.id.etTodayDateKeluarBBM);
        etLiterBBM = findViewById(R.id.etLiterKeluarBBM);
        acVehicleKeluarBBM = findViewById(R.id.acVehicleKeluarBBM);
        etNoteKeluarBBM = findViewById(R.id.etNoteKeluarBBM);
        btnBackKeluarBBM = findViewById(R.id.btnCancelPengeluaranBBM);

        String todayDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        etTodayDate.setText(todayDate);

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleKeluarBBM.setAdapter(adapterVehicle);

        acVehicleKeluarBBM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PengeluaranBBM.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleKeluarBBM.getWindowToken(), 0);
            }
        });

    }

    public void submitPengeluaranBBM(View v) {
        if (selectedVehicle == null ||
                TextUtils.isEmpty(etLiterBBM.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi data").setConfirmText("OK").show();
        }
        else {
            nodocKeluarBBM = dbhelper.get_tbl_username(0) + "/SIVBBM/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

            dbhelper.insert_pengeluaranbbm(nodocKeluarBBM, selectedVehicle, etLiterBBM.getText().toString(), etNoteKeluarBBM.getText().toString());

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