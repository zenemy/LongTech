package com.julong.longtech.menuhcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.julong.longtech.BuildConfig;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static org.osmdroid.tileprovider.util.StorageUtils.getStorage;

public class AbsensiMandiri extends AppCompatActivity {

    private int REQUEST_IMAGE_CAPTURE = 1;
    protected static int START_INDEX=-2, DEST_INDEX=-1;
    String tipeKeteranganAbsen, nodocAbsensiMandiri, savedate, latAbsenMandiri, longAbsenMandiri;
    DatabaseHelper dbhelper;

    Button btnAbsensiMandiriMasuk, btnAbsensiMandiriPulang, btnSubmitAbsen;
    EditText etLokasiAbsensiMandiri;
    LinearLayout layoutAbsenMandiriCheckInOut, layoutLokasiAbsenMandiri, layoutMap;
    MapView mapAbsenLokasi;
    byte[] imgAbsensiMandiri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IConfigurationProvider provider = Configuration.getInstance();
        provider.setUserAgentValue(BuildConfig.APPLICATION_ID);
        provider.setOsmdroidBasePath(getStorage());
        provider.setOsmdroidTileCache(getStorage());
        setContentView(R.layout.activity_absensi_mandiri);

        dbhelper = new DatabaseHelper(this);
        savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        //Declare design ID
        layoutAbsenMandiriCheckInOut = findViewById(R.id.layoutAbsenMandiriCheckInOut);
        layoutLokasiAbsenMandiri = findViewById(R.id.layoutLokasiAbsensiMandiri);
        btnAbsensiMandiriMasuk = findViewById(R.id.btnAbsensiMandiriMasuk);
        btnAbsensiMandiriPulang = findViewById(R.id.btnAbsensiMandiriPulang);
        btnSubmitAbsen = findViewById(R.id.btnSubmitAbsensiMandiri);
        etLokasiAbsensiMandiri = findViewById(R.id.etLokasiAbsensiMandiri);
        mapAbsenLokasi = findViewById(R.id.mapViewAbsensiMandiri);
        layoutMap = findViewById(R.id.layoutMapAbsensiMandiri);

        mapAbsenLokasi.setTileSource(TileSourceFactory.MAPNIK);

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

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getLocation();
            nodocAbsensiMandiri = dbhelper.get_tbl_username(0) + "/ABSMDR/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
            Bitmap photoCamera = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            imgAbsensiMandiri = stream.toByteArray();
            dbhelper.insert_absmdr(nodocAbsensiMandiri, tipeKeteranganAbsen, "FOTO",
                    etLokasiAbsensiMandiri.getText().toString(), latAbsenMandiri, longAbsenMandiri, imgAbsensiMandiri);

            //Show map location of user
            layoutMap.setVisibility(View.VISIBLE);
            GeoPoint absenPoint = new GeoPoint(Double.parseDouble(latAbsenMandiri), Double.parseDouble(longAbsenMandiri));
            Marker titikAbsen = new Marker(mapAbsenLokasi);
            titikAbsen.setPosition(absenPoint);
            titikAbsen.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            titikAbsen.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.marker_person, null));
            mapAbsenLokasi.getController().setZoom(15);
            mapAbsenLokasi.getController().setCenter(absenPoint);
            mapAbsenLokasi.getController().animateTo(absenPoint);
            mapAbsenLokasi.getOverlays().add(titikAbsen);

            //Absen Status
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