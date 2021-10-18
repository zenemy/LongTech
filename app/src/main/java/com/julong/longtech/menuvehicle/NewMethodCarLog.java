package com.julong.longtech.menuvehicle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.DialogHelper.btnFotoKilometer;

public class NewMethodCarLog extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dlgHelper;

    // For XML design
    Button btnBack;
    TextView tvInfoVehicle;
    TabLayout tabNewCarLog;
    ImageView imgWorkResult;
    RecyclerView lvWorkResult;
    ScrollView scrollWorkInput;
    EditText etInputWorkResult;
    LinearLayout layoutWorkType;
    RelativeLayout layoutListWorkResult;
    TextInputLayout inputLayoutWorkResult;
    FloatingActionButton btnSwitchWorkInput;
    AutoCompleteTextView acWorkCategory, acWorkActivity,
            acEstate, acDivision, acWorkLocation;

    String nodocCarLog, selectedCategory, selectedActivity,
            selectedEstate, selectedDivision, selectedBlok, latitudeWork, longitudeWork;
    byte[] byteImgWorkResult;
    public static byte[] byteFotoKilometer;

    ActivityResultLauncher<Intent> intentLaunchCameraHasil, intentLaunchCameraKM;

    List<String> listCategoryCode, listActivityCode, listEstateCode, listDivisionCode;
    List<String> listCategoryName, listActivityName, listEstateName, listDivisionName, listBlokName;
    ArrayAdapter<String> adapterCategory, adapterActivity, adapterEstate, adapterDivision, adapterBlok;

    // List View hasil kerja
    List<ListResultCarLog> listCarLogs;
    AdapterResultCarLog adapterCarLogs;

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
                        loadListViewCarLogs();
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

        loadListViewCarLogs();

    }

    private void prepareDropdownData() {

        if (dbhelper.get_tbl_username(27) != null) {
            tvInfoVehicle.setText(dbhelper.get_tbl_username(19) + " " +  dbhelper.get_tbl_username(20) + " [" + dbhelper.get_tbl_username(27) + "]");
        }
        else {
            tvInfoVehicle.setText(dbhelper.get_tbl_username(19));
        }

        acWorkActivity.setKeyListener(null);
        acWorkCategory.setKeyListener(null);
        acEstate.setKeyListener(null);
        acDivision.setKeyListener(null);

        listCategoryName = dbhelper.get_loadtype(1, dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)));
        listCategoryCode = dbhelper.get_loadtype(0, dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)));
        adapterCategory = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listCategoryName);
        acWorkCategory.setAdapter(adapterCategory);

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

                listActivityName = dbhelper.get_loadcategory(dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)), selectedCategory, 1);
                listActivityCode = dbhelper.get_loadcategory(dbhelper.get_vehiclecodegroup(1, dbhelper.get_tbl_username(19)), selectedCategory, 0);
                adapterActivity = new ArrayAdapter<>(NewMethodCarLog.this, R.layout.spinnerlist, R.id.spinnerItem, listActivityName);
                acWorkActivity.setAdapter(adapterActivity);
            }
        });

        acWorkActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedActivity = listActivityCode.get(position);
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

        ArrayList<String> blokOutput = (ArrayList<String>) listBlokName;

        if (blokOutput.indexOf(acWorkLocation.getText().toString()) == -1) {
            selectedBlok = null;
        }

        if (selectedCategory == null) {
            SweetAlertDialog dlgError = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dlgError.setContentText("Pilih tipe pekerjaan").setConfirmText("OK").show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> dlgError.dismiss(), 3000);
        } else if (selectedDivision == null) {
            SweetAlertDialog dlgError = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dlgError.setContentText("Tentukan lokasi kerja").setConfirmText("OK").show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> dlgError.dismiss(), 3000);
        } else {
            getLocation();
            dbhelper.insert_new_carlog(selectedCategory, selectedActivity, selectedEstate, selectedDivision,
                    selectedBlok, etInputWorkResult.getText().toString(), latitudeWork, longitudeWork, byteImgWorkResult);

            selectedActivity = null;
            selectedEstate = null;
            selectedDivision = null;
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
        if (lvWorkResult.getAdapter().getItemCount() > 0) {
            dlgHelper.submitSelesaiCarLog(intentLaunchCameraKM);
        } else {
            Toast.makeText(this, "Belum ada hasil kerja terinput", Toast.LENGTH_LONG).show();
        }
    }

    private void loadListViewCarLogs() {

        LinearLayoutManager layoutCarLogResult = new LinearLayoutManager(this);
        lvWorkResult.setLayoutManager(layoutCarLogResult);

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
                        cursor.getString(cursor.getColumnIndex("timetr"))
                );
                listCarLogs.add(paramsCarLogs);
            } while (cursor.moveToNext());
        }
        adapterCarLogs = new AdapterResultCarLog(listCarLogs, this);
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
}