package com.julong.longtech;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.VerifikasiGIS;
import com.julong.longtech.menuworkshop.LaporanPerbaikan;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.menuhcm.ApelPagi.REQUEST_IMAGE_CAPTURE;
import static com.julong.longtech.menuhcm.ApelPagi.btnActionApel;
import static com.julong.longtech.menuhcm.ApelPagi.dataProcess;
import static com.julong.longtech.menuhcm.ApelPagi.intentLaunchFotoAnggota;
import static com.julong.longtech.menuhcm.ApelPagi.nodocApel;
import static com.julong.longtech.menuhcm.ApelPagi.scanBarcode;
import static com.julong.longtech.menuhcm.ApelPagi.selectedEmp;
import static com.julong.longtech.menuhcm.ApelPagi.selectedItemData;
import static com.julong.longtech.menuhcm.ApelPagi.selectedUnit;
import static com.julong.longtech.menuworkshop.LaporanPerbaikan.loadListViewMaterial;

public class DialogHelper extends Dialog {

    private Context activityContext;
    private Dialog dialogInfo;
    DatabaseHelper dbhelper;

    // Dialog Apel
    public static Dialog dlgMetodeAbsen, dlgTidakHadir;
    String tipeTidakHadir;

    // Dialog GIS
    private Dialog dlgSelesaiVerifikasi;

    //Dialog Workshop
    private Dialog dlgAddMaterialService;
    private String selectedMaterial, materialUOM;

    public DialogHelper(Context context) {
        super(context);
        activityContext = context;
        dbhelper = new DatabaseHelper(context);
    }

    public Dialog showDialogInfo(String infoTitle) {
        dialogInfo = new Dialog(activityContext);
        dialogInfo.setContentView(R.layout.dialog_info);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowDlgInfo = dialogInfo.getWindow();
        windowDlgInfo.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView tvDlgInfo = dialogInfo.findViewById(R.id.tvDlgInfoTitle);
        Button btnOk = dialogInfo.findViewById(R.id.btnDialogInfo);

        btnOk.setOnClickListener(view -> dialogInfo.dismiss());
        tvDlgInfo.setText(infoTitle);

        dialogInfo.show();

        return dialogInfo;
    }

