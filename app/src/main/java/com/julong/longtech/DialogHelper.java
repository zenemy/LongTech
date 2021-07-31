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
    final Dialog dialog = new Dialog(getContext());
    DatabaseHelper dbhelper = new DatabaseHelper(getContext());
    Button btn_ok, btn_ok2, btn_no;
    TextView tvtitle, tvjuduldialog;
    ImageView imgLogoDlg;
    public static String v_dlg_title, v_dlg_btn1, v_dlg_btn2, v_rtn_dlg_string;

    public void showDialogYesNo() {
        //Inisialisasi Object
        dialog.setContentView(R.layout.dialog_yesno);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        btn_ok = dialog.findViewById(R.id.btn_dlg2_ok);
        btn_no = dialog.findViewById(R.id.btn_dlg2_no);
        tvtitle = dialog.findViewById(R.id.tv_dlg2_title);
        tvjuduldialog = dialog.findViewById(R.id.textView43);
        imgLogoDlg = dialog.findViewById(R.id.imgDlgYesNo);

        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_companyimg(0), 0, dbhelper.get_companyimg(0).length);
            imgLogoDlg.setBackground(null);
            imgLogoDlg.setImageBitmap(compressedBitmap);
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

        dialog.show();

        //Event Klik Tombol OK
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_rtn_dlg_string = "OK";
                dialog.dismiss();
            }
        });

        //Event Klik Tombol NO
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_rtn_dlg_string = "NO";
                dialog.dismiss();
            }
        });

        //Event Cancel Dialog
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                v_rtn_dlg_string = "CANCEL";
            }
        });

    }

    public void showDialogInfo() {
        //Inisialisasi Object
        dialog.setContentView(R.layout.dialog_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        btn_ok = dialog.findViewById(R.id.btnDialogInfo);
        tvtitle = dialog.findViewById(R.id.tvDlgInfoTitle);
        tvjuduldialog = dialog.findViewById(R.id.tvSystemNameDlgInfo);
        imgLogoDlg = dialog.findViewById(R.id.imgLogoDlgInfo);

        //Ubah logo di dialog
        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_companyimg(0), 0, dbhelper.get_companyimg(0).length);
            imgLogoDlg.setBackground(null);
            imgLogoDlg.setImageBitmap(compressedBitmap);
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

        dialog.show();

        //Event Klik Tombol OK
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_rtn_dlg_string = "OK";
                dialog.dismiss();
            }
        });

    }

//    public void showDialogPassword() {
//        //Inisialisasi Object
//        dialog.setContentView(R.layout.dialog_password);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        btn_ok = dialog.findViewById(R.id.btn_dlg3_ok);
//        btn_no = dialog.findViewById(R.id.btn_dlg3_no);
//        tvtitle = dialog.findViewById(R.id.tv_dlg3_title);
//        et_text2 = dialog.findViewById(R.id.et_dlg3_password);
//
//        //Inisialisasi Variable Return
//        Activity_Login.v_rtn_dlg_string = "";
//        Activity_Login.v_rtn_dlg_string2 = "";
//
//        //Inisialisasi Object Title/Text
//        tvtitle.setText(Activity_Login.v_dlg_title);
//        btn_ok.setText(Activity_Login.v_dlg_btn1);
//        btn_no.setText(Activity_Login.v_dlg_btn2);
//
//        dialog.show();
//
//        //Event Klik Tombol OK
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(et_text2.getText())){
//                    et_text2.setError("Tidak Boleh Kosong, Silahkan Ulangi");
//                }
//                else {
//                    Activity_Login.v_rtn_dlg_string = "OK";
//                    Activity_Login.v_rtn_dlg_string2 = et_text2.getText().toString();
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        //Event Klik Tombol NO
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Activity_Login.v_rtn_dlg_string = "NO";
//                dialog.dismiss();
//            }
//        });
//
//        //Event Cancel Dialog
//        dialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                Activity_Login.v_rtn_dlg_string = "CANCEL";
//            }
//        });
//
//    }

}