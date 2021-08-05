package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class InspeksiHasilKerja extends AppCompatActivity {

    DatabaseHelper dbHelper;

   AutoCompleteTextView acKebunInspeksi, acDivisiInspeksi, acLokasiInspeksi, acKegiatanInspeksi, acSatuanKerjaInspeksi;
   TextInputLayout inputLayoutHasilInspeksi;
   EditText etHasilInspeksi, etDescInspeksi;
   ImageButton imgFotoInspeksi;

    private List<String> listNamaKebun, listKebunCode, listLokasiCode, listLokasiName,
            listDivisiName, listDivisiCode, listKegiatan, listKegiatanCode, listKegiatanSatuan;
    ArrayAdapter<String> adapterKebun, adapterDivisi, adapterLokasi, adapterKegiatan;

    String selectedKebunInspeksi, selectedDivisiInspeksi, selectedLokasiInspeksi, selectedKegiatanInspeksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_hasilkerja);

        dbHelper = new DatabaseHelper(this);

        // Declare Design ID
        acKebunInspeksi = findViewById(R.id.acKebunInspeksiKerja);
        acDivisiInspeksi = findViewById(R.id.acDivisiInspeksiKerja);
        acLokasiInspeksi = findViewById(R.id.acLokasiKerjaInspeksi);
        acKegiatanInspeksi = findViewById(R.id.acKegiatanInspeksiKerja);
        acSatuanKerjaInspeksi = findViewById(R.id.acSatuanInspeksiKerja);
        inputLayoutHasilInspeksi = findViewById(R.id.inputLayoutHasilInpeksi);
        etHasilInspeksi = findViewById(R.id.etQtyInspeksiKerja);
        etDescInspeksi = findViewById(R.id.etDescInspeksiKerja);
        imgFotoInspeksi = findViewById(R.id.imgInspeksiKerja);

        prepareHeaderData();

    }

    private void prepareHeaderData() {
        listNamaKebun = dbHelper.get_itemkebun(1);
        listKebunCode = dbHelper.get_itemkebun(0);
        adapterKebun = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listNamaKebun);
        acKebunInspeksi.setAdapter(adapterKebun);

        acKebunInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                acDivisiInspeksi.setText(null);
                acLokasiInspeksi.setText(null);
                acDivisiInspeksi.setAdapter(null);
                acLokasiInspeksi.setAdapter(null);
                selectedKebunInspeksi = listKebunCode.get(position);

                listDivisiName = dbHelper.get_itemdivisi(adapterKebun.getItem(position), 1);
                listDivisiCode = dbHelper.get_itemdivisi(adapterKebun.getItem(position), 0);
                adapterDivisi = new ArrayAdapter<String>(InspeksiHasilKerja.this, R.layout.spinnerlist, R.id.spinnerItem, listDivisiName);
                acDivisiInspeksi.setAdapter(adapterDivisi);
            }
        });

        acDivisiInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDivisiInspeksi = listDivisiCode.get(position);

                listLokasiCode = dbHelper.get_fieldcrop_filtered(selectedKebunInspeksi, selectedDivisiInspeksi, 0);
                listLokasiName = dbHelper.get_fieldcrop_filtered(selectedKebunInspeksi, selectedDivisiInspeksi, 1);
                adapterLokasi = new ArrayAdapter<String>(InspeksiHasilKerja.this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiName);
                acLokasiInspeksi.setAdapter(adapterLokasi);
            }
        });

        acLokasiInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedLokasiInspeksi = listLokasiCode.get(position);
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(InspeksiHasilKerja.this.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acLokasiInspeksi.getWindowToken(), 0);
            }
        });


    }
}