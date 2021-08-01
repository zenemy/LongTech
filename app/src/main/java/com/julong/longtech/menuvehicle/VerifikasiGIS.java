package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DownloadData;
import com.julong.longtech.menusetup.MyAccount;
import com.julong.longtech.menusetup.UploadData;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifikasiGIS extends AppCompatActivity {

    AutoCompleteTextView acKebunGIS, acDivisiGIS, acLokasiGIS, acKegiatanGIS, acIntervalGIS;
    EditText etHasilVerifikasi, etSatuanKerjaGIS;
    TextInputLayout inputLayoutHasilGIS;
    Button btnStartInterval, btnSubmitGIS;
    ImageButton imgTakePictGIS;
    FloatingActionButton btnTagLocation;
    ListView lvKoordinat;

    String[] arrayIntervalOption = {"2", "5", "10"};
    private List<ListParamGIS> listParamGIS;
    AdapterKoordinatGIS adapterKoordinatGIS;
    ArrayAdapter<String> adapterInterval;

    private List<String> listNamaKebunGIS, listKebunCodeGIS, listLokasiCodeGIS, listLokasiNameGIS,
            listDivisiNameGIS, listDivisiCodeGIS, listKegiatanName, listKegiatanCode, listKegiatanSatuan;
    ArrayAdapter<String> adapterKebunGIS, adapterDivisiGIS, adapterLokasiGIS, adapterKegiatanGIS;
    String selectedKebunGIS, selectedDivisiGIS, selectedLokasiGIS, selectedKegiatanGIS,
            selectedSatuanKegiatan, latGIS, longGIS, nodocVerifikasiGIS;
    private long intervalInMillis;
    DatabaseHelper dbhelper;
    Dialog dlgSelesaiVerifikasi;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_gis);

        dbhelper = new DatabaseHelper(this);

        // Declare attribute ID
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
        btnStartInterval = findViewById(R.id.btnStartIntervalGIS);
        inputLayoutHasilGIS = findViewById(R.id.inputLayoutHasilGIS);

        prepareHeaderData();

        btnTagLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

                if (dbhelper.get_statusverifikasigis(0).equals("0")) {
                    nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
                    dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, selectedKebunGIS, selectedDivisiGIS,
                            selectedLokasiGIS, selectedKegiatanGIS, etSatuanKerjaGIS.getText().toString(),
                            etHasilVerifikasi.getText().toString());

                    dbhelper.insert_verifikasigis_detail(nodocVerifikasiGIS, latGIS, longGIS);
                } else if (dbhelper.get_statusverifikasigis(0).equals("1")) {
                    nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);
                    dbhelper.insert_verifikasigis_detail(dbhelper.get_statusverifikasigis(1), latGIS, longGIS);
                } else if (dbhelper.get_statusverifikasigis(8).equals("0")) {

                }

            }
        });

        btnStartInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(acKebunGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acDivisiGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acLokasiGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acKegiatanGIS.getText().toString().trim())
                        || TextUtils.isEmpty(etHasilVerifikasi.getText().toString().trim())
                        || TextUtils.isEmpty(acIntervalGIS.getText().toString().trim())) {
                    new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.ERROR_TYPE).setContentText("Lengkapi Data!").show();
                }
                else if (acIntervalGIS.getText().toString().equals("2")) {
                    startIntervalGIS(2000);
                } else if (acIntervalGIS.getText().toString().equals("5")) {
                    startIntervalGIS(5000);
                } else if (acIntervalGIS.getText().toString().equals("10")) {
                    startIntervalGIS(10000);
                }
            }
        });

        btnSubmitGIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(acKebunGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acDivisiGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acLokasiGIS.getText().toString().trim())
                        || TextUtils.isEmpty(acKegiatanGIS.getText().toString().trim())
                        || TextUtils.isEmpty(etHasilVerifikasi.getText().toString().trim())
                        || TextUtils.isEmpty(acIntervalGIS.getText().toString().trim())) {
                    new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Lengkapi Data!").show();
                } else {
                    //Show summary dialog
                    dlgSelesaiVerifikasi = new Dialog(VerifikasiGIS.this);
                    dlgSelesaiVerifikasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlgSelesaiVerifikasi.setContentView(R.layout.dialog_summarygis);
                    dlgSelesaiVerifikasi.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    Window windowVerificationDone = dlgSelesaiVerifikasi.getWindow();
                    windowVerificationDone.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvEstateSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvEstateSummaryGIS);
                    TextView tvDivisiSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvDivisiSummaryGIS);
                    TextView tvLokasiSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvBlokSummaryGIS);
                    TextView tvKegiatanSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvActivitySummaryGIS);
                    TextView tvHasilSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvResultSummaryGIS);
                    TextView tvTotalKoordinat  = dlgSelesaiVerifikasi.findViewById(R.id.tvTotalKoordinatSummary);
                    Button btnBackSummary = dlgSelesaiVerifikasi.findViewById(R.id.btnBackSummaryGIS);
                    Button btnDoneSummary = dlgSelesaiVerifikasi.findViewById(R.id.btnDoneSummaryGIS);
                    dlgSelesaiVerifikasi.show();
                    btnBackSummary.setOnClickListener(view1 -> dlgSelesaiVerifikasi.dismiss());

                    tvEstateSummary.setText(acKebunGIS.getText().toString());
                    tvDivisiSummary.setText(acDivisiGIS.getText().toString());
                    tvLokasiSummary.setText(acLokasiGIS.getText().toString());
                    tvKegiatanSummary.setText(acKegiatanGIS.getText().toString());
                    tvHasilSummary.setText(etHasilVerifikasi.getText() + " " + etSatuanKerjaGIS.getText().toString());
                    btnDoneSummary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dlgSelesaiVerifikasi.dismiss();
                            new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Pekerjaan Selesai")
                                    .setConfirmClickListener(sweetAlertDialog -> finish()).setConfirmText("OK").show();
                        }
                    });
                }
            }
        });
        loadListViewKoordinat();
    }

    // Loop Interval Tag Location
    private void startIntervalGIS(int delayMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        intervalInMillis = currentTimeMillis + delayMillis;
        new AsyncInterval().execute();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                while (System.currentTimeMillis() < intervalInMillis) {
                    loadListViewKoordinat();
                }

                handler.removeCallbacks(this);
            }
        }, delayMillis);
    }

    private class AsyncInterval extends AsyncTask<Void, Integer, String> {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Mengambil Lokasi..");

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(Void...arg0) {

            while (System.currentTimeMillis() < intervalInMillis) {
                getLocation();
                if (dbhelper.get_statusverifikasigis(0).equals("0")) {
                    nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
                    dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, selectedKebunGIS, selectedDivisiGIS,
                            selectedLokasiGIS, selectedKegiatanGIS, etSatuanKerjaGIS.getText().toString(),
                            etHasilVerifikasi.getText().toString());
                    dbhelper.insert_verifikasigis_detail(nodocVerifikasiGIS, latGIS, longGIS);
                } else if (dbhelper.get_statusverifikasigis(0).equals("1")) {
                    nodocVerifikasiGIS = dbhelper.get_statusverifikasigis(1);
                    dbhelper.insert_verifikasigis_detail(dbhelper.get_statusverifikasigis(1), latGIS, longGIS);
                }
            }

            return "You are at PostExecute";
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

    private void prepareHeaderData() throws SQLiteException {

        nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

        listNamaKebunGIS = dbhelper.get_itemkebun(1);
        listKebunCodeGIS = dbhelper.get_itemkebun(0);
        adapterKebunGIS = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listNamaKebunGIS);
        acKebunGIS.setAdapter(adapterKebunGIS);

        adapterInterval = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayIntervalOption);
        acIntervalGIS.setAdapter(adapterInterval);

        listKegiatanCode = dbhelper.get_activitylist(0);
        listKegiatanName = dbhelper.get_activitylist(1);
        listKegiatanSatuan = dbhelper.get_activitylist(3);
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
            }
        });

        acKegiatanGIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedSatuanKegiatan = listKegiatanSatuan.get(position);
                selectedKegiatanGIS = listKegiatanCode.get(position);
                inputLayoutHasilGIS.setEnabled(true);
                etSatuanKerjaGIS.setText(selectedSatuanKegiatan);
                inputLayoutHasilGIS.setSuffixText(selectedSatuanKegiatan);
            }
        });

        // if the verification still on processs
        if (dbhelper.get_statusverifikasigis(0).equals("1") && dbhelper.get_statusverifikasigis(8).equals("")) {
            selectedKebunGIS = dbhelper.get_statusverifikasigis(2);
            selectedDivisiGIS = dbhelper.get_statusverifikasigis(3);
            selectedLokasiGIS = dbhelper.get_statusverifikasigis(4);
            selectedKegiatanGIS = dbhelper.get_statusverifikasigis(5);
            selectedSatuanKegiatan = dbhelper.get_statusverifikasigis(6);

            acKebunGIS.setText(dbhelper.get_singlekebun(selectedKebunGIS));
            acDivisiGIS.setText(dbhelper.get_singledivisi(selectedDivisiGIS));
            acLokasiGIS.setText(dbhelper.get_singlelokasi(selectedLokasiGIS));
            acKegiatanGIS.setText(dbhelper.get_singlekegiatan(selectedKegiatanGIS));
            etSatuanKerjaGIS.setText(selectedSatuanKegiatan);
            etHasilVerifikasi.setText(dbhelper.get_statusverifikasigis(7));
            inputLayoutHasilGIS.setEnabled(true);
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