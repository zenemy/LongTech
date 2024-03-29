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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
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
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menuhcm.ApelPagiAdapter;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.NewMethodCarLog;
import com.julong.longtech.menuvehicle.NewMethodRKH;
import com.julong.longtech.menuvehicle.VerifikasiGIS;
import com.julong.longtech.menuworkshop.LaporanPerbaikan;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.menuhcm.ApelPagi.REQUEST_IMAGE_CAPTURE;
import static com.julong.longtech.menuhcm.ApelPagi.btnActionApel;
import static com.julong.longtech.menuhcm.ApelPagi.dataProcess;
import static com.julong.longtech.menuhcm.ApelPagi.nodocApel;
import static com.julong.longtech.menuhcm.ApelPagi.selectedEmp;
import static com.julong.longtech.menuhcm.ApelPagi.selectedItemData;
import static com.julong.longtech.menuhcm.ApelPagi.selectedUnit;
import static com.julong.longtech.menuworkshop.LaporanPerbaikan.loadListViewMaterial;

public class DialogHelper extends Dialog {

    private Context activityContext;
    private Dialog dialogInfo;
    DatabaseHelper dbhelper;

    // Dialog Apel
    public static Dialog dlgTidakHadir;
    String tipeTidakHadir;

    // Dialog GIS
    private Dialog dlgSelesaiVerifikasi;

    //Dialog Workshop
    private Dialog dlgAddMaterialService;
    private String selectedMaterial, materialUOM;

    //Dialog RKH
    private Dialog dlgAddDetailRKH;
    private String selectedEstateRKH, selectedDivisionRKH, selectedBlockRKH, selectedActivityRKH;
    private List<String> listDivisionNameRKH, listDivisionCodeRKH, listBlokRKH;
    ArrayAdapter<String> adapterDivisionRKH, adapterBlok;

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

        // Submit material
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

