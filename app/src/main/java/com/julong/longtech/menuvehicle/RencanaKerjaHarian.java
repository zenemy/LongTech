package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.UploadAdapter;
import com.julong.longtech.menusetup.UploadParam;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RencanaKerjaHarian extends AppCompatActivity {

    public static String nodocRKH;
    String[] arrayMenuShift = {"Shift 1", "Shift 2", "Shift 3"};
    ArrayAdapter<String> adapterMenuShift;
    private List<String> listVehicleDlgRKH, listEmployeeDlgRKH;
    ArrayAdapter<String> adapterVehicleDlgRKH, adapterEmployeeDlgRKH;

    FloatingActionButton btnAddRKH;
    EditText etPelaksanaanTglRKH, etDescRKH;
    Button btnSubmitRKH, btnBackRKH;
    ListView listViewRKH;

    private List<ListParamRKH> listParamRKH;
    AdapterRKH adapterRKH;

    Dialog dlgAddUnit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkh);

        dbHelper = new DatabaseHelper(this);

        listViewRKH = findViewById(R.id.lvRKH);
        btnAddRKH = findViewById(R.id.btnAddRKH);
        etPelaksanaanTglRKH = findViewById(R.id.etPelaksanaanTglRKH);
        etDescRKH = findViewById(R.id.etDescRKH);
        btnSubmitRKH = findViewById(R.id.btnSubmitRKH);
        btnBackRKH = findViewById(R.id.btnBackRKH);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String tomorrowAsString = dateFormat.format(tomorrow);
        nodocRKH = dbHelper.get_tbl_username(0) + "/RKHVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

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
        AutoCompleteTextView acHelper1InputRKH = dlgAddUnit.findViewById(R.id.acHelper1InputRKH);
        AutoCompleteTextView acHelper2InputRKH = dlgAddUnit.findViewById(R.id.acHelper2InputRKH);
        EditText etInputRKHBBM = dlgAddUnit.findViewById(R.id.etInputRKHBBM);
        adapterMenuShift = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuShift);
        acShiftDriverRKH.setAdapter(adapterMenuShift);

        listVehicleDlgRKH = dbHelper.get_vehiclemasterdata();
        adapterVehicleDlgRKH = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleDlgRKH);
        acUnitInputRKH.setAdapter(adapterVehicleDlgRKH);

        listEmployeeDlgRKH = dbHelper.get_employee(1);
        adapterEmployeeDlgRKH = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeDlgRKH);
        acDriverInputRKH.setAdapter(adapterEmployeeDlgRKH);
        acHelper1InputRKH.setAdapter(adapterEmployeeDlgRKH);
        acHelper2InputRKH.setAdapter(adapterEmployeeDlgRKH);

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

        loadListViewRKH();

        btnDlgSimpanPilihUnitInputKRH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodocRKH = dbHelper.get_tbl_username(0) + "/RKHVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

                dbHelper.insert_rkh_detail1(nodocRKH, dbHelper.get_vehiclecode(0, acUnitInputRKH.getText().toString()), acUnitInputRKH.getText().toString(),
                        acShiftDriverRKH.getText().toString(), dbHelper.get_empcode(acDriverInputRKH.getText().toString()), dbHelper.get_empcode(acHelper1InputRKH.getText().toString()),
                        dbHelper.get_empcode(acHelper2InputRKH.getText().toString()), etInputRKHBBM.getText().toString());

                dbHelper.delete_rkh_header(nodocRKH);
                dbHelper.insert_rkh_header(nodocRKH, dbHelper.get_count_totalrkh(nodocRKH), etDescRKH.getText().toString(), "Proses");
                dlgAddUnit.dismiss();
                acUnitInputRKH.setText("");
                acShiftDriverRKH.setText("");
                acDriverInputRKH.setText("");
                acHelper1InputRKH.setText("");
                acHelper2InputRKH.setText("");
                etInputRKHBBM.setText(null);
                loadListViewRKH();
            }
        });

    }

    private void loadListViewRKH() {

        listViewRKH = findViewById(R.id.lvRKH);
        listParamRKH = new ArrayList<>();
        listParamRKH.clear();
        final Cursor cursor = dbHelper.listview_rkh(nodocRKH);
        if (cursor.moveToFirst()) {
            do {
                ListParamRKH listParamRKHS = new ListParamRKH(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)
                );
                listParamRKH.add(listParamRKHS);
            } while (cursor.moveToNext());
        }
        adapterRKH = new AdapterRKH(this, R.layout.item_lvrkh, listParamRKH);
        listViewRKH.setAdapter(adapterRKH);
    }
}