package com.julong.longtech.menuinventory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.PemeriksaanPengecekanHarian;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PermintaanBBM extends AppCompatActivity {

    DatabaseHelper dbhelper;
    String selectedVehicle, selectedGudang, nodocMintaBBM;
    byte[] byteFotoMintaBBM;

    AutoCompleteTextView acVehicle, acGudangMintaBBM;
    EditText etTodayDate, etLastKMHM, etJumlahPermintaanBBM, etNoteMintaBBM;
    ImageButton btnFotoPermintaanBBM;
    Button btnBackMintaBBM;

    List<String> listVehicle, listGudangName, listGudangCode;
    ArrayAdapter<String> adapterVehicle, adapterGudang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permintaan_bbm);

        dbhelper = new DatabaseHelper(this);

        etTodayDate = findViewById(R.id.etTodayDateMintaBBM);
        acVehicle = findViewById(R.id.acUnitMintaBBM);
        acGudangMintaBBM = findViewById(R.id.acGudangMintaBBM);
        etLastKMHM = findViewById(R.id.etMintaBBMKHM);
        etJumlahPermintaanBBM = findViewById(R.id.etJumlahPermintaanBBM);
        etNoteMintaBBM = findViewById(R.id.etNoteMintaBBM);
        btnFotoPermintaanBBM = findViewById(R.id.btnFotoMintaBBM);
        btnBackMintaBBM = findViewById(R.id.btnBackMintaBBM);

        String todayDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        etTodayDate.setText(todayDate);

        btnBackMintaBBM.setOnClickListener(view -> onBackPressed());

        if (dbhelper.get_tbl_username(3).equals("OPR")) {
            acVehicle.setText(dbhelper.get_vehiclename(2, dbhelper.get_tbl_username(19)));
            selectedVehicle = dbhelper.get_tbl_username(19);
        }

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicle.setAdapter(adapterVehicle);

        listGudangName = dbhelper.get_gudangmd(1);
        listGudangCode = dbhelper.get_gudangmd(0);
        adapterGudang = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listGudangName);
        acGudangMintaBBM.setAdapter(adapterGudang);

        acVehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PermintaanBBM.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicle.getWindowToken(), 0);
            }
        });

        acGudangMintaBBM.setOnItemClickListener((adapterView, view, position, l) -> selectedGudang = listGudangCode.get(position));

        ActivityResultLauncher<Intent> intentLaunchCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap photoCamera = (Bitmap) bundle.get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byteFotoMintaBBM = stream.toByteArray();
                        Bitmap bitmapBensin = BitmapFactory.decodeByteArray(byteFotoMintaBBM, 0, byteFotoMintaBBM.length);
                        btnFotoPermintaanBBM.setImageBitmap(bitmapBensin);

                    }

                }
        );

        btnFotoPermintaanBBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byteFotoMintaBBM = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(PermintaanBBM.this.getPackageManager()) != null) {
                    intentLaunchCamera.launch(takePictureIntent);
                }
            }
        });

    }

    public void submitPermintaahBBM(View v) {
        if (selectedVehicle == null || selectedGudang == null
        || TextUtils.isEmpty(etLastKMHM.getText().toString().trim())
        || TextUtils.isEmpty(etJumlahPermintaanBBM.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi data").setConfirmText("KEMBALI").show();
        }
        else {
            String valueKMHM = etLastKMHM.getText().toString().replace(".",",");
            etLastKMHM.setText(valueKMHM);

            nodocMintaBBM = dbhelper.get_tbl_username(0) + "/PRBBM/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

            dbhelper.insert_permintaanbbm(nodocMintaBBM, selectedVehicle, selectedGudang,
                    valueKMHM, etJumlahPermintaanBBM.getText().toString(),
                    etNoteMintaBBM.getText().toString(), byteFotoMintaBBM);

            SweetAlertDialog dlgFinishApel = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            dlgFinishApel.setCancelable(false);
            dlgFinishApel.setTitleText("Berhasil simpan");
            dlgFinishApel.setConfirmClickListener(sweetAlertDialog -> onBackPressed());
            dlgFinishApel.setConfirmText("OK").show();

        }
    }


    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}