package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.julong.longtech.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewMethodRKH extends AppCompatActivity {

    TabLayout tabNewRKH;
    Button btnBackRKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmethod_rkh);

        tabNewRKH = findViewById(R.id.tabLayoutNewRKH);
        btnBackRKH = findViewById(R.id.btnBackNewRKH);

        btnBackRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}