package com.julong.longtech.menuhcm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.HashPassword;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menuvehicle.InspeksiHasilKerja;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ApelPagi extends AppCompatActivity {

    public static int dataProcess, REQUEST_IMAGE_CAPTURE = 1;
    public static byte[] gambarApelPagi, gambarAnggota;
    public static String selectedEmp, selectedJabatan, selectedUnit, selectedItemData, selectedShift, nodocApel, latApel, longApel;
    Handler handler = new Handler();
    DatabaseHelper dbhelper;
    HashPassword hashFunction;

    RelativeLayout layoutAnggotaApel;
    LinearLayout layoutBtnApel;
    EditText etKemandoranApel, etLokasiApel, etDescApel;
    TextView tvHeaderApel;
    ImageView imgFotoApelPagi;
    ListView lvPimpinan, lvAnggota;
    public static Button btnActionApel;
    Button btnSubmitApel, btnBackApelPagi;
    FloatingActionButton btnAddEmpApel;
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

        nodocApel = dbhelper.get_tbl_username(0) + "/ABSAPL/" + new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
        hashFunction = new HashPassword(11);

        // Declare design ID
        tvHeaderApel = findViewById(R.id.tvHeaderApelPagi);
        imgFotoApelPagi = findViewById(R.id.imgCaptureApel);
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
        layoutAnggotaApel = findViewById(R.id.layoutAnggotaApel);
        btnBackApelPagi = findViewById(R.id.btnBackApelPagi);

        btnBackApelPagi.setOnClickListener(view -> onBackPressed());

        // Get selected shift value from intent
        Bundle bundle = getIntent().getExtras();
        selectedShift = bundle.getString("shiftapel");
        etKemandoranApel.setText(dbhelper.get_infokemandoranapel(0, dbhelper.get_tbl_username(18)) + " (" +selectedShift + ")");
        tvHeaderApel.setText("APEL PAGI " + apelDate);

        prepateTeamData();
        loadlvpimpinan();
        loadlvanggota();

        // Checking if the leader have checkin this morning,
        // so the leaders wont have to checkin again at middle shift briefing
        if (lvPimpinan.getAdapter().getCount() == 0) {
            Cursor cursorPimpinan = dbhelper.view_preparepimpinan_apelpagi();
            if (cursorPimpinan.moveToFirst()) {
                do {
                    dbhelper.insert_apelpagi_pimpinan(nodocApel, cursorPimpinan.getString(cursorPimpinan.getColumnIndex("empcode")),
                            cursorPimpinan.getString(cursorPimpinan.getColumnIndex("positioncode")),
                            cursorPimpinan.getString(cursorPimpinan.getColumnIndex("groupcode")));
                } while (cursorPimpinan.moveToNext());
            }
            loadlvpimpinan();
        } else {
            loadlvpimpinan();
        }

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
                loadlvpimpinan();
                loadlvanggota();
            }
        });

        tabApelPagi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        lvPimpinan.setVisibility(View.VISIBLE);
                        layoutAnggotaApel.setVisibility(View.GONE);
                        loadlvpimpinan();
                        break;
                    case 1:
                        loadlvanggota();
                        layoutAnggotaApel.setVisibility(View.VISIBLE);
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

        // Finish apel
        btnSubmitApel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etLokasiApel.getText().toString().trim()) || gambarApelPagi == null) {
                    new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Lengkapi Data!").setConfirmText("OK").show();
                }
                else {
                    final SweetAlertDialog warningExitDlg = new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.WARNING_TYPE);
                    warningExitDlg.setTitleText("Selesaikan apel pagi?");
                    warningExitDlg.setCancelText("KEMBALI");
                    warningExitDlg.setConfirmText("SELESAI");
                    warningExitDlg.showCancelButton(true);
                    warningExitDlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog dlgExit) {
                            dbhelper.updateselesai_apelpagi(nodocApel, etLokasiApel.getText().toString(), etDescApel.getText().toString());
                            dlgExit.dismiss();
                            SweetAlertDialog dlgFinishApel = new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.SUCCESS_TYPE);
                            dlgFinishApel.setCancelable(false);
                            dlgFinishApel.setTitleText("Apel Pagi selesai");
                            dlgFinishApel.setConfirmClickListener(sweetAlertDialog -> onBackPressed());
                            dlgFinishApel.setConfirmText("OK").show();
                        }
                    });
                    warningExitDlg.show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Apel foto rame
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

                dbhelper.update_fotorame_apel(dbhelper.check_existingapel(1, selectedShift), latApel, longApel, gambarApelPagi);
            }
        }

        // Apel foto per person
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
                final SweetAlertDialog dlgStartOK = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                dlgStartOK.setTitleText("Berhasil Absen").setContentText(dbhelper.get_empname(selectedEmp)).setConfirmText("OK").show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dlgStartOK.dismiss();

                        handler.removeCallbacks(this);
                    }
                }, 2000);
                loadlvanggota();
                loadlvpimpinan();
            }
        }

        // Scan QR code employee
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

                    // Checkin does the QR code match the selected employee
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

    public void addApelMember(View v) {

        List<String> listAddEmpKemandoran;
        ArrayAdapter<String> adapterAddEmpKemandoran;

        Dialog dlgInsertEmp = new Dialog(this);
        dlgInsertEmp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgInsertEmp.setContentView(R.layout.dialog_insertemp_apel);
        dlgInsertEmp.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowAddEmp = dlgInsertEmp.getWindow();
        windowAddEmp.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        AutoCompleteTextView acAddEmpDlgApel = dlgInsertEmp.findViewById(R.id.acAddEmpDlgApel);
        Button btnBackDlgAddEmp = dlgInsertEmp.findViewById(R.id.btnBackDlgAddEmpApel);
        Button btnOkDlgAddEmp = dlgInsertEmp.findViewById(R.id.btnOkDlgAddEmpApel);
        dlgInsertEmp.show();
        btnBackDlgAddEmp.setOnClickListener(view -> dlgInsertEmp.dismiss());

        listAddEmpKemandoran = dbhelper.get_emp_mygang(selectedShift);
        adapterAddEmpKemandoran = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listAddEmpKemandoran);
        acAddEmpDlgApel.setAdapter(adapterAddEmpKemandoran);

        btnOkDlgAddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(acAddEmpDlgApel.getText().toString().trim())) {
                    Toast.makeText(ApelPagi.this, "Isi karyawan terlebih dahulu", Toast.LENGTH_LONG).show();
                }
                else {
                    dbhelper.insert_apelpagi_anggota(nodocApel, dbhelper.get_empcode(0, acAddEmpDlgApel.getText().toString()),
                            dbhelper.get_position_ancakcode(1, acAddEmpDlgApel.getText().toString()),
                            dbhelper.get_position_ancakcode(2, acAddEmpDlgApel.getText().toString()));
                    loadlvanggota();
                    dlgInsertEmp.dismiss();

                    final SweetAlertDialog dlgAddSuccess = new SweetAlertDialog(ApelPagi.this, SweetAlertDialog.SUCCESS_TYPE);
                    dlgAddSuccess.setTitleText("Berhasil Menambahkan");
                    dlgAddSuccess.setContentText(dbhelper.get_empcode(1, acAddEmpDlgApel.getText().toString()));
                    dlgAddSuccess.setConfirmText("OK").show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dlgAddSuccess.dismiss();

                            handler.removeCallbacks(this);
                        }
                    }, 2000);
                }
            }
        });
    }

    public void prepateTeamData() throws SQLiteException {
        dbhelper = new DatabaseHelper(this);

        if (dbhelper.check_existingapel(0, selectedShift).equals("1") && dbhelper.check_existingapel(4, selectedShift).equals("")) {
            try {
                etLokasiApel.setText(dbhelper.check_existingapel(2, selectedShift));
                etDescApel.setText(dbhelper.check_existingapel(3, selectedShift));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dbhelper.count_fotoapel(dbhelper.check_existingapel(1, selectedShift)).equals("1")) {
                gambarApelPagi = dbhelper.get_fotoapelrame(dbhelper.check_existingapel(1, selectedShift));
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambarApelPagi, 0, gambarApelPagi.length);
                imgFotoApelPagi.setForeground(null);
                imgFotoApelPagi.setImageBitmap(compressedBitmap);
            }
        }
        else if (dbhelper.check_existingapel(4, selectedShift).equals("0")
                || dbhelper.check_existingapel(4, selectedShift).equals("1")) {
            layoutBtnApel.setVisibility(View.GONE);
            btnAddEmpApel.setVisibility(View.GONE);
            imgFotoApelPagi.setEnabled(false);
            etLokasiApel.setFocusable(false);
            etDescApel.setFocusable(false);
            lvAnggota.setEnabled(false);
            lvPimpinan.setEnabled(false);
            etLokasiApel.setText(dbhelper.check_existingapel(2, selectedShift));
            etDescApel.setText(dbhelper.check_existingapel(3, selectedShift));
            gambarApelPagi = dbhelper.get_fotoapelrame(dbhelper.check_existingapel(1, selectedShift));
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambarApelPagi, 0, gambarApelPagi.length);
            imgFotoApelPagi.setForeground(null);
            imgFotoApelPagi.setImageBitmap(compressedBitmap);
        }
        else {

            dbhelper.insert_apelpagi_header(nodocApel, selectedShift);

            // Insertin team data into transaction
            Cursor cursorAnggota = dbhelper.view_prepareanggota_apelpagi(selectedShift);
            if (cursorAnggota.moveToFirst()) {
                do {
                    dbhelper.insert_apelpagi_anggota(nodocApel, cursorAnggota.getString(cursorAnggota.getColumnIndex("empcode")),
                            cursorAnggota.getString(cursorAnggota.getColumnIndex("positioncode")),
                            cursorAnggota.getString(cursorAnggota.getColumnIndex("unitcode")));
                } while (cursorAnggota.moveToNext());
            }

        }

        tabApelPagi.getTabAt(1).select();

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
        apelAdapter = new ApelPagiAdapter(this, listsPimpinan);
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
        apelAdapter = new ApelPagiAdapter(this, listsAnggota);
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

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(4, backIntent);
        finish();
    }
}