package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;
import com.julong.longtech.R;
import android.os.Bundle;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KartuKerjaVehicle extends AppCompatActivity {

    String savedate;

    EditText etTodayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartukerja_vehicle);



        savedate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());


    }
}