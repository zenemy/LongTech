package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KartuKerjaVehicle extends AppCompatActivity {

    String savedate;

    TextView tvTanggalCarLog;
    AutoCompleteTextView acLoadTypeCarLog, acHelper1CarLog, acHelper2CarLog, acLoadCategoryCarLog;

    private List<String> listMuatanCarLog, listEmployee, listCategoryMuatan;
    ArrayAdapter<String> adapterMuatanCarLog, adapterEmployee, adapterCategoryMuatan;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartukerja_vehicle);

        dbhelper = new DatabaseHelper(this);

        tvTanggalCarLog = findViewById(R.id.tvTanggalCarLog);
        acLoadTypeCarLog = findViewById(R.id.acLoadTypeCarLog);
        acHelper1CarLog = findViewById(R.id.acHelper1CarLog);
        acHelper2CarLog = findViewById(R.id.acHelper2CarLog);
        acLoadCategoryCarLog = findViewById(R.id.acLoadCategoryCarLog);

        listMuatanCarLog = dbhelper.get_loadtype();
        adapterMuatanCarLog = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listMuatanCarLog);
        acLoadTypeCarLog.setAdapter(adapterMuatanCarLog);

        listEmployee = dbhelper.get_employee();
        adapterEmployee = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listEmployee);
        acHelper1CarLog.setAdapter(adapterEmployee);
        acHelper2CarLog.setAdapter(adapterEmployee);

        acLoadTypeCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acLoadCategoryCarLog.setText("");
                listCategoryMuatan = dbhelper.get_loadcategory(adapterMuatanCarLog.getItem(position));
                adapterCategoryMuatan = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listCategoryMuatan);
                acLoadCategoryCarLog.setAdapter(adapterCategoryMuatan);
            }
        });

        todayDate();
    }

    void todayDate() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                savedate = "Minggu, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.MONDAY:
                savedate = "Senin, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.TUESDAY:
                savedate = "Selasa, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.WEDNESDAY:
                savedate = "Rabu, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.THURSDAY:
                savedate = "Kamis, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.FRIDAY:
                savedate = "Jumat, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
            case Calendar.SATURDAY:
                savedate = "Sabtu, " + new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                break;
        }

        tvTanggalCarLog.setText(savedate);

    }
}