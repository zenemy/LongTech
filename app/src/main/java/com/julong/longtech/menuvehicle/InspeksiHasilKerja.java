package com.julong.longtech.menuvehicle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.julong.longtech.menuhcm.AbsensiMandiri;
import com.julong.longtech.menuhcm.ApelPagi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InspeksiHasilKerja extends AppCompatActivity {

    private static int REQUEST_IMAGE_CAPTURE = 1;
    DatabaseHelper dbHelper;

   AutoCompleteTextView acKebunInspeksi, acDivisiInspeksi, acLokasiInspeksi, acKegiatanInspeksi;
   EditText etDescInspeksi;
   ImageButton imgFotoInspeksi;
   Button btnSubmitInspeksi, btnBackInspeksi;

    private List<String> listNamaKebun, listKebunCode, listLokasiInspeksi, listDivisiName,
            listDivisiCode, listKegiatanInspeksi, listKegiatanSatuanCode, listKegiatanSatuanName;
    ArrayAdapter<String> adapterKebun, adapterDivisi, adapterLokasi, adapterKegiatan;

    String nodocInspeksi, selectedKebunInspeksi, selectedDivisiInspeksi, selectedLokasiInspeksi,
            selectedKegiatanInspeksi, selectedSatuanInspeksi, latInspeksi, longInspeksi;
    byte[] byteImgInspeksi;

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
        etDescInspeksi = findViewById(R.id.etDescInspeksiKerja);
        imgFotoInspeksi = findViewById(R.id.imgInspeksiKerja);
        btnSubmitInspeksi = findViewById(R.id.btnSubmitInspeksi);
        btnBackInspeksi = findViewById(R.id.btnBackInspeksi);

        prepareHeaderData();

        btnBackInspeksi.setOnClickListener(view -> finish());

        ActivityResultLauncher<Intent> intentLaunchCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        imgFotoInspeksi.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byteImgInspeksi = stream.toByteArray();
                    }
                }
        );

        imgFotoInspeksi.setOnClickListener(v -> {

            ArrayList<String> activityOutput = (ArrayList<String>) listKegiatanInspeksi;
            ArrayList<String> locOutput = (ArrayList<String>) listLokasiInspeksi;

            // Checking empty fields and restrict user from selecting other than autocompletion list
            if (TextUtils.isEmpty(acLokasiInspeksi.getText().toString().trim()) && TextUtils.isEmpty(acKegiatanInspeksi.getText().toString().trim())) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Lengkapi Data!").show();
            }
            else if (activityOutput.indexOf(acKegiatanInspeksi.getText().toString()) == -1 || locOutput.indexOf(acLokasiInspeksi.getText().toString()) == -1) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Data tidak valid!").show();
            }
            else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    intentLaunchCamera.launch(takePictureIntent);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }

        });

        btnSubmitInspeksi.setOnClickListener(v -> {

            nodocInspeksi = dbHelper.get_tbl_username(0) + "/IHKVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

            ArrayList<String> activityOutput = (ArrayList<String>) listKegiatanInspeksi;
            ArrayList<String> locOutput = (ArrayList<String>) listLokasiInspeksi;

            if (TextUtils.isEmpty(acLokasiInspeksi.getText().toString().trim()) || TextUtils.isEmpty(acKegiatanInspeksi.getText().toString().trim())) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Lengkapi Data!").show();
            }
            else if (activityOutput.indexOf(acKegiatanInspeksi.getText().toString()) == -1 || locOutput.indexOf(acLokasiInspeksi.getText().toString()) == -1) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Data tidak valid!").show();
            }
            else {
                getLocation();
                dbHelper.insert_kegiataninspeksi(nodocInspeksi, selectedKebunInspeksi, selectedDivisiInspeksi,
                        selectedLokasiInspeksi, selectedKegiatanInspeksi, selectedSatuanInspeksi,
                        null, latInspeksi, longInspeksi, byteImgInspeksi);

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Inspeksi")
                        .setConfirmText("SIMPAN").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent backIntent = new Intent();
                        setResult(727, backIntent);
                        finish();
                    }
                }).show();
            }

        });

    }

    private void prepareHeaderData() {
        listNamaKebun = dbHelper.get_itemkebun(1);
        listKebunCode = dbHelper.get_itemkebun(0);
        adapterKebun = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listNamaKebun);
        acKebunInspeksi.setAdapter(adapterKebun);

        listKegiatanInspeksi = dbHelper.get_activitylist(1);
        listKegiatanSatuanCode = dbHelper.get_activitylist(3);
        listKegiatanSatuanName = dbHelper.get_activitylist(4);
        adapterKegiatan = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanInspeksi);
        acKegiatanInspeksi.setAdapter(adapterKegiatan);

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

                listLokasiInspeksi = dbHelper.get_fieldcrop_filtered(selectedKebunInspeksi, selectedDivisiInspeksi, 1);
                adapterLokasi = new ArrayAdapter<String>(InspeksiHasilKerja.this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiInspeksi);
                acLokasiInspeksi.setAdapter(adapterLokasi);
            }
        });

        acLokasiInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedLokasiInspeksi = dbHelper.get_singlelokasiCode(adapterLokasi.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(InspeksiHasilKerja.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acLokasiInspeksi.getWindowToken(), 0);
            }
        });

        acKegiatanInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedKegiatanInspeksi = dbHelper.get_singlekegiatancode(adapterKegiatan.getItem(position));
                selectedSatuanInspeksi = dbHelper.get_singlekegiatanname(selectedKegiatanInspeksi, 1);

                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(InspeksiHasilKerja.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acKegiatanInspeksi.getWindowToken(), 0);
            }
        });

    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latInspeksi = String.valueOf(latitude);
        longInspeksi = String.valueOf(longitude);
    }
}