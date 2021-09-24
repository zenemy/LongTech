package com.julong.longtech.menuworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LaporanPerbaikan extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedVehicle, nodocLaporanPerbaikan, selectedMaterial, materialUOM,
            selectedVehicleGroup, latServiceProcess, longServiceProcess;
    Dialog dlgAddMaterial, dlgServiceCompletionStatus;

    EditText etKegiatanPerbaikan;
    AutoCompleteTextView acVehicleLaporanService;
    RecyclerView lvMaterialLaporanService, lvMekanikLaporanService;
    Button btnBackLaporanService;
    TabLayout tabLaporanPerbaikan;
    ConstraintLayout layoutMaterial;
    TextView tvPlaceholderLvMaterial;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    List<String> listMaterial;
    ArrayAdapter<String> adapterMaterial;

    List<ListMaterialProsesPerbaikan> listViewMaterial;
    AdapterMaterialProsesPerbaikan adapterLvMaterial;

    List<ListMekanikPerintahService> listMekaniks;
    AdapterMekanikPerintahService adapterLvMekanik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_perbaikan);

        dbhelper = new DatabaseHelper(this);

        // Declare design ID
        acVehicleLaporanService = findViewById(R.id.acVehicleLaporanService);
        lvMaterialLaporanService = findViewById(R.id.lvMaterialLaporanService);
        lvMekanikLaporanService = findViewById(R.id.lvMekanikLaporanService);
        btnBackLaporanService = findViewById(R.id.btnBackLaporanService);
        etKegiatanPerbaikan = findViewById(R.id.etKegiatanLaporanService);
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
                loadListViewMaterial();

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
    }

    public void addMaterialService(View v) {
        dlgAddMaterial = new Dialog(this);
        dlgAddMaterial.setCanceledOnTouchOutside(false);
        dlgAddMaterial.setContentView(R.layout.dialog_addmaterial);
        dlgAddMaterial.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowDlgMaterial = dlgAddMaterial.getWindow();
        windowDlgMaterial.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView acAddMaterial = dlgAddMaterial.findViewById(R.id.acDlgMaterialService);
        TextInputLayout inputLayoutQtyMaterial = dlgAddMaterial.findViewById(R.id.inputLayoutDlgQtyMaterial);
        EditText etQtyMaterial = dlgAddMaterial.findViewById(R.id.etDlgQtyMaterial);
        Button btnOkDlgMaterial = dlgAddMaterial.findViewById(R.id.btnOkDlgMaterialService);
        Button btnBackDlgMaterial = dlgAddMaterial.findViewById(R.id.btnBackDlgMaterialService);
        dlgAddMaterial.show();

        btnBackDlgMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgAddMaterial.dismiss();
                selectedMaterial = null;
                acAddMaterial.setText(null);
                etQtyMaterial.setText(null);
            }
        });

        listMaterial = dbhelper.get_listmaterialmd();
        adapterMaterial = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listMaterial);
        acAddMaterial.setAdapter(adapterMaterial);

        acAddMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedMaterial = dbhelper.get_single_materialcode(adapterMaterial.getItem(position), 0);
                materialUOM = dbhelper.get_single_materialcode(adapterMaterial.getItem(position), 1);
                inputLayoutQtyMaterial.setSuffixText(dbhelper.get_single_materialcode(adapterMaterial.getItem(position), 1));
            }
        });

        btnOkDlgMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedMaterial == null) {
                    Toast.makeText(LaporanPerbaikan.this, "Pilih Material!", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(etQtyMaterial.getText().toString().trim())) {
                    Toast.makeText(LaporanPerbaikan.this, "Isi kuantitas material!", Toast.LENGTH_LONG).show();
                }
                else {
                    dbhelper.insert_prosesperbaikan_detailmaterial(null, selectedMaterial, etQtyMaterial.getText().toString(), materialUOM);
                    selectedMaterial = null;
                    acAddMaterial.setText(null);
                    etQtyMaterial.setText(null);
                    dlgAddMaterial.dismiss();
                    tvPlaceholderLvMaterial.setVisibility(View.GONE);
                    loadListViewMaterial();
                }

            }
        });

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

        Button btnDoneVehicleService = dlgServiceCompletionStatus.findViewById(R.id.btnDoneServiceProcess);
        Button btnNotCompleteService = dlgServiceCompletionStatus.findViewById(R.id.btnNotCompleteServiceProcess);
        Button btnWaitSparepartServiceProcess = dlgServiceCompletionStatus.findViewById(R.id.btnWaitSparepartServiceProcess);
        Button btnCancelDlgService = dlgServiceCompletionStatus.findViewById(R.id.btnBackDlgServiceProcess);
        dlgServiceCompletionStatus.show();

        btnCancelDlgService.setOnClickListener(view -> dlgServiceCompletionStatus.dismiss());

        // Insert transaction into database
        btnDoneVehicleService.setOnClickListener(view ->  insertTransactionData("Service Selesai"));
        btnWaitSparepartServiceProcess.setOnClickListener(view -> insertTransactionData("Menunggu Sparepart"));
        btnNotCompleteService.setOnClickListener(view -> insertTransactionData("Service belum selesai"));
    }

    private void insertTransactionData(String submitType) {
        getLocation();
        nodocLaporanPerbaikan = dbhelper.get_tbl_username(0) + "/PSWS/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
        dbhelper.insert_prosesperbaikan_header(nodocLaporanPerbaikan, selectedVehicle, submitType,
                etKegiatanPerbaikan.getText().toString(), latServiceProcess, longServiceProcess);

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

    public void loadListViewMaterial() {

        LinearLayoutManager layoutMaterial = new LinearLayoutManager(this);
        lvMaterialLaporanService.setLayoutManager(layoutMaterial);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                lvMaterialLaporanService.getContext(), layoutMaterial.getOrientation());
        lvMaterialLaporanService.addItemDecoration(dividerItemDecoration);

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
        adapterLvMaterial = new AdapterMaterialProsesPerbaikan(listViewMaterial, this);
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