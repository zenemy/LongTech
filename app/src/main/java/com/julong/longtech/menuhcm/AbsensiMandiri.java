package com.julong.longtech.menuhcm;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.julong.longtech.BuildConfig;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static org.osmdroid.tileprovider.util.StorageUtils.getStorage;

public class AbsensiMandiri extends AppCompatActivity {

    String nodocAbsensiMandiri, savedate, latAbsenMandiri, longAbsenMandiri;
    DatabaseHelper dbhelper;

    TextView tvEmpName, tvEmpPosition, tvTodayDate;
    Button btnAbsensiMandiriMasuk, btnAbsensiMandiriPulang, btnSubmitAbsen;
    EditText etLokasiAbsensiMandiri;
    LinearLayout layoutAbsenMandiriCheckInOut, layoutLokasiAbsenMandiri;
    byte[] imgAbsensiMandiri;

    Integer tipeKeteranganAbsen;

    ActivityResultLauncher<Intent> intentLaunchCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_mandiri);

        dbhelper = new DatabaseHelper(this);
        savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        //Declare design ID
        tvEmpName = findViewById(R.id.tvEmpNameAbsensiMandiri);
        tvEmpPosition = findViewById(R.id.tvEmpPositionAbsensiMandiri);
        layoutAbsenMandiriCheckInOut = findViewById(R.id.layoutAbsenMandiriCheckInOut);
        layoutLokasiAbsenMandiri = findViewById(R.id.layoutLokasiAbsensiMandiri);
        btnAbsensiMandiriMasuk = findViewById(R.id.btnAbsensiMandiriMasuk);
        btnAbsensiMandiriPulang = findViewById(R.id.btnAbsensiMandiriPulang);
        btnSubmitAbsen = findViewById(R.id.btnSubmitAbsensiMandiri);
        etLokasiAbsensiMandiri = findViewById(R.id.etLokasiAbsensiMandiri);
        tvTodayDate = findViewById(R.id.tvTodayDateAbsensiMandiri);

        tvEmpName.setText(dbhelper.get_tbl_username(10));
        tvEmpPosition.setText(dbhelper.get_tbl_username(13));
        todayDate();

        tipeKeteranganAbsen = 1;

        btnAbsensiMandiriMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipeKeteranganAbsen = 1;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCamera.launch(takePictureIntent);
                }
            }
        });

        btnAbsensiMandiriPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipeKeteranganAbsen = 2;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCamera.launch(takePictureIntent);
                }
            }
        });

        intentLaunchCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    getLocation();
                    nodocAbsensiMandiri = dbhelper.get_tbl_username(0) + "/ABSMDR/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    imgAbsensiMandiri = stream.toByteArray();

                    if (tipeKeteranganAbsen == 1) {
                        dbhelper.insert_absmdr(nodocAbsensiMandiri, "CHECKIN", "FOTO",
                                etLokasiAbsensiMandiri.getText().toString(), latAbsenMandiri, longAbsenMandiri, imgAbsensiMandiri);
                    }

                    else if (tipeKeteranganAbsen == 2) {
                        dbhelper.insert_absmdr(nodocAbsensiMandiri, "CHECKOUT", "FOTO",
                                etLokasiAbsensiMandiri.getText().toString(), latAbsenMandiri, longAbsenMandiri, imgAbsensiMandiri);
                    }

                    //Show map location of user
                    layoutAbsenMandiriCheckInOut.setVisibility(View.GONE);

//                    try {
//                        layoutMap.setVisibility(View.VISIBLE);
//                        GeoPoint absenPoint = new GeoPoint(Double.parseDouble(latAbsenMandiri), Double.parseDouble(longAbsenMandiri));
//                        Marker titikAbsen = new Marker(mapAbsenLokasi);
//                        titikAbsen.setPosition(absenPoint);
//                        titikAbsen.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                        titikAbsen.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.marker_person, null));
//                        mapAbsenLokasi.getController().setZoom(15.0);
//                        mapAbsenLokasi.getController().setCenter(absenPoint);
//                        mapAbsenLokasi.getController().animateTo(absenPoint);
//                        mapAbsenLokasi.getOverlays().add(titikAbsen);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        layoutMap.setVisibility(View.GONE);
//                    }

                    //Absen Status
                    if (tipeKeteranganAbsen == 1) {
                        new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Absen Masuk")
                                .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent backIntent = new Intent();
                                setResult(727, backIntent);
                                finish();
                            }
                        }).show();
                    }
                    else if (tipeKeteranganAbsen == 2) {
                        new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Absen Pulang")
                                .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent backIntent = new Intent();
                                setResult(727, backIntent);
                                finish();
                            }
                        }).show();
                    }

                    imgAbsensiMandiri = null;
                    etLokasiAbsensiMandiri.setText(null);
                    layoutLokasiAbsenMandiri.setVisibility(View.GONE);
                    btnSubmitAbsen.setVisibility(View.GONE);
                }
            }
        );

        btnSubmitAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etLokasiAbsensiMandiri.getText().toString().trim())) {
                    new SweetAlertDialog(AbsensiMandiri.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Masukkan Lokasi Absen").setConfirmText("OK").show();
                }
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        intentLaunchCamera.launch(takePictureIntent);
                    }
                }

            }
        });

    }

    private void todayDate() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                savedate = "Minggu, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.MONDAY:
                savedate = "Senin, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.TUESDAY:
                savedate = "Selasa, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.WEDNESDAY:
                savedate = "Rabu, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.THURSDAY:
                savedate = "Kamis, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.FRIDAY:
                savedate = "Jumat, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.SATURDAY:
                savedate = "Sabtu, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
        }

        tvTodayDate.setText(savedate);

    }

    private void checkAutoDateTime() {
        try {
            if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 1) {

            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Tanggal dan Waktu Otomatis");
                alertDialog.setMessage("Pengaturan tanggal dan waktu tidak otomatis. " +
                        "Mohon aktifkan tanggal dan waktu otomatis di menu pengaturan.");

                alertDialog.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                        startActivity(intent);
                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();

                alert.getButton(BUTTON_NEGATIVE).setVisibility(View.GONE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAutoDateTime();
    }

    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latAbsenMandiri = String.valueOf(latitude);
            longAbsenMandiri = String.valueOf(longitude);
        } else{
            gpsTracker.showSettingsAlert();
        }
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
            Intent backIntent = new Intent();
            setResult(727, backIntent);
            finish();
        }

    }
}