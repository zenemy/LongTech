package com.julong.longtech.menuworkshop;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.julong.longtech.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class PerintahPerbaikan extends AppCompatActivity {

    EditText etTimePerintahPerbaikan, etDatePerintahPerbaikan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perintah_perbaikan);

        etTimePerintahPerbaikan = findViewById(R.id.etTimePerintahPerbaikan);
        etDatePerintahPerbaikan = findViewById(R.id.etDatePerintahPerbaikan);

        etDatePerintahPerbaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Pilih Tanggal Perbaikan");
                MaterialDatePicker<Long> picker = builder.build();
                picker.show(getSupportFragmentManager(), picker.toString());

                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("GMT+07:00"));
                        utc.setTimeInMillis(selection);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String formatted = format.format(utc.getTime());
                        etDatePerintahPerbaikan.setText(formatted);
                    }
                });
            }
        });

        etTimePerintahPerbaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(10)
                        .setTitleText("Pilih Jam Perbaikan")
                        .build();
            }
        });

    }
}