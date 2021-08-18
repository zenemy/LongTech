package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DownloadData;
import com.julong.longtech.menusetup.UploadAdapter;
import com.julong.longtech.menusetup.UploadParam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RencanaKerjaHarian extends AppCompatActivity {

    public static String nodocRKH;
    String[] arrayMenuShift = {"Shift 1", "Shift 2", "Shift 3"};
    ArrayAdapter<String> adapterMenuShift;
    private List<String> listVehicleDlg, listEmployeeDlg;
    ArrayAdapter<String> adapterVehicleDlgRkh, adapterEmployeeDlgRKH;

    LinearLayout layoutBtnRKH;
    FloatingActionButton btnAddRKH;
    EditText etPelaksanaanTglRKH, etDescRKH;
    Button btnSubmitRKH, btnBackRKH;
    public static ListView listViewRKH;

    private List<ListParamRKH> listParamRKH;
    AdapterRKH adapterRKH;
    String selectedHelper1, selectedHelper2, selectedDriver, selectedVehicle;
    public static String selectedDateRKH;

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

        nodocRKH = dbHelper.get_tbl_username(0) + "/RKHVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

        // Set tanggal pelaksanaan
        etPelaksanaanTglRKH.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date()));
        selectedDateRKH = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Setting min date
        MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis());
        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
        listValidators.add(dateValidatorMin);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);
        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilderRange.build());

        //Datepicker tgl pelaksanaan
        MaterialDatePicker<Long> datePickerRKH = materialDatePickerBuilder.setTitleText("Select Date").build();

        etPelaksanaanTglRKH.setOnClickListener(v -> datePickerRKH.show(getSupportFragmentManager(), "TGLRKH"));

        datePickerRKH.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
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
                selectedDateRKH = formatSave.format(date);
                etPelaksanaanTglRKH.setText(simpleFormatView.format(date));

                Cursor cursorUnit = dbHelper.view_prepareunit_rkh();
                if (cursorUnit.moveToFirst()) {
                    do {
                        dbHelper.insert_rkh_detail1(nodocRKH,
                                cursorUnit.getString(cursorUnit.getColumnIndex("unitcode")),
                                selectedDateRKH,
                                cursorUnit.getString(cursorUnit.getColumnIndex("shiftcode")),
                                cursorUnit.getString(cursorUnit.getColumnIndex("drivercode")),
                                null, null, "0");
                    } while (cursorUnit.moveToNext());
                }

                loadListViewRKH();
                dbHelper.insert_rkh_header(nodocRKH, selectedDateRKH, listViewRKH.getAdapter().getCount());
                if (listViewRKH.getAdapter().getCount() > 0) {
                    btnAddRKH.setVisibility(View.VISIBLE);
                    etPelaksanaanTglRKH.setOnClickListener(view -> Toast.makeText(RencanaKerjaHarian.this,
                            "Selesaikan RKH saat ini dahulu!", Toast.LENGTH_LONG).show());
                }

            }
        });

        loadListViewRKH();

        btnSubmitRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.updateselesai_rkh(nodocRKH, etDescRKH.getText().toString());

                SweetAlertDialog finishRkhDlg = new SweetAlertDialog(RencanaKerjaHarian.this, SweetAlertDialog.SUCCESS_TYPE);
                finishRkhDlg.setCancelable(false);
                finishRkhDlg.setTitleText("Berhasil Menyimpan!");
                finishRkhDlg.setConfirmClickListener(sweetAlertDialog -> onBackPressed());
                finishRkhDlg.show();
            }
        });

        if (dbHelper.get_statusrkh(0).equals("1") && dbHelper.get_statusrkh(4).equals("")) {
            try {
                DateFormat formatShow = new SimpleDateFormat("yyyy-MM-dd");
                Date newDate = formatShow.parse(dbHelper.get_statusrkh(2));
                formatShow = new SimpleDateFormat("dd MMMM yyyy");
                String dateShow = formatShow.format(newDate);
                etPelaksanaanTglRKH.setText(dateShow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nodocRKH = dbHelper.get_statusrkh(1);
            selectedDateRKH = dbHelper.get_statusrkh(2);
            etPelaksanaanTglRKH.setOnClickListener(view -> Toast.makeText(this, "Selesaikan RKH saat ini dahulu!", Toast.LENGTH_LONG).show());
        }

        btnBackRKH.setOnClickListener(view -> onBackPressed());

    }

    public void prepateTeamData() throws SQLiteException {

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
        adapterMenuShift = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuShift);
        acShiftDriverRKH.setAdapter(adapterMenuShift);

        // List adapter vehicle
        listVehicleDlg = dbHelper.get_vehiclemasterdata();
        adapterVehicleDlgRkh = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleDlg);
        acUnitInputRKH.setAdapter(adapterVehicleDlgRkh);

        // List adapter emp
        listEmployeeDlg = dbHelper.get_employee();
        adapterEmployeeDlgRKH = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listEmployeeDlg);
        acDriverInputRKH.setAdapter(adapterEmployeeDlgRKH);
        acHelper1InputRKH.setAdapter(adapterEmployeeDlgRKH);
        acHelper2InputRKH.setAdapter(adapterEmployeeDlgRKH);

        // Get selected code id
        acUnitInputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedVehicle = dbHelper.get_vehiclecodeonly((String) adapterView.getItemAtPosition(position));
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acUnitInputRKH.getWindowToken(), 0);
        });

        acDriverInputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedDriver = dbHelper.get_empcode((String) adapterView.getItemAtPosition(position));
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acDriverInputRKH.getWindowToken(), 0);
        });

        acHelper1InputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedHelper1 = dbHelper.get_empcode((String) adapterView.getItemAtPosition(position));
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acHelper1InputRKH.getWindowToken(), 0);
        });

        acHelper2InputRKH.setOnItemClickListener((adapterView, view, position, l) -> {
            selectedHelper2 = dbHelper.get_empcode((String) adapterView.getItemAtPosition(position));
            InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            keyboardMgr.hideSoftInputFromWindow(acHelper2InputRKH.getWindowToken(), 0);
        });

        btnDlgCancelPilihUnitInputKRH.setOnClickListener(v1 -> dlgAddUnit.dismiss());

        // Simpan RKH
        btnDlgSimpanPilihUnitInputKRH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> employeeOutput = (ArrayList<String>) listEmployeeDlg;
                ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicleDlg;

                // Checking empty fields
                if (TextUtils.isEmpty(acUnitInputRKH.getText().toString().trim()) || TextUtils.isEmpty(acShiftDriverRKH.getText().toString().trim())
                    || TextUtils.isEmpty(acDriverInputRKH.getText().toString().trim()) || TextUtils.isEmpty(etInputRKHBBM.getText().toString().trim())) {
                    Toast.makeText(RencanaKerjaHarian.this, "Lengkapi Data!", Toast.LENGTH_LONG).show();
                }
                // Restrict user from input data other than autocompletion list
                else if (employeeOutput.indexOf(acDriverInputRKH.getText().toString()) == -1
                        || (!TextUtils.isEmpty(acHelper1InputRKH.getText().toString().trim())
                        && employeeOutput.indexOf(acHelper1InputRKH.getText().toString()) == -1)
                        || (!TextUtils.isEmpty(acHelper2InputRKH.getText().toString().trim())
                        && employeeOutput.indexOf(acHelper2InputRKH.getText().toString()) == -1)) {
                    Toast.makeText(RencanaKerjaHarian.this, "Karyawan tidak valid!", Toast.LENGTH_LONG).show();
                }
                else if (vehicleOutput.indexOf(acUnitInputRKH.getText().toString()) == -1 ) {
                    Toast.makeText(RencanaKerjaHarian.this, "Kendaraan tidak valid!", Toast.LENGTH_LONG).show();
                }
                else if (dbHelper.get_statusrkh(4).equals("")) {
                    dbHelper.insert_rkh_detail1(nodocRKH, selectedVehicle, selectedDateRKH, acShiftDriverRKH.getText().toString(),
                            selectedDriver, selectedHelper1, selectedHelper2, etInputRKHBBM.getText().toString());

                    dbHelper.delete_rkh_header(nodocRKH);
                    dlgAddUnit.dismiss();
                    acUnitInputRKH.setText("");
                    acShiftDriverRKH.setText("");
                    acDriverInputRKH.setText("");
                    acHelper1InputRKH.setText("");
                    acHelper2InputRKH.setText("");
                    etInputRKHBBM.setText(null);
                    loadListViewRKH();

                    if (listViewRKH.getAdapter().getCount() > 0) {
                        etPelaksanaanTglRKH.setOnClickListener(view -> Toast.makeText(RencanaKerjaHarian.this,
                                "Selesaikan RKH saat ini dahulu!", Toast.LENGTH_LONG).show());
                    }
                }
            }
        });

        dlgAddUnit.show();
    }

    public static void loadListViewRKH() {

        List<ListParamRKH> listParamRKH;
        AdapterRKH adapterRKH;
        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(listViewRKH.getContext());

        listParamRKH = new ArrayList<>();
        listParamRKH.clear();
        final Cursor cursor = dbhelper.listview_rkh(nodocRKH);
        if (cursor.moveToFirst()) {
            do {
                ListParamRKH listParamRKHS = new ListParamRKH(cursor.getString(0),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getInt(6)
                );
                listParamRKH.add(listParamRKHS);
            } while (cursor.moveToNext());
        }
        adapterRKH = new AdapterRKH(listViewRKH.getContext(), R.layout.item_lvrkh, listParamRKH);
        listViewRKH.setAdapter(adapterRKH);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}