package com.julong.longtech.menuhcm;

import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AbsensiMandiri extends AppCompatActivity {

    private int REQUEST_IMAGE_CAPTURE = 1;
    String tipeKeteranganAbsen, nodocAbsensiMandiri, savedate, latAbsenMandiri, longAbsenMandiri;
    DatabaseHelper dbhelper;

    Button btnAbsensiMandiriMasuk, btnAbsensiMandiriPulang, btnSubmitAbsen;
    EditText etLokasiAbsensiMandiri;
    LinearLayout layoutAbsenMandiriCheckInOut, layoutLokasiAbsenMandiri;
    byte[] imgAbsensiMandiri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_mandiri);

        dbhelper = new DatabaseHelper(this);
        savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        layoutAbsenMandiriCheckInOut = findViewById(R.id.layoutAbsenMandiriCheckInOut);
        layoutLokasiAbsenMandiri = findViewById(R.id.layoutLokasiAbsensiMandiri);
        btnAbsensiMandiriMasuk = findViewById(R.id.btnAbsensiMandiriMasuk);
        btnAbsensiMandiriPulang = findViewById(R.id.btnAbsensiMandiriPulang);
        btnSubmitAbsen = findViewById(R.id.btnSubmitAbsensiMandiri);
        etLokasiAbsensiMandiri = findViewById(R.id.etLokasiAbsensiMandiri);

        btnAbsensiMandiriMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipeKeteranganAbsen = "CHECKIN";
                layoutAbsenMandiriCheckInOut.setVisibility(View.GONE);
                layoutLokasiAbsenMandiri.setVisibility(View.VISIBLE);
                btnSubmitAbsen.setVisibility(View.VISIBLE);
            }
        });

        btnAbsensiMandiriPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipeKeteranganAbsen = "CHECKOUT";
                layoutAbsenMandiriCheckInOut.setVisibility(View.GONE);
                layoutLokasiAbsenMandiri.setVisibility(View.VISIBLE);
                btnSubmitAbsen.setVisibility(View.VISIBLE);
            }
        });

        btnSubmitAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etLokasiAbsensiMandiri.getText().toString().trim())) {
                    new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Masukkan User ID").setConfirmText("OK").show();
                }
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                        takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLocation();
        nodocAbsensiMandiri = dbhelper.get_tbl_username(0) + "/ABSMDR/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
        Bitmap photoCamera = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        imgAbsensiMandiri = stream.toByteArray();
        dbhelper.insert_absmdr(nodocAbsensiMandiri, tipeKeteranganAbsen, "FOTO", etLokasiAbsensiMandiri.getText().toString(), latAbsenMandiri, longAbsenMandiri, imgAbsensiMandiri);

        if (tipeKeteranganAbsen.equals("CHECKIN")) {
            new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Absen Masuk")
                    .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            }).show();
        }
        else if (tipeKeteranganAbsen.equals("CHECKOUT")) {
            new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Absen Pulang")
                    .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            }).show();
        }
        else if (tipeKeteranganAbsen.equals("RESTIN")) {
            new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.SUCCESS_TYPE).setContentText("Berhasil Absen Mulai Istirahat")
                    .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            }).show();
        }
        else {
            new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.SUCCESS_TYPE).setContentText("Berhasil Absen Selesai Istirahat")
                    .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            }).show();
        }

        imgAbsensiMandiri = null;
        tipeKeteranganAbsen = null;
        etLokasiAbsensiMandiri.setText(null);
        layoutAbsenMandiriCheckInOut.setVisibility(View.VISIBLE);
        layoutLokasiAbsenMandiri.setVisibility(View.GONE);
        btnSubmitAbsen.setVisibility(View.GONE);

    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latAbsenMandiri = String.valueOf(latitude);
        longAbsenMandiri = String.valueOf(longitude);
    }

    @Override
    public void onBackPressed() {

        if (tipeKeteranganAbsen != null || layoutLokasiAbsenMandiri.getVisibility() == View.VISIBLE) {
            tipeKeteranganAbsen = null;
            layoutAbsenMandiriCheckInOut.setVisibility(View.VISIBLE);
            layoutLokasiAbsenMandiri.setVisibility(View.GONE);
            btnSubmitAbsen.setVisibility(View.GONE);
        }
        else {
            finish();
        }

    }
}