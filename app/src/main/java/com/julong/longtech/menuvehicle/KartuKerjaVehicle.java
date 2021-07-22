package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KartuKerjaVehicle extends AppCompatActivity {

    String savedate;
    private KeyListener keyListenerEtHasilKerja;

    EditText etHasilKerjaCarLog;
    TextView tvTanggalCarLog, tvInfoUnitCarLog;
    AutoCompleteTextView acLoadTypeCarLog, acHelper1CarLog, acHelper2CarLog, acLoadCategoryCarLog;
    LinearLayout layoutHelperCarlog, layoutAsalCarLog, layoutTujuanCarLog;
    Button btnSubmitCarlog, btnBackCarLog;
    ImageButton btnAddHasilKerja, btnMinusHasilKerja;
    TextInputLayout inputLayoutAsalKebun, inputLayoutAsalDivisi, inputLayoutAsalLokasi, inputLayoutTujuanKebun,
            inputLayoutTujuanDivisi, inputLayoutTujuanLokasi, inputLayoutTujuanKegiatan, inputLayoutHasilKerjaCarLog;

    private List<String> listMuatanCarLog, listEmployee, listCategoryMuatan;
    ArrayAdapter<String> adapterMuatanCarLog, adapterEmployee, adapterCategoryMuatan;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartukerja_vehicle);

        dbhelper = new DatabaseHelper(this);

        tvTanggalCarLog = findViewById(R.id.tvTanggalCarLog);
        tvInfoUnitCarLog = findViewById(R.id.tvInfoUnitCarLog);
        layoutAsalCarLog = findViewById(R.id.layoutAsalMuatanCarLog);
        layoutTujuanCarLog = findViewById(R.id.layoutTujuanMuatanCarLog);
        layoutHelperCarlog = findViewById(R.id.layoutHelperCarlog);
        acLoadTypeCarLog = findViewById(R.id.acLoadTypeCarLog);
        acHelper1CarLog = findViewById(R.id.acHelper1CarLog);
        acHelper2CarLog = findViewById(R.id.acHelper2CarLog);
        acLoadCategoryCarLog = findViewById(R.id.acLoadCategoryCarLog);
        btnAddHasilKerja = findViewById(R.id.btnAddHasilKerjaCarLog);
        btnMinusHasilKerja = findViewById(R.id.btnReduceHasilKerjaCarLog);
        btnSubmitCarlog = findViewById(R.id.btnSubmitCarLog);
        btnBackCarLog = findViewById(R.id.btnBackCarLog);
        inputLayoutAsalKebun = findViewById(R.id.inputLayoutAsalKebun);
        inputLayoutAsalDivisi = findViewById(R.id.inputLayoutAsalDivisi);
        inputLayoutAsalLokasi = findViewById(R.id.inputLayoutAsalLokasi);
        inputLayoutTujuanKebun = findViewById(R.id.inputLayoutTujuanKebun);
        inputLayoutTujuanDivisi = findViewById(R.id.inputLayoutTujuanDivisi);
        inputLayoutTujuanLokasi = findViewById(R.id.inputLayoutTujuanLokasi);
        inputLayoutTujuanKegiatan = findViewById(R.id.inputLayoutTujuanKegiatan);
        etHasilKerjaCarLog = findViewById(R.id.etHasilKerjaCarLog);
        inputLayoutHasilKerjaCarLog = findViewById(R.id.inputLayoutHasilKerjaCarLog);
        keyListenerEtHasilKerja = etHasilKerjaCarLog.getKeyListener();

        btnBackCarLog.setOnClickListener(v -> finish());

        todayDate();

        tvInfoUnitCarLog.setText(dbhelper.get_tbl_username(19));

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
                layoutTujuanCarLog.setVisibility(View.GONE);
                layoutAsalCarLog.setVisibility(View.GONE);
                layoutHelperCarlog.setVisibility(View.GONE);
                btnAddHasilKerja.setVisibility(View.GONE);
                btnMinusHasilKerja.setVisibility(View.GONE);
                inputLayoutHasilKerjaCarLog.setSuffixText(null);
                listCategoryMuatan = dbhelper.get_loadcategory(adapterMuatanCarLog.getItem(position));
                adapterCategoryMuatan = new ArrayAdapter<String>(KartuKerjaVehicle.this, R.layout.spinnerlist, R.id.spinnerItem, listCategoryMuatan);
                acLoadCategoryCarLog.setAdapter(adapterCategoryMuatan);
            }
        });

        acLoadCategoryCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dbhelper.layoutsetting_carlog(0, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutHelperCarlog.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(0, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutHelperCarlog.setVisibility(View.GONE);
                }

                //Layout Asal Muatan
                if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutAsalCarLog.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                    inputLayoutAsalKebun.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(2, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutAsalKebun.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                    inputLayoutAsalDivisi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(3, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutAsalDivisi.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutAsalCarLog.setVisibility(View.VISIBLE);
                    inputLayoutAsalLokasi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(4, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutAsalLokasi.setVisibility(View.GONE);
                }

                // Layout Tujuan Muatan
                if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("Y") && dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("N") && dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    layoutTujuanCarLog.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanKebun.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(5, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanKebun.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanDivisi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(6, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanDivisi.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanLokasi.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(7, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanLokasi.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    layoutTujuanCarLog.setVisibility(View.VISIBLE);
                    inputLayoutTujuanKegiatan.setVisibility(View.VISIBLE);
                } else if (dbhelper.layoutsetting_carlog(8, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    inputLayoutTujuanKegiatan.setVisibility(View.GONE);
                }

                if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("Y")) {
                    etHasilKerjaCarLog.setKeyListener(keyListenerEtHasilKerja);
                    btnAddHasilKerja.setVisibility(View.GONE);
                    btnMinusHasilKerja.setVisibility(View.GONE);
                } else if (dbhelper.layoutsetting_carlog(9, adapterCategoryMuatan.getItem(position)).equals("N")) {
                    etHasilKerjaCarLog.setKeyListener(null);
                    btnAddHasilKerja.setVisibility(View.VISIBLE);
                    btnMinusHasilKerja.setVisibility(View.VISIBLE);
                }

                inputLayoutHasilKerjaCarLog.setSuffixText(dbhelper.settingcarlog_satuanhasilkerja(adapterCategoryMuatan.getItem(position)));

            }
        });

        etHasilKerjaCarLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etHasilKerjaCarLog.getText().toString().contains("-")) {
                    etHasilKerjaCarLog.setText("0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });


        btnAddHasilKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(etHasilKerjaCarLog.getText().toString()) + 1;
                etHasilKerjaCarLog.setText(String.valueOf(result));
            }
        });

        btnMinusHasilKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(etHasilKerjaCarLog.getText().toString()) - 1;
                etHasilKerjaCarLog.setText(String.valueOf(result));
            }
        });

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