    public Dialog showSummaryVerifikasiGIS() {
        dlgSelesaiVerifikasi = new Dialog(activityContext);
        dlgSelesaiVerifikasi.setContentView(R.layout.dialog_summarygis);
        dlgSelesaiVerifikasi.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowVerificationDone = dlgSelesaiVerifikasi.getWindow();
        windowVerificationDone.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView tvUnitSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvHESummaryGIS);
        TextView tvDriverSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvDriverSummaryGIS);
        TextView tvLokasiSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvBlokSummaryGIS);
        TextView tvKegiatanSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvActivitySummaryGIS);
        TextView tvHasilSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvResultSummaryGIS);
        TextView tvTotalKoordinat = dlgSelesaiVerifikasi.findViewById(R.id.tvTotalKoordinatSummary);
        Button btnBackSummary = dlgSelesaiVerifikasi.findViewById(R.id.btnBackSummaryGIS);
        Button btnDoneSummary = dlgSelesaiVerifikasi.findViewById(R.id.btnDoneSummaryGIS);
        dlgSelesaiVerifikasi.show();
        btnBackSummary.setOnClickListener(view1 -> dlgSelesaiVerifikasi.dismiss());

        tvUnitSummary.setText(dbhelper.get_vehiclename(0, VerifikasiGIS.selectedVehicleGIS));
        tvDriverSummary.setText(dbhelper.get_empname(VerifikasiGIS.selectedDriverGIS));
        tvLokasiSummary.setText(VerifikasiGIS.acLokasiGIS.getText().toString());
        tvKegiatanSummary.setText(VerifikasiGIS.acKegiatanGIS.getText().toString());
        tvHasilSummary.setText(VerifikasiGIS.etHasilVerifikasi.getText());
        tvTotalKoordinat.setText(dbhelper.get_count_totalkoordinatgis(VerifikasiGIS.nodocVerifikasiGIS));

        btnDoneSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgSelesaiVerifikasi.dismiss();
                dbhelper.updatestatus_verifikasigis(VerifikasiGIS.nodocVerifikasiGIS,
                        VerifikasiGIS.etHasilVerifikasi.getText().toString(),
                        VerifikasiGIS.byteFotoGIS, VerifikasiGIS.etSesuaiSOP.getText().toString());

                SweetAlertDialog sweetDlgVerifikasiDone = new SweetAlertDialog(activityContext, SweetAlertDialog.SUCCESS_TYPE);
                sweetDlgVerifikasiDone.setTitleText("Verifikasi Selesai");
                sweetDlgVerifikasiDone.setConfirmText("SELESAI");
                sweetDlgVerifikasiDone.setCancelable(false);
                sweetDlgVerifikasiDone.setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    Intent backIntent = new Intent();
                    ((Activity) activityContext).setResult(727, backIntent);
                    ((Activity) activityContext).finish();
                });
                sweetDlgVerifikasiDone.show();
            }
        });

        return dlgSelesaiVerifikasi;
    }

    public void dlgApelAnggota(String employeeName, String employeeCode, String itemData) {
        //Show method dialog
        dlgMetodeAbsen = new Dialog(activityContext);
        dlgMetodeAbsen.setContentView(R.layout.dialog_metodeabsen);
        dlgMetodeAbsen.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowMetodeAbsen = dlgMetodeAbsen.getWindow();
        windowMetodeAbsen.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView tvHeaderDlgMetode = dlgMetodeAbsen.findViewById(R.id.tvHeaderDlgMetodeApel);
        Button btnApelQR = dlgMetodeAbsen.findViewById(R.id.btnApelQR);
        Button btnApelFoto = dlgMetodeAbsen.findViewById(R.id.btnApelFoto);
        Button btnApelTidakHadir = dlgMetodeAbsen.findViewById(R.id.btnApelTidakHadir);
        Button btnBackDlgApel = dlgMetodeAbsen.findViewById(R.id.btnBackDlgApel);
        tvHeaderDlgMetode.setText(employeeName);
        dlgMetodeAbsen.show();

        btnBackDlgApel.setOnClickListener(view1 -> dlgMetodeAbsen.dismiss());

        // Metode Foto
        btnApelFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataProcess = 2;
                selectedEmp = employeeCode;
                selectedItemData = itemData;
                dlgMetodeAbsen.dismiss();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activityContext.getPackageManager()) != null) {
                    intentLaunchFotoAnggota.launch(takePictureIntent);
                }
            }
        });

        // Metode Scan
        btnApelQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataProcess = 3;
                selectedEmp = employeeCode;
                selectedItemData = itemData;
                dlgMetodeAbsen.dismiss();
                scanBarcode((ApelPagi) activityContext);
            }
        });

        // Tidak Hadir
        btnApelTidakHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgMetodeAbsen.dismiss();

                // Show dialog tidak hadir
                dlgTidakHadir = new Dialog(activityContext);
                dlgTidakHadir.setContentView(R.layout.dialog_apeltdkhadir);
                dlgTidakHadir.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                Window windowTdkHadir = dlgTidakHadir.getWindow();
                windowTdkHadir.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView tvHeaderDlgMetode = dlgTidakHadir.findViewById(R.id.tvHeaderDlgTdkHadirApel);

                // Radio selection tdk hadir
                RadioButton radioApelCuti = dlgTidakHadir.findViewById(R.id.radioApelCuti);
                RadioButton radioApelP1 = dlgTidakHadir.findViewById(R.id.radioApelP1);
                RadioButton radioApelTK = dlgTidakHadir.findViewById(R.id.radioApelTK);
                RadioButton radioApelSakit = dlgTidakHadir.findViewById(R.id.radioApelSakit);
                RadioButton radioApelP2 = dlgTidakHadir.findViewById(R.id.radioApelP2);
                RadioButton radioApelMangkir = dlgTidakHadir.findViewById(R.id.radioApelMangkir);

                EditText etNoteDlgTdkHadir = dlgTidakHadir.findViewById(R.id.etNoteDlgTdkHadir);
                Button btnKonfirmTdkHadir = dlgTidakHadir.findViewById(R.id.btnApelConfirmTdkHadir);
                Button btnBackTdkHadir = dlgTidakHadir.findViewById(R.id.btnBackDlgApelTdkHadir);
                tvHeaderDlgMetode.setText(employeeName);
                dlgTidakHadir.show();

                selectedEmp = employeeCode;
                selectedItemData = itemData;

                radioApelCuti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tipeTidakHadir = "CT";
                        radioApelCuti.setChecked(true);
                        radioApelP1.setChecked(false);
                        radioApelMangkir.setChecked(false);
                        radioApelTK.setChecked(false);
                        radioApelP2.setChecked(false);
                        radioApelSakit.setChecked(false);
                    }
                });

                radioApelP1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tipeTidakHadir = "P1";
                        radioApelP1.setChecked(true);
                        radioApelCuti.setChecked(false);
                        radioApelMangkir.setChecked(false);
                        radioApelTK.setChecked(false);
                        radioApelP2.setChecked(false);
                        radioApelSakit.setChecked(false);
                    }
                });

                radioApelP2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tipeTidakHadir = "P2";
                        radioApelP2.setChecked(true);
                        radioApelP1.setChecked(false);
                        radioApelMangkir.setChecked(false);
                        radioApelTK.setChecked(false);
                        radioApelCuti.setChecked(false);
                        radioApelSakit.setChecked(false);
                    }
                });

                radioApelSakit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tipeTidakHadir = "S1";
                        radioApelSakit.setChecked(true);
                        radioApelP1.setChecked(false);
                        radioApelMangkir.setChecked(false);
                        radioApelTK.setChecked(false);
                        radioApelP2.setChecked(false);
                        radioApelCuti.setChecked(false);
                    }
                });

                radioApelMangkir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tipeTidakHadir = "MK";
                        radioApelMangkir.setChecked(true);
                        radioApelP1.setChecked(false);
                        radioApelCuti.setChecked(false);
                        radioApelTK.setChecked(false);
                        radioApelP2.setChecked(false);
                        radioApelSakit.setChecked(false);
                    }
                });

                radioApelTK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tipeTidakHadir = "TK";
                        radioApelTK.setChecked(true);
                        radioApelP1.setChecked(false);
                        radioApelMangkir.setChecked(false);
                        radioApelCuti.setChecked(false);
                        radioApelP2.setChecked(false);
                        radioApelSakit.setChecked(false);
                    }
                });

                btnKonfirmTdkHadir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tipeTidakHadir != null) {
                            dbhelper.updateapel_tidakhadir(nodocApel, selectedItemData, selectedEmp,
                                    tipeTidakHadir, etNoteDlgTdkHadir.getText().toString());
                            btnActionApel.performClick();
                            dlgTidakHadir.dismiss();

                            final SweetAlertDialog dlgStartOK = new SweetAlertDialog(activityContext, SweetAlertDialog.SUCCESS_TYPE);
                            dlgStartOK.setTitleText("Tidak Hadir").setContentText(dbhelper.get_empname(selectedEmp)).setConfirmText("OK").show();
                        }
                        else {
                            Toast.makeText(activityContext, "Pilih alasan tidak hadir", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                btnBackTdkHadir.setOnClickListener(view12 -> {
                    dlgTidakHadir.dismiss();
                    dlgMetodeAbsen.show();
                });
            }
        });
    }

    public Dialog showDlgAddMaterialService(TextView tvPlaceholderLvMaterial) {

        selectedMaterial = null;
        List<String> listMaterial;
        ArrayAdapter<String> adapterMaterial;

        dlgAddMaterialService = new Dialog(activityContext);
        dlgAddMaterialService.setCanceledOnTouchOutside(false);
        dlgAddMaterialService.setContentView(R.layout.dialog_addmaterial);
        dlgAddMaterialService.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowDlgMaterial = dlgAddMaterialService.getWindow();
        windowDlgMaterial.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView acAddMaterial = dlgAddMaterialService.findViewById(R.id.acDlgMaterialService);
        TextInputLayout inputLayoutQtyMaterial = dlgAddMaterialService.findViewById(R.id.inputLayoutDlgQtyMaterial);
        EditText etQtyMaterial = dlgAddMaterialService.findViewById(R.id.etDlgQtyMaterial);
        Button btnOkDlgMaterial = dlgAddMaterialService.findViewById(R.id.btnOkDlgMaterialService);
        Button btnBackDlgMaterial = dlgAddMaterialService.findViewById(R.id.btnBackDlgMaterialService);
        dlgAddMaterialService.show();

        btnBackDlgMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgAddMaterialService.dismiss();
                selectedMaterial = null;
                acAddMaterial.setText(null);
                etQtyMaterial.setText(null);
            }
        });

        listMaterial = dbhelper.get_listmaterialmd();
        adapterMaterial = new ArrayAdapter<>(activityContext, R.layout.spinnerlist, R.id.spinnerItem, listMaterial);
        acAddMaterial.setAdapter(adapterMaterial);

        acAddMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedMaterial = dbhelper.get_single_materialcode(adapterMaterial.getItem(position), 0);
                materialUOM = dbhelper.get_single_materialcode(adapterMaterial.getItem(position), 1);
                inputLayoutQtyMaterial.setSuffixText(dbhelper.get_single_materialcode(adapterMaterial.getItem(position), 1));

                // Hide keyboard after vehicle selected
                InputMethodManager keyboardMgr = (InputMethodManager) activityContext.getSystemService(activityContext.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acAddMaterial.getWindowToken(), 0);
            }
        });

        btnOkDlgMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedMaterial == null) {
                    Toast.makeText(activityContext, "Pilih Material!", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(etQtyMaterial.getText().toString().trim())) {
                    Toast.makeText(activityContext, "Isi kuantitas material!", Toast.LENGTH_LONG).show();
                }
                else {
                    dbhelper.insert_prosesperbaikan_detailmaterial(null, selectedMaterial,
                            etQtyMaterial.getText().toString(), materialUOM);
                    selectedMaterial = null;
                    acAddMaterial.setText(null);
                    etQtyMaterial.setText(null);
                    dlgAddMaterialService.dismiss();
                    tvPlaceholderLvMaterial.setVisibility(View.GONE);
                    loadListViewMaterial(activityContext);
                }

            }
        });

        return dlgAddMaterialService;
    }
}