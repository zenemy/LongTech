package com.julong.longtech.menuworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menuhistory.HistoryAdapterRKH;
import com.julong.longtech.menuhistory.ListHistoryRKH;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menusetup.UploadData;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PerintahPerbaikan extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedVehicle, nodocPerintahService;

    AutoCompleteTextView acVehiclePerintahService;
    EditText etEstimasiPerbaikan;
    RecyclerView lvMekanikPerintahService;
    FloatingActionButton btnAddMekanik;
    Button btnBackPerintahService, btnSubmitPerintahService;
    ImageButton btnReduceEstimasiService, btnAddEstimasiService;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    List<ListMekanikPerintahService> listMekaniks;
    AdapterMekanikPerintahService adapterLvMekanik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perintah_perbaikan);

        dbhelper = new DatabaseHelper(this);

        acVehiclePerintahService = findViewById(R.id.acVehiclePerintahService);
        etEstimasiPerbaikan = findViewById(R.id.etEstimasiPerintahService);
        btnAddMekanik = findViewById(R.id.btnAddPerintahPerbaikan);
        lvMekanikPerintahService = findViewById(R.id.lvMekanikPerintahService);
        btnBackPerintahService = findViewById(R.id.btnBackPerintahService);
        btnAddEstimasiService = findViewById(R.id.btnAddEstimasiPerintahService);
        btnReduceEstimasiService = findViewById(R.id.btnReduceEstimasiPerintahService);
        btnSubmitPerintahService = findViewById(R.id.btnSubmitPerintahService);

        btnBackPerintahService.setOnClickListener(view -> onBackPressed());

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehiclePerintahService.setAdapter(adapterVehicle);

        acVehiclePerintahService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehiclePerintahService.getWindowToken(), 0);
            }
        });

        btnAddEstimasiService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(etEstimasiPerbaikan.getText().toString()) + 1;
                etEstimasiPerbaikan.setText(String.valueOf(result));
            }
        });

        btnReduceEstimasiService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(etEstimasiPerbaikan.getText().toString()) - 1;
                etEstimasiPerbaikan.setText(String.valueOf(result));
            }
        });

        etEstimasiPerbaikan.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etEstimasiPerbaikan.getText().toString().contains("-")) {
                    etEstimasiPerbaikan.setText("0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        loadListViewMekanik();

    }

    public void eventAddMekanikList(View v) {
        if (selectedVehicle == null) { // Check vehicle value
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Pilih Kendaraan")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        acVehiclePerintahService.requestFocus();
                        sweetAlertDialog.dismiss();
                    }).setConfirmText("OK").show();
        }
        else {
            nodocPerintahService = dbhelper.get_tbl_username(0) + "/WOWS/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

            dbhelper.insert_perintahservice_header(nodocPerintahService, selectedVehicle, etEstimasiPerbaikan.getText().toString());
            Cursor cursorMekanik = dbhelper.view_preparemekanik_service();
            if (cursorMekanik.moveToFirst()) {
                do {
                    dbhelper.insert_perintahservice_detail(nodocPerintahService,
                            cursorMekanik.getString(cursorMekanik.getColumnIndex("gangcode")),
                            cursorMekanik.getString(cursorMekanik.getColumnIndex("empcode")));
                } while (cursorMekanik.moveToNext());
            }
            btnAddMekanik.setVisibility(View.GONE);
            loadListViewMekanik();
        }

    }

    public void submitPerintahPerbaikan(View v) {
        if (selectedVehicle == null) { // Check vehicle value
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Pilih Kendaraan")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        acVehiclePerintahService.requestFocus();
                        sweetAlertDialog.dismiss();
                    }).setConfirmText("OK").show();
        }
        else if (TextUtils.isEmpty(etEstimasiPerbaikan.getText().toString().trim())) { //Check empty Estimated Time field
            Snackbar.make(v, "Input estimasi pengerjaan", Snackbar.LENGTH_LONG).setAnchorView(btnSubmitPerintahService)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> etEstimasiPerbaikan.requestFocus()).show();
        }
        else if (lvMekanikPerintahService.getAdapter().getItemCount() == 0) { // Check empty listview
            Snackbar.make(v, "List mekanik kosong", Snackbar.LENGTH_LONG).setAnchorView(btnSubmitPerintahService)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
        }
        else { // Save perintah perbaikan data
            dbhelper.updatesubmit_perintahservice(selectedVehicle, etEstimasiPerbaikan.getText().toString());
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Simpan")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent backIntent = new Intent();
                            setResult(727, backIntent);
                            finish();
                        }
                    }).show();
        }
    }

    private void loadListViewMekanik() {

        LinearLayoutManager layoutRKH = new LinearLayoutManager(this);
        lvMekanikPerintahService.setLayoutManager(layoutRKH);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        lvMekanikPerintahService.addItemDecoration(dividerItemDecoration);

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
        lvMekanikPerintahService.setAdapter(adapterLvMekanik);
    }

    @Override
    public void onBackPressed() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("DELETE FROM tr_01 WHERE datatype = 'WOWS' AND uploaded IS NULL");
        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'WOWS' AND uploaded IS NULL");
        finish();
    }
}