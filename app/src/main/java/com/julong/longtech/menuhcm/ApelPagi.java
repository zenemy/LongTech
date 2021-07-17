package com.julong.longtech.menuhcm;

import androidx.appcompat.app.AppCompatActivity;
import com.julong.longtech.R;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApelPagi extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    EditText etWaktuApelPagi;
    ImageButton imgFotoApelPagi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apelpagi);

        String savedate = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        etWaktuApelPagi = findViewById(R.id.etWaktuApelPagi);
        imgFotoApelPagi = findViewById(R.id.imgCaptureApelPagi);

        etWaktuApelPagi.setText(savedate);

        imgFotoApelPagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });
    }
}