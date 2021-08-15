package com.julong.longtech;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.julong.longtech.menuvehicle.VerifikasiGIS;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogHelper extends Dialog {

    private Context activityContext;

    public DialogHelper(Context context) {
        super(context);
        activityContext = context;
    }

    //Define Object
    private Dialog dialogInfo, dialogYesNo;
    DatabaseHelper dbhelper = new DatabaseHelper(getContext());
    Button btn_ok, btn_ok2, btn_no;
    TextView tvtitle, tvjuduldialog;
    public static ImageView imgLogoDlgHelperInfo, imgLogoDlgYesNo;
    public static String v_dlg_title, v_dlg_btn1, v_dlg_btn2, v_rtn_dlg_string;

    public void showDialogYesNo() {
        //Inisialisasi Object
        dialogYesNo = new Dialog(getContext());
        dialogYesNo.setContentView(R.layout.dialog_yesno);
        dialogYesNo.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowPreviewGambar = dialogYesNo.getWindow();
        windowPreviewGambar.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        btn_ok = dialogYesNo.findViewById(R.id.btn_dlg2_ok);
        btn_no = dialogYesNo.findViewById(R.id.btn_dlg2_no);
        tvtitle = dialogYesNo.findViewById(R.id.tv_dlg2_title);
        tvjuduldialog = dialogYesNo.findViewById(R.id.textView43);
        imgLogoDlgYesNo = dialogYesNo.findViewById(R.id.imgDlgYesNo);

        //Inisialisasi Variable Return
        v_rtn_dlg_string = "";

        //Inisialisasi Object Title/Text
        tvtitle.setText(v_dlg_title);
        btn_ok.setText(v_dlg_btn1);
        btn_no.setText(v_dlg_btn2);

        dialogYesNo.show();

        //Event Klik Tombol OK
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_rtn_dlg_string = "OK";
                dialogYesNo.dismiss();
            }
        });

        //Event Klik Tombol NO
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_rtn_dlg_string = "NO";
                dialogYesNo.dismiss();
            }
        });

        //Event Cancel Dialog
        dialogYesNo.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                v_rtn_dlg_string = "CANCEL";
            }
        });

    }

    public void showDialogInfo() {
        //Inisialisasi Object
        dialogInfo = new Dialog(getContext());
        dialogInfo.setContentView(R.layout.dialog_info);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowPreviewGambar = dialogInfo.getWindow();
        windowPreviewGambar.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        btn_ok = dialogInfo.findViewById(R.id.btnDialogInfo);
        tvtitle = dialogInfo.findViewById(R.id.tvDlgInfoTitle);
        tvjuduldialog = dialogInfo.findViewById(R.id.tvSystemNameDlgInfo);
        imgLogoDlgHelperInfo = dialogInfo.findViewById(R.id.imgLogoDlgInfo);

        tvjuduldialog.setText(LoginActivity.namasystem);

        //Inisialisasi Object Title/Text
        tvtitle.setText(v_dlg_title);
        btn_ok.setText(v_dlg_btn1);

        dialogInfo.show();

        //Event Klik Tombol OK
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_rtn_dlg_string = "OK";
                dialogInfo.dismiss();
            }
        });

    }

    public void showSummaryVerifikasiGIS() {
        Dialog dlgSelesaiVerifikasi = new Dialog(activityContext);
        dlgSelesaiVerifikasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgSelesaiVerifikasi.setContentView(R.layout.dialog_summarygis);
        dlgSelesaiVerifikasi.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowVerificationDone = dlgSelesaiVerifikasi.getWindow();
        windowVerificationDone.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView tvEstateSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvEstateSummaryGIS);
        TextView tvDivisiSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvDivisiSummaryGIS);
        TextView tvLokasiSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvBlokSummaryGIS);
        TextView tvKegiatanSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvActivitySummaryGIS);
        TextView tvHasilSummary = dlgSelesaiVerifikasi.findViewById(R.id.tvResultSummaryGIS);
        TextView tvTotalKoordinat = dlgSelesaiVerifikasi.findViewById(R.id.tvTotalKoordinatSummary);
        Button btnBackSummary = dlgSelesaiVerifikasi.findViewById(R.id.btnBackSummaryGIS);
        Button btnDoneSummary = dlgSelesaiVerifikasi.findViewById(R.id.btnDoneSummaryGIS);
        dlgSelesaiVerifikasi.show();
        btnBackSummary.setOnClickListener(view1 -> dlgSelesaiVerifikasi.dismiss());

        tvEstateSummary.setText(VerifikasiGIS.acKebunGIS.getText().toString());
        tvDivisiSummary.setText(VerifikasiGIS.acDivisiGIS.getText().toString());
        tvLokasiSummary.setText(VerifikasiGIS.acLokasiGIS.getText().toString());
        tvKegiatanSummary.setText(VerifikasiGIS.acKegiatanGIS.getText().toString());
        tvHasilSummary.setText(VerifikasiGIS.etHasilVerifikasi.getText() + " " + VerifikasiGIS.etSatuanKerjaGIS.getText().toString());
        tvTotalKoordinat.setText(dbhelper.get_count_totalkoordinatgis(VerifikasiGIS.nodocVerifikasiGIS));
        btnDoneSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgSelesaiVerifikasi.dismiss();
                dbhelper.updatestatus_verifikasigis(VerifikasiGIS.nodocVerifikasiGIS, VerifikasiGIS.etHasilVerifikasi.getText().toString());
                new SweetAlertDialog(activityContext, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Pekerjaan Selesai")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            Intent backIntent = new Intent();
                            ((Activity) activityContext).setResult(727, backIntent);
                            ((Activity) activityContext).finish();
                        }).setConfirmText("SELESAI").show();
            }
        });
    }

}