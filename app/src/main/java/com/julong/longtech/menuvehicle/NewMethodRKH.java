package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewMethodRKH extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dlgHelper;

    TabLayout tabNewRKH;
    Button btnBackRKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmethod_rkh);

        dbhelper = new DatabaseHelper(this);
        dlgHelper = new DialogHelper(this);

        tabNewRKH = findViewById(R.id.tabLayoutNewRKH);
        btnBackRKH = findViewById(R.id.btnBackNewRKH);

        btnBackRKH.setOnClickListener(view -> onBackPressed());

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