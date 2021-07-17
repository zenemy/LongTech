package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julong.longtech.R;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RencanaKerjaHarian extends AppCompatActivity {

    String[] arrayMenuShift = {"Shift 1", "Shift 2", "Shift 3"};
    ArrayAdapter<String> adapterMenuShift;

    FloatingActionButton btnAddRKH;
    EditText etPelaksanaanTglRKH;
    Button btnSubmitRKH, btnBackRKH;


    Dialog dlgAddUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkh);

        btnAddRKH = findViewById(R.id.btnAddRKH);
        etPelaksanaanTglRKH = findViewById(R.id.etPelaksanaanTglRKH);
        btnSubmitRKH = findViewById(R.id.btnSubmitRKH);
        btnBackRKH = findViewById(R.id.btnBackRKH);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String tomorrowAsString = dateFormat.format(tomorrow);

        etPelaksanaanTglRKH.setText(tomorrowAsString);

        dlgAddUnit = new Dialog(this);
        dlgAddUnit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgAddUnit.setContentView(R.layout.dialog_selectunit_inputrkh);
        dlgAddUnit.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowValueA2 = dlgAddUnit.getWindow();
        dlgAddUnit.setCanceledOnTouchOutside(false);
        windowValueA2.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btnDlgCancelPilihUnitInputKRH = dlgAddUnit.findViewById(R.id.btnDlgCancelPilihUnitInputKRH);
        Button btnDlgSimpanPilihUnitInputKRH = dlgAddUnit.findViewById(R.id.btnDlgSimpanPilihUnitInputKRH);
        AutoCompleteTextView acShiftDriverRKH = dlgAddUnit.findViewById(R.id.acShiftDriverRKH);
        adapterMenuShift = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuShift);
        acShiftDriverRKH.setAdapter(adapterMenuShift);

        btnAddRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAddUnit.show();
            }
        });

        btnDlgCancelPilihUnitInputKRH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAddUnit.dismiss();
            }
        });

        btnDlgSimpanPilihUnitInputKRH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RencanaKerjaHarian.this, InputRincianRKH.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}