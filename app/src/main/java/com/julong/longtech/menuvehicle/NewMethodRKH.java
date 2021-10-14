package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menuworkshop.AdapterMaterialProsesPerbaikan;
import com.julong.longtech.menuworkshop.ListMaterialProsesPerbaikan;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.julong.longtech.ui.home.HomeFragment.lvfragment;

public class NewMethodRKH extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dlgHelper;

    AutoCompleteTextView acVehicleRKH, acKondisiUnitNewRKH;
    TabLayout tabNewRKH;
    Button btnBackRKH;
    EditText etDateRKH, etBreakdownDesc, etDateUnitReady;
    TextInputLayout inputLayoutBreakdownDesc, inputLayoutDateUnitReady;
    TextView tvPlaceholderDetailRKH;
    RecyclerView lvDriver;
    ConstraintLayout layoutLokasiKegiatanUnit;
    public static RecyclerView lvLokasiKegiatanUnit;

    public static String rkhWorkdate;
    String selectedDateUnitReady, selectedVehicle, selectedVehicleGroup;
    MaterialDatePicker<Long> datePickerRKH, datePickerUnitReady;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    String[] arrayMenuKondisi = {"Normal", "Breakdown", "Standby"};
    ArrayAdapter<String> adapterMenuKondisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmethod_rkh);

        dbhelper = new DatabaseHelper(this);
        dlgHelper = new DialogHelper(this);

        acVehicleRKH = findViewById(R.id.acVehicleNewRKH);
        tabNewRKH = findViewById(R.id.tabLayoutNewRKH);
        btnBackRKH = findViewById(R.id.btnBackNewRKH);
        etDateRKH = findViewById(R.id.etWorkDateNewRKH);
        lvDriver = findViewById(R.id.lvDriverNewRKH);
        lvLokasiKegiatanUnit = findViewById(R.id.lvLokasiKegiatanUnitRKH);
        acKondisiUnitNewRKH = findViewById(R.id.acKondisiUnitNewRKH);
        etDateUnitReady = findViewById(R.id.etDateUnitReadyNewRKH);
        etBreakdownDesc = findViewById(R.id.etBreakdownDescNewRKH);
        layoutLokasiKegiatanUnit = findViewById(R.id.layoutLokasiKegiatanUniRKH);
        inputLayoutBreakdownDesc = findViewById(R.id.inputLayoutBreakdownDescNewRKH);
        inputLayoutDateUnitReady = findViewById(R.id.inputLayoutDateUnitReadyNewRKH);
        tvPlaceholderDetailRKH = findViewById(R.id.tvPlaceholderDetailRKH);

        btnBackRKH.setOnClickListener(view -> onBackPressed());

        // Setting min date
        MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
        listValidators.add(dateValidatorMin);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);
        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilderRange.build());

        //Datepicker tgl pelaksanaan
        datePickerRKH = materialDatePickerBuilder.setTitleText("Tanggal Pelaksanaan").build();

        datePickerRKH.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormatView = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                SimpleDateFormat formatSave = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                Date date = new Date(selection + offsetFromUTC);
                rkhWorkdate = formatSave.format(date);
                etDateRKH.setText(simpleFormatView.format(date));

            }
        });

        etDateRKH.setOnClickListener(view -> datePickerRKH.show(getSupportFragmentManager(), "TGLNEWRKH"));

        prepareData();

        tabNewRKH.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        layoutLokasiKegiatanUnit.setVisibility(View.VISIBLE);
                        lvDriver.setVisibility(View.GONE);
                        break;
                    case 1:
                        layoutLokasiKegiatanUnit.setVisibility(View.GONE);
                        lvDriver.setVisibility(View.VISIBLE);
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

        // Setting up datepicker estimasi unit ready
        datePickerUnitReady = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        datePickerUnitReady.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormatView = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                SimpleDateFormat formatSave = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                Date date = new Date(selection + offsetFromUTC);
                selectedDateUnitReady = formatSave.format(date);
                etDateUnitReady.setText(simpleFormatView.format(date));

            }
        });
        etDateUnitReady.setOnClickListener(v -> datePickerUnitReady.show(getSupportFragmentManager(), "RKHUNITREADY"));
        loadListViewDetailRKH(this);
    }

    private void prepareData() {

        //Setting dropdown vehicle data
        listVehicle = dbhelper.get_vehiclecodelist();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleRKH.setAdapter(adapterVehicle);

        //Setting dropdown kondisi vehicle data
        adapterMenuKondisi = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuKondisi);
        acKondisiUnitNewRKH.setAdapter(adapterMenuKondisi);

        acVehicleRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = adapterVehicle.getItem(position);
                selectedVehicleGroup = dbhelper.get_vehiclecodegroup(1, selectedVehicle);
            }
        });
        acKondisiUnitNewRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!adapterMenuKondisi.getItem(position).equals("Normal")){
                    inputLayoutBreakdownDesc.setVisibility(View.VISIBLE);
                    inputLayoutDateUnitReady.setVisibility(View.VISIBLE);
                }
                else {
                    inputLayoutBreakdownDesc.setVisibility(View.GONE);
                    inputLayoutDateUnitReady.setVisibility(View.GONE);
                }
            }
        });
    }

    public void addLocationActivityRKH(View v) {
        if (rkhWorkdate == null) {
            Snackbar.make(v, "Harap pilih tanggal", Snackbar.LENGTH_LONG).setAnchorView(btnBackRKH)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
        }
        else if (selectedVehicle == null) {
            Snackbar.make(v, "Harap pilih kendaraan", Snackbar.LENGTH_LONG).setAnchorView(btnBackRKH)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
        }
        else {
            dlgHelper.addLocationActivityRKH(selectedVehicleGroup, tvPlaceholderDetailRKH);
        }
    }

    public static void loadListViewDetailRKH(Context activityContext) {

       DatabaseHelper dbhelper;
       dbhelper = new DatabaseHelper(activityContext);

        LinearLayoutManager layoutMaterial = new LinearLayoutManager(activityContext);
        lvLokasiKegiatanUnit.setLayoutManager(layoutMaterial);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                activityContext, layoutMaterial.getOrientation());
        lvLokasiKegiatanUnit.addItemDecoration(dividerItemDecoration);

        List<ListDetailNewRKH> listViewRKH;
        AdapterDetailNewRKH adapterLvMaterial;

        listViewRKH = new ArrayList<>();
        listViewRKH.clear();
        final Cursor cursor = dbhelper.listview_activitylocation_newrkh();
        if (cursor.moveToFirst()) {
            do {
                ListDetailNewRKH paramsMaterial = new ListDetailNewRKH(
                        cursor.getString(cursor.getColumnIndex("lokasi")),
                        cursor.getString(cursor.getColumnIndex("activity")),
                        cursor.getString(cursor.getColumnIndex("targetkerja")),
                        cursor.getString(cursor.getColumnIndex("satuankerja"))
                );
                listViewRKH.add(paramsMaterial);
            } while (cursor.moveToNext());
        }
        adapterLvMaterial = new AdapterDetailNewRKH(listViewRKH, activityContext);
        lvLokasiKegiatanUnit.setAdapter(adapterLvMaterial);
    }


    @Override
    public void onBackPressed() {
        final SweetAlertDialog warningExitDlg = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        warningExitDlg.setTitleText("Batalkan RKH?");
        warningExitDlg.setCancelText("TIDAK");
        warningExitDlg.setConfirmText("YA");
        warningExitDlg.showCancelButton(true);
        warningExitDlg.setConfirmClickListener(sDialog  -> {
            sDialog.dismiss();
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            db.execSQL("DELETE FROM tr_02 WHERE datatype = 'RKHVH' AND uploaded IS NULL");
            Intent backIntent = new Intent();
            setResult(727, backIntent);
            finish();
        });
        warningExitDlg.show();


    }

}