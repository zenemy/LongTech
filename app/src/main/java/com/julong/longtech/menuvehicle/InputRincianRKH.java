package com.julong.longtech.menuvehicle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InputRincianRKH extends AppCompatActivity {

    FloatingActionButton btnShowInputRincianKerja;
    EditText etVehicleRincianKegiatan, etDriverRincianKegiatan, etShiftRincianKegiatan;
    Button btnSimpanPenginputanRKH, btnCancelPenginputanRKH;
    TextView tvUnitRincianRKH;
    ListView lvRincianRKH;
    LinearLayout layoutBtnRincianRKH;

    Dialog dlgInputRincianKerja, dlgSubmitRincianRKH;
    String todayDateRincianRKH, nodocRKH, vehicleCodeRKH, vehicleNameRKH, shiftRincianRKH, driverNameDetailRKH,
           driverCodeDetailRKH, selectedKegiatan, selectedLokasi, selectedSatuan;

    private List<String> listKegiatanRincianRKH, listLokasiRincianRKH, listLoadTypeGS;

    private List<ListParamRincianRKH> listParamRincian;
    AdapterRincianRKH adapterRincian;
    ArrayAdapter<String> adapterKegiatan, adapterLokasi, adapterLoadTypeGS;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_rincian_rkh);

        // Declare design ID
        dbhelper = new DatabaseHelper(this);
        btnShowInputRincianKerja = findViewById(R.id.btnInputRincianRKH);
        etVehicleRincianKegiatan = findViewById(R.id.etVehicleRincianRKH);
        etDriverRincianKegiatan = findViewById(R.id.etDriverRincianKegiatan);
        etShiftRincianKegiatan = findViewById(R.id.etShiftRincianKegiatan);
        layoutBtnRincianRKH = findViewById(R.id.layoutBtnRincianRKH);
        btnSimpanPenginputanRKH = findViewById(R.id.btnSimpanPenginputanRKH);
        btnCancelPenginputanRKH = findViewById(R.id.btnCancelPenginputanRKH);
        tvUnitRincianRKH = findViewById(R.id.tvUnitRincianRKH);
        lvRincianRKH = findViewById(R.id.lvRincianRKH);

        btnCancelPenginputanRKH.setOnClickListener(view -> finish());

        // Get data intent from RKH
        Bundle bundle = getIntent().getExtras();
        todayDateRincianRKH = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        nodocRKH = bundle.getString("nodoc");
        vehicleCodeRKH = bundle.getString("vehiclecode");
        vehicleNameRKH = bundle.getString("vehiclename");
        shiftRincianRKH = bundle.getString("shiftkerja");
        driverCodeDetailRKH = bundle.getString("drivercode");
        driverNameDetailRKH = bundle.getString("drivername");

        tvUnitRincianRKH.setText("Kegiatan RKH " + vehicleCodeRKH);
        etVehicleRincianKegiatan.setText(vehicleNameRKH);
        etDriverRincianKegiatan.setText(driverNameDetailRKH);
        etShiftRincianKegiatan.setText(shiftRincianRKH);

        loadListViewRKH();

        if (dbhelper.get_statusrkh(4).equals("0") || dbhelper.get_statusrkh(4).equals("1")) {
            layoutBtnRincianRKH.setVisibility(View.GONE);
            btnShowInputRincianKerja.setVisibility(View.GONE);
        }
    }

    public void SubmitRincianRKH(View v) {

        new SweetAlertDialog(InputRincianRKH.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Menyimpan")
                .setConfirmClickListener(sweetAlertDialog -> finish()).setConfirmText("OK").show();

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
        AutoCompleteTextView acKegiatanRincianRKH = dlgInputRincianKerja.findViewById(R.id.acKegiatanKerjaRincianRKH);
        EditText etTargetRincianRKH = dlgInputRincianKerja.findViewById(R.id.etTargetRincianRKH);
        EditText etAdditionalNoteRincianRKH = dlgInputRincianKerja.findViewById(R.id.etAdditionalNoteRincianRKH);
        Button btnDlgCancelInputRincianRKH = dlgInputRincianKerja.findViewById(R.id.btnDlgCancelInputRincianRKH);
        Button btnDlgSimpanInputRincianRKH = dlgInputRincianKerja.findViewById(R.id.btnDlgSimpanInputRincianRKH);
        TextInputLayout inputLayoutTargetKerja = dlgInputRincianKerja.findViewById(R.id.inputLayoutTargetRincianRKH);
        dlgInputRincianKerja.show();

        listLoadTypeGS = dbhelper.get_gs_loadtype_filtered(dbhelper.get_vehiclename(1, vehicleCodeRKH));
        adapterLoadTypeGS = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listLoadTypeGS);
        acLoadTypeRincianRKH.setAdapter(adapterLoadTypeGS);

        listLokasiRincianRKH = dbhelper.get_fieldcrop(1);
        adapterLokasi = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listLokasiRincianRKH);
        acWorkPlaceRincianRKH.setAdapter(adapterLokasi);

        ArrayList<String> kegiatanOutput = (ArrayList<String>) listKegiatanRincianRKH;
        ArrayList<String> lokasiOutput = (ArrayList<String>) listLokasiRincianRKH;

        acLoadTypeRincianRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                acKegiatanRincianRKH.setText(null);
                etSatuanKerjaRincianRKH.setText(null);
                inputLayoutTargetKerja.setSuffixText(null);
                selectedSatuan = null;

