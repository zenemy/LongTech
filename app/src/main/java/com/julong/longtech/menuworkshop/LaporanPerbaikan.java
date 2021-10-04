package com.julong.longtech.menuworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.ui.home.HomeFragment.lvfragment;

public class LaporanPerbaikan extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dlgHelper;
    String selectedVehicle, nodocLaporanPerbaikan, selectedVehicleGroup,
            latServiceProcess, longServiceProcess, selectedETA;

    //Dialog submit service
    Dialog dlgServiceCompletionStatus;
    EditText etLokasiService, etDlgServiceETA;
    MaterialDatePicker.Builder<Long> materialDatePickerBuilder;
    MaterialDatePicker<Long> datepickerETA;

    EditText etKegiatanPerbaikan;
    AutoCompleteTextView acVehicleLaporanService;
    public static RecyclerView lvMaterialLaporanService, lvMekanikLaporanService;
    Button btnBackLaporanService;
    TabLayout tabLaporanPerbaikan;
    ConstraintLayout layoutMaterial;
    TextView tvPlaceholderLvMaterial;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    List<String> listMaterial;
    ArrayAdapter<String> adapterMaterial;

    List<ListMekanikPerintahService> listMekaniks;
    AdapterMekanikPerintahService adapterLvMekanik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_perbaikan);

        dbhelper = new DatabaseHelper(this);
        dlgHelper = new DialogHelper(this);

        // Declare design ID
        acVehicleLaporanService = findViewById(R.id.acVehicleLaporanService);
        lvMaterialLaporanService = findViewById(R.id.lvMaterialLaporanService);
        lvMekanikLaporanService = findViewById(R.id.lvMekanikLaporanService);
        btnBackLaporanService = findViewById(R.id.btnBackLaporanService);
        etKegiatanPerbaikan = findViewById(R.id.etKegiatanLaporanService);
        etLokasiService = findViewById(R.id.etLokLaporanService);
        tabLaporanPerbaikan = findViewById(R.id.tabLaporanPerbaikan);
        layoutMaterial = findViewById(R.id.layoutMaterialLaporanService);
        tvPlaceholderLvMaterial = findViewById(R.id.tvPlaceholderLvMaterialService);

        btnBackLaporanService.setOnClickListener(view -> onBackPressed());

        tabLaporanPerbaikan.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        layoutMaterial.setVisibility(View.VISIBLE);
                        lvMekanikLaporanService.setVisibility(View.GONE);
                        break;
                    case 1:
                        lvMekanikLaporanService.setVisibility(View.VISIBLE);
                        layoutMaterial.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleLaporanService.setAdapter(adapterVehicle);

        acVehicleLaporanService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                selectedVehicleGroup = dbhelper.get_vehiclecodegroup(1, selectedVehicle);
                loadListViewMaterial(LaporanPerbaikan.this);

                Cursor cursorMekanik = dbhelper.view_preparemekanik_service();
                if (cursorMekanik.moveToFirst()) {
                    do {
                        dbhelper.insert_mekanik_laporanservice(
                                cursorMekanik.getString(cursorMekanik.getColumnIndex("gangcode")),
                                cursorMekanik.getString(cursorMekanik.getColumnIndex("empcode")));
                    } while (cursorMekanik.moveToNext());
                }

                loadListViewMekanik();

                // Hide keyboard after vehicle selected
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleLaporanService.getWindowToken(), 0);
            }
        });

        // Setting min date
        materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
        listValidators.add(dateValidatorMin);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);
        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilderRange.build());
        datepickerETA = materialDatePickerBuilder.setTitleText("Estimasi Tanggal Selesai Perbaikan").build();

        datepickerETA.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
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
                selectedETA = formatSave.format(date);
                etDlgServiceETA.setText(simpleFormatView.format(date));
                dlgServiceCompletionStatus.show();
            }
        });
    }

    public void addMaterialService(View v) {
        dlgHelper.showDlgAddMaterialService(tvPlaceholderLvMaterial);
    }

    public void submitLaporanPerbaikan(View v) {
        // Check empty fields and invalid data input as usual
        ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicle;
        if (TextUtils.isEmpty(acVehicleLaporanService.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Pilih Kendaraan").setConfirmText("OK").show();
        }
        else if (TextUtils.isEmpty(etKegiatanPerbaikan.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Isi kegiatan").setConfirmText("OK").show();
        }
        else if (vehicleOutput.indexOf(acVehicleLaporanService.getText().toString()) == -1) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Kendaraan salah").setConfirmText("OK").show();
        }
        else {
            showDlgSubmitPerbaikan();
        }
    }

    private void showDlgSubmitPerbaikan() {

        dlgServiceCompletionStatus = new Dialog(this);
        dlgServiceCompletionStatus.setCanceledOnTouchOutside(false);
        dlgServiceCompletionStatus.setContentView(R.layout.dialog_submitlaporanservice);
        dlgServiceCompletionStatus.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowDlgPerbaikan = dlgServiceCompletionStatus.getWindow();
        windowDlgPerbaikan.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        selectedETA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        TextInputLayout inputLayoutDlgServiceETA = dlgServiceCompletionStatus.findViewById(R.id.inputLayoutServiceETA);
        etDlgServiceETA = dlgServiceCompletionStatus.findViewById(R.id.etDateDlgServiceETA);

        LinearLayout layoutDlgServiceDelayed = dlgServiceCompletionStatus.findViewById(R.id.layoutDlgServiceDelayed);
        Button btnDoneVehicleService = dlgServiceCompletionStatus.findViewById(R.id.btnDoneServiceProcess);
        Button btnNotCompleteService = dlgServiceCompletionStatus.findViewById(R.id.btnNotCompleteServiceProcess);
        Button btnWaitSparepartService = dlgServiceCompletionStatus.findViewById(R.id.btnWaitSparepartServiceProcess);
        Button btnCancelDlgService = dlgServiceCompletionStatus.findViewById(R.id.btnBackDlgLaporanService);
        Button btnSimpanDlgService = dlgServiceCompletionStatus.findViewById(R.id.btnSimpanDlgLaporanService);
        View dividerBtnDlg = dlgServiceCompletionStatus.findViewById(R.id.dividerBtnDlgService);
        dlgServiceCompletionStatus.show();

        btnCancelDlgService.setOnClickListener(view -> dlgServiceCompletionStatus.dismiss());

        etDlgServiceETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgServiceCompletionStatus.hide();
                datepickerETA.show(getSupportFragmentManager(), "ETASERVICE");
            }
        });

        // Insert transaction into database
        btnDoneVehicleService.setOnClickListener(view ->  insertTransactionData("Service Selesai"));

        btnWaitSparepartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDoneVehicleService.setVisibility(View.GONE);
                layoutDlgServiceDelayed.setVisibility(View.GONE);
                inputLayoutDlgServiceETA.setVisibility(View.VISIBLE);
                dividerBtnDlg.setVisibility(View.VISIBLE);
                btnSimpanDlgService.setVisibility(View.VISIBLE);

                btnSimpanDlgService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(etDlgServiceETA.getText().toString().trim())) {
                            Toast.makeText(LaporanPerbaikan.this, "Tentukan Estimasi Selesai!", Toast.LENGTH_LONG).show();
                        } else {
                            insertTransactionData("Menunggu Sparepart");
                        }
                    }
                });

            }
        });

        btnNotCompleteService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDoneVehicleService.setVisibility(View.GONE);
                layoutDlgServiceDelayed.setVisibility(View.GONE);
                inputLayoutDlgServiceETA.setVisibility(View.VISIBLE);
                dividerBtnDlg.setVisibility(View.VISIBLE);
                btnSimpanDlgService.setVisibility(View.VISIBLE);
                btnSimpanDlgService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(etDlgServiceETA.getText().toString().trim())) {
                            Toast.makeText(LaporanPerbaikan.this, "Tentukan Estimasi Selesai!", Toast.LENGTH_LONG).show();
                        } else {
                            insertTransactionData("Service Belum Selesai");
                        }
                    }
                });
            }
        });
    }

    private void insertTransactionData(String submitType) {
        getLocation();

        nodocLaporanPerbaikan = dbhelper.get_tbl_username(0) + "/PSWS/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
        dbhelper.insert_prosesperbaikan_header(nodocLaporanPerbaikan, selectedETA, selectedVehicle, submitType,
                etKegiatanPerbaikan.getText().toString(), latServiceProcess,
                longServiceProcess, etLokasiService.getText().toString());

        // Delete unselected materials and mekaniks
        dbhelper.update_detail_laporanperbaikan(nodocLaporanPerbaikan);
        dlgServiceCompletionStatus.dismiss();
        SweetAlertDialog dlgSuccess = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        dlgSuccess.setTitleText("Berhasill Simpan");
        dlgSuccess.setConfirmClickListener(sweetAlertDialog -> {
            Intent backIntent = new Intent();
            setResult(727, backIntent);
            finish();
        });
        dlgSuccess.show();
        new Handler().postDelayed(() -> {
            Intent backIntent = new Intent();
            setResult(727, backIntent);
            finish();
        }, 2000);

    }

    public static void loadListViewMaterial(Context activityContext) {

        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(lvfragment.getContext());

        LinearLayoutManager layoutMaterial = new LinearLayoutManager(activityContext);
        lvMaterialLaporanService.setLayoutManager(layoutMaterial);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                lvMaterialLaporanService.getContext(), layoutMaterial.getOrientation());
        lvMaterialLaporanService.addItemDecoration(dividerItemDecoration);

        List<ListMaterialProsesPerbaikan> listViewMaterial;
        AdapterMaterialProsesPerbaikan adapterLvMaterial;

        listViewMaterial = new ArrayList<>();
        listViewMaterial.clear();
        final Cursor cursor = dbhelper.listview_material();
        if (cursor.moveToFirst()) {
            do {
                ListMaterialProsesPerbaikan paramsMaterial = new ListMaterialProsesPerbaikan
                        (cursor.getString(0), cursor.getInt(1), cursor.getString(2));
                listViewMaterial.add(paramsMaterial);
            } while (cursor.moveToNext());
        }
        adapterLvMaterial = new AdapterMaterialProsesPerbaikan(listViewMaterial, activityContext);
        lvMaterialLaporanService.setAdapter(adapterLvMaterial);
    }

    private void loadListViewMekanik() {

        LinearLayoutManager layoutMekanik = new LinearLayoutManager(this);
        lvMekanikLaporanService.setLayoutManager(layoutMekanik);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        lvMekanikLaporanService.addItemDecoration(dividerItemDecoration);

        listMekaniks = new ArrayList<>();
        listMekaniks.clear();
        final Cursor cursor = dbhelper.listview_mekanik_mintaservice();
        if (cursor.moveToFirst()) {
            do {
                ListMekanikPerintahService paramsMekanik = new ListMekanikPerintahService(
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("empcode"))
                );
                listMekaniks.add(paramsMekanik);
            } while (cursor.moveToNext());
        }
        adapterLvMekanik = new AdapterMekanikPerintahService(listMekaniks, this);
        lvMekanikLaporanService.setAdapter(adapterLvMekanik);
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latServiceProcess = String.valueOf(latitude);
        longServiceProcess = String.valueOf(longitude);
    }

    @Override
    public void onBackPressed() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'PSWS' AND itemdata = 'DETAIL2' AND uploaded IS NULL");
        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'PSWS' AND itemdata = 'DETAIL1' AND uploaded IS NULL");
        finish();
    }
}