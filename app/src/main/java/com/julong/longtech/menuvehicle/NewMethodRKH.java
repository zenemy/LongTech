package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class NewMethodRKH extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dlgHelper;

    AutoCompleteTextView acVehicleRKH, acKondisiUnitNewRKH;
    TabLayout tabNewRKH;
    Button btnBackRKH;
    EditText etDateRKH, etBreakdownDesc, etDateUnitReady;
    TextInputLayout inputLayoutBreakdownDesc, inputLayoutDateUnitReady;

    public static String rkhWorkdate;
    String selectedDateUnitReady;
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
        acKondisiUnitNewRKH = findViewById(R.id.acKondisiUnitNewRKH);
        etDateUnitReady = findViewById(R.id.etDateUnitReadyNewRKH);
        etBreakdownDesc = findViewById(R.id.etBreakdownDescNewRKH);
        inputLayoutBreakdownDesc = findViewById(R.id.inputLayoutBreakdownDescNewRKH);
        inputLayoutDateUnitReady = findViewById(R.id.inputLayoutDateUnitReadyNewRKH);

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

        listVehicle = dbhelper.get_vehiclecodelist();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleRKH.setAdapter(adapterVehicle);

        adapterMenuKondisi = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuKondisi);
        acKondisiUnitNewRKH.setAdapter(adapterMenuKondisi);

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
    }

    public void addLocationActivityRKH(View v) {
        dlgHelper.addLocationActivityRKH("HE");
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }

}