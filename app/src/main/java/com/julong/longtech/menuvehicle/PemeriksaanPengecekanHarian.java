package com.julong.longtech.menuvehicle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;
import com.julong.longtech.menuhcm.AbsensiMandiri;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PemeriksaanPengecekanHarian extends AppCompatActivity {

    DatabaseHelper dbHelper;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    byte[] byteImgP2H;

    AutoCompleteTextView acKendaraanP2H;
    LinearLayout layoutKebocoranOli, layoutBautMur, layoutKonsidiOliMesin, layoutKonsidiOliTransmisi, layoutKonsidiOliHidrolik,
            layoutKonsidiOliRem, layoutKonsidiOliBBM, layoutKonsidiAirflow, layoutKonsidiRadiator, layoutKondisiBan,
            layoutKerusakan, layoutSabukKipas, layoutSuaraMesin, layoutLampuKendaraan, layoutSpionKendaraan, layoutGrease;

    CheckBox checkKebocoranOli, checkBautMur, checkKonsidiOliMesin, checkKonsidiOliTransmisi, checkKonsidiOliHidrolik,
            checkKonsidiOliRem, checkKonsidiOliBBM, checkKonsidiAirflow, checkKonsidiRadiator, checkKondisiBan,
            checkKerusakan, checkSabukKipas, checkSuaraMesin, checkLampuKendaraan, checkSpionKendaraan, checkGrease;

    EditText etKebocoranOli, etBautMur, etKonsidiOliMesin, etKonsidiOliTransmisi, etKonsidiOliHidrolik,
            etKonsidiOliRem, etKonsidiOliBBM, etKonsidiAirflow, etKonsidiRadiator, etKMHM, etKondisiBan,
            etKerusakan, etSabukKipas, etSuaraMesin, etLampuKendaraan, etSpionKendaraan, etDescPengecekan, etGrease;

    ImageView imgTakePictP2H;
    Button btnCancelP2H;

    ActivityResultLauncher<Intent> intentFotoP2H;

    String nodocP2H, latitudeP2H, longitudeP2H, selectedVehicleCode;
    private List<String> listVehicleP2H;
    ArrayAdapter<String> adapterVehicleP2H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemeriksaanpengecekan_harian);

        dbHelper = new DatabaseHelper(this);
        acKendaraanP2H = findViewById(R.id.acKendaraanKerjaP2H);
        etDescPengecekan = findViewById(R.id.etNoteP2H);
        imgTakePictP2H = findViewById(R.id.imgTakePictP2H);
        btnCancelP2H = findViewById(R.id.btnCancelP2H);
        etKMHM = findViewById(R.id.etKMHMP2H);

        // P2H items
        layoutKebocoranOli = findViewById(R.id.layoutKebocoranOli);
        layoutBautMur = findViewById(R.id.layoutBautMur);
        layoutKonsidiOliMesin = findViewById(R.id.layoutOliMesin);
        layoutKonsidiOliTransmisi = findViewById(R.id.layoutOliTransmisi);
        layoutKonsidiOliHidrolik = findViewById(R.id.layoutOliHidrolik);
        layoutKonsidiOliRem = findViewById(R.id.layoutOliRem);
        layoutKonsidiOliBBM = findViewById(R.id.layoutOliBBM);
        layoutKonsidiAirflow = findViewById(R.id.layoutSaringanUdara);
        layoutKonsidiRadiator = findViewById(R.id.layoutRadiator);
        layoutKondisiBan = findViewById(R.id.layoutKondisiBan);
        layoutKerusakan = findViewById(R.id.layoutRusakBengkok);
        layoutSabukKipas = findViewById(R.id.layoutSabukKipas);
        layoutSuaraMesin = findViewById(R.id.layoutSuaraMesin);
        layoutLampuKendaraan = findViewById(R.id.layoutLampuP2H);
        layoutSpionKendaraan = findViewById(R.id.layoutSpionP2H);
        layoutGrease = findViewById(R.id.layoutGreaseP2H);

        checkKebocoranOli = findViewById(R.id.checkboxKebocoranOli);
        checkBautMur = findViewById(R.id.checkboxBautMur);
        checkKonsidiOliMesin = findViewById(R.id.checkboxOliMesin);
        checkKonsidiOliTransmisi = findViewById(R.id.cbOliTransmisi);
        checkKonsidiOliHidrolik = findViewById(R.id.cbOliHidrolik);
        checkKonsidiOliRem = findViewById(R.id.cbOliRem);
        checkKonsidiOliBBM = findViewById(R.id.cbOliBBM);
        checkKonsidiAirflow = findViewById(R.id.cbSaringanUdara);
        checkKonsidiRadiator = findViewById(R.id.cbRadiator);
        checkKondisiBan = findViewById(R.id.cbKondisiBan);
        checkKerusakan = findViewById(R.id.cbRusakBengkok);
        checkSabukKipas = findViewById(R.id.cbSabukKipas);
        checkSuaraMesin = findViewById(R.id.cbSuaraMesin);
        checkLampuKendaraan = findViewById(R.id.cbLampuP2H);
        checkSpionKendaraan = findViewById(R.id.cbSpionP2H);
        checkGrease = findViewById(R.id.cbGreaseP2H);

        etKebocoranOli = findViewById(R.id.etKebocoranOli);
        etBautMur = findViewById(R.id.etBautMur);
        etKonsidiOliMesin = findViewById(R.id.etOliMesin);
        etKonsidiOliTransmisi = findViewById(R.id.etOliTransmisi);
        etKonsidiOliHidrolik = findViewById(R.id.etOliHidrolik);
        etKonsidiOliRem = findViewById(R.id.etOliRem);
        etKonsidiOliBBM = findViewById(R.id.etOliBBM);
        etKonsidiAirflow = findViewById(R.id.etSaringanUdara);
        etKonsidiRadiator = findViewById(R.id.etRadiator);
        etKondisiBan = findViewById(R.id.etKondisiBan);
        etKerusakan = findViewById(R.id.etRusakBengkok);
        etSabukKipas = findViewById(R.id.etSabukKipas);
        etSuaraMesin = findViewById(R.id.etSuaraMesin);
        etLampuKendaraan = findViewById(R.id.etLampuP2H);
        etSpionKendaraan = findViewById(R.id.etSpionP2H);
        etGrease = findViewById(R.id.etGreaseP2H);

        btnCancelP2H.setOnClickListener(v -> finish());

        if (dbHelper.get_tbl_username(3).equals("OPR")) {
            selectedVehicleCode = dbHelper.get_tbl_username(19);

            acKendaraanP2H.setText(dbHelper.get_vehiclename(2, dbHelper.get_tbl_username(19)));
        }

        if (dbHelper.get_tbl_username(27) != null) {
            etKMHM.setText(dbHelper.get_tbl_username(27));
        }

        // Vehicle list adapter for acVehicle
        listVehicleP2H = dbHelper.get_vehiclemasterdata();
        adapterVehicleP2H = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleP2H);
        acKendaraanP2H.setAdapter(adapterVehicleP2H);

        intentFotoP2H = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byteImgP2H = stream.toByteArray();
                    imgTakePictP2H.setImageBitmap(photoCamera);
                }
            }
        );

        imgTakePictP2H.setOnClickListener(v -> {
            byteImgP2H = null;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                intentFotoP2H.launch(takePictureIntent);
            }
        });

        acKendaraanP2H.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicleCode = dbHelper.get_vehiclecodeonly((String) adapterView.getItemAtPosition(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PemeriksaanPengecekanHarian.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acKendaraanP2H.getWindowToken(), 0);
            }
        });

        eventClickLayout();

    }

    private void eventClickLayout() {
        layoutKebocoranOli.setOnClickListener(v -> {

            if (checkKebocoranOli.isChecked()) {
                checkKebocoranOli.setChecked(false);
            }
            else {
                checkKebocoranOli.setChecked(true);
            }
        });

        layoutBautMur.setOnClickListener(v -> {
            if (checkBautMur.isChecked()) {
                checkBautMur.setChecked(false);
            }
            else {
                checkBautMur.setChecked(true);
            }
        });

        layoutKonsidiOliMesin.setOnClickListener(v -> {
            if (checkKonsidiOliMesin.isChecked()) {
                checkKonsidiOliMesin.setChecked(false);
            }
            else {
                checkKonsidiOliMesin.setChecked(true);
            }
        });

        layoutKonsidiOliTransmisi.setOnClickListener(v -> {
            if (checkKonsidiOliTransmisi.isChecked()) {
                checkKonsidiOliTransmisi.setChecked(false);
            }
            else {
                checkKonsidiOliTransmisi.setChecked(true);
            }
        });

        layoutKonsidiOliHidrolik.setOnClickListener(v -> {
            if (checkKonsidiOliHidrolik.isChecked()) {
                checkKonsidiOliHidrolik.setChecked(false);
            }
            else {
                checkKonsidiOliHidrolik.setChecked(true);
            }
        });

        layoutKonsidiOliRem.setOnClickListener(v -> {
            if (checkKonsidiOliRem.isChecked()) {
                checkKonsidiOliRem.setChecked(false);
            }
            else {
                checkKonsidiOliRem.setChecked(true);
            }
        });

        layoutKonsidiOliBBM.setOnClickListener(v -> {
            if (checkKonsidiOliBBM.isChecked()) {
                checkKonsidiOliBBM.setChecked(false);
            }
            else {
                checkKonsidiOliBBM.setChecked(true);
            }
        });

        layoutKonsidiAirflow.setOnClickListener(v -> {
            if (checkKonsidiAirflow.isChecked()) {
                checkKonsidiAirflow.setChecked(false);
            }
            else {
                checkKonsidiAirflow.setChecked(true);
            }
        });

        layoutKonsidiRadiator.setOnClickListener(v -> {
            if (checkKonsidiRadiator.isChecked()) {
                checkKonsidiRadiator.setChecked(false);
            }
            else {
                checkKonsidiRadiator.setChecked(true);
            }
        });

        layoutKondisiBan.setOnClickListener(v -> {
            if (checkKondisiBan.isChecked()) {
                checkKondisiBan.setChecked(false);
            }
            else {
                checkKondisiBan.setChecked(true);
            }
        });

        layoutKerusakan.setOnClickListener(v -> {
            if (checkKerusakan.isChecked()) {
                checkKerusakan.setChecked(false);
            }
            else {
                checkKerusakan.setChecked(true);
            }
        });

        layoutSabukKipas.setOnClickListener(v -> {
            if (checkSabukKipas.isChecked()) {
                checkSabukKipas.setChecked(false);
            }
            else {
                checkSabukKipas.setChecked(true);
            }
        });

        layoutSuaraMesin.setOnClickListener(v -> {
            if (checkSuaraMesin.isChecked()) {
                checkSuaraMesin.setChecked(false);
            }
            else {
                checkSuaraMesin.setChecked(true);
            }
        });

        layoutLampuKendaraan.setOnClickListener(v -> {
            if (checkLampuKendaraan.isChecked()) {
                checkLampuKendaraan.setChecked(false);
            }
            else {
                checkLampuKendaraan.setChecked(true);
            }
        });

        layoutSpionKendaraan.setOnClickListener(v -> {
            if (checkSpionKendaraan.isChecked()) {
                checkSpionKendaraan.setChecked(false);
            }
            else {
                checkSpionKendaraan.setChecked(true);
            }
        });

        layoutGrease.setOnClickListener(v -> {
            if (checkGrease.isChecked()) {
                checkGrease.setChecked(false);
            }
            else {
                checkGrease.setChecked(true);
            }
        });
    }

    public void eventSubmitP2H(View v) {
        getLocation();
        nodocP2H = dbHelper.get_tbl_username(0) + "/P2HVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

        ArrayList<String> vehicleOutput = (ArrayList<String>) listVehicleP2H;

        try {
            if (TextUtils.isEmpty(acKendaraanP2H.getText().toString().trim()) || byteImgP2H == null) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Lengkapi Data!").show();
            }
            // Restrict user from input data other than autocompletion list
            else if (vehicleOutput.indexOf(acKendaraanP2H.getText().toString()) == -1) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Kendaraan tidak valid!").show();
            }
            else {

                String newValueKM = etKMHM.getText().toString().replace(".",",");
                etKMHM.setText(newValueKM);

                dbHelper.insert_dataP2H_header(nodocP2H, dbHelper.get_vehiclecodegroup(1, selectedVehicleCode), selectedVehicleCode,
                        etDescPengecekan.getText().toString(), latitudeP2H, longitudeP2H, byteImgP2H);

                if (checkKebocoranOli.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H01", "KEBOCORAN OLI, AIR, SOLAR DAN MINYAK REM", "CHECK", etKebocoranOli.getText().toString());
                }

                if (checkBautMur.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H02", "BAUT DAN MUR", "CHECK", etBautMur.getText().toString());
                }

                if (checkKonsidiOliMesin.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H03", "KETINGGIAN DAN KONDISI OLI MESIN", "CHECK", etKonsidiOliMesin.getText().toString());
                }

                if (checkKonsidiOliTransmisi.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H04", "KETINGGIAN DAN KONDISI OLI TRANSMISI", "CHECK", etKonsidiOliTransmisi.getText().toString());
                }

                if (checkKonsidiOliHidrolik.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H05", "KETINGGIAN DAN KONDISI OLI HIDROLIK", "CHECK", etKonsidiOliHidrolik.getText().toString());
                }

                if (checkKonsidiOliRem.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H06", "KETINGGIAN DAN KONDISI OLI REM", "CHECK", etKonsidiOliRem.getText().toString());
                }

                if (checkKonsidiOliBBM.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H08", "KETINGGIAN DAN KONDISI OLI BAHAN BAKAR", "CHECK", etKonsidiOliBBM.getText().toString());
                }

                if (checkKonsidiAirflow.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H09", "KETINGGIAN DAN KONDISI SARINGAN UDARA", "CHECK", etKonsidiAirflow.getText().toString());
                }

                if (checkKonsidiRadiator.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H10", "KETINGGIAN DAN KONDISI RADIATOR", "CHECK", etKonsidiRadiator.getText().toString());
                }

                if (checkKondisiBan.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H11", "KETINGGIAN DAN KONDISI BAN", "CHECK", etKondisiBan.getText().toString());
                }

                if (checkKerusakan.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H12", "KERUSAKAN BENGKOK, PENYOK DAN PATAH", "CHECK", etKerusakan.getText().toString());
                }

                if (checkSabukKipas.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H13", "KEKENCANGAN SABUK KIPAS", "CHECK", etSabukKipas.getText().toString());
                }

                if (checkSuaraMesin.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H14", "KEHALUSAN SUARA MESIN", "CHECK", etSuaraMesin.getText().toString());
                }

                if (checkLampuKendaraan.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H15", "LAMPU DEPAN DAN LAMPU BELAKANG", "CHECK", etLampuKendaraan.getText().toString());
                }

                if (checkSpionKendaraan.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H16", "SPION KANAN DAN SPION KIRI", "CHECK", etSpionKendaraan.getText().toString());
                }

                if (checkGrease.isChecked()) {
                    dbHelper.insert_dataP2H_detail(nodocP2H, "P2H17", "KONDISI SUDAH GREASE ATAU BELUM", "CHECK", etSpionKendaraan.getText().toString());
                }

                dbHelper.update_kmhm(newValueKM);

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setContentText("Selesai P2H")
                        .setConfirmText("OK") .setConfirmClickListener(sweetAlertDialog -> {
                            Intent backIntent = new Intent();
                            setResult(6, backIntent);
                            finish();
                        }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void onTimeAwalSelected(int hour, int minute) {
//        String hourAsText;
//        String minuteAsText;
//
//        if (hour < 10) {
//            hourAsText = '0' + String.valueOf(hour);
//        } else {
//            hourAsText = String.valueOf(hour);
//        }
//
//        if (minute < 10) {
//            minuteAsText = '0' + String.valueOf(minute);
//        } else {
//            minuteAsText = String.valueOf(minute);
//        }
//
//        String showTime = hourAsText + ":" + minuteAsText;
//
//    }
//
//    private void onTimeAkhirSelected(int hour, int minute) {
//        String hourAsText;
//        String minuteAsText;
//
//        if (hour < 10) {
//            hourAsText = '0' + String.valueOf(hour);
//        } else {
//            hourAsText = String.valueOf(hour);
//        }
//
//        if (minute < 10) {
//            minuteAsText = '0' + String.valueOf(minute);
//        } else {
//            minuteAsText = String.valueOf(minute);
//        }
//
//        String showTime = hourAsText + ":" + minuteAsText;
//    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latitudeP2H = String.valueOf(latitude);
        longitudeP2H = String.valueOf(longitude);
    }
}