package com.julong.longtech.menuvehicle;

import static com.julong.longtech.DatabaseHelper.url_api;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menuinventory.PengeluaranBBM;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifikasiGIS extends AppCompatActivity {

    AutoCompleteTextView acLokasiGIS, acKegiatanGIS;
    EditText etHasilVerifikasi, etHasilOperator, etDateGIS, etSesuaiSOP, etDescOpr, etLokasiOpr;

    AutoCompleteTextView acDriverGIS, acVehicleGIS;
    TextInputLayout inputLayoutHasilGIS, inputLayoutHasilOpr;
    Button btnSubmitGIS, btnBackGIS;
    ImageView imgTakePicGIS;
    LinearLayout layoutBtnVerifikasi;

    MaterialDatePicker<Long> datePickerRKH;

    private List<String> listVehicleGIS, listLokasiGIS, listDriverGIS, listKegiatanGIS;
    ArrayAdapter<String> adapterVehicleGIS, adapterDriverGIS, adapterLokasiGIS, adapterKegiatanGIS;

    byte[] byteFotoGIS;
    String selectedDriverGIS, selectedVehicleGIS;
    String nodocCarLog, selectedTeamCode, selectedDate, selectedVehicleGroup, selectedLokasiGIS,
            selectedKegiatanGIS, selectedSatuanKegiatan, latGIS, longGIS;
    String nodocVerifikasiGIS;

    DatabaseHelper dbhelper;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_gis);

        dbhelper = new DatabaseHelper(this);
        dialogHelper = new DialogHelper(this);

        // Declare attribute ID
        etDateGIS = findViewById(R.id.etDateGIS);
        btnBackGIS = findViewById(R.id.btnBackGIS);
        acDriverGIS = findViewById(R.id.acDriverGIS);
        acLokasiGIS = findViewById(R.id.acLokasiGIS);
        acVehicleGIS = findViewById(R.id.acVehicleGIS);
        btnSubmitGIS = findViewById(R.id.btnSubmitGIS);
        etSesuaiSOP = findViewById(R.id.etSesuaiSOPGIS);
        acKegiatanGIS = findViewById(R.id.acKegiatanGIS);
        imgTakePicGIS = findViewById(R.id.imgTakePictGIS);
        etDescOpr = findViewById(R.id.etOprDescVerification);
        etLokasiOpr = findViewById(R.id.etLokasiOprVerification);
        etHasilOperator = findViewById(R.id.etHasilOprVerification);
        etHasilVerifikasi = findViewById(R.id.etHasilVerificationGIS);
        inputLayoutHasilGIS = findViewById(R.id.inputLayoutHasilGIS);
        layoutBtnVerifikasi = findViewById(R.id.layoutBtnVerifikasi);
        inputLayoutHasilOpr = findViewById(R.id.inputLayoutHasilOprVerification);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            etDateGIS.setText(extras.getString("workdate"));
            acLokasiGIS.setText(extras.getString("blokcode"));
            etLokasiOpr.setText(extras.getString("blokcode"));
            acKegiatanGIS.setText(extras.getString("kegiatanunit"));
            acVehicleGIS.setText(extras.getString("unitcode"));
            etDescOpr.setText(extras.getString("descopr"));
            etHasilOperator.setText(extras.getString("resultopr"));
            acDriverGIS.setText(extras.getString("drivercode") + " - " + extras.getString("drivername"));

            nodocCarLog = extras.getString("nodoc");
            selectedDate = extras.getString("workdate");
            selectedTeamCode = extras.getString("teamcode");
            selectedDriverGIS = dbhelper.get_empcode(0, acDriverGIS.getText().toString());
            selectedVehicleGIS = acVehicleGIS.getText().toString();
            selectedKegiatanGIS = dbhelper.get_singlekegiatancode(acKegiatanGIS.getText().toString());
            selectedSatuanKegiatan = dbhelper.get_singlekegiatanname(selectedKegiatanGIS, 1);
            selectedLokasiGIS = dbhelper.get_singlelokasiCode(acLokasiGIS.getText().toString());

            etLokasiOpr.setKeyListener(null);
            etHasilOperator.setKeyListener(null);
            etDescOpr.setKeyListener(null);
            acVehicleGIS.setKeyListener(null);
            acDriverGIS.setKeyListener(null);
            acKegiatanGIS.setKeyListener(null);

            acVehicleGIS.setDropDownHeight(0);
            acDriverGIS.setDropDownHeight(0);
            acKegiatanGIS.setDropDownHeight(0);

            inputLayoutHasilOpr.setSuffixText(selectedSatuanKegiatan);
            inputLayoutHasilGIS.setSuffixText(selectedSatuanKegiatan);
            inputLayoutHasilGIS.setEnabled(true);

        } else {
            SweetAlertDialog dlgFinish = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dlgFinish.setCancelable(false);
            dlgFinish.setContentText("Data Car Log tidak ada");
            dlgFinish.setConfirmClickListener(sweetAlertDialog -> {
                Intent loginIntent = new Intent(this, MainActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            });
            dlgFinish.setConfirmText("OK").show();
        }

        prepareHeaderData();

        ActivityResultLauncher<Intent> intentLaunchCameraHasil = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byteFotoGIS = stream.toByteArray();
                    imgTakePicGIS.setImageBitmap(photoCamera);
                    imgTakePicGIS.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imgTakePicGIS.setForeground(null);
                    }
                }
            }
        );

        imgTakePicGIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byteFotoGIS = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCameraHasil.launch(takePictureIntent);
                }
            }
        });

    }

    private void prepareHeaderData() throws SQLiteException {

        nodocVerifikasiGIS = dbhelper.get_tbl_username(0) + "/GISVH/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

        listDriverGIS = dbhelper.get_operatoronly();
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

        etDateGIS.setOnClickListener(view -> {
            if (getIntent().getExtras() == null)
                datePickerRKH.show(getSupportFragmentManager(), "DATEPICKER_GIS");
        });

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
                selectedVehicleGroup = dbhelper.get_vehiclecodegroup(1, adapterVehicleGIS.getItem(position));

                listKegiatanGIS = dbhelper.get_transportactivity(selectedVehicleGroup);
                adapterKegiatanGIS = new ArrayAdapter<>(VerifikasiGIS.this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanGIS);
                acKegiatanGIS.setAdapter(adapterKegiatanGIS);

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

                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(VerifikasiGIS.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acKegiatanGIS.getWindowToken(), 0);
            }
        });

        btnBackGIS.setOnClickListener(view -> onBackPressed());

    }

    // Finish verification work
    public void eventSubmitVerifikasi(View v) {
        if (TextUtils.isEmpty(acLokasiGIS.getText().toString().trim())
            || TextUtils.isEmpty(acKegiatanGIS.getText().toString().trim())
            || TextUtils.isEmpty(etHasilVerifikasi.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi Data!").show();
        } else {
            v.setEnabled(false);
            String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            RequestQueue requestQueueGIS = Volley.newRequestQueue(this);
            String url_uploadbbm = url_api + "dataupload/uploadverifgis_new.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uploadbbm, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonPostUploadBBM = new JSONObject(response);
                        if (jsonPostUploadBBM.getString("UPLOADBBM").equals("SUCCESS")) {
                            dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, nodocCarLog, selectedDate, selectedVehicleGIS, selectedDriverGIS,
                                    selectedLokasiGIS, selectedKegiatanGIS, selectedSatuanKegiatan,
                                    etHasilVerifikasi.getText().toString(), etSesuaiSOP.getText().toString(), 1);
                            SweetAlertDialog dlgFinish = new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.SUCCESS_TYPE);
                            dlgFinish.setCancelable(false);
                            dlgFinish.setTitleText("Berhasil Simpan");
                            dlgFinish.setConfirmText("SELESAI");
                            dlgFinish.setConfirmClickListener(sweetAlertDialog -> {
                                Intent backIntent = new Intent();
                                backIntent.putExtra("workdate", selectedDate);
                                backIntent.putExtra("teamcode", selectedTeamCode);
                                setResult(727, backIntent);
                                finish();
                            });
                            dlgFinish.setConfirmText("OK").show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestQueueGIS.stop();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    requestQueueGIS.stop();
                    dbhelper.insert_verifikasigis_header(nodocVerifikasiGIS, nodocCarLog, selectedDate, selectedVehicleGIS, selectedDriverGIS,
                            selectedLokasiGIS, selectedKegiatanGIS, selectedSatuanKegiatan,
                            etHasilVerifikasi.getText().toString(), etSesuaiSOP.getText().toString(), 0);
                    SweetAlertDialog dlgFinish = new SweetAlertDialog(VerifikasiGIS.this, SweetAlertDialog.SUCCESS_TYPE);
                    dlgFinish.setCancelable(false);
                    dlgFinish.setConfirmText("SELESAI");
                    dlgFinish.setConfirmClickListener(sweetAlertDialog -> {
                        Intent backIntent = new Intent();
                        backIntent.putExtra("workdate", selectedDate);
                        backIntent.putExtra("teamcode", selectedTeamCode);
                        setResult(727, backIntent);
                        finish();
                    });
                    dlgFinish.setConfirmText("OK").show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nodoc", nodocVerifikasiGIS);
                    params.put("datatype", "GISVH");
                    params.put("subdatatype", dbhelper.get_tbl_username(0));
                    params.put("compid", dbhelper.get_tbl_username(14));
                    params.put("siteid", dbhelper.get_tbl_username(15));
                    params.put("date1", savedate);
                    params.put("date2", savedate);
                    params.put("date3", selectedDate);
                    params.put("text1", selectedVehicleGIS);
                    params.put("text2", selectedDriverGIS);
                    params.put("text3", selectedLokasiGIS);
                    params.put("text4", selectedKegiatanGIS);
                    params.put("text5", selectedSatuanKegiatan);
                    params.put("text6", etHasilVerifikasi.getText().toString());
                    params.put("text7", etSesuaiSOP.getText().toString());
                    params.put("text8", nodocCarLog);
                    params.put("userid", dbhelper.get_tbl_username(0));
                    return params;
                }
            };
            requestQueueGIS.add(stringRequest);

        }
    }

    @Override
    public void onBackPressed() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("DELETE FROM tr_01 WHERE datatype = 'GISVH' AND uploaded IS NULL");
        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'GISVH' AND uploaded IS NULL");
        finish();
    }
}