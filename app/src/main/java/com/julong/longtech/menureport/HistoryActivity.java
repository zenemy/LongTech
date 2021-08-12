package com.julong.longtech.menureport;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryActivity extends AppCompatActivity {

    ListView lvHistoryApel, lvHistoryCarLog;
    AutoCompleteTextView acMenuRiwayat;
    EditText etDatepicker;

    DatabaseHelper dbhelper;

    private List<ListHistoryApel> listApelHistories;
    HistoryApelAdapter adapterLvHistory;

    String[] arrayMenuHistory = {"APEL PAGI", "CAR LOG"};
    ArrayAdapter<String> adapterMenuHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbhelper = new DatabaseHelper(this);

        lvHistoryApel = findViewById(R.id.lvHistoryApel);
        lvHistoryCarLog = findViewById(R.id.lvHistoryCarLog);
        acMenuRiwayat = findViewById(R.id.acMenuRiwayat);
        etDatepicker = findViewById(R.id.etDateRiwayat);
        lvHistoryApel.setDivider(null);
        lvHistoryCarLog.setDivider(null);

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etDatepicker.setText(todayDate);

        adapterMenuHistory = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayMenuHistory);
        acMenuRiwayat.setAdapter(adapterMenuHistory);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        etDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                etDatepicker.setText(simpleFormat.format(date));

                loadListViewReportApel();
            }
        });

        acMenuRiwayat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lvHistoryApel.setVisibility(View.GONE);
                lvHistoryCarLog.setVisibility(View.GONE);
            }
        });

        loadListViewReportApel();
    }

    private void loadListViewReportApel() {
        lvHistoryApel = findViewById(R.id.lvHistoryApel);
        listApelHistories = new ArrayList<>();
        listApelHistories.clear();
        final Cursor cursor = dbhelper.listview_reportapel(etDatepicker.getText().toString());
        if (cursor.moveToFirst()) {
            do {
                ListHistoryApel paramsApelHistory = new ListHistoryApel(
                        cursor.getString(cursor.getColumnIndex("documentno")),
                        cursor.getString(cursor.getColumnIndex("tglapel")),
                        cursor.getString(cursor.getColumnIndex("waktuapel")),
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("jabatan")),
                        cursor.getString(cursor.getColumnIndex("jeniskehadiran")),
                        cursor.getString(cursor.getColumnIndex("absenmethod")),
                        cursor.getBlob(cursor.getColumnIndex("fotoabsen"))
                );
                listApelHistories.add(paramsApelHistory);
            } while (cursor.moveToNext());
        }
        adapterLvHistory = new HistoryApelAdapter(this, R.layout.listview_historyapelpagi, listApelHistories);
        lvHistoryApel.setAdapter(adapterLvHistory);
    }
}