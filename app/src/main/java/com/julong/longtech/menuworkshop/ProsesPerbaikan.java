package com.julong.longtech.menuworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menuhistory.HistoryAdapterRKH;
import com.julong.longtech.menuhistory.ListHistoryRKH;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProsesPerbaikan extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedVehicle, nodocProsesPerbaikan,
            slectedDate, selectedVehicleGroup, latServiceProcess, longServiceProcess;
    Dialog dlgServiceCompletionStatus;

    EditText etDateService, etKegiatanPerbaikan;
    AutoCompleteTextView acVehicleServiceProcess;
    RecyclerView lvMaterialServiceProcess;
    Button btnBackServiceProcess;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    List<ListMaterialProsesPerbaikan> listMaterial;
    AdapterMaterialProsesPerbaikan adapterMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_perbaikan);

        dbhelper = new DatabaseHelper(this);

        // Declare design ID
        etDateService = findViewById(R.id.etDateServiceProcess);
        acVehicleServiceProcess = findViewById(R.id.acVehicleServiceProcess);
        lvMaterialServiceProcess = findViewById(R.id.lvMaterialServiceProcess);
        btnBackServiceProcess = findViewById(R.id.btnBackServiceProcess);
        etKegiatanPerbaikan = findViewById(R.id.etKegiatanProsesPerbaikan);

        btnBackServiceProcess.setOnClickListener(view -> onBackPressed());

        String todayDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        etDateService.setText(todayDate);

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleServiceProcess.setAdapter(adapterVehicle);

        acVehicleServiceProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                selectedVehicleGroup = dbhelper.get_vehiclecodegroup(1, selectedVehicle);
                loadListViewHistoryRKH(selectedVehicleGroup);

                // Hide keyboard after vehicle selected
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleServiceProcess.getWindowToken(), 0);
            }
        });
    }

    public void loadListViewHistoryRKH(String vehicleGroup) {

        LinearLayoutManager layoutRKH = new LinearLayoutManager(this);
        lvMaterialServiceProcess.setLayoutManager(layoutRKH);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        lvMaterialServiceProcess.addItemDecoration(dividerItemDecoration);

        listMaterial = new ArrayList<>();
        listMaterial.clear();
        final Cursor cursor = dbhelper.listview_material(vehicleGroup);
        if (cursor.moveToFirst()) {
            do {
                ListMaterialProsesPerbaikan paramsMaterial = new ListMaterialProsesPerbaikan
                        (cursor.getString(1), cursor.getString(0), cursor.getString(2));
                listMaterial.add(paramsMaterial);
            } while (cursor.moveToNext());
        }
        adapterMaterial = new AdapterMaterialProsesPerbaikan(listMaterial, this);
        lvMaterialServiceProcess.setAdapter(adapterMaterial);
    }

    public void submitProsesPerbaikan(View v) {
        // Check empty fields and invalid data input as usual
        ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicle;
        if (TextUtils.isEmpty(acVehicleServiceProcess.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Pilih Kendaraan").setConfirmText("OK").show();
        }
        else if (TextUtils.isEmpty(etKegiatanPerbaikan.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Isi kegiatan").setConfirmText("OK").show();
        }
        else if (vehicleOutput.indexOf(acVehicleServiceProcess.getText().toString()) == -1) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Kendaraan salah").setConfirmText("OK").show();
        }
        else {
            showDlgSubmitPerbaikan();
        }
    }

    private void showDlgSubmitPerbaikan() {
        dlgServiceCompletionStatus = new Dialog(this);
        dlgServiceCompletionStatus.setCanceledOnTouchOutside(false);
        dlgServiceCompletionStatus.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgServiceCompletionStatus.setContentView(R.layout.dialog_serviceprocess);
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
        nodocProsesPerbaikan = dbhelper.get_tbl_username(0) + "/SRWS/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
        dbhelper.insert_prosesperbaikan_header(nodocProsesPerbaikan, selectedVehicle, submitType,
                etKegiatanPerbaikan.getText().toString(), latServiceProcess, longServiceProcess);

        // Inserting selected material
        for (int i = 0; i < AdapterMaterialProsesPerbaikan.materialList.size(); i++) {
            dbhelper.insert_prosesperbaikan_detail1(nodocProsesPerbaikan,
                    AdapterMaterialProsesPerbaikan.materialList.get(i).getMaterialCode(),
                    AdapterMaterialProsesPerbaikan.materialList.get(i).getEditTextValue());
        }

        // Delete unselected materials
        dbhelper.getWritableDatabase().execSQL("DELETE FROM tr_02 WHERE datatype = 'PSWS' AND (text2 = '' OR text2 IS NULL) AND uploaded = 0");
        dlgServiceCompletionStatus.dismiss();
        SweetAlertDialog dlgSuccess = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        dlgSuccess.setTitleText("Berhasill Simpan");
        dlgSuccess.setConfirmClickListener(sweetAlertDialog -> onBackPressed());
        dlgSuccess.show();
        new Handler().postDelayed(() -> onBackPressed(), 2000);
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latServiceProcess = String.valueOf(latitude);
        longServiceProcess = String.valueOf(longitude);
    }

    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}