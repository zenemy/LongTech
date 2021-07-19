package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.julong.longtech.R;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

public class InputRincianRKH extends AppCompatActivity {

    FloatingActionButton btnShowInputRincianKerja;
    EditText etDriverRincianKegiatan, etShiftRincianKegiatan;
    Button btnSimpanPenginputanRKH, btnCancelPenginputanRKH;

    Dialog dlgInputRincianKerja, dlgSubmitRincianRKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_rincian_rkh);

        btnShowInputRincianKerja = findViewById(R.id.btnInputRincianRKH);
        etDriverRincianKegiatan = findViewById(R.id.etDriverRincianKegiatan);
        etShiftRincianKegiatan = findViewById(R.id.etShiftRincianKegiatan);
        btnSimpanPenginputanRKH = findViewById(R.id.btnSimpanPenginputanRKH);
        btnCancelPenginputanRKH = findViewById(R.id.btnCancelPenginputanRKH);

    }

    public void SubmitRincianRKH(View v) {

        dlgSubmitRincianRKH = new Dialog(this);
        dlgSubmitRincianRKH.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgSubmitRincianRKH.setContentView(R.layout.dialog_submitrincianrkh);
        dlgSubmitRincianRKH.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlgSubmitRincianRKH.setCanceledOnTouchOutside(false);
        Window windowDlgSubmitRKH = dlgSubmitRincianRKH.getWindow();
        windowDlgSubmitRKH.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dlgSubmitRincianRKH.show();

    }


    public void InputRincianRKH(View v) {

        dlgInputRincianKerja = new Dialog(this);
        dlgInputRincianKerja.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgInputRincianKerja.setContentView(R.layout.dialog_inputrincianrkh);
        dlgInputRincianKerja.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlgInputRincianKerja.setCanceledOnTouchOutside(false);
        Window windowDlgInputRincianKerja = dlgInputRincianKerja.getWindow();
        windowDlgInputRincianKerja.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btnDlgCancelInputRincianRKH = dlgInputRincianKerja.findViewById(R.id.btnDlgCancelInputRincianRKH);
        Button btnDlgSimpanInputRincianRKH = dlgInputRincianKerja.findViewById(R.id.btnDlgSimpanInputRincianRKH);
        dlgInputRincianKerja.show();

        btnDlgCancelInputRincianRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgInputRincianKerja.dismiss();
            }
        });
    }
}