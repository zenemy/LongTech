package com.julong.longtech.menuvehicle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewMethodCarLog extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dlgHelper;

    // For XML design
    Button btnBack;
    TextView tvInfoVehicle;
    TabLayout tabNewCarLog;
    ImageView imgWorkResult;

    ScrollView scrollWorkInput;
    LinearLayout layoutWorkType;
    EditText etInputWorkResult, etWorkDate;
    RelativeLayout layoutListWorkResult;
    TextInputLayout inputLayoutWorkResult;
    FloatingActionButton btnSwitchWorkInput;
    AutoCompleteTextView acWorkCategory, acWorkActivity,
            acEstate, acDivision, acWorkLocation;

    // Dialog KM / HM
    ImageView btnFotoKilometer;

    String selectedCategory, selectedActivity,
            selectedEstate, selectedDivision, selectedBlok, latitudeWork, longitudeWork;
    byte[] byteImgWorkResult, byteFotoKilometer;

    MaterialDatePicker<Long> datePickerVKP;
    ActivityResultLauncher<Intent> intentLaunchCameraHasil, intentLaunchCameraKM;

    List<String> listCategoryCode, listEstateCode, listDivisionCode;
    List<String> listCategoryName, listActivityName, listEstateName, listDivisionName, listBlokName;
    ArrayAdapter<String> adapterCategory, adapterActivity, adapterEstate, adapterDivision, adapterBlok;

    // List View hasil kerja
    public static RecyclerView lvWorkResult;
    public static List<ListResultCarLog> listCarLogs;
    public static AdapterResultCarLog adapterCarLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmethod_carlog);

        dbhelper = new DatabaseHelper(this);
        dlgHelper = new DialogHelper(this);

        // Declare design ID
        btnBack = findViewById(R.id.btnBackCarLog);
        acEstate = findViewById(R.id.acEstateCarLog);
        tabNewCarLog = findViewById(R.id.tabNewCarLog);
        acDivision = findViewById(R.id.acDivisionCarLog);
        etWorkDate = findViewById(R.id.etWorkDateNewCarLog);
        lvWorkResult = findViewById(R.id.lvWorkResultCarLog);
        acWorkLocation = findViewById(R.id.acLocationCarLog);
        tvInfoVehicle = findViewById(R.id.tvInfoUnitNewCarLog);
        imgWorkResult = findViewById(R.id.imgWorkResultCarLog);
        acWorkActivity = findViewById(R.id.acActivityNewCarLog);
        layoutWorkType = findViewById(R.id.layoutWorkTypeCarlog);
        etInputWorkResult = findViewById(R.id.etWorkResultCarLog);
        scrollWorkInput = findViewById(R.id.scrollWorkInputCarLog);
        btnSwitchWorkInput = findViewById(R.id.btnSwitchWorkInput);
        acWorkCategory = findViewById(R.id.acWorkCategoryNewCarLog);
        layoutListWorkResult = findViewById(R.id.layoutListWorkResultCarLog);
        inputLayoutWorkResult = findViewById(R.id.inputLayoutWorkResultCarLog);

        btnBack.setOnClickListener(view -> onBackPressed());

        tabNewCarLog.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        layoutWorkType.setVisibility(View.VISIBLE);
                        scrollWorkInput.setVisibility(View.VISIBLE);
                        layoutListWorkResult.setVisibility(View.GONE);
                        break;
                    case 1:
                        layoutWorkType.setVisibility(View.GONE);
                        scrollWorkInput.setVisibility(View.GONE);
                        layoutListWorkResult.setVisibility(View.VISIBLE);
                        loadListViewCarLogs(NewMethodCarLog.this);
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
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        etWorkDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        // Setting up min max date datepicker
        MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2));
        CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(System.currentTimeMillis());
        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
        listValidators.add(dateValidatorMin);
        listValidators.add(dateValidatorMax);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);
        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilderRange.build());

        // Datepicker tgl pelaksanaan
        datePickerVKP = materialDatePickerBuilder.setTitleText("Tanggal Bekerja").build();

        // Setting OK button on the datepicker
        datePickerVKP.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                etWorkDate.setText(simpleFormat.format(date));
            }
        });

        // Show datepicker
        etWorkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!datePickerVKP.isAdded())
                    datePickerVKP.show(getSupportFragmentManager(), "TGLCARLOG");
            }
        });

        prepareDropdownData();

        intentLaunchCameraHasil = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byteImgWorkResult = stream.toByteArray();
                    imgWorkResult.setImageBitmap(photoCamera);
                    imgWorkResult.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        );

        imgWorkResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCameraHasil.launch(takePictureIntent);
                }
            }
        });

        // Switch to tab 1
        btnSwitchWorkInput.setOnClickListener(view -> tabNewCarLog.getTabAt(0).select());

        intentLaunchCameraKM = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    byteFotoKilometer = null;
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byteFotoKilometer = stream.toByteArray();
                    btnFotoKilometer.setImageBitmap(photoCamera);
                    btnFotoKilometer.setBackground(null);
                }
            }
        );

        loadListViewCarLogs(this);

    }

    private void prepareDropdownData() {
        if (dbhelper.get_tbl_username(27) != null) {
            tvInfoVehicle.setText(dbhelper.get_tbl_username(19) + " " +  dbhelper.get_tbl_username(20) + " [" + dbhelper.get_tbl_username(27) + "]");
        }
        else {
            tvInfoVehicle.setText(dbhelper.get_tbl_username(19));
        }

        acWorkCategory.setKeyListener(null);
        acEstate.setKeyListener(null);
        acDivision.setKeyListener(null);

        // Populate activity category
        listCategoryName = dbhelper.get_loadtype(1, dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)));
        listCategoryCode = dbhelper.get_loadtype(0, dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)));
        adapterCategory = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listCategoryName);
        acWorkCategory.setAdapter(adapterCategory);

        // Populate estate data
        listEstateName = dbhelper.get_itemkebun(1);
        listEstateCode = dbhelper.get_itemkebun(0);
        adapterEstate = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listEstateName);
        acEstate.setAdapter(adapterEstate);

        acWorkCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                acWorkActivity.setText(null);
                selectedActivity = null;
                selectedCategory = listCategoryCode.get(position);
                inputLayoutWorkResult.setSuffixText(null);

                // When category selected, populate detailed activity data
                listActivityName = dbhelper.get_loadcategory(dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)), selectedCategory, 1);
                adapterActivity = new ArrayAdapter<>(NewMethodCarLog.this, R.layout.spinnerlist, R.id.spinnerItem, listActivityName);
                acWorkActivity.setAdapter(adapterActivity);
            }
        });

        acWorkActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedActivity = dbhelper.get_selected_activity(dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)), selectedCategory, adapterActivity.getItem(position));
                inputLayoutWorkResult.setSuffixText(dbhelper.get_singlekegiatanname(selectedActivity, 1));
            }
        });

        acEstate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedEstate = listEstateCode.get(position);
                selectedDivision = null;
                selectedBlok = null;
                acDivision.setText(null);
                acWorkLocation.setText(null);

                // When estate selected, populate division data
                listDivisionName = dbhelper.get_itemdivisi(selectedEstate, 1);
                listDivisionCode = dbhelper.get_itemdivisi(selectedEstate, 0);
                adapterDivision = new ArrayAdapter<>(NewMethodCarLog.this, R.layout.spinnerlist, R.id.spinnerItem, listDivisionName);
                acDivision.setAdapter(adapterDivision);
            }
        });

        acDivision.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDivision = listDivisionCode.get(position);
                selectedBlok = null;
                acWorkLocation.setText(null);

                // When division selected, populate blok data
                listBlokName = dbhelper.get_fieldcrop_filtered(selectedEstate, selectedDivision, 1);
                adapterBlok = new ArrayAdapter<>(NewMethodCarLog.this, R.layout.spinnerlist, R.id.spinnerItem, listBlokName);
                acWorkLocation.setAdapter(adapterBlok);
            }
        });

        acWorkLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedBlok = dbhelper.get_singlelokasiCode(adapterBlok.getItem(position));

                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(KartuKerjaVehicle.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acWorkLocation.getWindowToken(), 0);
            }
        });

    }

    public void addNewCarLog(View v) {
        if (selectedActivity == null) {
            SweetAlertDialog dlgError = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dlgError.setContentText("Pilih aktifitas pekerjaan").setConfirmText("OK").show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> dlgError.dismiss(), 3000);
        } else if (selectedDivision == null || selectedBlok == null) {
            SweetAlertDialog dlgError = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dlgError.setContentText("Tentukan lokasi kerja").setConfirmText("OK").show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> dlgError.dismiss(), 3000);
        }
        else if (TextUtils.isEmpty(etInputWorkResult.getText().toString().trim())) {
            SweetAlertDialog dlgError = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dlgError.setContentText("Isi hasil kerja").setConfirmText("OK").show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> dlgError.dismiss(), 3000);
        }
        else {
            getLocation();

            dbhelper.insert_new_carlog(etWorkDate.getText().toString(), selectedCategory, selectedActivity, selectedEstate,
                    selectedDivision, selectedBlok, etInputWorkResult.getText().toString(), latitudeWork, longitudeWork, byteImgWorkResult);

            selectedActivity = null;
            selectedEstate = null;
            selectedDivision = null;
            selectedBlok = null;
            selectedBlok = null;

            acWorkActivity.setText(null);
            acEstate.setText(null);
            acDivision.setText(null);
            acWorkLocation.setText(null);
            etInputWorkResult.setText(null);
            inputLayoutWorkResult.setSuffixText(null);

        }
    }

    public void submitSelesaiCarLog(View v) {
        submitSelesaiCarLog().show();
    }

    public Dialog submitSelesaiCarLog() {
        //Showing finishDlg
        Dialog dlgSelesaiCarLog = new Dialog(this);
        dlgSelesaiCarLog.setContentView(R.layout.dlg_selesaicarlog);
        dlgSelesaiCarLog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window dlgDoneCarLogWindow = dlgSelesaiCarLog.getWindow();
        dlgDoneCarLogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Declare design ID
        Button btnCancelDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.btnCancelDlgCarLog);
        Button btnDoneDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.btnDoneDlgCarLog);
        EditText etDateDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etDateDlgCarLog);
        EditText tvJamAkhirDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.tvJamAkhirDlgCarLog);
        EditText etKMHMAwalDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etKMHMAwalDlgCarLog);
        EditText etKMHMAkhirDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etKMHMAkhirDlgCarLog);
        EditText etNoteDlgCarLog = dlgSelesaiCarLog.findViewById(R.id.etNoteDlgCarLog);
        TextView tvJudulFotoKM = dlgSelesaiCarLog.findViewById(R.id.tvJudulFotoKM);
        TextInputLayout inputlayoutDlgKmAkhir = dlgSelesaiCarLog.findViewById(R.id.inputlayoutDlgKmAkhir);
        btnFotoKilometer = dlgSelesaiCarLog.findViewById(R.id.imgKilometerDlgCarLog);

        String currenttdate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        String currenttime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        etDateDlgCarLog.setText(currenttdate);
        tvJamAkhirDlgCarLog.setText(currenttime);

        if (dbhelper.get_tbl_username(27) != null) {
            etKMHMAwalDlgCarLog.setText(dbhelper.get_tbl_username(27));
        }

        btnCancelDlgCarLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgSelesaiCarLog.dismiss();
                byteFotoKilometer = null;
                etKMHMAkhirDlgCarLog.setText(null);
                etNoteDlgCarLog.setText(null);
            }
        });

        btnFotoKilometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCameraKM.launch(takePictureIntent);
                }
            }
        });

        btnDoneDlgCarLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentValue = etKMHMAwalDlgCarLog.getText().toString().replace(",",".");

                if (TextUtils.isEmpty(etKMHMAwalDlgCarLog.getText().toString().trim()) || TextUtils.isEmpty(etKMHMAkhirDlgCarLog.getText().toString().trim())) {

                    inputlayoutDlgKmAkhir.setHelperTextEnabled(false);
                    inputlayoutDlgKmAkhir.setHelperText(null);
                    inputlayoutDlgKmAkhir.setErrorEnabled(true);
                    inputlayoutDlgKmAkhir.setError("Wajib diisi!");
                    Toast.makeText(NewMethodCarLog.this, "Isi info kilometer!", Toast.LENGTH_LONG).show();

                }
                else if (byteFotoKilometer == null) {
                    btnFotoKilometer.startAnimation(AnimationUtils.loadAnimation(NewMethodCarLog.this, R.anim.errorshake));
                    tvJudulFotoKM.startAnimation(AnimationUtils.loadAnimation(NewMethodCarLog.this, R.anim.errorshake));
                    Toast.makeText(NewMethodCarLog.this, "Foto Kilometer Akhir!", Toast.LENGTH_LONG).show();
                }
                else if (Float.parseFloat(currentValue) > Float.parseFloat(etKMHMAkhirDlgCarLog.getText().toString())) {
                    Toast.makeText(NewMethodCarLog.this, "Kilometer akhir salah", Toast.LENGTH_LONG).show();
                }
                else {
                    //Replace dot with comma
                    String newStartValue = etKMHMAwalDlgCarLog.getText().toString().replace(".",",");
                    etKMHMAwalDlgCarLog.setText(newStartValue);
                    String newEndValue = etKMHMAkhirDlgCarLog.getText().toString().replace(".",",");
                    etKMHMAkhirDlgCarLog.setText(newEndValue);

                    dbhelper.update_kmhm(etKMHMAkhirDlgCarLog.getText().toString());
                    dbhelper.submitNewCarLog(etKMHMAwalDlgCarLog.getText().toString(), etKMHMAkhirDlgCarLog.getText().toString(),
                            etNoteDlgCarLog.getText().toString(), byteFotoKilometer);

                    dlgSelesaiCarLog.dismiss();

                    SweetAlertDialog okCarLogDlg = new SweetAlertDialog(NewMethodCarLog.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Pekerjaan Selesai");
                    okCarLogDlg.setCancelable(false);
                    okCarLogDlg.setCanceledOnTouchOutside(false);
                    okCarLogDlg.setConfirmClickListener(sweetAlertDialog -> {
                        byteFotoKilometer = null;
                        Intent backIntent = new Intent();
                        backIntent.putExtra("workdate", etWorkDate.getText().toString());
                        setResult(3, backIntent);
                        finish();
                    }).setConfirmText("OK").show();
                }
            }
        });

        return dlgSelesaiCarLog;
    }

    public static void loadListViewCarLogs(Context activityContext) {
        LinearLayoutManager layoutCarLogResult = new LinearLayoutManager(activityContext);
        lvWorkResult.setLayoutManager(layoutCarLogResult);

        DatabaseHelper dbhelper = new DatabaseHelper(activityContext);

        listCarLogs = new ArrayList<>();
        listCarLogs.clear();
        final Cursor cursor = dbhelper.listview_new_carlogs();
        if (cursor.moveToFirst()) {
            do {
                ListResultCarLog paramsCarLogs = new ListResultCarLog(
                        cursor.getString(cursor.getColumnIndex("activity")),
                        cursor.getString(cursor.getColumnIndex("divisi")),
                        cursor.getString(cursor.getColumnIndex("blok")),
                        cursor.getString(cursor.getColumnIndex("hasilkerja")),
                        cursor.getString(cursor.getColumnIndex("satuankerja")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("datetr")),
                        cursor.getString(cursor.getColumnIndex("timetr"))
                );
                listCarLogs.add(paramsCarLogs);
            } while (cursor.moveToNext());
        }
        adapterCarLogs = new AdapterResultCarLog(listCarLogs, activityContext);
        lvWorkResult.setAdapter(adapterCarLogs);
    }

    public void getLocation() { // Get user current coordinate
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latitudeWork = String.valueOf(latitude);
            longitudeWork = String.valueOf(longitude);
        } else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("workdate", etWorkDate.getText().toString());
        setResult(3, backIntent);
        finish();
    }
}