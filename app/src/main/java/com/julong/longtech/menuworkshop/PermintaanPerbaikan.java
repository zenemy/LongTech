package com.julong.longtech.menuworkshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.VerifikasiGIS;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PermintaanPerbaikan extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedTipePermintaanService, selectedVehicle, nodocServiceReq,
            selectedBlok, latKerusakan, longKerusakan;
    byte[] byteFotoPermintaanService;

    RadioGroup radioGroupTipePerbaikan;
    AutoCompleteTextView acVehicle, acLokasiRusak;
    EditText etCatatanPerbaikan;
    Button btnBackMintaService;
    ImageView imgTakePict;

    List<String> listVehicle, listLokasiRKH;
    ArrayAdapter<String> adapterVehicle, adapterLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permintaan_perbaikan);

        dbhelper = new DatabaseHelper(this);

        acVehicle = findViewById(R.id.acVehicleMintaServicer);
        etCatatanPerbaikan = findViewById(R.id.etNoteMintaService);
        btnBackMintaService = findViewById(R.id.btnBackMintaService);
        imgTakePict = findViewById(R.id.imgTakePictPermintaanPerbaikan);
        radioGroupTipePerbaikan = findViewById(R.id.radioGroupMintaServiceType);
        acLokasiRusak = findViewById(R.id.acLokasiMintaService);

        btnBackMintaService.setOnClickListener(view -> finish());

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicle.setAdapter(adapterVehicle);

        listLokasiRKH = dbhelper.get_fieldcrop(1);
        adapterLokasi = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiRKH);
        acLokasiRusak.setAdapter(adapterLokasi);

        if (dbhelper.get_tbl_username(3).equals("OPR")) {
            selectedVehicle = dbhelper.get_tbl_username(19);

            acVehicle.setText(dbhelper.get_vehiclename(2, dbhelper.get_tbl_username(19)));
        }

        radioGroupTipePerbaikan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioTuneUpMintaService:
                        selectedTipePermintaanService = "STU";
                        break;
                    case R.id.radioMintaServiceRingan:
                        selectedTipePermintaanService = "SLC";
                        break;
                    case R.id.radioInsidenMintaService:
                        selectedTipePermintaanService = "SIC";
                        break;

                }
            }
        });

        acVehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PermintaanPerbaikan.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicle.getWindowToken(), 0);
            }
        });

        acLokasiRusak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedBlok = dbhelper.get_singlelokasiCode(adapterLokasi.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PermintaanPerbaikan.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acLokasiRusak.getWindowToken(), 0);
            }
        });

        ActivityResultLauncher<Intent> intentLaunchCameraHasil = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Bitmap photoCamera = (Bitmap) bundle.get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byteFotoPermintaanService = stream.toByteArray();
                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteFotoPermintaanService, 0, byteFotoPermintaanService.length);
                        imgTakePict.setImageBitmap(compressedBitmap);
                        imgTakePict.setScaleType(ImageView.ScaleType.FIT_XY);

                    }

                }
        );

        imgTakePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byteFotoPermintaanService = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCameraHasil.launch(takePictureIntent);
                }
            }
        });

    }

    public void submitPermintaanPerbaikan(View v) {
        ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicle;
        if (selectedTipePermintaanService ==  null) {
            Snackbar.make(v, "Harap pilih tipe perbaikan", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
        }
        else if (selectedVehicle == null) {
            Snackbar.make(v, "Harap pilih kendaraan alat berat", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> { acVehicle.requestFocus(); }).show();
        }
        else if (TextUtils.isEmpty(etCatatanPerbaikan.getText().toString().trim())) {
            Snackbar.make(v, "Harap input deskripsi lokasi", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> { etCatatanPerbaikan.requestFocus(); }).show();
        }
        else if (vehicleOutput.indexOf(acVehicle.getText().toString()) == -1) {
            Snackbar.make(v, "Data Kendaraan salah", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> { acVehicle.requestFocus(); }).show();
        }
        else {
            getLocation();
            nodocServiceReq = dbhelper.get_tbl_username(0) + "/SRWS/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

            dbhelper.insert_permintaanperbaikan(nodocServiceReq, selectedVehicle, selectedTipePermintaanService, selectedBlok,
                    etCatatanPerbaikan.getText().toString(), latKerusakan, longKerusakan, byteFotoPermintaanService);

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Submit")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        Intent backIntent = new Intent();
                        setResult(727, backIntent);
                        finish();
                    }).setConfirmText("OK").show();
        }
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latKerusakan = String.valueOf(latitude);
        longKerusakan = String.valueOf(longitude);
    }

}