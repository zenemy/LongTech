package com.julong.longtech.menuhcm;

import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class BiodataKaryawan extends AppCompatActivity {

    ImageView imgFotoBiodataKaryawan;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodatakaryawan);

        dbhelper = new DatabaseHelper(this);

        imgFotoBiodataKaryawan = findViewById(R.id.imgFotoBiodataKaryawan);

        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(), 0, dbhelper.get_gambar_user().length);
            imgFotoBiodataKaryawan.setBackground(null);
            imgFotoBiodataKaryawan.setImageBitmap(compressedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}