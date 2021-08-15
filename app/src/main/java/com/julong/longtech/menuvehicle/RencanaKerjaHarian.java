package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DownloadData;
import com.julong.longtech.menusetup.UploadAdapter;
import com.julong.longtech.menusetup.UploadParam;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RencanaKerjaHarian extends AppCompatActivity {

    public static String nodocRKH;
    String[] arrayMenuShift = {"Shift 1", "Shift 2", "Shift 3"};
    ArrayAdapter<String> adapterMenuShift;
    private List<String> listVehicleCodeDlg, listVehicleNameDlg, listEmployeeNameDlg, listDriverCodeDlg, listHelper1Dlg, listHelper2Dlg;
    ArrayAdapter<String> adapterVehicleDlgRKH, adapterEmployeeDlgRKH;

    LinearLayout layoutBtnRKH;
    FloatingActionButton btnAddRKH;
    EditText etPelaksanaanTglRKH, etDescRKH;
    Button btnSubmitRKH, btnBackRKH;
    ListView listViewRKH;

    private List<ListParamRKH> listParamRKH;
    AdapterRKH adapterRKH;
    String selectedHelper1, selectedHelper2, selectedDriver, selectedVehicle;

    Dialog dlgAddUnit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkh);

        dbHelper = new DatabaseHelper(this);

        // Declare design ID
        listViewRKH = findViewById(R.id.lvRKH);
        btnAddRKH = findViewById(R.id.btnAddRKH);
        etDescRKH = findViewById(R.id.etDescRKH);
        btnBackRKH = findViewById(R.id.btnBackRKH);
        layoutBtnRKH = findViewById(R.id.layoutBtnRKH);
        btnSubmitRKH = findViewById(R.id.btnSubmitRKH);
        etPelaksanaanTglRKH = findViewById(R.id.etPelaksanaanTglRKH);

        // Set tanggal pelaksanaan
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String tomorrowAsString = dateFormat.format(tomorrow);
        nodocRKH = dbHelper.get_tbl_username(0) + "/RKHVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
        etPelaksanaanTglRKH.setText(tomorrowAsString);

        btnBackRKH.setOnClickListener(v -> finish());

        loadListViewRKH();

        btnSubmitRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.updateselesai_rkh(nodocRKH, etDescRKH.getText().toString());

                SweetAlertDialog finishRkhDlg = new SweetAlertDialog(RencanaKerjaHarian.this, SweetAlertDialog.SUCCESS_TYPE);
                finishRkhDlg.setCancelable(false);
                finishRkhDlg.setTitleText("Berhasil Menyimpan!");
                finishRkhDlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent backIntent = new Intent();
                        setResult(727, backIntent);
                        finish();
                    }
                });
            }
        });

        if (dbHelper.get_statusrkh(0).equals("1") && dbHelper.get_statusrkh(4).equals("")) {
            etPelaksanaanTglRKH.setText(dbHelper.get_statusrkh(2));
        }
        else if (dbHelper.get_statusrkh(4).equals("0") || dbHelper.get_statusrkh(4).equals("1")) {
            listViewRKH.setEnabled(false);
            layoutBtnRKH.setVisibility(View.GONE);
            btnAddRKH.setVisibility(View.GONE);
        }

    }

    public void eventAddUnitRKH(View v) {

        selectedDriver = null;
        selectedHelper1 = null;
        selectedHelper2 = null;
        selectedVehicle = null;

        //Popup dialog rincian RKH
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

        // List adapter vehicle
        listVehicleCodeDlg = dbHelper.get_vehiclemasterdata(0);
        listVehicleNameDlg = dbHelper.get_vehiclemasterdata(1);
        adapterVehicleDlgRKH = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleNameDlg);
        acUnitInputRKH.setAdapter(adapterVehicleDlgRKH);

        // List adapter emp
        listEmployeeNameDlg = dbHelper.get_employee(1);
        listDriverCodeDlg = dbHelper.get_employee(0);
        listHelper1Dlg = dbHelper.get_employee(0);
        listHelper2Dlg = dbHelper.get_employee(0);
        adapterEmployeeDlgRKH = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeNameDlg);
        acDriverInputRKH.setAdapter(adapterEmployeeDlgRKH);
        acHelper1InputRKH.setAdapter(adapterEmployeeDlgRKH);
        acHelper2InputRKH.setAdapter(adapterEmployeeDlgRKH);

        // Get selected code id
        acUnitInputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedVehicle = listVehicleCodeDlg.get(position);
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(RencanaKerjaHarian.this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acUnitInputRKH.getWindowToken(), 0);
        });

        acDriverInputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedDriver = listDriverCodeDlg.get(position);
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(RencanaKerjaHarian.this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acDriverInputRKH.getWindowToken(), 0);
        });

        acHelper1InputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedHelper1 = listHelper1Dlg.get(position);
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(RencanaKerjaHarian.this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acHelper1InputRKH.getWindowToken(), 0);
        });

        acHelper2InputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedHelper2 = listHelper2Dlg.get(position);
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(RencanaKerjaHarian.this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acHelper2InputRKH.getWindowToken(), 0);
        });

        btnDlgCancelPilihUnitInputKRH.setOnClickListener(v1 -> dlgAddUnit.dismiss());

        // Simpan RKH
        btnDlgSimpanPilihUnitInputKRH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodocRKH = dbHelper.get_tbl_username(0) + "/RKHVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

                ArrayList<String> employeeOutput = (ArrayList<String>) listEmployeeNameDlg;
                ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicleNameDlg;

                // Checking empty fields
                if (TextUtils.isEmpty(acUnitInputRKH.getText().toString().trim()) || TextUtils.isEmpty(acShiftDriverRKH.getText().toString().trim())
                    || TextUtils.isEmpty(acDriverInputRKH.getText().toString().trim()) || TextUtils.isEmpty(acHelper1InputRKH.getText().toString().trim())
                    || TextUtils.isEmpty(acHelper2InputRKH.getText().toString().trim()) || TextUtils.isEmpty(etInputRKHBBM.getText().toString().trim())) {
                    Toast.makeText(RencanaKerjaHarian.this, "Lengkapi Data!", Toast.LENGTH_LONG).show();
                }
                // Restrict user from input data other than autocompletion list
                else if (employeeOutput.indexOf(acDriverInputRKH.getText().toString()) == -1
                        || employeeOutput.indexOf(acHelper1InputRKH.getText().toString()) == -1
                        || employeeOutput.indexOf(acHelper2InputRKH.getText().toString()) == -1) {
                    Toast.makeText(RencanaKerjaHarian.this, "Karyawan tidak valid!", Toast.LENGTH_LONG).show();
                }
                else if (vehicleOutput.indexOf(acUnitInputRKH.getText().toString()) == -1 ) {
                    Toast.makeText(RencanaKerjaHarian.this, "Kendaraan tidak valid!", Toast.LENGTH_LONG).show();
                }
                else if (dbHelper.get_statusrkh(4).equals("")) {
                    dbHelper.insert_rkh_detail1(nodocRKH, selectedVehicle, acShiftDriverRKH.getText().toString(),
                            selectedDriver, selectedHelper1, selectedHelper2, etInputRKHBBM.getText().toString());

                    dbHelper.delete_rkh_header(nodocRKH);
                    dbHelper.insert_rkh_header(nodocRKH, dbHelper.get_count_totalrkh(nodocRKH), etDescRKH.getText().toString());
                    dlgAddUnit.dismiss();
                    acUnitInputRKH.setText("");
                    acShiftDriverRKH.setText("");
                    acDriverInputRKH.setText("");
                    acHelper1InputRKH.setText("");
                    acHelper2InputRKH.setText("");
                    etInputRKHBBM.setText(null);
                    loadListViewRKH();
                }
            }
        });

        dlgAddUnit.show();
    }

    private void loadListViewRKH() {
        listViewRKH = findViewById(R.id.lvRKH);
        listParamRKH = new ArrayList<>();
        listParamRKH.clear();
        final Cursor cursor = dbHelper.listview_rkh(nodocRKH);
        if (cursor.moveToFirst()) {
            do {
                ListParamRKH listParamRKHS = new ListParamRKH(cursor.getString(0),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6)
                );
                listParamRKH.add(listParamRKHS);
            } while (cursor.moveToNext());
        }
        adapterRKH = new AdapterRKH(this, R.layout.item_lvrkh, listParamRKH);
        listViewRKH.setAdapter(adapterRKH);
    }
}