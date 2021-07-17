package com.julong.longtech.menuhcm;

import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.R;
import com.julong.longtech.menusetup.DownloadData;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MesinAbsensi extends AppCompatActivity {

    private String statusTipeAbsen, nodocMesinAbsensi, latMesinAbsensi, longMesinAbsensi;
    DatabaseHelper dbhelper;

    LinearLayout linearLayoutMetodeAbsen, linearLayoutPilihAbsen, numpadLayoutMesinAbsensi, layoutIdAbsensiEmp, layoutInfoEmp;
    Button btnAbsenMasukMesinAbsensi, btnAbsenPulangMesinAbsensi, btnBackMetodeMesinAbsensi, btnMetodeIDMesinAbsensi, btnBackNumpadLayout;

    //Numpad Mesin Absensi
    Button numpad1, numpad2, numpad3, numpad4, numpad5, numpad6, numpad7, numpad8, numpad9, numpad0, numpadOK, numpadDel;
    TextView tvTipeAbsenMesinAbsensi, tvIdAbsensiEmp, tvEmpMesinAbsensi, tvJabatanMesinAbsensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesinabsensi);
        dbhelper = new DatabaseHelper(this);

        btnAbsenMasukMesinAbsensi = findViewById(R.id.btnAbsensiMesinMasuk);
        btnAbsenPulangMesinAbsensi = findViewById(R.id.btnAbsensiMesinPulang);
        linearLayoutMetodeAbsen = findViewById(R.id.linearLayoutPilihMetodeMesinAbsen);
        linearLayoutPilihAbsen = findViewById(R.id.linearPilihAbsen);
        btnBackMetodeMesinAbsensi = findViewById(R.id.btnBackMetodeMesinAbsen);
        numpadLayoutMesinAbsensi = findViewById(R.id.numpadLayoutMesinAbsensi);
        btnMetodeIDMesinAbsensi = findViewById(R.id.btnMetodeMesinID);
        tvTipeAbsenMesinAbsensi = findViewById(R.id.tvTipeMesinAbsensi);
        tvIdAbsensiEmp = findViewById(R.id.tvIdAbsensiEmp);
        layoutInfoEmp = findViewById(R.id.layoutMesinAbsensiInfoEmp);
        layoutIdAbsensiEmp = findViewById(R.id.layoutIdAbsensiEmp);
        tvEmpMesinAbsensi = findViewById(R.id.tvEmpMesinAbsensi);
        tvJabatanMesinAbsensi = findViewById(R.id.tvJabatanMesinAbsensi);

        //Numpad Mesin Absensi
        numpad1 = findViewById(R.id.numpad1MesinAbsensi);
        numpad2 = findViewById(R.id.numpad2MesinAbsensi);
        numpad3 = findViewById(R.id.numpad3MesinAbsensi);
        numpad4 = findViewById(R.id.numpad4MesinAbsensi);
        numpad5 = findViewById(R.id.numpad5MesinAbsensi);
        numpad6 = findViewById(R.id.numpad6MesinAbsensi);
        numpad7 = findViewById(R.id.numpad7MesinAbsensi);
        numpad8 = findViewById(R.id.numpad8MesinAbsensi);
        numpad9 = findViewById(R.id.numpad9MesinAbsensi);
        numpad0 = findViewById(R.id.numpad0MesinAbsensi);
        numpadOK = findViewById(R.id.numpadSimpanMesinAbsensi);
        numpadDel = findViewById(R.id.numpadDelMesinAbsensi);
        btnBackNumpadLayout = findViewById(R.id.btnBackNumpadLayoutMesinAbsen);
        functionNumpadMesinAbsensi();

        btnAbsenMasukMesinAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTipeAbsen = "CHECKIN";
                linearLayoutPilihAbsen.setVisibility(View.GONE);
                linearLayoutMetodeAbsen.setVisibility(View.VISIBLE);
                tvTipeAbsenMesinAbsensi.setText(statusTipeAbsen);
            }
        });

        btnAbsenPulangMesinAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTipeAbsen = "CHECKOUT";
                linearLayoutPilihAbsen.setVisibility(View.GONE);
                linearLayoutMetodeAbsen.setVisibility(View.VISIBLE);
                tvTipeAbsenMesinAbsensi.setText(statusTipeAbsen);
            }
        });

        btnMetodeIDMesinAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numpadLayoutMesinAbsensi.setVisibility(View.VISIBLE);
                layoutIdAbsensiEmp.setVisibility(View.VISIBLE);
                linearLayoutMetodeAbsen.setVisibility(View.GONE);
            }
        });

        btnBackMetodeMesinAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutMetodeAbsen.setVisibility(View.GONE);
                linearLayoutPilihAbsen.setVisibility(View.VISIBLE);
                tvTipeAbsenMesinAbsensi.setText("MESIN ABSENSI KARYAWAN");
            }
        });

    }

    private void functionNumpadMesinAbsensi() {
        numpad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("1");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "1");
                }
            }
        });

        numpad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("2");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "2");
                }
            }
        });

        numpad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("3");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "3");
                }
            }
        });

        numpad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("4");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "4");
                }
            }
        });

        numpad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("5");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "5");
                }
            }
        });

        numpad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("6");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "6");
                }
            }
        });

        numpad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("7");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "7");
                }
            }
        });

        numpad8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("8");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "8");
                }
            }
        });

        numpad9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("9");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "9");
                }
            }
        });

        numpad0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvIdAbsensiEmp.length() == 0) {
                    tvIdAbsensiEmp.setText("0");
                }
                else {
                    tvIdAbsensiEmp.setText(tvIdAbsensiEmp.getText().toString() + "0");
                }
            }
        });

        numpadDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIdAbsensiEmp.setText(null);
            }
        });

        numpadOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                if (tvIdAbsensiEmp.length() == 0) {
                    new SweetAlertDialog(MesinAbsensi.this, SweetAlertDialog.ERROR_TYPE).setContentText("Masukkan ID Absensi").setConfirmText("OK").show();
                } else {
                    if (tvIdAbsensiEmp.getText().toString().equals(dbhelper.check_mesinabsensi(3, tvIdAbsensiEmp.getText().toString()))) {
                        nodocMesinAbsensi = dbhelper.get_tbl_username(0) + "/ABSMSN/" +  new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
                        tvEmpMesinAbsensi.setText(dbhelper.check_mesinabsensi(1, tvIdAbsensiEmp.getText().toString()));
                        tvJabatanMesinAbsensi.setText(dbhelper.check_mesinabsensi(2, tvIdAbsensiEmp.getText().toString()));
                        dbhelper.insert_absensimesin(nodocMesinAbsensi, tvIdAbsensiEmp.getText().toString(), dbhelper.check_mesinabsensi(0, tvIdAbsensiEmp.getText().toString()),
                                statusTipeAbsen, "FINGER", "Home",latMesinAbsensi, longMesinAbsensi);
                        new SweetAlertDialog(MesinAbsensi.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Absen").setConfirmText("OK").show();
                    }
                    layoutInfoEmp.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBackNumpadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numpadLayoutMesinAbsensi.setVisibility(View.GONE);
                linearLayoutMetodeAbsen.setVisibility(View.VISIBLE);
                layoutIdAbsensiEmp.setVisibility(View.GONE);
                layoutInfoEmp.setVisibility(View.GONE);
                tvIdAbsensiEmp.setText(null);
            }
        });

    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        latMesinAbsensi = String.valueOf(latitude);
        longMesinAbsensi = String.valueOf(longitude);
    }

    //Function Tombol Back
    @Override
    public void onBackPressed() {

        if (statusTipeAbsen != null && linearLayoutMetodeAbsen.getVisibility() == View.VISIBLE) {
            statusTipeAbsen = null;
            linearLayoutMetodeAbsen.setVisibility(View.GONE);
            linearLayoutPilihAbsen.setVisibility(View.VISIBLE);
            tvTipeAbsenMesinAbsensi.setText("MESIN ABSENSI KARYAWAN");
        }
        else if (statusTipeAbsen != null && numpadLayoutMesinAbsensi.getVisibility() == View.VISIBLE) {
            numpadLayoutMesinAbsensi.setVisibility(View.GONE);
            linearLayoutMetodeAbsen.setVisibility(View.VISIBLE);
            layoutIdAbsensiEmp.setVisibility(View.GONE);
            layoutInfoEmp.setVisibility(View.GONE);
        }
        else {
            finish();
        }

    }
}