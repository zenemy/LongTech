package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RencanaKerjaHarian extends AppCompatActivity {

    public static String nodocRKH, selectedLokasi, selectedKegiatan;
    public static AdapterRKH adapterRKH;

    LinearLayout layoutBtnRKH;
    AutoCompleteTextView acKegiatanKerjaRKH, acLokasiKerjaRKH;
    EditText etPelaksanaanTglRKH, etTotalTargetRKH;
    Button btnSubmitRKH, btnBackRKH;
    TextInputLayout inputLayoutTotalTarget;
    public static ListView listViewRKH;

    List<String> listKegiatanRKH, listLokasiRKH;
    ArrayAdapter<String> adapterKegiatan, adapterLokasi;
    MaterialDatePicker<Long> datePickerRKH;

    public static String selectedDateRKH;
    String selectedVehicleType;
    Dialog dlgVehicleTypeRKH;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkh);

        dbHelper = new DatabaseHelper(this);

        // Declare design ID
        listViewRKH = findViewById(R.id.lvRKH);
        etTotalTargetRKH = findViewById(R.id.etTargetRKH);
        btnBackRKH = findViewById(R.id.btnBackRKH);
        layoutBtnRKH = findViewById(R.id.layoutBtnRKH);
        btnSubmitRKH = findViewById(R.id.btnSubmitRKH);
        acLokasiKerjaRKH = findViewById(R.id.acLokasiKerjaRKH);
        acKegiatanKerjaRKH = findViewById(R.id.acKegiatanKerjaRKH);
        etPelaksanaanTglRKH = findViewById(R.id.etPelaksanaanTglRKH);
        inputLayoutTotalTarget = findViewById(R.id.inputLayoutHasilKerjaRKH);
        nodocRKH = dbHelper.get_tbl_username(0) + "/RKHVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

        // Setting min date
        MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
        listValidators.add(dateValidatorMin);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);
        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilderRange.build());

        //Datepicker tgl pelaksanaan
        datePickerRKH = materialDatePickerBuilder.setTitleText("Tanggal Pelaksanaan").build();

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

                Cursor cursorUnit = dbHelper.view_prepareunit_rkh(selectedVehicleType);
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
                dbHelper.insert_rkh_header(nodocRKH, selectedDateRKH, selectedVehicleType);
                acKegiatanKerjaRKH.setEnabled(true);
                acLokasiKerjaRKH.setEnabled(true);
                if (listViewRKH.getAdapter().getCount() > 0) {
                    etPelaksanaanTglRKH.setOnClickListener(view -> new SweetAlertDialog(RencanaKerjaHarian.this,
                            SweetAlertDialog.ERROR_TYPE).setContentText("Selesaikan RKH dahulu!").setConfirmText("OK").show());
                }

            }
        });

        etPelaksanaanTglRKH.setOnClickListener(view -> datePickerRKH.show(getSupportFragmentManager(), "TGLRKH"));

        // Checking unfinished input
        if (dbHelper.get_statusrkh(0).equals("1")) {
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
            selectedVehicleType = dbHelper.get_statusrkh(3);

            if (dbHelper.get_statusrkh(4) != null) {
                selectedLokasi = dbHelper.get_statusrkh(4);
                acLokasiKerjaRKH.setText(dbHelper.get_singlelokasi(selectedLokasi));
            }
            if (dbHelper.get_statusrkh(5) != null) {
                selectedKegiatan = dbHelper.get_statusrkh(5);
                acKegiatanKerjaRKH.setText(dbHelper.get_singletransportratename(selectedKegiatan));
            }

            etPelaksanaanTglRKH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Selesaikan simpan RKH terlebih dahulu!", Snackbar.LENGTH_LONG).setAnchorView(layoutBtnRKH)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }
            });

            acKegiatanKerjaRKH.setEnabled(true);
            acLokasiKerjaRKH.setEnabled(true);

            listKegiatanRKH = dbHelper.get_transportactivity(selectedVehicleType);
            adapterKegiatan = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanRKH);
            acKegiatanKerjaRKH.setAdapter(adapterKegiatan);

            loadListViewRKH();
        } else {
            showVehicleTypeDlg();
        }

        listLokasiRKH = dbHelper.get_fieldcrop(1);
        adapterLokasi = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiRKH);
        acLokasiKerjaRKH.setAdapter(adapterLokasi);

        acLokasiKerjaRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedLokasi = dbHelper.get_singlelokasiCode(adapterLokasi.getItem(position));
                dbHelper.update_headerLokasiRKH(nodocRKH, selectedVehicleType, selectedLokasi);

                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acLokasiKerjaRKH.getWindowToken(), 0);
            }
        });

        acKegiatanKerjaRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedKegiatan = dbHelper.get_singletransportratecode(adapterKegiatan.getItem(position));
                dbHelper.update_headerKegiatanRKH(nodocRKH, selectedVehicleType, selectedKegiatan);

                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acKegiatanKerjaRKH.getWindowToken(), 0);
                inputLayoutTotalTarget.setSuffixText(dbHelper.get_singlekegiatanname(selectedKegiatan, 1));
                adapterRKH.notifyDataSetChanged();
            }
        });

        // Saving RKH data
        btnSubmitRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> kegiatanOutput = (ArrayList<String>) listKegiatanRKH;
                ArrayList<String> lokasiOutput = (ArrayList<String>) listLokasiRKH;

                if (selectedKegiatan == null || selectedLokasi == null) {
                    Snackbar.make(view, "Harap lengkapi kegiatan / lokasi", Snackbar.LENGTH_LONG).setAnchorView(layoutBtnRKH)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }
                else if (lokasiOutput.indexOf(acLokasiKerjaRKH.getText().toString()) == -1
                        || kegiatanOutput.indexOf(acKegiatanKerjaRKH.getText().toString()) == -1) {
                    Snackbar.make(view, "Kegiatan / lokasi salah", Snackbar.LENGTH_LONG).setAnchorView(layoutBtnRKH)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }
                else if (listViewRKH.getAdapter().getCount() == 0) {
                    Snackbar.make(view, "Data list RKH kosong", Snackbar.LENGTH_LONG).setAnchorView(layoutBtnRKH)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }
                else {
                    dbHelper.updateselesai_rkh(nodocRKH, etTotalTargetRKH.getText().toString(), selectedVehicleType);

                    SweetAlertDialog finishRkhDlg = new SweetAlertDialog(RencanaKerjaHarian.this, SweetAlertDialog.SUCCESS_TYPE);
                    finishRkhDlg.setCancelable(false);
                    finishRkhDlg.setTitleText("Berhasil Menyimpan!");
                    finishRkhDlg.setConfirmClickListener(sweetAlertDialog -> onBackPressed());
                    finishRkhDlg.show();
                }
            }
        });

        btnBackRKH.setOnClickListener(view -> onBackPressed());


    }

    // Pilih Tipe Kendaraan
    private void showVehicleTypeDlg() {
        dlgVehicleTypeRKH = new Dialog(this);
        dlgVehicleTypeRKH.setCanceledOnTouchOutside(false);
        dlgVehicleTypeRKH.setContentView(R.layout.dialog_vehicletyperkh);
        dlgVehicleTypeRKH.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowDlgVehicleType = dlgVehicleTypeRKH.getWindow();
        windowDlgVehicleType.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        AutoCompleteTextView acDlgVehicleType = dlgVehicleTypeRKH.findViewById(R.id.acDlgVehicleTypeRKH);
        Button btnCancelDlgVehicleRKH = dlgVehicleTypeRKH.findViewById(R.id.btnCancelDlgVehicleRKH);
        Button btnOkDlgVehicleRKH = dlgVehicleTypeRKH.findViewById(R.id.btnOkDlgVehicleRKH);
        dlgVehicleTypeRKH.show();

        List<String> listVehicleType;
        ArrayAdapter<String> adapterVehicleType;

        listVehicleType = dbHelper.get_vehiclegroup_filterkemandoran();
        adapterVehicleType = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleType);
        acDlgVehicleType.setAdapter(adapterVehicleType);

        btnCancelDlgVehicleRKH.setOnClickListener(view -> finish());

        dlgVehicleTypeRKH.setOnCancelListener(dialogInterface -> this.finish());

        acDlgVehicleType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicleType = dbHelper.get_single_vehicletypegroup(adapterVehicleType.getItem(position));

                listKegiatanRKH = dbHelper.get_transportactivity(selectedVehicleType);
                adapterKegiatan = new ArrayAdapter<String>(RencanaKerjaHarian.this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanRKH);
                acKegiatanKerjaRKH.setAdapter(adapterKegiatan);
            }
        });

        btnOkDlgVehicleRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedVehicleType == null) {
                    Toast.makeText(RencanaKerjaHarian.this, "Harap pilih tipe kendaraan!", Toast.LENGTH_LONG).show();
                }
                else {
                    dlgVehicleTypeRKH.dismiss();
                    datePickerRKH.show(getSupportFragmentManager(), "TGLRKH");
                }
            }
        });
    }

    public static void loadListViewRKH() {
        List<ListParamRKH> listParamRKH;
        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(listViewRKH.getContext());

        listParamRKH = new ArrayList<>();
        listParamRKH.clear();
        final Cursor cursor = dbhelper.listview_rkh(nodocRKH);
        if (cursor.moveToFirst()) {
            do {
                ListParamRKH listParamRKHS = new ListParamRKH(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2)
                );
                listParamRKH.add(listParamRKHS);
            } while (cursor.moveToNext());
        }
        adapterRKH = new AdapterRKH(listViewRKH.getContext(), listParamRKH);
        listViewRKH.setAdapter(adapterRKH);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}