//                listKegiatanRincianRKH = dbhelper.get_activitysap_filtered(dbhelper.get_single_gsloadtype(adapterLoadTypeGS.getItem(position)),
//                        dbhelper.get_vehiclename(1, vehicleCodeRKH));
//                adapterKegiatan = new ArrayAdapter<String>(InputRincianRKH.this, R.layout.spinnerlist, R.id.spinnerItem, listKegiatanRincianRKH);
//                acKegiatanRincianRKH.setAdapter(adapterKegiatan);
            }
        });

        acKegiatanRincianRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedKegiatan = dbhelper.get_single_satuanmuatan_activitysap(adapterKegiatan.getItem(position), 0);
                selectedSatuan = dbhelper.get_single_satuanmuatan_activitysap(adapterKegiatan.getItem(position), 1);
                etSatuanKerjaRincianRKH.setText(selectedSatuan);
                inputLayoutTargetKerja.setSuffixText(selectedSatuan);
            }
        });

        acWorkPlaceRincianRKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedLokasi = dbhelper.get_singlelokasiCode(adapterLokasi.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(InputRincianRKH.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acWorkPlaceRincianRKH.getWindowToken(), 0);
            }
        });

        btnDlgCancelInputRincianRKH.setOnClickListener(v1 -> dlgInputRincianKerja.dismiss());

        btnDlgSimpanInputRincianRKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLokasi == null || selectedKegiatan == null
                    || TextUtils.isEmpty(etTargetRincianRKH.getText().toString().trim())) {
                    Toast.makeText(InputRincianRKH.this, "Lengkapi Data!", Toast.LENGTH_LONG).show();
                }
                else if (lokasiOutput.indexOf(acWorkPlaceRincianRKH.getText().toString()) == -1) {
                    Toast.makeText(InputRincianRKH.this, "Lokasi tidak valid!", Toast.LENGTH_LONG).show();
                }
                else {
                    dbhelper.insert_rkh_detail2(nodocRKH, vehicleCodeRKH, RencanaKerjaHarian.selectedDateRKH,
                            shiftRincianRKH, driverCodeDetailRKH, selectedSatuan, selectedLokasi,
                            selectedKegiatan, null, null, etTargetRincianRKH.getText().toString(),
                            etAdditionalNoteRincianRKH.getText().toString());
                    dlgInputRincianKerja.dismiss();
                    loadListViewRKH();
                }

            }
        });
    }

    private void loadListViewRKH() {

        listParamRincian = new ArrayList<>();
        listParamRincian.clear();
        final Cursor cursor = dbhelper.listview_rincianrkh(nodocRKH, vehicleCodeRKH, shiftRincianRKH);
        if (cursor.moveToFirst()) {
            do {
                ListParamRincianRKH paramsRincian = new ListParamRincianRKH(
                        cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)
                );
                listParamRincian.add(paramsRincian);
            } while (cursor.moveToNext());
        }
        adapterRincian = new AdapterRincianRKH(this, R.layout.item_lvdetailrkh, listParamRincian);
        lvRincianRKH.setAdapter(adapterRincian);
    }

}