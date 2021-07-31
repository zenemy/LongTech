package com.julong.longtech;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.menusetup.RegistrasiKaryawan;
import com.julong.longtech.menuvehicle.AdjustmentUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.DatabaseHelper.systemCode;
import static com.julong.longtech.DatabaseHelper.url_api;

public class LoginActivity extends AppCompatActivity {

    //==============================================================================================
    //Deklarasi Variable
    //==============================================================================================
    //String
    public static String v_dlg_title, v_dlg_btn1, v_dlg_btn2, v_rtn_dlg_string;
    public static String return_koneksi, url_data, checkuser, server_url;
    private List<String> languages;
    ArrayAdapter<String> adapterMenuLanguage;
    //Integer
    private int RESULT_LOAD_IMG = 100, v_data_proses;
    //ByteArray
    byte[] gambar;
    //END===========================================================================================

    //==============================================================================================
    //Deklarasi Object
    //==============================================================================================
    //Class / package / Helper
    DatabaseHelper dbhelper;
    HashPassword hashPassword;
    Dialog dialogregistrasi, dialoginfo, dialoggambar, dialoginsertpassword;
    DialogHelper dialogHelper;
    Handler handler = new Handler();

    //Object
    EditText et_username, et_password;
    Button btnlogin, btnDialogInfo;
    public static ImageView imgphoto, imagedialog;
    ImageView imglogo, imgbackground, imgtakephotoreg;
    TextView tv_lupasandi, tvversion, tvnamasystem, tvLoginHeader, tvKeteranganBahasa,
            tvDlgInfoTitle;
    AutoCompleteTextView imgChangeLanguage;
    //END===========================================================================================

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inisialisasi Obejct=======================================================================
        //Class / package / Helper
        dbhelper = new DatabaseHelper(this);
        hashPassword = new HashPassword(11);
        dialogHelper = new DialogHelper(this);
        //Object Text View
        tvLoginHeader = findViewById(R.id.tvHeaderLogin);
        tv_lupasandi = findViewById(R.id.txt_lupasandi);
        tvversion = findViewById(R.id.tvversion);
        tvnamasystem = findViewById(R.id.textView2);
        tvKeteranganBahasa = findViewById(R.id.tvKeteranganPilihanBahasa);
        //Object Edit Text
        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.etUserID);
        et_username.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //Object Button
        btnlogin = findViewById(R.id.btnlogin);
        //Obejct Foto / Backgroud
        imgphoto = findViewById(R.id.myPict2);
        imglogo = findViewById(R.id.myPict);
        imgbackground = findViewById(R.id.imgBgLogin);
        imgChangeLanguage = findViewById(R.id.imgChangeLanguage);
        //Dialog Info
        dialoginfo = new Dialog(this);
        dialoginfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoginfo.setContentView(R.layout.dialog_info);
        dialoginfo.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialoginfo.setCanceledOnTouchOutside(false);
        Window dialoginfoWindow = dialoginfo.getWindow();
        dialoginfoWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ImageView imgLogoDlgInfo = (ImageView) dialoginfo.findViewById(R.id.imgLogoDlgInfo);
        TextView tvSystemNameDlgInfo = (TextView) dialoginfo.findViewById(R.id.tvSystemNameDlgInfo);
        tvDlgInfoTitle = (TextView) dialoginfo.findViewById(R.id.tvDlgInfoTitle);
        btnDialogInfo = (Button) dialoginfo.findViewById(R.id.btnDialogInfo);
        btnDialogInfo.setOnClickListener(v -> dialoginfo.dismiss());
        // Dialog Gambar
        dialoggambar = new Dialog(this);
        dialoggambar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoggambar.setContentView(R.layout.dialog_gambar);
        dialoggambar.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowPreviewGambar = dialoggambar.getWindow();
        windowPreviewGambar.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        imagedialog = dialoggambar.findViewById(R.id.imgDlgFotoUser);
        TextView tvnik = dialoggambar.findViewById(R.id.tvNikDlgShowImg);
        TextView tvnama = dialoggambar.findViewById(R.id.tvNamaDlgShowImg);
        TextView tvJabatanDlgShowImg = dialoggambar.findViewById(R.id.tvJabatanDlgShowImg);
        TextView tvTelpDlgImg = dialoggambar.findViewById(R.id.tvTelpDlgImg);
        TextView tvDeptDlgImg = dialoggambar.findViewById(R.id.tvDeptDlgImg);

        //Perijinan=================================================================================
        try {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                //Requesting permission
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Setup Profile System======================================================================
        //tambahkan kondisi jika table company url kosong
        try {
            btnlogin.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor(dbhelper.get_tbl_username(26))));
            tvLoginHeader.setTextColor(Color.parseColor(dbhelper.get_tbl_username(26)));
            tv_lupasandi.setTextColor(Color.parseColor(dbhelper.get_tbl_username(26)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                et_password.setCompoundDrawableTintList(ColorStateList.valueOf(Color
                        .parseColor(dbhelper.get_tbl_username(26))));
            }

            if (Build.VERSION.SDK_INT >= 21) {
                Window statusbar = getWindow();
                statusbar.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                statusbar.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                statusbar.setStatusBarColor(Color.parseColor(dbhelper.get_tbl_username(26)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Nama System (jangan ambil dari username, ambil dari company info)
//        try {
//            tvnamasystem.setText(dbhelper.get_tbl_username(25));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Menampilkan Logo
//            try {
//                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(1), 0, dbhelper.get_gambar_user(1).length);
//                imglogo.setImageBitmap(compressedBitmap);
//                imglogo.setVisibility(View.VISIBLE);
//                imglogo.setBackground(null);
//                tvlogo.setVisibility(View.GONE);
//                //imgphoto.setForeground(null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        //Background Company
//            try {
//                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(2), 0, dbhelper.get_gambar_user(2).length);
//                imgbackground.setImageBitmap(compressedBitmap);
//                imgbackground.setVisibility(View.VISIBLE);
//                imgbackground.setBackground(null);
//                //imgphoto.setForeground(null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        //Setup Profile User========================================================================
        if (!dbhelper.get_count_tbl_username().equals("0")) {
            et_username.setText(dbhelper.get_tbl_username(1));

            if (dbhelper.get_tbl_username(26).equals("INDONESIA")) {
                tvKeteranganBahasa.setText("ID");
            } else if (dbhelper.get_tbl_username(26).equals("ENGLISH")) {
                tvKeteranganBahasa.setText("EN");
            } else if (dbhelper.get_tbl_username(26).equals("CHINA")) {
                tvKeteranganBahasa.setText("CN");
            }

            try {
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(),
                        0, dbhelper.get_gambar_user().length);
                imgphoto.setBackground(null);
                imgphoto.setImageBitmap(compressedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Event OnClick untuk Test Menu=============================================================
        imglogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(LoginActivity.this, AdjustmentUnit.class);
                startActivity(intent);*/

                v_dlg_title = "TEST DIALOG";
                v_dlg_btn1 = "OK";
                dialogHelper.showDialogInfo();
                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 500);
                        if (v_rtn_dlg_string.equals("CANCEL") ||
                                v_rtn_dlg_string.equals("NO")) {
                            v_rtn_dlg_string = "";
                            handler.removeCallbacks(this);
                        }
                        if (v_rtn_dlg_string.equals("OK")) {
                            v_rtn_dlg_string = "";
                            handler.removeCallbacks(this);
                            finish();
                        }
                    }
                }, 500);*/



            }
        });

        //Event OnClick Tampilkan User Profile======================================================
        imgphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (dbhelper.get_gambar_user() != null) {
                        dialoggambar.show();
                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                                dbhelper.get_gambar_user(), 0, dbhelper.get_gambar_user().length);
                        imagedialog.setImageBitmap(compressedBitmap);
                        imagedialog.setBackground(null);
                        tvnik.setText(dbhelper.get_tbl_username(8));
                        tvnama.setText(dbhelper.get_tbl_username(10));
                        tvJabatanDlgShowImg.setText(dbhelper.get_tbl_username(13));
                        tvTelpDlgImg.setText(dbhelper.get_tbl_username(9));
                        tvDeptDlgImg.setText(dbhelper.get_tbl_username(16));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Event OnClick Mengganti Bahasa============================================================
        imgChangeLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterMenuLanguage.getItem(position).toString().equals("INDONESIA")) {
                    tvKeteranganBahasa.setText("ID");
                    imgChangeLanguage.setText(null);
                    if (!dbhelper.get_count_tbl_username().equals("0")) {
                        dbhelper.update_userlanguage("INDONESIA");
                    }
                } else if (adapterMenuLanguage.getItem(position).toString().equals("CHINA")) {
                    tvKeteranganBahasa.setText("CN");
                    imgChangeLanguage.setText(null);
                    if (!dbhelper.get_count_tbl_username().equals("0")) {
                        dbhelper.update_userlanguage("CHINA");
                    }
                } else {
                    tvKeteranganBahasa.setText("EN");
                    imgChangeLanguage.setText(null);
                    if (!dbhelper.get_count_tbl_username().equals("0")) {
                        dbhelper.update_userlanguage("ENGLISH");
                    }
                }
            }
        });

        //????======================================================================================
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnlogin.performClick();
                }
                return false;
            }
        });

        //Event Update Version dan Company info=====================================================
        generate_version();
        generate_companyinfo();
        //generate_language();
    }

    public void eventClickLogin(View view) {

        dialoginsertpassword = new Dialog(this);
        dialoginsertpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoginsertpassword.setContentView(R.layout.dialog_insertpassword);
        dialoginsertpassword.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window windowInsertPassword = dialoginsertpassword.getWindow();
        windowInsertPassword.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        TextInputLayout layoutDlgInsertPassword = (TextInputLayout) dialoginsertpassword
                .findViewById(R.id.layoutDlgInsertPassword);
        TextInputLayout layoutDlgConfirmPassword = (TextInputLayout) dialoginsertpassword
                .findViewById(R.id.layoutDlgConfirmPassword);
        TextInputLayout layoutDlgRegistKey = (TextInputLayout) dialoginsertpassword
                .findViewById(R.id.layoutDlgRegistKey);
        EditText etDlgInsertPassword = (EditText) dialoginsertpassword
                .findViewById(R.id.etDlgInsertPassword);
        EditText etDlgConfirmPassword = (EditText) dialoginsertpassword
                .findViewById(R.id.etDlgConfirmPassword);
        EditText etDlgRegistKey = (EditText) dialoginsertpassword.findViewById(R.id.etDlgRegistKey);
        Button btnSubmitDlgPassword = (Button) dialoginsertpassword
                .findViewById(R.id.btnSubmitDlgChangePassword);
        Button btnBackDlgPassword = (Button) dialoginsertpassword
                .findViewById(R.id.btnBackDlgChangePassword);

        btnBackDlgPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDlgConfirmPassword.setText(null);
                etDlgInsertPassword.setText(null);
                etDlgRegistKey.setText(null);
                dialoginsertpassword.dismiss();
            }
        });

        if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Masukkan User ID").setConfirmText("OK").show();
        } else if (TextUtils.isEmpty(et_password.getText().toString().trim())
                && (et_username.getText().toString().equals(dbhelper.get_tbl_username(0)) ||
                et_username.getText().toString().equals(dbhelper.get_tbl_username(1)) ||
                et_username.getText().toString().equals(dbhelper.get_tbl_username(8)))) {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Masukkan Password").setConfirmText("OK").show();
        } else if (TextUtils.isEmpty(et_password.getText().toString().trim())
                && (!et_username.getText().toString().equals(dbhelper.get_tbl_username(0)) ||
                !et_username.getText().toString().equals(dbhelper.get_tbl_username(1)) ||
                !et_username.getText().toString().equals(dbhelper.get_tbl_username(8)))) {
            final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this,
                    SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText("Pengecekan Akun");
            pDialog.setCancelable(false);
            pDialog.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    url_data = url_api + "checklogin.php?userid=" + et_username.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject jsonBody = new JSONObject();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            url_data, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPost = new JSONObject(response.toString());
                                checkuser = jsonPost.getString("LOGIN");
                                if (checkuser.equals("INACTIVEUSER")) {
                                    tvDlgInfoTitle.setText("User ID tidak aktif, silahkan " +
                                            "hubungi HRD dan silahkan ulangi.");
                                    dbhelper.delete_data_username();
                                    et_username.setText(null);
                                    et_password.setText(null);
                                    imgphoto.setBackground(ContextCompat.getDrawable(
                                            LoginActivity.this, R.drawable.border_dialog));
                                    imgphoto.setImageDrawable(ContextCompat.getDrawable(
                                            LoginActivity.this, R.drawable.username));
                                    pDialog.dismissWithAnimation();
                                    dialoginfo.show();
                                    btnDialogInfo.setOnClickListener(v13 -> dialoginfo.dismiss());
                                } else if (checkuser.equals("CHANGEPASSWORD")) {
                                    pDialog.dismissWithAnimation();
                                    dialoginsertpassword.show();
                                    btnSubmitDlgPassword.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(etDlgInsertPassword
                                                    .getText().toString().trim())) {
                                                layoutDlgInsertPassword.setError(
                                                        "Input Password Pribadi");
                                                layoutDlgConfirmPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                            } else if (TextUtils.isEmpty(etDlgConfirmPassword
                                                    .getText().toString().trim())) {
                                                layoutDlgConfirmPassword.setError(
                                                        "Masukkan Kembali Password Pribadi");
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                            } else if (TextUtils.isEmpty(etDlgRegistKey
                                                    .getText().toString().trim())) {
                                                layoutDlgRegistKey.setError(
                                                        "Masukkan Kunci Registrasi");
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgConfirmPassword.setError(null);
                                            } else if (!etDlgInsertPassword.getText().toString()
                                                    .equals(etDlgConfirmPassword.getText()
                                                            .toString())) {
                                                layoutDlgInsertPassword.setError(
                                                        "Password does not match");
                                                layoutDlgConfirmPassword.setError(
                                                        "Password does not match");
                                                layoutDlgRegistKey.setError(null);
                                            } else {
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgConfirmPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                                RequestQueue requestQueueChangePassword =
                                                        Volley.newRequestQueue(
                                                                getApplicationContext());
                                                server_url = url_api + "dlgchangepassword.php";
                                                StringRequest stringRequest = new StringRequest(
                                                        Request.Method.POST, server_url,
                                                        new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonPostChangePassword =
                                                                    new JSONObject(response.toString());
                                                            if (jsonPostChangePassword
                                                                    .getString("CHANGEPASSWORD")
                                                                    .equals("SUCCESS")) {
                                                                et_password.setText(null);
                                                                dialoginsertpassword.dismiss();
                                                                new SweetAlertDialog(LoginActivity
                                                                        .this, SweetAlertDialog
                                                                        .SUCCESS_TYPE)
                                                                        .setTitleText(
                                                                                "Berhasil Merubah Password")
                                                                        .setContentText(
                                                                                "Silahkan Login Kembali")
                                                                        .setConfirmText("OK").show();
                                                            } else {
                                                                layoutDlgRegistKey.setError(
                                                                        "Kunci Registrasi tidak sesuai");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        requestQueueChangePassword.stop();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(LoginActivity.this,
                                                                "Error ...", Toast.LENGTH_LONG)
                                                                .show();
                                                        error.printStackTrace();
                                                        requestQueueChangePassword.stop();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams()
                                                            throws AuthFailureError {
                                                        Map<String, String> params =
                                                                new HashMap<String, String>();
                                                        params.put("userid", et_username
                                                                .getText().toString());
                                                        params.put("password", etDlgConfirmPassword
                                                                .getText().toString());
                                                        params.put("registkey", etDlgRegistKey
                                                                .getText().toString());
                                                        return params;
                                                    }
                                                };
                                                requestQueueChangePassword.add(stringRequest);
                                            }
                                        }
                                    });
                                    btnBackDlgPassword.setOnClickListener(v14 ->
                                            dialoginsertpassword.dismiss());
                                } else if (checkuser.equals("INSERTPASSWORD")) {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(LoginActivity.this,
                                            SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Silahkan Masukkan Password")
                                            .setConfirmText("OK").show();
                                } else if (checkuser.equals("NOUSER")) {
                                    pDialog.dismissWithAnimation();
                                    tvDlgInfoTitle.setText("User ID tidak terdaftar, " +
                                            "silahkan hubungi HRD dan silahkan ulangi.");
                                    et_username.setText(null);
                                    et_password.setText(null);
                                    dialoginfo.show();
                                    btnDialogInfo.setOnClickListener(v1 -> dialoginfo.dismiss());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Masukkan Password")
                                    .setConfirmText("OK").show();
                        }
                    });
                    queue.add(stringRequest);
                    handler.removeCallbacks(this);
                }
            }, 2000);
        } else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this,
                    SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#305031"));
            pDialog.setTitleText("Pengecekan Akun");
            pDialog.setCancelable(false);
            pDialog.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    url_data = url_api + "checklogin.php?userid=" +
                            et_username.getText().toString() + "&password=" +
                            et_password.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject jsonBody = new JSONObject();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            url_data, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPost = new JSONObject(response.toString());
                                checkuser = jsonPost.getString("LOGIN");
                                if (checkuser.equals("INACTIVEUSER")) {
                                    tvDlgInfoTitle.setText("User ID tidak aktif, silahkan " +
                                            "hubungi HRD dan silahkan ulangi.");
                                    dbhelper.delete_data_username();
                                    et_username.setText(null);
                                    et_password.setText(null);
                                    imgphoto.setBackground(ContextCompat.getDrawable(LoginActivity
                                            .this, R.drawable.border_dialog));
                                    imgphoto.setImageDrawable(ContextCompat.getDrawable(LoginActivity
                                            .this, R.drawable.username));
                                    pDialog.dismissWithAnimation();
                                    dialoginfo.show();
                                    btnDialogInfo.setOnClickListener(v13 -> dialoginfo.dismiss());
                                } else if (checkuser.equals("CHANGEPASSWORD")) {
                                    pDialog.dismissWithAnimation();
                                    dialoginsertpassword.show();
                                    btnSubmitDlgPassword.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(etDlgInsertPassword.getText()
                                                    .toString().trim())) {
                                                layoutDlgInsertPassword.setError(
                                                        "Input Password Pribadi");
                                                layoutDlgConfirmPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                            } else if (TextUtils.isEmpty(etDlgConfirmPassword
                                                    .getText().toString().trim())) {
                                                layoutDlgConfirmPassword.setError(
                                                        "Masukkan Kembali Password Pribadi");
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                            } else if (TextUtils.isEmpty(etDlgRegistKey.getText()
                                                    .toString().trim())) {
                                                layoutDlgRegistKey.setError(
                                                        "Masukkan Kunci Registrasi");
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgConfirmPassword.setError(null);
                                            } else if (!etDlgInsertPassword.getText().toString()
                                                    .equals(etDlgConfirmPassword.getText()
                                                            .toString())) {
                                                layoutDlgInsertPassword.setError(
                                                        "Password does not match");
                                                layoutDlgConfirmPassword.setError(
                                                        "Password does not match");
                                                layoutDlgRegistKey.setError(null);
                                            } else {
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgConfirmPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                                RequestQueue requestQueueChangePassword =
                                                        Volley.newRequestQueue(
                                                                getApplicationContext());
                                                server_url = url_api + "dlgchangepassword.php";
                                                StringRequest stringRequest =
                                                        new StringRequest(Request.Method.POST,
                                                                server_url,
                                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonPostChangePassword =
                                                                    new JSONObject(
                                                                            response.toString());
                                                            if (jsonPostChangePassword
                                                                    .getString("CHANGEPASSWORD")
                                                                    .equals("SUCCESS")) {
                                                                et_password.setText(null);
                                                                dialoginsertpassword.dismiss();
                                                                new SweetAlertDialog(
                                                                        LoginActivity.this,
                                                                        SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setTitleText("" +
                                                                                "Berhasil Merubah Password")
                                                                        .setContentText(
                                                                                "Silahkan Login Kembali")
                                                                        .setConfirmText("OK")
                                                                        .show();
                                                            } else {
                                                                layoutDlgRegistKey.setError(
                                                                        "Kunci Registrasi tidak sesuai");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        requestQueueChangePassword.stop();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(LoginActivity.this,
                                                                "Error ...", Toast.LENGTH_LONG)
                                                                .show();
                                                        error.printStackTrace();
                                                        requestQueueChangePassword.stop();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams()
                                                            throws AuthFailureError {
                                                        Map<String, String> params =
                                                                new HashMap<String, String>();
                                                        params.put("userid", et_username
                                                                .getText().toString());
                                                        params.put("password", etDlgConfirmPassword
                                                                .getText().toString());
                                                        params.put("registkey", etDlgRegistKey
                                                                .getText().toString());
                                                        return params;
                                                    }
                                                };
                                                requestQueueChangePassword.add(stringRequest);
                                            }
                                        }
                                    });
                                    btnBackDlgPassword.setOnClickListener(v12 ->
                                            dialoginsertpassword.dismiss());
                                } else if (checkuser.equals("SUCCESS") ||
                                        checkuser.equals("CONFIRM")) {
                                    dbhelper.delete_data_username();
                                    et_password.setText(null);

                                    dbhelper.insert_tblusername(
                                            jsonPost.getString("USERID"),
                                            jsonPost.getString("USERNAME"),
                                            jsonPost.getString("USERTYPE"),
                                            jsonPost.getString("POSITION_NAME"),
                                            jsonPost.getString("COMP_ID"),
                                            jsonPost.getString("SITE_ID"),
                                            jsonPost.getString("DEPTCODE"),
                                            jsonPost.getString("DIVCODE"),
                                            jsonPost.getString("GANGCODE"),
                                            jsonPost.getString("ANCAKCODE"),
                                            jsonPost.getString("SHIFTCODE"),
                                            jsonPost.getString("NO_TELP"),
                                            jsonPost.getString("EMAILUSER"),
                                            jsonPost.getString("EMPNAME"),
                                            jsonPost.getString("EMPCODE"),
                                            jsonPost.getString("USERPASSWORD"),
                                            jsonPost.getString("USERLANGUAGE"));

                                    if (!jsonPost.getString("BLOBUSERPHOTO").equals("")) {
                                        byte[] decodedUserPhoto = Base64.decode(
                                                jsonPost.getString("BLOBUSERPHOTO"),
                                                Base64.DEFAULT);
                                        dbhelper.update_userpphoto(decodedUserPhoto);
                                    }

                                    try {
                                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                                                dbhelper.get_gambar_user(), 0,
                                                dbhelper.get_gambar_user().length);
                                        imgphoto.setBackground(null);
                                        imgphoto.setImageBitmap(compressedBitmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        imgphoto.setBackground(ContextCompat.getDrawable(
                                                LoginActivity.this, R.drawable.border_dialog));
                                        imgphoto.setImageDrawable(ContextCompat.getDrawable(
                                                LoginActivity.this, R.drawable.username));
                                    }

                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);

                                } else if (checkuser.equals("WRONGPASSWORD")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(LoginActivity.this,
                                            SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Password Salah")
                                            .setConfirmText("OK").show();
                                } else {
                                    pDialog.dismiss();
                                    tvDlgInfoTitle.setText("User ID tidak terdaftar, " +
                                            "silahkan hubungi HRD dan silahkan ulangi.");
                                    et_username.setText(null);
                                    et_password.setText(null);
                                    dialoginfo.show();
                                    btnDialogInfo.setOnClickListener(v1 -> dialoginfo.dismiss());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (et_username.getText().toString().equals(
                                    dbhelper.get_tbl_username(0)) ||
                                    et_username.getText().toString()
                                            .equals(dbhelper.get_tbl_username(1)) ||
                                    et_username.getText().toString()
                                            .equals(dbhelper.get_tbl_username(8))) {
                                boolean checkPassword = hashPassword.CheckPassword(
                                        et_password.getText().toString(),
                                        dbhelper.get_tbl_username(4));
                                if (checkPassword == true) {
                                    pDialog.dismiss();
                                    et_password.setText(null);
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(LoginActivity.this,
                                            SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Password Salah")
                                            .setConfirmText("OK").show();
                                }
                            } else {
                                return_koneksi = "";
                                v_dlg_title = "Harap Periksa Jaringan Internet Anda";
                                v_dlg_btn1 = "OK";
                                dialogHelper.showDialogInfo();
                            }
                        }
                    });
                    queue.add(stringRequest);
                    handler.removeCallbacks(this);
                }
            }, 2000);
        }
    }

    //Function Check Version========================================================================
    void generate_version() {
        RequestQueue queue = Volley.newRequestQueue(this);
        return_koneksi = null;
        if (systemCode != null || systemCode.equals("")) {
            url_data = url_api + "dsi_version.php?systemcode=" + systemCode;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonPost = new JSONObject(response.toString());
                        return_koneksi = "OK";
                        dbhelper.insert_tbl_version(
                                jsonPost.getString("VERSIONNUMBER"),
                                jsonPost.getString("VERSIONNAME"),
                                jsonPost.getString("TDATE"),
                                jsonPost.getString("REMARKS"),
                                jsonPost.getString("link_download"));
                        String status_update;
                        try {
                            if (!dbhelper.get_tbl_version(2).equals(dbhelper.get_tbl_version(6))) {
                                status_update = "NEW";
                                dbhelper.updatestatusversion(status_update);
                                tvversion.setTextColor(0xFFD51616);
                                tvversion.setText("Update System Tersedia");
                                tvDlgInfoTitle.setText("Update System Tersedia\n\n" +
                                        dbhelper.get_tbl_version(7) + "\n[" +
                                        dbhelper.get_tbl_version(8) + "]\n\n" +
                                        dbhelper.get_tbl_version(9));
                                dialoginfo.show();

                                tvversion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tvDlgInfoTitle.setText("Update System Tersedia\n\n" +
                                                dbhelper.get_tbl_version(7) + "\n[" +
                                                dbhelper.get_tbl_version(8) + "]\n\n" +
                                                dbhelper.get_tbl_version(9));
                                        dialoginfo.show();
                                    }
                                });
                            } else {
                                status_update = "NO";
                                dbhelper.updatestatusversion(status_update);
                                tvversion.setTextColor(0xff305031);
                                tvversion.setText(dbhelper.get_tbl_version(3));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    return_koneksi = null;
                    tvDlgInfoTitle.setText("Anda Berada Pada Mode Offline");
                    dialoginfo.show();
                    String status_update;
                    status_update = "NO";
                    dbhelper.updatestatusversion(status_update);
                    tvversion.setTextColor(0xff305031);
                    tvversion.setText(dbhelper.get_tbl_version(3));
                }
            });
            queue.add(stringRequest);
        }
    }

    //Function Update Company Setting===============================================================
    void generate_companyinfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        if (systemCode != null || systemCode.equals("")) {
            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()).format(new Date());
            dbhelper.get_companyinfo(14);
            url_data = url_api + "fetchdata/getcompanyinfo.php?systemcode=" + systemCode
                    + "&sysdate=" + currentDateTime;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonPost = new JSONObject(response.toString());
                        try {
                            //Jika dapat perintah update, update table company: background logo dan nama system
                            if (jsonPost.getString("RESPON").equals("UPDATECOMPANY")) {
                                byte[] decodedLogoComp = Base64.decode(jsonPost
                                        .getString("LOGOCOMP"), Base64.DEFAULT);
                                byte[] decodedBgComp = Base64.decode(jsonPost
                                        .getString("BACKGROUNDIMG"), Base64.DEFAULT);

                                dbhelper.insert_companyinfo(jsonPost.getString("GROUPCOMPANYCODE"),
                                        decodedLogoComp, decodedBgComp,
                                        jsonPost.getString("SYSTEMNAME"),
                                        jsonPost.getString("URLAPI"),
                                        jsonPost.getString("PICNAME"),
                                        jsonPost.getString("PICEMAIL"),
                                        jsonPost.getString("PICNOTELP"),
                                        jsonPost.getString("ADDRESS"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
    }

    //Function Update Company Setting===============================================================
    void generate_language() {
        url_data = url_api + "dsi_language.php";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("DATALANGUAGE");

                            int i = 0;
                            if (dbhelper.count_dataGS01("GS02", "LANGUAGE").equals("0")) {
                                while (i < jsonArray.length()) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    dbhelper.insert_dataGS01(
                                            jsonObject1.getString("GROUPPARAMCODE"),
                                            jsonObject1.getString("GROUPPARAMDESC"),
                                            jsonObject1.getString("PARAMETERCODE"),
                                            jsonObject1.getString("PARAMETERDESC"),
                                            jsonObject1.getString("SEQ_NO"));
                                    i++;
                                }
                                languages = dbhelper.get_loginlanguage();
                                adapterMenuLanguage = new ArrayAdapter<String>(LoginActivity.this,
                                        R.layout.spinnerlist, R.id.spinnerItem, languages);
                                adapterMenuLanguage.setDropDownViewResource(R.layout.spinnerlist);
                                imgChangeLanguage.setAdapter(adapterMenuLanguage);
                                imgChangeLanguage.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        imgChangeLanguage.showDropDown();
                                        return false;
                                    }
                                });
                            } else {
                                languages = dbhelper.get_loginlanguage();
                                adapterMenuLanguage = new ArrayAdapter<String>(LoginActivity.this,
                                        R.layout.spinnerlist, R.id.spinnerItem, languages);
                                adapterMenuLanguage.setDropDownViewResource(R.layout.spinnerlist);
                                imgChangeLanguage.setAdapter(adapterMenuLanguage);
                                imgChangeLanguage.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        imgChangeLanguage.showDropDown();
                                        return false;
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    //Function Tombol Back==========================================================================
    @Override
    public void onBackPressed() {
        v_dlg_title = "Apakah anda yakin akan keluar?";
        v_dlg_btn1 = "YA";
        v_dlg_btn2 = "TIDAK";
        dialogHelper.showDialogYesNo();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                if (v_rtn_dlg_string.equals("CANCEL") ||
                        v_rtn_dlg_string.equals("NO")) {
                    v_rtn_dlg_string = "";
                    handler.removeCallbacks(this);
                }
                if (v_rtn_dlg_string.equals("OK")) {
                    v_rtn_dlg_string = "";
                    handler.removeCallbacks(this);
                    finish();
                }
            }
        }, 500);
    }
}