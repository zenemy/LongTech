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

   AutoCompleteTextView acVehicleInspeksi, acDriverInspeksi, acLokasiInspeksi, acKegiatanInspeksi;
   EditText etDescInspeksi;
   ImageButton imgFotoInspeksi;
   Button btnSubmitInspeksi, btnBackInspeksi;

    private List<String> listVehicleName, listLokasiInspeksi,
            listDriverName, listKegiatanInspeksi;
    ArrayAdapter<String> adapterVehicle, adapterDriver, adapterLokasi, adapterKegiatan;

    String nodocInspeksi, selectedVehicle, selectedDriver, selectedLokasiInspeksi,
            selectedKegiatanInspeksi, selectedSatuanInspeksi, latInspeksi, longInspeksi;
    byte[] byteImgInspeksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_hasilkerja);

        dbHelper = new DatabaseHelper(this);

        // Declare Design ID
        acVehicleInspeksi = findViewById(R.id.acVehicleInspeksi);
        acDriverInspeksi = findViewById(R.id.acDriverInspeksi);
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
                dbHelper.insert_kegiataninspeksi(nodocInspeksi, selectedVehicle, selectedDriver,
                        selectedLokasiInspeksi, selectedKegiatanInspeksi,
                        etDescInspeksi.getText().toString(), latInspeksi, longInspeksi, byteImgInspeksi);

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

        listVehicleName = dbHelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleName);
        acVehicleInspeksi.setAdapter(adapterVehicle);

        listKegiatanInspeksi = dbHelper.get_all_transport();
        adapterKegiatan = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanInspeksi);
        acKegiatanInspeksi.setAdapter(adapterKegiatan);

        listDriverName = dbHelper.get_employee();
        adapterDriver = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listDriverName);
        acDriverInspeksi.setAdapter(adapterDriver);

        listLokasiInspeksi = dbHelper.get_fieldcrop(1);
        adapterLokasi = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiInspeksi);
        acLokasiInspeksi.setAdapter(adapterLokasi);

        acVehicleInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbHelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(InspeksiHasilKerja.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleInspeksi.getWindowToken(), 0);
            }
        });

        acDriverInspeksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDriver = dbHelper.get_empcode(0, adapterDriver.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(InspeksiHasilKerja.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleInspeksi.getWindowToken(), 0);
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