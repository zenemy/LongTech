package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InputRincianRKH extends AppCompatActivity {

    FloatingActionButton btnShowInputRincianKerja;
    EditText etDriverRincianKegiatan, etShiftRincianKegiatan, etWaktuKerjaRincianRKH;
    Button btnSimpanPenginputanRKH, btnCancelPenginputanRKH;
    TextView tvUnitRincianRKH;

    Dialog dlgInputRincianKerja, dlgSubmitRincianRKH;
    String todayDateRincianRKH, nodocRKH, vehicleNameRKH, shiftRincianRKH, driverDetailRKH;

    private List<String> listMuatanRincianRKH;
    ArrayAdapter<String> adapterMuatanRincianRKH;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_rincian_rkh);

        dbhelper = new DatabaseHelper(this);
        btnShowInputRincianKerja = findViewById(R.id.btnInputRincianRKH);
        etDriverRincianKegiatan = findViewById(R.id.etDriverRincianKegiatan);
        etShiftRincianKegiatan = findViewById(R.id.etShiftRincianKegiatan);
        btnSimpanPenginputanRKH = findViewById(R.id.btnSimpanPenginputanRKH);
        btnCancelPenginputanRKH = findViewById(R.id.btnCancelPenginputanRKH);
        tvUnitRincianRKH = findViewById(R.id.tvUnitRincianRKH);

        Bundle bundle = getIntent().getExtras();
        todayDateRincianRKH = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        nodocRKH = bundle.getString("nodoc");
        vehicleNameRKH = bundle.getString("vehicle");
        shiftRincianRKH = bundle.getString("shiftkerja");
        driverDetailRKH = bundle.getString("driver");

        tvUnitRincianRKH.setText("Kegiatan RKH " + vehicleNameRKH);
        etDriverRincianKegiatan.setText(driverDetailRKH);
        etShiftRincianKegiatan.setText(shiftRincianRKH);

    }

    public void SubmitRincianRKH(View v) {

        dlgSubmitRincianRKH = new Dialog(this);
        dlgSubmitRincianRKH.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgSubmitRincianRKH.setContentView(R.layout.dialog_submitrincianrkh);
        dlgSubmitRincianRKH.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
        AutoCompleteTextView acLoadTypeRincianRKH = dlgInputRincianKerja.findViewById(R.id.acLoadTypeRincianRKH);
        EditText etSatuanKerjaRincianRKH = dlgInputRincianKerja.findViewById(R.id.etSatuanKerjaRincianRKH);
        AutoCompleteTextView acWorkPlaceRincianRKH = dlgInputRincianKerja.findViewById(R.id.acWorkPlaceRincianRKH);
        AutoCompleteTextView acKegiatanKerjaRincianRKH = dlgInputRincianKerja.findViewById(R.id.acKegiatanKerjaRincianRKH);
        AutoCompleteTextView acTargetRincianRKH = dlgInputRincianKerja.findViewById(R.id.acTargetRincianRKH);
        etWaktuKerjaRincianRKH = dlgInputRincianKerja.findViewById(R.id.etWaktuKerjaRincianRKH);
        EditText etKMHMRincianRKH = dlgInputRincianKerja.findViewById(R.id.etKMHMRincianRKH);
        EditText etAdditionalNoteRincianRKH = dlgInputRincianKerja.findViewById(R.id.etAdditionalNoteRincianRKH);
        Button btnDlgCancelInputRincianRKH = dlgInputRincianKerja.findViewById(R.id.btnDlgCancelInputRincianRKH);
        Button btnDlgSimpanInputRincianRKH = dlgInputRincianKerja.findViewById(R.id.btnDlgSimpanInputRincianRKH);
        dlgInputRincianKerja.show();

        listMuatanRincianRKH = dbhelper.get_loadtype();
        adapterMuatanRincianRKH = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listMuatanRincianRKH);
        acLoadTypeRincianRKH.setAdapter(adapterMuatanRincianRKH);

        etWaktuKerjaRincianRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker timePickerJamRKH = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build();
                timePickerJamRKH.show(getSupportFragmentManager(), "TAG");

                timePickerJamRKH.addOnPositiveButtonClickListener(selection -> {
                    onWaktuKerjaSelected(timePickerJamRKH.getHour(), timePickerJamRKH.getMinute());
                });
            }
        });

        btnDlgCancelInputRincianRKH.setOnClickListener(v1 -> dlgInputRincianKerja.dismiss());
        btnDlgSimpanInputRincianRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper.insert_rkh_detail2(nodocRKH, dbhelper.get_vehiclecode(0, vehicleNameRKH), vehicleNameRKH, shiftRincianRKH,dbhelper.get_empcode(driverDetailRKH), acLoadTypeRincianRKH.getText().toString(),
                        etSatuanKerjaRincianRKH.getText().toString(), acWorkPlaceRincianRKH.getText().toString(), acKegiatanKerjaRincianRKH.getText().toString(), etWaktuKerjaRincianRKH.getText().toString(),
                        etKMHMRincianRKH.getText().toString(), acTargetRincianRKH.getText().toString(), etAdditionalNoteRincianRKH.getText().toString());
            }
        });
    }

    private void onWaktuKerjaSelected(int hour, int minute) {
        String hourAsText;
        String minuteAsText;

        if (hour < 10) {
            hourAsText = '0' + String.valueOf(hour);
        } else {
            hourAsText = String.valueOf(hour);
        }

        if (minute < 10) {
            minuteAsText = '0' + String.valueOf(minute);
        } else {
            minuteAsText = String.valueOf(minute);
        }

        String showTime = hourAsText + ":" + minuteAsText;
        etWaktuKerjaRincianRKH.setText(showTime);

    }

}