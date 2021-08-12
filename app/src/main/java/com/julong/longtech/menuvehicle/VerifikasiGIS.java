package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifikasiGIS extends AppCompatActivity {

    public static AutoCompleteTextView acKebunGIS, acDivisiGIS, acLokasiGIS, acKegiatanGIS, acIntervalGIS;
    public static EditText etHasilVerifikasi, etSatuanKerjaGIS;
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

    private List<String> listNamaKebunGIS, listKebunCodeGIS, listLokasiCodeGIS, listLokasiNameGIS, listDivisiNameGIS,
            listDivisiCodeGIS, listKegiatanName, listKegiatanCode, listKegiatanSatuanCode, listKegiatanSatuanName;
    ArrayAdapter<String> adapterKebunGIS, adapterDivisiGIS, adapterLokasiGIS, adapterKegiatanGIS;

    private boolean isRunning;
    String selectedKebunGIS, selectedDivisiGIS, selectedLokasiGIS, selectedKegiatanGIS,
            selectedSatuanKegiatan, latGIS, longGIS;
    public static String nodocVerifikasiGIS;
    private long intervalInMillis;

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
        acKebunGIS = findViewById(R.id.acKebunGIS);
        acDivisiGIS = findViewById(R.id.acDivisiGIS);
        acLokasiGIS = findViewById(R.id.acLokasiGIS);
        btnSubmitGIS = findViewById(R.id.btnSubmitGIS);
        lvKoordinat = findViewById(R.id.lvKoordinatGIS);
        acKegiatanGIS = findViewById(R.id.acKegiatanGIS);
        acIntervalGIS = findViewById(R.id.acIntervalGIS);
        etHasilVerifikasi = findViewById(R.id.etHasilGIS);
        imgTakePictGIS = findViewById(R.id.imgTakePictGIS);
        btnTagLocation = findViewById(R.id.btnTagLocationGIS);
        etSatuanKerjaGIS = findViewById(R.id.etSatuanKerjaGIS);
        btnStopInterval = findViewById(R.id.btnStopIntervalGIS);
        btnStartInterval = findViewById(R.id.btnStartIntervalGIS);
        inputLayoutHasilGIS = findViewById(R.id.inputLayoutHasilGIS);
        layoutBtnVerifikasi = findViewById(R.id.layoutBtnVerifikasi);

        prepareHeaderData();

        // Single tag loc
        btnTagLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                ArrayList<String> kegiatanOutput = (ArrayList<String>) listKegiatanName;
                ArrayList<String> lokasiOutput = (ArrayList<String>) listLokasiNameGIS;

                if (dbhelper.get_statusverifikasigis(0).equals("0")) {
                    if (lokasiOutput.indexOf(acLokasiGIS.getText().toString()) == -1
                            || kegiatanOutput.indexOf(acKegiatanGIS.getText().toString()) == -1) {
                        Toast.makeText(VerifikasiGIS.this, "Lokasi / Kegiatan tidak valid!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
                        dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, selectedKebunGIS, selectedDivisiGIS,
                                selectedLokasiGIS, selectedKegiatanGIS, etSatuanKerjaGIS.getText().toString(),
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

        btnStartInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> kegiatanOutput = (ArrayList<String>) listKegiatanName;
                ArrayList<String> lokasiOutput = (ArrayList<String>) listLokasiNameGIS;

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
            dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, selectedKebunGIS, selectedDivisiGIS,
                    selectedLokasiGIS, selectedKegiatanGIS, etSatuanKerjaGIS.getText().toString(),
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

        listNamaKebunGIS = dbhelper.get_itemkebun(1);
        listKebunCodeGIS = dbhelper.get_itemkebun(0);
        adapterKebunGIS = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listNamaKebunGIS);
        acKebunGIS.setAdapter(adapterKebunGIS);

        adapterInterval = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayIntervalOption);
        acIntervalGIS.setAdapter(adapterInterval);

        listKegiatanCode = dbhelper.get_activitylist(0);
        listKegiatanName = dbhelper.get_activitylist(1);
        listKegiatanSatuanCode = dbhelper.get_activitylist(3);
        listKegiatanSatuanName = dbhelper.get_activitylist(4);
        adapterKegiatanGIS = new ArrayAdapter<String>(VerifikasiGIS.this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanName);
        acKegiatanGIS.setAdapter(adapterKegiatanGIS);

        acKebunGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                acDivisiGIS.setText(null);
                acLokasiGIS.setText(null);
                acDivisiGIS.setAdapter(null);
                selectedKebunGIS = listKebunCodeGIS.get(position);

                listDivisiNameGIS = dbhelper.get_itemdivisi(adapterKebunGIS.getItem(position), 1);
                listDivisiCodeGIS = dbhelper.get_itemdivisi(adapterKebunGIS.getItem(position), 0);
                adapterDivisiGIS = new ArrayAdapter<String>(VerifikasiGIS.this, R.layout.spinnerlist, R.id.spinnerItem, listDivisiNameGIS);
                acDivisiGIS.setAdapter(adapterDivisiGIS);
            }
        });

        acDivisiGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDivisiGIS = listDivisiCodeGIS.get(position);

                listLokasiCodeGIS = dbhelper.get_fieldcrop_filtered(selectedKebunGIS, selectedDivisiGIS, 0);
                listLokasiNameGIS = dbhelper.get_fieldcrop_filtered(selectedKebunGIS, selectedDivisiGIS, 1);
                adapterLokasiGIS = new ArrayAdapter<String>(VerifikasiGIS.this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiNameGIS);
                acLokasiGIS.setAdapter(adapterLokasiGIS);
            }
        });

        acLokasiGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedLokasiGIS = listLokasiCodeGIS.get(position);
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.this.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acLokasiGIS.getWindowToken(), 0);
            }
        });

        acKegiatanGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedSatuanKegiatan = listKegiatanSatuanCode.get(position);
                selectedKegiatanGIS = listKegiatanCode.get(position);
                inputLayoutHasilGIS.setEnabled(true);
                etSatuanKerjaGIS.setText(listKegiatanSatuanName.get(position));
                inputLayoutHasilGIS.setSuffixText(selectedSatuanKegiatan);

                btnTagLocation.setVisibility(View.VISIBLE);

                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.this.INPUT_METHOD_SERVICE);
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

        btnBackGIS.setOnClickListener(view -> finish());

        // if the verification still on processs
        if (dbhelper.get_statusverifikasigis(0).equals("1") && dbhelper.get_statusverifikasigis(8).equals("")) {
            nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);

            selectedKebunGIS = dbhelper.get_statusverifikasigis(2);
            selectedDivisiGIS = dbhelper.get_statusverifikasigis(3);
            selectedLokasiGIS = dbhelper.get_statusverifikasigis(4);
            selectedKegiatanGIS = dbhelper.get_statusverifikasigis(5);
            selectedSatuanKegiatan = dbhelper.get_statusverifikasigis(6);

            acKebunGIS.setDropDownHeight(0);
            acDivisiGIS.setDropDownHeight(0);
            acLokasiGIS.setDropDownHeight(0);
            acLokasiGIS.setKeyListener(null);
            acKegiatanGIS.setKeyListener(null);
            acKegiatanGIS.setDropDownHeight(0);

            acKebunGIS.setText(dbhelper.get_singlekebun(selectedKebunGIS));
            acDivisiGIS.setText(dbhelper.get_singledivisi(selectedDivisiGIS));
            acLokasiGIS.setText(dbhelper.get_singlelokasi(selectedLokasiGIS));
            acKegiatanGIS.setText(dbhelper.get_singlekegiatan(selectedKegiatanGIS));
            etSatuanKerjaGIS.setText(selectedSatuanKegiatan);
            etHasilVerifikasi.setText(dbhelper.get_statusverifikasigis(7));
            inputLayoutHasilGIS.setEnabled(true);
            btnTagLocation.setVisibility(View.VISIBLE);

            loadListViewKoordinat();
        }
    }

    public void eventSubmitVerifikasi(View v) {
        if (TextUtils.isEmpty(acLokasiGIS.getText().toString().trim())
                || TextUtils.isEmpty(acKegiatanGIS.getText().toString().trim())
                || TextUtils.isEmpty(etHasilVerifikasi.getText().toString().trim())) {
            new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi Data!").show();
        }
        else if (isRunning) {
            new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.ERROR_TYPE)
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
        GPSTracker gps = new GPSTracker(VerifikasiGIS.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latGIS = String.valueOf(latitude);
        longGIS = String.valueOf(longitude);
    }
}