    public Dialog showDlgBriefingTdkHadir(String empCode, String empName, String positionCode,
                                          String vehicleCode, String latApel, String longApel) {

        dlgTidakHadir = new Dialog(activityContext);
        dlgTidakHadir.setContentView(R.layout.dialog_apeltdkhadir);
        dlgTidakHadir.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowTdkHadir = dlgTidakHadir.getWindow();
        windowTdkHadir.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView tvHeaderDlgTdkHadir = dlgTidakHadir.findViewById(R.id.tvHeaderDlgTdkHadirApel);
        Button btnBackDlgTdkHadir = dlgTidakHadir.findViewById(R.id.btnBackDlgApelTdkHadir);
        Button btnConfirmTdkHadir = dlgTidakHadir.findViewById(R.id.btnApelConfirmTdkHadir);
        RadioButton radioApelCuti = dlgTidakHadir.findViewById(R.id.radioApelCuti);
        RadioButton radioApelP1 = dlgTidakHadir.findViewById(R.id.radioApelP1);
        RadioButton radioApelTK = dlgTidakHadir.findViewById(R.id.radioApelTK);
        RadioButton radioApelSakit = dlgTidakHadir.findViewById(R.id.radioApelSakit);
        RadioButton radioApelP2 = dlgTidakHadir.findViewById(R.id.radioApelP2);
        RadioButton radioApelMangkir = dlgTidakHadir.findViewById(R.id.radioApelMangkir);
        EditText etNoteDlgTdkHadir = dlgTidakHadir.findViewById(R.id.etNoteDlgTdkHadir);

        tvHeaderDlgTdkHadir.setText(empName);
        btnBackDlgTdkHadir.setOnClickListener(view1 -> dlgTidakHadir.dismiss());

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

        btnConfirmTdkHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipeTidakHadir != null) {
                    dlgTidakHadir.dismiss();

                    dbhelper.insert_briefing_anggota(empCode, positionCode, vehicleCode, tipeTidakHadir,
                            tipeTidakHadir, etNoteDlgTdkHadir.getText().toString(), latApel, longApel);
                    ApelPagi.adapterApel.notifyDataSetChanged();

                    final SweetAlertDialog dlgOK = new SweetAlertDialog(activityContext, SweetAlertDialog.SUCCESS_TYPE);
                    dlgOK.setTitleText("Tidak Hadir").setContentText(empName).setConfirmText("OK").show();
                }
                else {
                    Toast.makeText(activityContext, "Pilih alasan tidak hadir", Toast.LENGTH_LONG).show();
                }
            }
        });

        dlgTidakHadir.show();

        return dlgTidakHadir;
    }

    public Dialog addLocationActivityRKH(String vehicleType, TextView tvPlaceHolderDetailRKH) {

        List<String> listActivity, listEstateName, listEstateCode, listBlok;
        ArrayAdapter<String> adapterActivity, adapterEstate;

        dlgAddDetailRKH = new Dialog(activityContext);
        dlgAddDetailRKH.setContentView(R.layout.dialog_inputrincianrkh);
        dlgAddDetailRKH.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowTdkHadir = dlgAddDetailRKH.getWindow();
        windowTdkHadir.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView acEstate = dlgAddDetailRKH.findViewById(R.id.acEstateRincianRKH);
        AutoCompleteTextView acDivision = dlgAddDetailRKH.findViewById(R.id.acDivisionRincianRKH);
        AutoCompleteTextView acWorkLocation = dlgAddDetailRKH.findViewById(R.id.acLocationRincianRKH);
        AutoCompleteTextView acWorkActivity = dlgAddDetailRKH.findViewById(R.id.acActivityRincianRKH);
        TextInputLayout inputLayoutTarget = dlgAddDetailRKH.findViewById(R.id.inputLayoutTargetRKH);
        EditText etTargetRincian = dlgAddDetailRKH.findViewById(R.id.etTargetRincianRKH);
        Button btnDlgDismiss = dlgAddDetailRKH.findViewById(R.id.btnDlgCancelRincianRKH);
        Button btnDlgSimpan = dlgAddDetailRKH.findViewById(R.id.btnDlgSimpanRincianRKH);

        btnDlgDismiss.setOnClickListener(view -> dlgAddDetailRKH.dismiss());
        acEstate.setKeyListener(null);
        acDivision.setKeyListener(null);

        // Start setting up dropdown data
        listActivity = dbhelper.get_transportactivity(vehicleType);
        adapterActivity = new ArrayAdapter<>(activityContext, R.layout.spinnerlist, R.id.spinnerItem, listActivity);
        acWorkActivity.setAdapter(adapterActivity);

        listEstateName = dbhelper.get_itemkebun(1);
        listEstateCode = dbhelper.get_itemkebun(0);
        adapterEstate = new ArrayAdapter<String>(activityContext, R.layout.spinnerlist, R.id.spinnerItem, listEstateName);
        acEstate.setAdapter(adapterEstate);

        acEstate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedEstateRKH = listEstateCode.get(position);
                selectedDivisionRKH = null;
                selectedBlockRKH = null;
                acDivision.setText(null);
                acWorkLocation.setText(null);
                acDivision.setAdapter(null);

                listDivisionNameRKH = dbhelper.get_itemdivisi(selectedEstateRKH, 1);
                listDivisionCodeRKH = dbhelper.get_itemdivisi(selectedEstateRKH, 0);
                adapterDivisionRKH = new ArrayAdapter<>(activityContext, R.layout.spinnerlist, R.id.spinnerItem, listDivisionNameRKH);
                acDivision.setAdapter(adapterDivisionRKH);
            }
        });

        acDivision.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDivisionRKH = listDivisionCodeRKH.get(position);
                selectedBlockRKH = null;
                acWorkLocation.setText(null);
                acWorkLocation.setAdapter(null);

                listBlokRKH = dbhelper.get_fieldcrop_filtered(selectedEstateRKH, selectedDivisionRKH, 1);
                adapterBlok = new ArrayAdapter<>(activityContext, R.layout.spinnerlist, R.id.spinnerItem, listBlokRKH);
                acWorkLocation.setAdapter(adapterBlok);
            }
        });

        acWorkLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedBlockRKH = dbhelper.get_singlelokasiCode(adapterBlok.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) activityContext
                        .getSystemService(activityContext.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acWorkActivity.getWindowToken(), 0);
            }
        });

        acWorkActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedActivityRKH = dbhelper.get_singlekegiatancode(adapterActivity.getItem(position));
                inputLayoutTarget.setSuffixText(dbhelper.get_singlekegiatanname(selectedActivityRKH, 1));
                InputMethodManager keyboardMgr = (InputMethodManager) activityContext
                        .getSystemService(activityContext.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acWorkActivity.getWindowToken(), 0);
            }
        });
        // Setting up dropdown data done

        btnDlgSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Checking empty text field first
                if (selectedBlockRKH == null) {
                    Toast.makeText(activityContext, "Pilih Lokasi Kerja!", Toast.LENGTH_LONG).show();
                }
                else if (selectedActivityRKH == null) {
                    Toast.makeText(activityContext, "Pilih Kegiatan Kerja!", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(etTargetRincian.getText().toString().trim())) {
                    Toast.makeText(activityContext, "Isi Target Kerja!", Toast.LENGTH_LONG).show();
                }
                else {
                    dbhelper.insert_newrkh_detail(NewMethodRKH.rkhWorkdate, selectedEstateRKH, selectedDivisionRKH,
                            selectedBlockRKH, selectedActivityRKH, etTargetRincian.getText().toString(),
                            dbhelper.get_singlekegiatanname(selectedActivityRKH, 1));

                    selectedEstateRKH = null;
                    selectedDivisionRKH = null;
                    selectedBlockRKH = null;
                    selectedActivityRKH = null;

                    acEstate.setText(null);
                    acDivision.setText(null);
                    acWorkActivity.setText(null);
                    etTargetRincian.setText(null);
                    dlgAddDetailRKH.dismiss();

                    tvPlaceHolderDetailRKH.setVisibility(View.GONE);
                    NewMethodRKH.loadListViewDetailRKH(activityContext);
                }

            }
        });

        dlgAddDetailRKH.show();
        return dlgAddDetailRKH;

    }


}