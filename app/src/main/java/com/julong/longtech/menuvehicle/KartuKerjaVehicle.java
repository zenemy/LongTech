package com.julong.longtech.menuvehicle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DownloadData;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KartuKerjaVehicle extends AppCompatActivity {

    private KeyListener keyListenerEtHasilKerja, keyListenerJumlahRitase;
    byte[] gambarCarLog, fotoKilometer;
    ActivityResultLauncher<Intent> intentLaunchCameraHasil, intentLaunchCameraKM;

    EditText etHasilKerjaCarLog, etCatatanCarLog, etHasilSatuanMuat, etHasilKerjaLaterite;
    TextView tvInfoUnitCarLog;
    AutoCompleteTextView acLoadTypeCarLog, acHelper1CarLog, acHelper2CarLog, acLoadCategoryCarLog, acAsalKebunCarLog, acAsalDivisiCarLog,
            acAsalLokasiCarLog, acTujuanKebunCarLog, acTujuanDivisiCarLog, acTujuanLokasiCarLog, acTujuanKegiatanCarLog;
    LinearLayout layoutHelperCarlog, layoutAsalCarLog, layoutTujuanCarLog, layoutHasilKerja, layoutRitaseMuat;
    Button btnSubmitCarlog, btnBackCarLog;
    ImageButton btnAddHasilKerja, btnMinusHasilKerja, btnReduceHasilKerjaLaterite, btnAddHasilKerjaLaterite, btnCameraCarLog, btnFotoKilometer;
    TextInputLayout inputLayoutAsalKebun, inputLayoutAsalDivisi, inputLayoutAsalLokasi, inputLayoutTujuanKebun, inputLayoutHelper1CarLog, inputLayoutSatuanMuat,
            inputLayoutHelper2CarLog, inputLayoutTujuanDivisi, inputLayoutTujuanLokasi, inputLayoutTujuanKegiatan, inputLayoutHasilKerjaCarLog;
    Dialog dlgSelesaiCarLog;
    View parentLayout;

    // The Code
    private List<String> listTypeLoadCode, listCategoryLoadCode, listAsalKebunCode, listAsalDivisiCode,
            listAsalLokasi, listTujuanLokasi, listTujuanKebunCode, listTujuanDivisiCode, listTujuanKegiatan;
    String savedate, nodocCarLog, selectedTypeLoad, selectedCategoryLoad, selectedHelper1, selectedHelper2, selectedAsalKebun, selectedAsalDivisi, selectedAsalLokasi,
            selectedTujuanKebun, selectedTujuanDivisi, selectedTujuanLokasi, selectedTujuanKegiatan, latCarLog, longCarLog;

    //The Name
    private List<String> listMuatanCarLog, listEmployee, listCategoryMuatan, listAsalKebunCarLog, listAsalDivisiCarLog,
            listTujuanKebunCarLog, listTujuanDivisiCarLog;
    ArrayAdapter<String> adapterMuatanCarLog, adapterEmployee, adapterCategoryMuatan, adapterAsalKebunCarLog, adapterAsalDivisiCarLog, adapterTujuanKebunCarLog,
            adapterTujuanDivisiCarLog, adapterLokasiAsalCarLog, adapterLokasiTujuanCarLog, adapterKegiatanTujuan;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartukerja_vehicle);

        dbhelper = new DatabaseHelper(this);
        tvInfoUnitCarLog = findViewById(R.id.tvInfoUnitCarLog);
        layoutAsalCarLog = findViewById(R.id.layoutAsalMuatanCarLog);
        layoutTujuanCarLog = findViewById(R.id.layoutTujuanMuatanCarLog);
        layoutHelperCarlog = findViewById(R.id.layoutHelperCarlog);
        layoutHasilKerja = findViewById(R.id.LayoutHasilKerja);
        inputLayoutHelper1CarLog = findViewById(R.id.inputLayoutHelper1CarLog);
        inputLayoutHelper2CarLog = findViewById(R.id.inputLayoutHelper2CarLog);
        acLoadTypeCarLog = findViewById(R.id.acLoadTypeCarLog);
        acHelper1CarLog = findViewById(R.id.acHelper1CarLog);
        acHelper2CarLog = findViewById(R.id.acHelper2CarLog);
        layoutRitaseMuat = findViewById(R.id.layoutRitaseMuat);
        acAsalKebunCarLog = findViewById(R.id.acAsalKebunCarLog);
        acAsalDivisiCarLog = findViewById(R.id.acAsalDivisiCarLog);
        acAsalLokasiCarLog = findViewById(R.id.acAsalLokasiCarLog);
        acLoadCategoryCarLog = findViewById(R.id.acLoadCategoryCarLog);
        acTujuanKebunCarLog = findViewById(R.id.acTujuanKebunCarLog);
        acTujuanDivisiCarLog = findViewById(R.id.acTujuanDivisiCarLog);
        acTujuanLokasiCarLog = findViewById(R.id.acTujuanLokasiCarLog);
        acTujuanKegiatanCarLog = findViewById(R.id.acTujuanKegiatanCarLog);
        btnAddHasilKerja = findViewById(R.id.btnAddHasilKerjaCarLog);
        btnMinusHasilKerja = findViewById(R.id.btnReduceHasilKerjaCarLog);
        btnAddHasilKerjaLaterite = findViewById(R.id.btnAddHasilKerjaLaterite);
        btnReduceHasilKerjaLaterite = findViewById(R.id.btnReduceHasilKerjaLaterite);
        btnSubmitCarlog = findViewById(R.id.btnSubmitCarLog);
        btnBackCarLog = findViewById(R.id.btnBackCarLog);
        inputLayoutAsalKebun = findViewById(R.id.inputLayoutAsalKebun);
        inputLayoutAsalDivisi = findViewById(R.id.inputLayoutAsalDivisi);
        inputLayoutAsalLokasi = findViewById(R.id.inputLayoutAsalLokasi);
        inputLayoutTujuanKebun = findViewById(R.id.inputLayoutTujuanKebun);
        inputLayoutTujuanDivisi = findViewById(R.id.inputLayoutTujuanDivisi);
        inputLayoutTujuanLokasi = findViewById(R.id.inputLayoutTujuanLokasi);
        inputLayoutTujuanKegiatan = findViewById(R.id.inputLayoutTujuanKegiatan);
        inputLayoutSatuanMuat = findViewById(R.id.inputLayoutSatuanMuat);
        etHasilKerjaCarLog = findViewById(R.id.etHasilKerjaCarLog);
        etCatatanCarLog = findViewById(R.id.etCatatanCarLog);
        etHasilKerjaLaterite = findViewById(R.id.etHasilKerjaLaterite);
        etHasilSatuanMuat = findViewById(R.id.etHasilSatuanMuat);
        inputLayoutHasilKerjaCarLog = findViewById(R.id.inputLayoutHasilKerjaCarLog);
        btnCameraCarLog = findViewById(R.id.imgCarLog);
        keyListenerEtHasilKerja = etHasilKerjaCarLog.getKeyListener();
        keyListenerJumlahRitase = etHasilKerjaLaterite.getKeyListener();
        parentLayout = findViewById(android.R.id.content);

        prepareCarLogData();

        intentLaunchCameraHasil = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    gambarCarLog = stream.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambarCarLog, 0, gambarCarLog.length);
                    btnCameraCarLog.setImageBitmap(compressedBitmap);
                    btnCameraCarLog.setBackground(null);

                }

            }
        );


        intentLaunchCameraKM = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    fotoKilometer = stream.toByteArray();
                    Bitmap bitmapKM = BitmapFactory.decodeByteArray(fotoKilometer, 0, fotoKilometer.length);
                    btnFotoKilometer.setImageBitmap(bitmapKM);
                    btnFotoKilometer.setBackground(null);

                }

            }
        );

        btnCameraCarLog.setOnClickListener(v -> {
            gambarCarLog = null;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                intentLaunchCameraHasil.launch(takePictureIntent);
            }
        });

        acAsalKebunCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedAsalDivisi = null;
                acAsalDivisiCarLog.setText(null);

                selectedAsalLokasi = null;
                acAsalLokasiCarLog.setText(null);

                selectedAsalKebun = listAsalKebunCode.get(position);
                listAsalDivisiCarLog = dbhelper.get_itemdivisi(adapterAsalKebunCarLog.getItem(position), 1);
                listAsalDivisiCode = dbhelper.get_itemdivisi(adapterAsalKebunCarLog.getItem(position), 0);

                adapterAsalDivisiCarLog = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listAsalDivisiCarLog);
                acAsalDivisiCarLog.setAdapter(adapterAsalDivisiCarLog);

            }
        });

        acTujuanKegiatanCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedTujuanKegiatan = dbhelper.get_singlekegiatanCarLog((String) adapterView.getItemAtPosition(position));
            }
        });

        acAsalDivisiCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedAsalLokasi = null;
                acAsalLokasiCarLog.setText(null);

                selectedAsalDivisi = listAsalDivisiCode.get(position);
                listAsalLokasi = dbhelper.get_fieldcrop_filtered(selectedAsalKebun, selectedAsalDivisi, 1);
                adapterLokasiAsalCarLog = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listAsalLokasi);
                acAsalLokasiCarLog.setAdapter(adapterLokasiAsalCarLog);
            }
        });

        acHelper1CarLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if( -1 != text.toString().indexOf("\n") ) {
                    ViewGroup.LayoutParams paramsAc = acHelper1CarLog.getLayoutParams();
                    paramsAc.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    paramsAc.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    acHelper1CarLog.setLayoutParams(paramsAc);

                    ViewGroup.LayoutParams params = inputLayoutHelper1CarLog.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    inputLayoutHelper1CarLog.setLayoutParams(params);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        acHelper2CarLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if( -1 != text.toString().indexOf("\n") ) {
                    ViewGroup.LayoutParams paramsAc = acHelper2CarLog.getLayoutParams();
                    paramsAc.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    paramsAc.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    acHelper2CarLog.setLayoutParams(paramsAc);

                    ViewGroup.LayoutParams params = inputLayoutHelper2CarLog.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    inputLayoutHelper2CarLog.setLayoutParams(params);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        acHelper1CarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedHelper1 = dbhelper.get_empcode(0, (String) adapterView.getItemAtPosition(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acHelper1CarLog.getWindowToken(), 0);
            }
        });

        acHelper2CarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedHelper2 = dbhelper.get_empcode(0, (String) adapterView.getItemAtPosition(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acHelper2CarLog.getWindowToken(), 0);
            }
        });

        acTujuanKebunCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTujuanDivisi = null;
                acTujuanDivisiCarLog.setText(null);

                selectedTujuanLokasi = null;
                acTujuanLokasiCarLog.setText(null);

                selectedTujuanKebun = listTujuanKebunCode.get(position);
                listTujuanDivisiCarLog = dbhelper.get_itemdivisi(adapterTujuanKebunCarLog.getItem(position), 1);
                listTujuanDivisiCode = dbhelper.get_itemdivisi(adapterTujuanKebunCarLog.getItem(position), 0);
                adapterTujuanDivisiCarLog = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listTujuanDivisiCarLog);
                acTujuanDivisiCarLog.setAdapter(adapterTujuanDivisiCarLog);
            }
        });

        acAsalLokasiCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedAsalLokasi = dbhelper.get_singlelokasiCode((String) adapterView.getItemAtPosition(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acAsalLokasiCarLog.getWindowToken(), 0);
            }
        });

        acTujuanLokasiCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedTujuanLokasi = dbhelper.get_singlelokasiCode((String) adapterView.getItemAtPosition(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acTujuanLokasiCarLog.getWindowToken(), 0);
            }
        });

        acTujuanDivisiCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedTujuanLokasi = null;
                acTujuanLokasiCarLog.setText(null);

                selectedTujuanDivisi = listTujuanDivisiCode.get(position);
                listTujuanLokasi = dbhelper.get_fieldcrop_filtered(selectedTujuanKebun, selectedTujuanDivisi, 1);
                adapterLokasiTujuanCarLog = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listTujuanLokasi);
                acTujuanLokasiCarLog.setAdapter(adapterLokasiTujuanCarLog);
            }
        });

        acLoadTypeCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Clear Value
                acLoadCategoryCarLog.setText(null);
                acHelper1CarLog.setText(null);
                acHelper2CarLog.setText(null);
                acAsalKebunCarLog.setText(null);
                acAsalDivisiCarLog.setText(null);
                acAsalDivisiCarLog.setText("");

                acTujuanKebunCarLog.setText("");
                acTujuanDivisiCarLog.setText("");
                acTujuanLokasiCarLog.setText("");
                acTujuanKegiatanCarLog.setText("");
                etHasilKerjaLaterite.setText(null);
                etHasilKerjaCarLog.setText(null);
                etCatatanCarLog.setText(null);
                etHasilSatuanMuat.setText(null);
                gambarCarLog = null;
                etCatatanCarLog.setText(null);
                btnCameraCarLog.setImageResource(R.drawable.ic_menu_camera);

                //Gone Group input
                layoutHasilKerja.setVisibility(View.GONE);
                layoutTujuanCarLog.setVisibility(View.GONE);
                layoutAsalCarLog.setVisibility(View.GONE);
                layoutHelperCarlog.setVisibility(View.GONE);
                btnAddHasilKerja.setVisibility(View.GONE);
                btnMinusHasilKerja.setVisibility(View.GONE);

                inputLayoutHasilKerjaCarLog.setSuffixText(null);
                selectedTypeLoad = listTypeLoadCode.get(position);
                listCategoryMuatan = dbhelper.get_loadcategory(adapterMuatanCarLog.getItem(position), 1);
                listCategoryLoadCode = dbhelper.get_loadcategory(adapterMuatanCarLog.getItem(position), 0);
                adapterCategoryMuatan = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listCategoryMuatan);
                acLoadCategoryCarLog.setAdapter(adapterCategoryMuatan);
            }
        });

        acLoadCategoryCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedCategoryLoad = listCategoryLoadCode.get(position);

                //Clear Value
                acHelper1CarLog.setText(null);
                acHelper2CarLog.setText(null);
                acAsalKebunCarLog.setText(null);
                acAsalDivisiCarLog.setText(null);
                acAsalDivisiCarLog.setText("");

                acTujuanKebunCarLog.setText("");
                acTujuanDivisiCarLog.setText("");
                acTujuanLokasiCarLog.setText("");
                acTujuanKegiatanCarLog.setText("");
                etHasilKerjaLaterite.setText(null);
                etHasilKerjaCarLog.setText(null);
                etCatatanCarLog.setText(null);
                etHasilSatuanMuat.setText(null);
                gambarCarLog = null;
                etCatatanCarLog.setText(null);
                btnCameraCarLog.setImageResource(R.drawable.ic_menu_camera);

                if (dbhelper.layoutsetting_carlog(0, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(1, adapterCategoryMuatan.getItem(position)).equals("Y")) {
//                    layoutHelperCarlog.setVisibility(View.VISIBLE);
//                    inputLayoutHelper1CarLog.setVisibility(View.VISIBLE);
//                    inputLayoutHelper2CarLog.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(0, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(1, adapterCategoryMuatan.getItem(position)).equals("N")) {
//                    layoutHelperCarlog.setVisibility(View.VISIBLE);
//                    inputLayoutHelper1CarLog.setVisibility(View.VISIBLE);
//                    inputLayoutHelper2CarLog.setVisibility(View.GONE);
                }
                else if (dbhelper.layoutsetting_carlog(0, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(1, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutHelperCarlog.setVisibility(View.GONE);
                }

                //Layout Asal Muatan
                if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutAsalCarLog.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                    inputLayoutAsalKebun.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutAsalKebun.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                    inputLayoutAsalDivisi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutAsalDivisi.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                    inputLayoutAsalLokasi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutAsalLokasi.setVisibility(View.GONE);
                }

                // Layout Tujuan Muatan
                if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutTujuanCarLog.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanKebun.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanKebun.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanDivisi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanDivisi.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanLokasi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanLokasi.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanKegiatan.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanKegiatan.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutHasilKerja.setVisibility(View.VISIBLE);
                    etHasilKerjaCarLog.setKeyListener(keyListenerEtHasilKerja);
                    etHasilKerjaCarLog.setText(null);
                    etHasilSatuanMuat.setText(null);
                    etHasilKerjaLaterite.setText(null);
                    etCatatanCarLog.setText(null);
                    btnAddHasilKerja.setVisibility(View.GONE);
                    btnMinusHasilKerja.setVisibility(View.GONE);
                    inputLayoutSatuanMuat.setVisibility(View.GONE);
                    layoutRitaseMuat.setVisibility(View.GONE);
                } else if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutHasilKerja.setVisibility(View.VISIBLE);
                    etHasilKerjaCarLog.setKeyListener(null);
                    etHasilKerjaCarLog.setText("0");
                    etHasilSatuanMuat.setText(null);
                    etHasilKerjaLaterite.setText(null);
                    etCatatanCarLog.setText(null);
                    btnAddHasilKerja.setVisibility(View.VISIBLE);
                    btnMinusHasilKerja.setVisibility(View.VISIBLE);
                    inputLayoutSatuanMuat.setVisibility(View.GONE);
                    layoutRitaseMuat.setVisibility(View.GONE);
                } else if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("R")) {
                    layoutHasilKerja.setVisibility(View.VISIBLE);
                    etHasilKerjaCarLog.setKeyListener(null);
                    etHasilKerjaCarLog.setText("1");
                    etHasilSatuanMuat.setText(null);
                    etHasilKerjaLaterite.setText(null);
                    etCatatanCarLog.setText(null);
                    btnAddHasilKerja.setVisibility(View.GONE);
                    btnMinusHasilKerja.setVisibility(View.GONE);
                    inputLayoutSatuanMuat.setVisibility(View.GONE);
                    layoutRitaseMuat.setVisibility(View.GONE);
                } else if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("L")) {
                    layoutHasilKerja.setVisibility(View.VISIBLE);
                    etHasilKerjaCarLog.setKeyListener(null);
                    etHasilKerjaLaterite.setKeyListener(null);
                    etHasilKerjaCarLog.setText("0");
                    etHasilKerjaLaterite.setText("0");
                    inputLayoutSatuanMuat.setSuffixText("M3");
                    btnAddHasilKerjaLaterite.setVisibility(View.VISIBLE);
                    btnReduceHasilKerjaLaterite.setVisibility(View.VISIBLE);
                    inputLayoutSatuanMuat.setVisibility(View.VISIBLE);
                    layoutRitaseMuat.setVisibility(View.VISIBLE);
                    btnAddHasilKerja.setVisibility(View.GONE);
                    btnMinusHasilKerja.setVisibility(View.GONE);
                } else if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("NA")) {
                    layoutHasilKerja.setVisibility(View.GONE);
                    etHasilKerjaCarLog.setText(null);
                    etHasilSatuanMuat.setText(null);
                    etHasilKerjaLaterite.setText(null);
                    etCatatanCarLog.setText(null);
                }

                inputLayoutHasilKerjaCarLog.setSuffixText(dbhelper.settingcarlog_satuanhasilkerja(adapterCategoryMuatan.getItem(position)));

            }
        });

        etHasilKerjaLaterite.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etHasilKerjaLaterite.getText().toString().contains("-")) {
                    etHasilKerjaLaterite.setText("0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etHasilKerjaCarLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etHasilKerjaCarLog.getText().toString().contains("-")) {
                    etHasilKerjaCarLog.setText("0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        if (dbhelper.layoutsetting_carlog(9, acLoadCategoryCarLog.getText().toString()).equals("L")) {
            etHasilSatuanMuat.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub

                    if (etHasilSatuanMuat.getText().toString().length() > 0) {
                        btnAddHasilKerjaLaterite.setEnabled(true);
                        btnReduceHasilKerjaLaterite.setEnabled(true);
                        inputLayoutSatuanMuat.setError(null);
                        inputLayoutSatuanMuat.setErrorEnabled(false);
                    }
                    else if (etHasilSatuanMuat.getText().toString().length() == 0) {
                        etHasilKerjaCarLog.setText("0");
                        etHasilKerjaLaterite.setText("0");
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // TODO Auto-generated method stub
                }
            });
        }

        acAsalKebunCarLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (acAsalKebunCarLog.getText().toString().equals("")) {
                    acAsalDivisiCarLog.setAdapter(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        acTujuanKebunCarLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (acTujuanKebunCarLog.getText().toString().equals("")) {
                    acTujuanDivisiCarLog.setAdapter(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        btnAddHasilKerjaLaterite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etHasilSatuanMuat.getText().toString().trim())) {
                    inputLayoutSatuanMuat.setErrorEnabled(true);
                    inputLayoutSatuanMuat.setError("Isi Satuan Muat");
                }
                else if (etHasilSatuanMuat.getText().toString().equals("0")) {
                    inputLayoutSatuanMuat.setErrorEnabled(true);
                    inputLayoutSatuanMuat.setError("Satuan Muat kurang!");
                }
                else {
                    int result = Integer.parseInt(etHasilKerjaLaterite.getText().toString()) + 1;
                    etHasilKerjaLaterite.setText(String.valueOf(result));

                    int resultKerja = Integer.parseInt(etHasilKerjaCarLog.getText().toString()) + Integer.parseInt(etHasilSatuanMuat.getText().toString());
                    etHasilKerjaCarLog.setText(String.valueOf(resultKerja));
                }

            }
        });

        btnReduceHasilKerjaLaterite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etHasilSatuanMuat.getText().toString().trim())) {
                    inputLayoutSatuanMuat.setErrorEnabled(true);
                    inputLayoutSatuanMuat.setError("Isi Satuan Muat");
                }
                else if (etHasilSatuanMuat.getText().toString().equals("0")) {
                    inputLayoutSatuanMuat.setErrorEnabled(true);
                    inputLayoutSatuanMuat.setError("Satuan Muat kurang!");
                }
                else {
                    int result = Integer.parseInt(etHasilKerjaLaterite.getText().toString()) - 1;
                    etHasilKerjaLaterite.setText(String.valueOf(result));

                    int resultKerja = Integer.parseInt(etHasilKerjaCarLog.getText().toString()) - Integer.parseInt(etHasilSatuanMuat.getText().toString());
                    etHasilKerjaCarLog.setText(String.valueOf(resultKerja));
                }

            }
        });

        btnAddHasilKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(etHasilKerjaCarLog.getText().toString()) + 1;
                etHasilKerjaCarLog.setText(String.valueOf(result));
            }
        });

        btnMinusHasilKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(etHasilKerjaCarLog.getText().toString()) - 1;
                etHasilKerjaCarLog.setText(String.valueOf(result));
            }
        });

        btnBackCarLog.setOnClickListener(v -> onBackPressed());
    }

    void prepareCarLogData() {
        tvInfoUnitCarLog.setText(dbhelper.get_tbl_username(19) + " [" + dbhelper.get_tbl_username(27) + "]");
        selectedAsalDivisi = "";
        selectedTujuanDivisi = "";
        selectedHelper1 = "";
        selectedHelper2 = "";
        selectedAsalKebun = "";
        selectedAsalDivisi = "";
        selectedAsalLokasi = "";
        selectedTujuanKebun = "";
        selectedTujuanDivisi = "";
        selectedTujuanLokasi = "";
        selectedTujuanKegiatan = "";

        if (dbhelper.get_transactionstatuscarlog(0).equals("1")) {
            acLoadTypeCarLog.setText(dbhelper.get_singleloadtype(dbhelper.get_transactionstatuscarlog(3)));
            acLoadCategoryCarLog.setText(dbhelper.get_singlecategoryloadtype(dbhelper.get_transactionstatuscarlog(4)));
            acLoadTypeCarLog.setDropDownHeight(0);
            acLoadCategoryCarLog.setDropDownHeight(0);
            inputLayoutHasilKerjaCarLog.setSuffixText(dbhelper.settingcarlog_satuanhasilkerja(acLoadCategoryCarLog.getText().toString()));

            btnSubmitCarlog.setText("SELESAI BEKERJA");

            if (dbhelper.get_transactionstatuscarlog(5).length() > 0) {
                acHelper1CarLog.setText(dbhelper.get_empname(dbhelper.get_transactionstatuscarlog(5)));
                acHelper1CarLog.setDropDownHeight(0);
                acHelper1CarLog.setKeyListener(null);
//                layoutHelperCarlog.setVisibility(View.VISIBLE);
                inputLayoutHelper1CarLog.setVisibility(View.VISIBLE);
            }

            if (dbhelper.get_transactionstatuscarlog(6).length() > 0) {
                acHelper2CarLog.setText(dbhelper.get_empname(dbhelper.get_transactionstatuscarlog(6)));
                acHelper2CarLog.setDropDownHeight(0);
                acHelper2CarLog.setKeyListener(null);
                inputLayoutHelper2CarLog.setVisibility(View.VISIBLE);
            }

            if (dbhelper.get_transactionstatuscarlog(7).length() > 0) {
                layoutAsalCarLog.setVisibility(View.VISIBLE);
                inputLayoutAsalKebun.setVisibility(View.VISIBLE);
                acAsalKebunCarLog.setText(dbhelper.get_singlekebun(dbhelper.get_transactionstatuscarlog(7)));
                acAsalKebunCarLog.setDropDownHeight(0);
                selectedAsalKebun = dbhelper.get_transactionstatuscarlog(7);
            }

            if (dbhelper.get_transactionstatuscarlog(8).length() > 0) {
                layoutAsalCarLog.setVisibility(View.VISIBLE);
                inputLayoutAsalDivisi.setVisibility(View.VISIBLE);
                acAsalDivisiCarLog.setText(dbhelper.get_singledivisi(dbhelper.get_transactionstatuscarlog(8)));
                acAsalDivisiCarLog.setDropDownHeight(0);
                selectedAsalDivisi = dbhelper.get_transactionstatuscarlog(8);
            }

            if (dbhelper.get_transactionstatuscarlog(9).length() > 0) {
                layoutAsalCarLog.setVisibility(View.VISIBLE);
                inputLayoutAsalLokasi.setVisibility(View.VISIBLE);
                acAsalLokasiCarLog.setText(dbhelper.get_singlelokasi(dbhelper.get_transactionstatuscarlog(9)));
                acAsalLokasiCarLog.setDropDownHeight(0);
                acAsalLokasiCarLog.setKeyListener(null);
                selectedAsalLokasi = dbhelper.get_transactionstatuscarlog(9);
            }

            if (dbhelper.get_transactionstatuscarlog(10).length() > 0) {
                layoutTujuanCarLog.setVisibility(View.VISIBLE);
                inputLayoutTujuanKebun.setVisibility(View.VISIBLE);
                acTujuanKebunCarLog.setText(dbhelper.get_singlekebun(dbhelper.get_transactionstatuscarlog(10)));
                acTujuanKebunCarLog.setDropDownHeight(0);
                selectedTujuanKebun = dbhelper.get_transactionstatuscarlog(10);
            }

            if (dbhelper.get_transactionstatuscarlog(11).length() > 0) {
                layoutTujuanCarLog.setVisibility(View.VISIBLE);
                inputLayoutTujuanDivisi.setVisibility(View.VISIBLE);
                acTujuanDivisiCarLog.setText(dbhelper.get_singledivisi(dbhelper.get_transactionstatuscarlog(11)));
                acTujuanDivisiCarLog.setDropDownHeight(0);
                selectedTujuanDivisi = dbhelper.get_transactionstatuscarlog(11);
            }

            if (dbhelper.get_transactionstatuscarlog(12).length() > 0) {
                layoutTujuanCarLog.setVisibility(View.VISIBLE);
                inputLayoutTujuanLokasi.setVisibility(View.VISIBLE);
                acTujuanLokasiCarLog.setText(dbhelper.get_singlelokasi(dbhelper.get_transactionstatuscarlog(12)));
                acTujuanLokasiCarLog.setDropDownHeight(0);
                acTujuanLokasiCarLog.setKeyListener(null);
                selectedTujuanLokasi = dbhelper.get_transactionstatuscarlog(12);
            }

            if (dbhelper.get_transactionstatuscarlog(13).length() > 0) {
                layoutTujuanCarLog.setVisibility(View.VISIBLE);
                inputLayoutTujuanKegiatan.setVisibility(View.VISIBLE);
                acTujuanKegiatanCarLog.setText(dbhelper.get_transactionstatuscarlog(13));
                acTujuanKegiatanCarLog.setDropDownHeight(0);
                acTujuanKegiatanCarLog.setKeyListener(null);
                selectedTujuanKegiatan = dbhelper.get_transactionstatuscarlog(13);
            }

            if (dbhelper.get_transactionstatuscarlog(14).length() > 0) {
                layoutHasilKerja.setVisibility(View.VISIBLE);
                inputLayoutSatuanMuat.setVisibility(View.VISIBLE);
                etHasilSatuanMuat.setText(dbhelper.get_transactionstatuscarlog(14));
            }

            if (dbhelper.get_transactionstatuscarlog(15).length() > 0) {
                layoutHasilKerja.setVisibility(View.VISIBLE);
                layoutRitaseMuat.setVisibility(View.VISIBLE);
                etHasilKerjaLaterite.setText(dbhelper.get_transactionstatuscarlog(15));
            }

            if (dbhelper.get_transactionstatuscarlog(16).length() > 0) {
                layoutHasilKerja.setVisibility(View.VISIBLE);
                inputLayoutHasilKerjaCarLog.setVisibility(View.VISIBLE);
                etHasilKerjaCarLog.setText(dbhelper.get_transactionstatuscarlog(16));
            }

            try {
                if (dbhelper.get_fotocarlog(dbhelper.getnodoc_todayprosescarlog()).length > 0) {
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_fotocarlog(dbhelper.getnodoc_todayprosescarlog()), 0, dbhelper.get_fotocarlog(dbhelper.getnodoc_todayprosescarlog()).length);
                    btnCameraCarLog.setBackground(null);
                    btnCameraCarLog.setImageBitmap(compressedBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        listMuatanCarLog = dbhelper.get_loadtype(1, dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)));
        listTypeLoadCode = dbhelper.get_loadtype(0, dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)));
        adapterMuatanCarLog = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listMuatanCarLog);
        acLoadTypeCarLog.setAdapter(adapterMuatanCarLog);

        listAsalKebunCarLog = dbhelper.get_itemkebun(1);
        listAsalKebunCode = dbhelper.get_itemkebun(0);
        adapterAsalKebunCarLog = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listAsalKebunCarLog);
        acAsalKebunCarLog.setAdapter(adapterAsalKebunCarLog);

        listTujuanKebunCarLog = dbhelper.get_itemkebun(1);
        listTujuanKebunCode = dbhelper.get_itemkebun(0);
        adapterTujuanKebunCarLog = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listTujuanKebunCarLog);
        acTujuanKebunCarLog.setAdapter(adapterTujuanKebunCarLog);

        listTujuanKegiatan = dbhelper.get_tujuankegiatancarlog(1);
        adapterKegiatanTujuan = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listTujuanKegiatan);
        acTujuanKegiatanCarLog.setAdapter(adapterKegiatanTujuan);

        listEmployee = dbhelper.get_employee();
        adapterEmployee = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listEmployee);
        acHelper1CarLog.setAdapter(adapterEmployee);
        acHelper2CarLog.setAdapter(adapterEmployee);
    }

    public void eventSubmitCarlog(View v) {

        if (TextUtils.isEmpty(acLoadCategoryCarLog.getText().toString().trim())) {
            new SweetAlertDialog(KartuKerjaVehicle.this, SweetAlertDialog.ERROR_TYPE).setContentText("Pilih Kategori Muatan").setConfirmText("OK").show();
        }
        else if (TextUtils.isEmpty(acAsalKebunCarLog.getText().toString().trim()) || TextUtils.isEmpty(acAsalDivisiCarLog.getText().toString().trim())) {
            Snackbar.make(v, "Harap isi Tujuan Kebun / Divisi", Snackbar.LENGTH_LONG).setAnchorView(btnSubmitCarlog)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> {
                        inputLayoutAsalKebun.requestFocus();
                        inputLayoutAsalDivisi.requestFocus();
                    }).show();
        }
        else if (TextUtils.isEmpty(acTujuanKebunCarLog.getText().toString().trim()) || TextUtils.isEmpty(acTujuanDivisiCarLog.getText().toString().trim())) {
            Snackbar.make(v, "Harap isi Tujuan Kebun / Divisi", Snackbar.LENGTH_LONG).setAnchorView(btnSubmitCarlog)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> {
                        inputLayoutTujuanKebun.requestFocus();
                        inputLayoutTujuanDivisi.requestFocus();
                    }).show();
        }
        else if (TextUtils.isEmpty(etHasilKerjaCarLog.getText().toString().trim())) {
            Snackbar.make(v, "Harap isi Hasil Kerja", Snackbar.LENGTH_LONG).setAnchorView(btnSubmitCarlog)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("OKAY", view -> etHasilKerjaCarLog.requestFocus()).show();
        }
        else {
            dlgSelesaiCarLog = new Dialog(KartuKerjaVehicle.this);
            dlgSelesaiCarLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlgSelesaiCarLog.setContentView(R.layout.dlg_selesaicarlog);
            dlgSelesaiCarLog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Window dlgDoneCarLogWindow = dlgSelesaiCarLog.getWindow();
            dlgDoneCarLogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Button btnCancelDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.btnCancelDlgCarLog);
            Button btnDoneDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.btnDoneDlgCarLog);
            EditText etDateDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etDateDlgCarLog);
            EditText tvJamAkhirDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.tvJamAkhirDlgCarLog);
            EditText etKMHMAwalDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etKMHMAwalDlgCarLog);
            EditText etKMHMAkhirDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etKMHMAkhirDlgCarLog);
            EditText etKMHMAkhirKomaDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etKMHMAkhirKomaDlgCarLog);
            EditText etNoteDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etNoteDlgCarLog);
            TextView tvJudulFotoKM = dlgSelesaiCarLog.findViewById(R.id.tvJudulFotoKM);
            TextInputLayout inputlayoutDlgKmAkhir = dlgSelesaiCarLog.findViewById(R.id.inputlayoutDlgKmAkhir);
            TextInputLayout inputlayoutDlgKmAkhirDecimal = dlgSelesaiCarLog.findViewById(R.id.inputlayoutDlgKmAkhirDecimal);
            btnFotoKilometer = dlgSelesaiCarLog.findViewById(R.id.imgKilometerDlgCarLog);

            String currenttdate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
            String currenttime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            etDateDlgCarLog.setText(currenttdate);
            etNoteDlgCarLog.setText(etCatatanCarLog.getText().toString());
            tvJamAkhirDlgCarLog.setText(currenttime);
            etKMHMAwalDlgCarLog.setText(dbhelper.get_tbl_username(27));
            dlgSelesaiCarLog.show();

            etKMHMAkhirDlgCarLog.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    if (etKMHMAkhirDlgCarLog.getText().toString().length() > 0) {
                        inputlayoutDlgKmAkhir.setHelperTextEnabled(true);
                        inputlayoutDlgKmAkhir.setHelperText("Max. 6 Digit");
                        inputlayoutDlgKmAkhir.setErrorEnabled(false);
                        inputlayoutDlgKmAkhir.setError(null);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // TODO Auto-generated method stub
                }
            });

            btnCancelDlgCarLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgSelesaiCarLog.dismiss();
                    fotoKilometer = null;
                    etKMHMAkhirDlgCarLog.setText(null);
                    etNoteDlgCarLog.setText(null);
                }
            });

            btnFotoKilometer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fotoKilometer = null;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(KartuKerjaVehicle.this.getPackageManager()) != null) {
                        intentLaunchCameraKM.launch(takePictureIntent);
                    }
                }
            });

            btnDoneDlgCarLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLocation();
                    if (TextUtils.isEmpty(etKMHMAkhirDlgCarLog.getText().toString().trim())) {

                        inputlayoutDlgKmAkhir.setHelperTextEnabled(false);
                        inputlayoutDlgKmAkhir.setHelperText(null);
                        inputlayoutDlgKmAkhir.setErrorEnabled(true);
                        inputlayoutDlgKmAkhir.setError("Wajib diisi!");

                    }
                    else if (fotoKilometer == null ) {
                        btnFotoKilometer.startAnimation(AnimationUtils.loadAnimation(KartuKerjaVehicle.this, R.anim.errorshake));
                        tvJudulFotoKM.startAnimation(AnimationUtils.loadAnimation(KartuKerjaVehicle.this, R.anim.errorshake));
                        Toast.makeText(KartuKerjaVehicle.this, "Foto Kilometer Akhir!", Toast.LENGTH_LONG).show();
                    }
                    else {

                        dbhelper.insert_carlog(nodocCarLog, dbhelper.get_tbl_username(27), selectedTypeLoad, selectedCategoryLoad, selectedHelper1, selectedHelper2,
                                selectedAsalKebun, selectedAsalDivisi, selectedAsalLokasi, selectedTujuanKebun, selectedTujuanDivisi, selectedTujuanLokasi,
                                selectedTujuanKegiatan, etHasilSatuanMuat.getText().toString(), etHasilKerjaLaterite.getText().toString(),
                                etHasilKerjaCarLog.getText().toString(), etCatatanCarLog.getText().toString(), latCarLog, longCarLog,
                                etKMHMAkhirDlgCarLog.getText().toString(), gambarCarLog, fotoKilometer);

                        dlgSelesaiCarLog.dismiss();
                        new SweetAlertDialog(KartuKerjaVehicle.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Pekerjaan Selesai")
                                .setConfirmClickListener(sweetAlertDialog -> onBackPressed()).setConfirmText("OK").show();


                    }

                }
            });
        }

    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(KartuKerjaVehicle.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latCarLog = String.valueOf(latitude);
        longCarLog = String.valueOf(longitude);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(3, backIntent);
        finish();
    }
}