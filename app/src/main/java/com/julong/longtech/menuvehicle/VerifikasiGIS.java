package com.julong.longtech.menuvehicle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifikasiGIS extends AppCompatActivity {

    public static AutoCompleteTextView acLokasiGIS, acKegiatanGIS, acIntervalGIS;
    public static EditText etHasilVerifikasi, etDateGIS;

    AutoCompleteTextView acDriverGIS, acVehicleGIS;
    TextInputLayout inputLayoutHasilGIS;
    Button btnStartInterval, btnStopInterval, btnSubmitGIS, btnBackGIS;
    ImageButton imgTakePictGIS;
    FloatingActionButton btnTagLocation;
    ListView lvKoordinat;
    LinearLayout layoutBtnVerifikasi;

    String[] arrayIntervalOption = {"2", "5", "10"};
    private List<ListParamGIS> listParamGIS;
    AdapterKoordinatGIS adapterKoordinatGIS;
    ArrayAdapter<String> adapterInterval;
    MaterialDatePicker<Long> datePickerRKH;

    private List<String> listVehicleGIS, listLokasiGIS, listDriverGIS, listKegiatanGIS;
    ArrayAdapter<String> adapterVehicleGIS, adapterDriverGIS, adapterLokasiGIS, adapterKegiatanGIS;

    private boolean isRunning;
    public static byte[] byteFotoGIS;
    public static String selectedDriverGIS, selectedVehicleGIS;
    String selectedDate, selectedLokasiGIS, selectedKegiatanGIS, selectedSatuanKegiatan, latGIS, longGIS;
    public static String nodocVerifikasiGIS;

    DatabaseHelper dbhelper;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_gis);

        dbhelper = new DatabaseHelper(this);
        dialogHelper = new DialogHelper(this);

        // Declare attribute ID
        btnBackGIS = findViewById(R.id.btnBackGIS);
        etDateGIS = findViewById(R.id.etDateGIS);
        acVehicleGIS = findViewById(R.id.acVehicleGIS);
        acDriverGIS = findViewById(R.id.acDriverGIS);
        acLokasiGIS = findViewById(R.id.acLokasiGIS);
        btnSubmitGIS = findViewById(R.id.btnSubmitGIS);
        lvKoordinat = findViewById(R.id.lvKoordinatGIS);
        acKegiatanGIS = findViewById(R.id.acKegiatanGIS);
        acIntervalGIS = findViewById(R.id.acIntervalGIS);
        etHasilVerifikasi = findViewById(R.id.etHasilGIS);
        imgTakePictGIS = findViewById(R.id.imgTakePictGIS);
        btnTagLocation = findViewById(R.id.btnTagLocationGIS);
        btnStopInterval = findViewById(R.id.btnStopIntervalGIS);
        btnStartInterval = findViewById(R.id.btnStartIntervalGIS);
        inputLayoutHasilGIS = findViewById(R.id.inputLayoutHasilGIS);
        layoutBtnVerifikasi = findViewById(R.id.layoutBtnVerifikasi);

        prepareHeaderData();

        ActivityResultLauncher<Intent> intentLaunchCameraHasil = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byteFotoGIS = stream.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteFotoGIS, 0, byteFotoGIS.length);
                    imgTakePictGIS.setImageBitmap(compressedBitmap);
                    imgTakePictGIS.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgTakePictGIS.setForeground(null);

                }

            }
        );

        imgTakePictGIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byteFotoGIS = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCameraHasil.launch(takePictureIntent);
                }
            }
        });

        // Single tag loc
        btnTagLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                ArrayList<String> kegiatanOutput = (ArrayList<String>) listKegiatanGIS;
                ArrayList<String> lokasiOutput = (ArrayList<String>) listLokasiGIS;

                if (dbhelper.get_statusverifikasigis(0).equals("0")) {
                    if (lokasiOutput.indexOf(acLokasiGIS.getText().toString()) == -1
                            || kegiatanOutput.indexOf(acKegiatanGIS.getText().toString()) == -1) {
                        Toast.makeText(VerifikasiGIS.this, "Lokasi / Kegiatan tidak valid!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
                        dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, selectedDate, selectedVehicleGIS, selectedDriverGIS,
                                selectedLokasiGIS, selectedKegiatanGIS, selectedSatuanKegiatan,
                                etHasilVerifikasi.getText().toString());

                        dbhelper.insert_verifikasigis_detail(nodocVerifikasiGIS, latGIS, longGIS, null, null);
                        loadListViewKoordinat();
                    }
                } else if (dbhelper.get_statusverifikasigis(0).equals("1")) {
                    nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);
                    dbhelper.insert_verifikasigis_detail(dbhelper.get_statusverifikasigis(1), latGIS, longGIS, null, null);
                    loadListViewKoordinat();
                }
            }
        });

        // Start loop location tagging
        btnStartInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> kegiatanOutput = (ArrayList<String>) listKegiatanGIS;
                ArrayList<String> lokasiOutput = (ArrayList<String>) listLokasiGIS;

                if (TextUtils.isEmpty(acLokasiGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acKegiatanGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acIntervalGIS.getText().toString().trim())) {
                    new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Lengkapi Data!").show();
                }
                else if (dbhelper.get_statusverifikasigis(0).equals("1") && dbhelper.get_statusverifikasigis(8).equals("")) {
                    if (acIntervalGIS.getText().toString().equals("2")) {
                        isRunning = true;
                        startIntervalGIS(2000);
                    } else if (acIntervalGIS.getText().toString().equals("5")) {
                        isRunning = true;
                        startIntervalGIS(5000);
                    } else if (acIntervalGIS.getText().toString().equals("10")) {
                        isRunning = true;
                        startIntervalGIS(10000);
                    }
                }
                else {
                    if (lokasiOutput.indexOf(acLokasiGIS.getText().toString()) == -1
                            || kegiatanOutput.indexOf(acKegiatanGIS.getText().toString()) == -1) {
                        Toast.makeText(VerifikasiGIS.this, "Lokasi / Kegiatan tidak valid!", Toast.LENGTH_LONG).show();
                    }
                    else if (acIntervalGIS.getText().toString().equals("2")) {
                        isRunning = true;
                        startIntervalGIS(2000);
                    } else if (acIntervalGIS.getText().toString().equals("5")) {
                        isRunning = true;
                        startIntervalGIS(5000);
                    } else if (acIntervalGIS.getText().toString().equals("10")) {
                        isRunning = true;
                        startIntervalGIS(10000);
                    }
                }
            }
        });

        btnStopInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = false;
                btnStopInterval.setVisibility(View.GONE);
                btnStartInterval.setVisibility(View.VISIBLE);
                nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);
                dbhelper.insert_verifikasigis_detail(dbhelper.get_statusverifikasigis(1), latGIS, longGIS, acIntervalGIS.getText().toString(), "STOP");
            }
        });

    }

    // Loop Interval Tag Location
    private void startIntervalGIS(int selectedMillis) {
        btnStopInterval.setVisibility(View.VISIBLE);
        btnStartInterval.setVisibility(View.GONE);

        getLocation();
        if (dbhelper.get_statusverifikasigis(0).equals("0")) {
            nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
            dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, selectedDate, selectedVehicleGIS, selectedDriverGIS,
                    selectedLokasiGIS, selectedKegiatanGIS, selectedSatuanKegiatan,
                    etHasilVerifikasi.getText().toString());
            dbhelper.insert_verifikasigis_detail(nodocVerifikasiGIS, latGIS, longGIS, acIntervalGIS.getText().toString(), "START");
        } else if (dbhelper.get_statusverifikasigis(0).equals("1")) {
            nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);
            dbhelper.insert_verifikasigis_detail(dbhelper.get_statusverifikasigis(1), latGIS, longGIS, acIntervalGIS.getText().toString(), "START");
        }
        loadListViewKoordinat();

        new Thread(new Runnable() {
            public void run() {
                while (isRunning) {
                    post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(selectedMillis);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(() -> getLocation());

                            if (latGIS != null) {
                                nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);
                                dbhelper.insert_verifikasigis_detail(dbhelper.get_statusverifikasigis(1), latGIS, longGIS,
                                        acIntervalGIS.getText().toString(),null);
                            }

                            runOnUiThread(() -> loadListViewKoordinat());
                        }
                    });
                }
            }
        }).start();

    }

    static void post(Runnable runnable) {
        runnable.run();
    }

    private void prepareHeaderData() throws SQLiteException {

        adapterInterval = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayIntervalOption);
        acIntervalGIS.setAdapter(adapterInterval);

        listDriverGIS = dbhelper.get_employee();
        adapterDriverGIS = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listDriverGIS);
        acDriverGIS.setAdapter(adapterDriverGIS);

        listVehicleGIS = dbhelper.get_vehiclecodelist();
        adapterVehicleGIS = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleGIS);
        acVehicleGIS.setAdapter(adapterVehicleGIS);

        listKegiatanGIS = dbhelper.get_all_transport();
        adapterKegiatanGIS = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanGIS);
        acKegiatanGIS.setAdapter(adapterKegiatanGIS);

        listLokasiGIS = dbhelper.get_fieldcrop(1);
        adapterLokasiGIS = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiGIS);
        acLokasiGIS.setAdapter(adapterLokasiGIS);

        //Datepicker tgl pelaksanaan

        datePickerRKH = MaterialDatePicker.Builder.datePicker().setTitleText("Tanggal Unit Kerja").build();

        datePickerRKH.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                etDateGIS.setText(simpleFormat.format(date));
                selectedDate = simpleFormat.format(date);

            }
        });

        etDateGIS.setOnClickListener(view -> datePickerRKH.show(getSupportFragmentManager(), "DATEPICKER_GIS"));

        acDriverGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDriverGIS = dbhelper.get_empcode(0, adapterDriverGIS.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acDriverGIS.getWindowToken(), 0);
            }
        });

        acVehicleGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicleGIS = adapterVehicleGIS.getItem(position);
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleGIS.getWindowToken(), 0);
            }
        });

        acLokasiGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedLokasiGIS = dbhelper.get_singlelokasiCode(adapterLokasiGIS.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acLokasiGIS.getWindowToken(), 0);
            }
        });

        acKegiatanGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedKegiatanGIS = dbhelper.get_singlekegiatancode(adapterKegiatanGIS.getItem(position));
                selectedSatuanKegiatan = dbhelper.get_singlekegiatanname(selectedKegiatanGIS, 1);
                inputLayoutHasilGIS.setSuffixText(selectedSatuanKegiatan);
                inputLayoutHasilGIS.setEnabled(true);
                btnTagLocation.setVisibility(View.VISIBLE);


                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acKegiatanGIS.getWindowToken(), 0);
            }
        });

        acIntervalGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isRunning = false;
                btnStartInterval.setVisibility(View.VISIBLE);
                btnStopInterval.setVisibility(View.GONE);
            }
        });

        btnBackGIS.setOnClickListener(view -> onBackPressed());

        // if the verification still on processs
        if (dbhelper.get_statusverifikasigis(0).equals("1") && dbhelper.get_statusverifikasigis(8).equals("")) {
            nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);

            selectedVehicleGIS = dbhelper.get_statusverifikasigis(2);
            selectedDriverGIS = dbhelper.get_statusverifikasigis(3);
            selectedLokasiGIS = dbhelper.get_statusverifikasigis(4);
            selectedKegiatanGIS = dbhelper.get_statusverifikasigis(5);
            selectedSatuanKegiatan = dbhelper.get_statusverifikasigis(6);

            acDriverGIS.setDropDownHeight(0);
            acVehicleGIS.setDropDownHeight(0);
            acLokasiGIS.setDropDownHeight(0);
            acLokasiGIS.setKeyListener(null);
            acKegiatanGIS.setKeyListener(null);
            acKegiatanGIS.setDropDownHeight(0);

            acVehicleGIS.setText(dbhelper.get_vehiclename(2, selectedVehicleGIS));
            acDriverGIS.setText(dbhelper.get_empname(selectedDriverGIS));
            acLokasiGIS.setText(dbhelper.get_singlelokasi(selectedLokasiGIS));
            acKegiatanGIS.setText(dbhelper.get_singlekegiatanname(selectedKegiatanGIS, 0));
            etHasilVerifikasi.setText(dbhelper.get_statusverifikasigis(7));
            inputLayoutHasilGIS.setEnabled(true);
            btnTagLocation.setVisibility(View.VISIBLE);

            loadListViewKoordinat();
        }
    }

    // Finish verification work
    public void eventSubmitVerifikasi(View v) {
        if (TextUtils.isEmpty(acLokasiGIS.getText().toString().trim())
                || TextUtils.isEmpty(acKegiatanGIS.getText().toString().trim())
                || TextUtils.isEmpty(etHasilVerifikasi.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi Data!").show();
        }
        else if (isRunning) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Hentikan location tagging!").show();
        }
        else {
            dialogHelper.showSummaryVerifikasiGIS();
        }
    }

    private void loadListViewKoordinat() {
        lvKoordinat = findViewById(R.id.lvKoordinatGIS);
        listParamGIS = new ArrayList<>();
        listParamGIS.clear();
        final Cursor cursor = dbhelper.listview_koordinatgis(nodocVerifikasiGIS);
        if (cursor.moveToFirst()) {
            do {
                ListParamGIS parametersGIS = new ListParamGIS(cursor.getString(1),
                        cursor.getString(2), cursor.getString(0)
                );
                listParamGIS.add(parametersGIS);
            } while (cursor.moveToNext());
        }
        adapterKoordinatGIS = new AdapterKoordinatGIS(this, R.layout.item_lvrkh, listParamGIS);
        lvKoordinat.setAdapter(adapterKoordinatGIS);
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latGIS = String.valueOf(latitude);
        longGIS = String.valueOf(longitude);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}