package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julong.longtech.DatabaseHelper;
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
import java.util.List;

public class RencanaKerjaHarian extends AppCompatActivity {

    String[] arrayMenuShift = {"Shift 1", "Shift 2", "Shift 3"};
    ArrayAdapter<String> adapterMenuShift;
    private List<String> listVehicleDlgRKH, listEmployeeDlgRKH;
    ArrayAdapter<String> adapterVehicleDlgRKH, adapterEmployeeDlgRKH;

    FloatingActionButton btnAddRKH;
    EditText etPelaksanaanTglRKH, etDescRKH;
    Button btnSubmitRKH, btnBackRKH;

    Dialog dlgAddUnit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkh);

        dbHelper = new DatabaseHelper(this);

        btnAddRKH = findViewById(R.id.btnAddRKH);
        etPelaksanaanTglRKH = findViewById(R.id.etPelaksanaanTglRKH);
        etDescRKH = findViewById(R.id.etDescRKH);
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
        AutoCompleteTextView acUnitInputRKH = dlgAddUnit.findViewById(R.id.acUnitInputRKH);
        AutoCompleteTextView acShiftDriverRKH = dlgAddUnit.findViewById(R.id.acShiftDriverRKH);
        AutoCompleteTextView acDriverInputRKH = dlgAddUnit.findViewById(R.id.acDriverInputRKH);
        adapterMenuShift = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuShift);
        acShiftDriverRKH.setAdapter(adapterMenuShift);

        listVehicleDlgRKH = dbHelper.get_vehiclemasterdata();
        adapterVehicleDlgRKH = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleDlgRKH);
        acUnitInputRKH.setAdapter(adapterVehicleDlgRKH);

        listEmployeeDlgRKH = dbHelper.get_employee();
        adapterEmployeeDlgRKH = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinnerlist, R.id.spinnerItem, listEmployeeDlgRKH);
        acDriverInputRKH.setAdapter(adapterEmployeeDlgRKH);

        btnAddRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAddUnit.show();
            }
        });

        btnBackRKH.setOnClickListener(v -> finish());

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