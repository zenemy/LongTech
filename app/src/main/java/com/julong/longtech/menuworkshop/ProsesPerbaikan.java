package com.julong.longtech.menuworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
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

public class ProsesPerbaikan extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedVehicle, selectedServiceType, nodocProsesPerbaikan,
            slectedDate, selectedVehicleGroup;

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

        etDateService = findViewById(R.id.etDateServiceProcess);
        acVehicleServiceProcess = findViewById(R.id.acVehicleServiceProcess);
        lvMaterialServiceProcess = findViewById(R.id.lvMaterialServiceProcess);
        btnBackServiceProcess = findViewById(R.id.btnBackServiceProcess);
        etKegiatanPerbaikan = findViewById(R.id.etKegiatanProsesPerbaikan);

        btnBackServiceProcess.setOnClickListener(view -> onBackPressed());

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleServiceProcess.setAdapter(adapterVehicle);
        showDlgPerbaikan();

        acVehicleServiceProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleServiceProcess.getWindowToken(), 0);
                selectedVehicleGroup = dbhelper.get_vehiclecodegroup(1, selectedVehicle);
                loadListViewHistoryRKH(selectedVehicleGroup);
            }
        });
    }

    private void showDlgPerbaikan() {
        Dialog dlgServiceProcess = new Dialog(this);
        dlgServiceProcess.setCanceledOnTouchOutside(false);
        dlgServiceProcess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgServiceProcess.setContentView(R.layout.dialog_serviceprocess);
        dlgServiceProcess.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowDlgPerbaikan = dlgServiceProcess.getWindow();
        windowDlgPerbaikan.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btnNewVehicleService = dlgServiceProcess.findViewById(R.id.btnNewServiceProcess);
        Button btnResumeVehicleService = dlgServiceProcess.findViewById(R.id.btnResumeServiceProcess);
        Button btnCancelDlgService = dlgServiceProcess.findViewById(R.id.btnBackDlgServiceProcess);

        btnNewVehicleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedServiceType = "NEW";
                dlgServiceProcess.dismiss();
                String todayDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                slectedDate= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                etDateService.setText(todayDate);
            }
        });

        btnResumeVehicleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedServiceType = "RESUME";
                dlgServiceProcess.dismiss();
            }
        });

        dlgServiceProcess.show();

        btnCancelDlgService.setOnClickListener(view -> finish());

        dlgServiceProcess.setOnCancelListener(dialogInterface -> this.finish());
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
                        (cursor.getString(0), cursor.getString(1));
                listMaterial.add(paramsMaterial);
            } while (cursor.moveToNext());
        }
        adapterMaterial = new AdapterMaterialProsesPerbaikan(listMaterial, this);
        lvMaterialServiceProcess.setAdapter(adapterMaterial);
    }

    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}