package com.julong.longtech.menuhistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    ListView lvHistoryCarLog;
    RecyclerView lvHistoryRKH, lvHistoryApel;
    AutoCompleteTextView acMenuRiwayat;
    EditText etDatepicker;

    DatabaseHelper dbhelper;

    private List<ListHistoryRKH> listHistoriesRKH;
    HistoryAdapterRKH adapterLvRKH;

    private List<ListHistoryApel> listApelHistories;
    HistoryApelAdapter adapterLvHistory;

    private List<ListHistoryCarLog> listHistoryCarLogs;
    HistoryCarlogAdapter carlogAdapter;

    String[] arrayMenuHistory = {"APEL PAGI", "CAR LOG", "RKH"};
    ArrayAdapter<String> adapterMenuHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbhelper = new DatabaseHelper(this);

        lvHistoryRKH = findViewById(R.id.lvHistoryRKH);
        etDatepicker = findViewById(R.id.etDateRiwayat);
        lvHistoryApel = findViewById(R.id.lvHistoryApel);
        acMenuRiwayat = findViewById(R.id.acMenuRiwayat);
        lvHistoryCarLog = findViewById(R.id.lvHistoryCarLog);

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

                if (acMenuRiwayat.getText().toString().equals("APEL PAGI")) {
                    lvHistoryApel.setVisibility(View.VISIBLE);
                    lvHistoryCarLog.setVisibility(View.GONE);
                    lvHistoryRKH.setVisibility(View.GONE);
                    loadListViewHistoryApel(simpleFormat.format(date));
                }
                else if (acMenuRiwayat.getText().toString().equals("CAR LOG")) {
                    lvHistoryApel.setVisibility(View.GONE);
                    lvHistoryCarLog.setVisibility(View.VISIBLE);
                    lvHistoryRKH.setVisibility(View.GONE);
                    loadListViewHistoryCarLog(simpleFormat.format(date));
                }
                else {
                    lvHistoryApel.setVisibility(View.GONE);
                    lvHistoryCarLog.setVisibility(View.GONE);
                    lvHistoryRKH.setVisibility(View.VISIBLE);
                    loadListViewHistoryRKH(simpleFormat.format(date));
                }
            }
        });

        acMenuRiwayat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                lvHistoryApel.setVisibility(View.GONE);
                lvHistoryCarLog.setVisibility(View.GONE);
                etDatepicker.setText(null);
            }
        });

    }

    private void loadListViewHistoryCarLog(String selectedDate) {
        listHistoryCarLogs = new ArrayList<>();
        listHistoryCarLogs.clear();
        final Cursor cursor = dbhelper.listview_historycarlog(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ListHistoryCarLog paramsCarLogHistory = new ListHistoryCarLog(
                        cursor.getString(cursor.getColumnIndex("documentno")),
                        cursor.getString(cursor.getColumnIndex("tglawal")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("kmawal")),
                        cursor.getString(cursor.getColumnIndex("kmakhir")),
                        cursor.getString(cursor.getColumnIndex("kategorimuatan")),
                        cursor.getString(cursor.getColumnIndex("jenismuatan")),
                        cursor.getString(cursor.getColumnIndex("hasilkerja")),
                        cursor.getString(cursor.getColumnIndex("satuankerja")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listHistoryCarLogs.add(paramsCarLogHistory);
            } while (cursor.moveToNext());
        }
        carlogAdapter = new HistoryCarlogAdapter(this, R.layout.listview_historycarlog, listHistoryCarLogs);
        lvHistoryCarLog.setAdapter(carlogAdapter);
    }

    private void loadListViewHistoryApel(String selectedDate) {

        LinearLayoutManager layoutApel = new LinearLayoutManager(this);
        lvHistoryApel.setLayoutManager(layoutApel);

        listApelHistories = new ArrayList<>();
        listApelHistories.clear();
        final Cursor cursor = dbhelper.listview_historyapel(selectedDate);
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
                        cursor.getBlob(cursor.getColumnIndex("fotoabsen")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listApelHistories.add(paramsApelHistory);
            } while (cursor.moveToNext());
        }
        adapterLvHistory = new HistoryApelAdapter(listApelHistories, this);
        lvHistoryApel.setAdapter(adapterLvHistory);
    }

    private void loadListViewHistoryRKH(String selectedDate) {

        LinearLayoutManager layoutRKH = new LinearLayoutManager(this);
        lvHistoryRKH.setLayoutManager(layoutRKH);

        listHistoriesRKH = new ArrayList<>();
        listHistoriesRKH.clear();
        final Cursor cursor = dbhelper.listview_historyRKH(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ListHistoryRKH paramsHistoryRKH = new ListHistoryRKH(
                        cursor.getString(cursor.getColumnIndex("documentno")),
                        cursor.getString(cursor.getColumnIndex("tglinput")),
                        cursor.getString(cursor.getColumnIndex("tglpelaksannaan")),
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("activity")),
                        cursor.getString(cursor.getColumnIndex("blok")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("shiftcode")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listHistoriesRKH.add(paramsHistoryRKH);
            } while (cursor.moveToNext());
        }
        adapterLvRKH = new HistoryAdapterRKH(listHistoriesRKH, this);
        lvHistoryRKH.setAdapter(adapterLvRKH);
    }
}