package com.julong.longtech;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogHelper extends Dialog {

    public DialogHelper(Context context) {
        super(context);
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
        btn_ok = dialogYesNo.findViewById(R.id.btn_dlg2_ok);
        btn_no = dialogYesNo.findViewById(R.id.btn_dlg2_no);
        tvtitle = dialogYesNo.findViewById(R.id.tv_dlg2_title);
        tvjuduldialog = dialogYesNo.findViewById(R.id.textView43);
        imgLogoDlgYesNo = dialogYesNo.findViewById(R.id.imgDlgYesNo);

        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_companyimg(0), 0, dbhelper.get_companyimg(0).length);
            imgLogoDlgYesNo.setImageBitmap(compressedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            btn_ok.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
//            btn_no.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
//            tvjuduldialog.setTextColor(Color.parseColor(dbhelper.get_tbl_username(26)));
//            if (dbhelper.get_tbl_username(25).equals("0")){
//                tvjuduldialog.setText("NAMA SYSTEM");
//            }
//            else {
//                tvjuduldialog.setText(dbhelper.get_tbl_username(25));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            tvjuduldialog.setText("NAMA SYSTEM");
//        }
//
//
//        try {
//            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(1), 0, dbhelper.get_gambar_user(1).length);
//            imgdialog.setImageBitmap(compressedBitmap);
//            //imgphoto.setForeground(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


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
        btn_ok = dialogInfo.findViewById(R.id.btnDialogInfo);
        tvtitle = dialogInfo.findViewById(R.id.tvDlgInfoTitle);
        tvjuduldialog = dialogInfo.findViewById(R.id.tvSystemNameDlgInfo);
        imgLogoDlgHelperInfo = dialogInfo.findViewById(R.id.imgLogoDlgInfo);

        //Ubah logo di dialog
        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_companyimg(0), 0, dbhelper.get_companyimg(0).length);
            imgLogoDlgHelperInfo.setImageBitmap(compressedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            btn_ok.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
//            tvjuduldialog.setTextColor(Color.parseColor(dbhelper.get_tbl_username(26)));
//            tvjuduldialog.setText(dbhelper.get_tbl_username(25));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(1), 0, dbhelper.get_gambar_user(1).length);
//            imgdialog.setImageBitmap(compressedBitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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

}