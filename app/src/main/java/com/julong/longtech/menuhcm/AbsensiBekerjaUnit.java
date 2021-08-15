package com.julong.longtech.menuhcm;

import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AbsensiBekerjaUnit extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String latAbsenUnit, longAbsenUnit, nodocAbsenUnit, savedate;
    private int REQUEST_IMAGE_CAPTURE = 1;
    byte[] imgAbsensiUnit;
    EditText etLokasiAbsensiUnit;
    Button btnSubmitAbsensiUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_bekerjaunit);

        dbhelper = new DatabaseHelper(this);
        savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        btnSubmitAbsensiUnit = findViewById(R.id.btnSimpanAbsensiUnit);
        etLokasiAbsensiUnit = findViewById(R.id.etLokasiAbsensiUnit);

        btnSubmitAbsensiUnit.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getLocation();
            nodocAbsenUnit = dbhelper.get_tbl_username(0) + "/ABSVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
            Bitmap photoCamera = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            imgAbsensiUnit = stream.toByteArray();
            dbhelper.insert_absvh(nodocAbsenUnit, "CHECKOUT", "FOTO", etLokasiAbsensiUnit.getText().toString(), latAbsenUnit, longAbsenUnit, imgAbsensiUnit);

            new SweetAlertDialog(AbsensiBekerjaUnit.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Absen Pulang")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        Intent backIntent = new Intent();
                        setResult(727, backIntent);
                        finish();
                    }).show();

            etLokasiAbsensiUnit.setText(null);
            imgAbsensiUnit = null;
        }

    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latAbsenUnit = String.valueOf(latitude);
        longAbsenUnit = String.valueOf(longitude);
    }

}