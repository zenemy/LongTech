package com.julong.longtech.menuhcm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.HashPassword;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.ui.home.AdapterHomeInfo;
import com.julong.longtech.ui.home.ParamListHomeInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ApelPagi extends AppCompatActivity {

    public static int dataProcess, REQUEST_IMAGE_CAPTURE = 1;
    public static byte[] gambarApelPagi, gambarAnggota;
    public static String selectedEmp, selectedJabatan, selectedUnit, selectedItemData, nodocApel, latApel, longApel;
    Handler handler = new Handler();
    DatabaseHelper dbhelper;
    HashPassword hashFunction;

    LinearLayout layoutBtnApel;
    EditText etWaktuApelPagi, etPimpinanApel, etKemandoranApel, etLokasiApel, etDescApel;
    TextView tvHeaderApel;
    ImageButton imgFotoApelPagi;
    ListView lvPimpinan, lvAnggota;
    public static Button btnActionApel;
    Button btnAddEmpApel, btnSubmitApel;
    TabLayout tabApelPagi;

    private List<ApelPagiList> listsPimpinan;
    private List<ApelPagiList> listsAnggota;
    private ApelPagiAdapter apelAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apelpagi);

        dbhelper = new DatabaseHelper(this);
        String apelDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        String apelTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        nodocApel = dbhelper.get_tbl_username(0) + "/ABSAPL/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
        hashFunction = new HashPassword(11);

        tvHeaderApel = findViewById(R.id.tvHeaderApelPagi);
        etWaktuApelPagi = findViewById(R.id.etWaktuApelPagi);
        imgFotoApelPagi = findViewById(R.id.imgCaptureApel);
        etPimpinanApel = findViewById(R.id.etPimpinanApel);
        etKemandoranApel = findViewById(R.id.etKemandoranApel);
        etLokasiApel = findViewById(R.id.etLokasiApel);
        etDescApel = findViewById(R.id.etNoteApel);
        lvPimpinan = findViewById(R.id.lvPimpinanApelPagi);
        lvAnggota = findViewById(R.id.lvAnggotaApelPagi);
        btnSubmitApel = findViewById(R.id.btnSimpanApelPagi);
        btnAddEmpApel = findViewById(R.id.btnAddEmpApel);
        layoutBtnApel = findViewById(R.id.layoutBtnApel);
        tabApelPagi = findViewById(R.id.tabApelPagi);
        btnActionApel = findViewById(R.id.btnActionApel);

        etPimpinanApel.setText(dbhelper.get_infokemandoranapel(2,dbhelper.get_tbl_username(18)));
        etKemandoranApel.setText(dbhelper.get_infokemandoranapel(0, dbhelper.get_tbl_username(18)));
        tvHeaderApel.setText("APEL PAGI " + apelDate);
        etWaktuApelPagi.setText(apelTime);

        prepateTeamData();
        loadlvpimpinan();
        loadlvanggota();

        imgFotoApelPagi.setOnClickListener(v -> {
            dataProcess = 1;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        });

        btnActionApel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadlvanggota();
                loadlvpimpinan();
            }
        });

        tabApelPagi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        lvPimpinan.setVisibility(View.VISIBLE);
                        lvAnggota.setVisibility(View.GONE);
                        loadlvpimpinan();
                        break;
                    case 1:
                        loadlvanggota();
                        lvAnggota.setVisibility(View.VISIBLE);
                        lvPimpinan.setVisibility(View.GONE);
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        btnSubmitApel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etLokasiApel.getText().toString().trim()) || gambarApelPagi == null) {
                    new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Lengkapi Data!").setConfirmText("OK").show();
                }
                final SweetAlertDialog warningExitDlg = new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.WARNING_TYPE);
                warningExitDlg.setTitleText("Selesaikan apel pagi?");
                warningExitDlg.setCancelText("KEMBALI");
                warningExitDlg.setConfirmText("SELESAI");
                warningExitDlg.showCancelButton(true);
                warningExitDlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dlgExit) {
                        dbhelper.updateselesai_apelpagi(nodocApel);
                        lvAnggota.setEnabled(false);
                        lvPimpinan.setEnabled(false);
                        dlgExit.dismissWithAnimation();
                        new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Apel Pagi selesai")
                                .setConfirmClickListener(sweetAlertDialog -> finish()).setConfirmText("OK").show();
                    }
                });
                warningExitDlg.show();


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (dataProcess == 1) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                getLocation();
                Bitmap photoCamera = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                gambarApelPagi = stream.toByteArray();
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambarApelPagi, 0, gambarApelPagi.length);
                imgFotoApelPagi.setImageBitmap(compressedBitmap);
                imgFotoApelPagi.setForeground(null);

                dbhelper.update_fotorame_apel(dbhelper.get_statusapelpagi(1), latApel, longApel, gambarApelPagi);
            }
        }

        if (dataProcess == 2) {
            gambarAnggota = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                getLocation();
                Bitmap photoCamera = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                gambarAnggota = stream.toByteArray();

                ApelPagiAdapter.dlgMetodeAbsen.dismiss();
                dbhelper.updateapel_fotoanggota(nodocApel, selectedItemData, selectedEmp, latApel, longApel, gambarAnggota);
                final SweetAlertDialog dlgStartOK = new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.SUCCESS_TYPE);
                dlgStartOK.setTitleText("Berhasil Absen").setContentText(dbhelper.get_empname(selectedEmp)).setConfirmText("OK").show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dlgStartOK.dismissWithAnimation();

                        handler.removeCallbacks(this);
                    }
                }, 2000);
                loadlvanggota();
                loadlvpimpinan();
            }
        }
        if (dataProcess == 3) {
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                } else {
                    String v_resultbarcode = result.getContents();

                    String hashedResult = v_resultbarcode.substring(0, v_resultbarcode.lastIndexOf("longtech"));
                    String originalContent = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) + selectedEmp;

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String timeResult = v_resultbarcode.substring(v_resultbarcode.indexOf("longtech")+8, v_resultbarcode.length());

                    Date dateResult = null;
                    try {
                        dateResult = format.parse(timeResult);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date currentDate = null;
                    try {
                        currentDate = format.parse(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long calcDifferences = currentDate.getTime() - dateResult.getTime();
                    long diffMinute = calcDifferences / (60 * 1000) % 60;

                    boolean checkPassword = hashFunction.CheckPassword(originalContent, hashedResult);
                    if (checkPassword) {
                        if (diffMinute <= 5) {
                            getLocation();
                            dbhelper.updateapel_scananggota(nodocApel, selectedItemData, selectedEmp, latApel, longApel);
                            final SweetAlertDialog dlgStartOK = new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.SUCCESS_TYPE);
                            dlgStartOK.setTitleText("Berhasil Absen").setContentText(dbhelper.get_empname(selectedEmp)).setConfirmText("OK").show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dlgStartOK.dismissWithAnimation();

                                    handler.removeCallbacks(this);
                                }
                            }, 2000);
                            loadlvanggota();
                            loadlvpimpinan();
                        }
                        else {
                            new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.ERROR_TYPE).setTitleText("QR Code expired!").setConfirmText("OK").show();
                        }
                    }
                    else {
                        new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.ERROR_TYPE).setTitleText("QR Code error!").setConfirmText("OK").show();
                    }
                }
            }
        }
    }

    public void prepateTeamData() throws SQLiteException {
        dbhelper = new DatabaseHelper(this);

        if (dbhelper.get_statusapelpagi(0).equals("1")) {
            try {
                etLokasiApel.setText(dbhelper.get_statusapelpagi(2));
                etDescApel.setText(dbhelper.get_apelpagiisdone(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dbhelper.count_fotoapel(dbhelper.get_statusapelpagi(1)).equals("1")) {
                etWaktuApelPagi.setText(dbhelper.get_statusapelpagi(3));
                gambarApelPagi = dbhelper.get_fotoapelrame(dbhelper.get_statusapelpagi(1));
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambarApelPagi, 0, gambarApelPagi.length);
                imgFotoApelPagi.setForeground(null);
                imgFotoApelPagi.setImageBitmap(compressedBitmap);
            }
        }
        else if (dbhelper.get_apelpagiisdone(0).equals("1")) {
            layoutBtnApel.setVisibility(View.GONE);
            imgFotoApelPagi.setEnabled(false);
            etLokasiApel.setFocusable(false);
            etDescApel.setFocusable(false);
            etLokasiApel.setText(dbhelper.get_apelpagiisdone(2));
            etWaktuApelPagi.setText(dbhelper.get_apelpagiisdone(3));
            etDescApel.setText(dbhelper.get_apelpagiisdone(4));
            gambarApelPagi = dbhelper.get_fotoapelrame(dbhelper.get_apelpagiisdone(1));
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambarApelPagi, 0, gambarApelPagi.length);
            imgFotoApelPagi.setForeground(null);
            imgFotoApelPagi.setImageBitmap(compressedBitmap);
        }
        else {
            dbhelper.insert_apelpagi_header(nodocApel);

            Cursor cursorAnggota = dbhelper.view_prepareanggota_apelpagi();
            if (cursorAnggota.moveToFirst()) {
                do {
                    dbhelper.insert_apelpagi_anggota(nodocApel, cursorAnggota.getString(cursorAnggota.getColumnIndex("empcode")),
                            cursorAnggota.getString(cursorAnggota.getColumnIndex("positioncode")),
                            cursorAnggota.getString(cursorAnggota.getColumnIndex("unitcode")));
                } while (cursorAnggota.moveToNext());
            }

            Cursor cursorPimpinan = dbhelper.view_preparepimpinan_apelpagi();
            if (cursorPimpinan.moveToFirst()) {
                do {
                    dbhelper.insert_apelpagi_pimpinan(nodocApel, cursorPimpinan.getString(cursorPimpinan.getColumnIndex("empcode")),
                            cursorPimpinan.getString(cursorPimpinan.getColumnIndex("positioncode")),
                            cursorPimpinan.getString(cursorPimpinan.getColumnIndex("groupcode")));
                } while (cursorPimpinan.moveToNext());
            }
        }

    }

    public void loadlvpimpinan() {
        listsPimpinan = new ArrayList<>();
        listsPimpinan.clear();
        Cursor cursor = dbhelper.listview_apelpagi_pimpinan();
        if (cursor.moveToFirst()) {
            do {
                ApelPagiList pimpinanLists = new ApelPagiList(
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("empcode")),
                        cursor.getString(cursor.getColumnIndex("positioncode")),
                        cursor.getString(cursor.getColumnIndex("positionname")),
                        cursor.getString(cursor.getColumnIndex("groupcode")),
                        cursor.getString(cursor.getColumnIndex("shiftcode")),
                        cursor.getString(cursor.getColumnIndex("metodeabsen")),
                        cursor.getString(cursor.getColumnIndex("waktuabsen")),
                        cursor.getString(cursor.getColumnIndex("itemdata")),
                        cursor.getBlob(cursor.getColumnIndex("fotoabsen"))
                );
                listsPimpinan.add(pimpinanLists);
            } while (cursor.moveToNext());
        }
        apelAdapter = new ApelPagiAdapter(ApelPagi.this, R.layout.apelpagi_adapter, listsPimpinan);
        lvPimpinan.setAdapter(apelAdapter);
    }

    public void loadlvanggota() {
        listsAnggota = new ArrayList<>();
        listsAnggota.clear();
        Cursor cursor = dbhelper.listview_apelpagi_anggota();
        if (cursor.moveToFirst()) {
            do {
                ApelPagiList anggotaLists = new ApelPagiList(
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("empcode")),
                        cursor.getString(cursor.getColumnIndex("positioncode")),
                        cursor.getString(cursor.getColumnIndex("positionname")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("shiftcode")),
                        cursor.getString(cursor.getColumnIndex("metodeabsen")),
                        cursor.getString(cursor.getColumnIndex("waktuabsen")),
                        cursor.getString(cursor.getColumnIndex("itemdata")),
                        cursor.getBlob(cursor.getColumnIndex("fotoabsen"))
                );
                listsAnggota.add(anggotaLists);
            } while (cursor.moveToNext());
        }
        apelAdapter = new ApelPagiAdapter(ApelPagi.this, R.layout.apelpagi_adapter, listsAnggota);
        lvAnggota.setAdapter(apelAdapter);
    }

    public static void scanBarcode(Activity activity) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Scan Employee QR Code");
        intentIntegrator.initiateScan();
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(ApelPagi.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latApel = String.valueOf(latitude);
        longApel = String.valueOf(longitude);
    }
}