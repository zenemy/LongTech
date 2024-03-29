package com.julong.longtech;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.julong.longtech.menuinventory.PenerimaanBBM;
import com.julong.longtech.menusetup.AESEnkrip;
import com.julong.longtech.menusetup.DownloadData;
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

    public static String return_koneksi, checkuser, namasystem;
    public static ImageView imgphoto, imagedialog;
    public static Bitmap bitmaplogosystem;

    private String url_data;
    private List<String> languages;
    ArrayAdapter<String> adapterMenuLanguage;

    //Class / package / Helper
    DatabaseHelper dbhelper;
    HashPassword hashPassword;
    DialogHelper dialogHelper;
    Handler handler = new Handler();
    Dialog dialoginfo, dialoggambar, dialoginsertpassword;
    SweetAlertDialog pDialog;

    //Object
    TextView tvversion, tvnamasystem, tvKeteranganBahasa, tvDlgInfoTitle;
    EditText et_username, et_password;
    Button btnlogin, btnDialogInfo;
    ImageView imglogo;
    AutoCompleteTextView imgChangeLanguage;
    LinearLayout layoutBgLogin;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbhelper = new DatabaseHelper(this);
        hashPassword = new HashPassword(11);
        dialogHelper = new DialogHelper(this);

        //Declare design ID
        tvversion = findViewById(R.id.tvversion);
        tvnamasystem = findViewById(R.id.textView2);
        tvKeteranganBahasa = findViewById(R.id.tvKeteranganPilihanBahasa);
        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.etUserID);
        et_username.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        btnlogin = findViewById(R.id.btnlogin);
        imgphoto = findViewById(R.id.myPict2);
        imglogo = findViewById(R.id.myPict);
        layoutBgLogin = findViewById(R.id.layoutBgLogin);
        imgChangeLanguage = findViewById(R.id.imgChangeLanguage);

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

        // Requesting permission
        try {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
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

        // Setup Profile System
        try {
            BitmapFactory.Options myOptions = new BitmapFactory.Options();
            myOptions.inDither = true;
            myOptions.inScaled = false;
            myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            myOptions.inDither = false;
            myOptions.inPurgeable = true;
            Bitmap preparedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bgoil, myOptions);
            Drawable background = new BitmapDrawable(preparedBitmap);
            layoutBgLogin.setBackgroundDrawable(background);

            try {
                Bitmap profileuser = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(),
                        0, dbhelper.get_gambar_user().length);
                imgphoto.setBackground(null);
                imgphoto.setImageBitmap(profileuser);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Pengecekan Akun");
        pDialog.setCancelable(false);

        //Setup Bahasa
        if (!dbhelper.get_count_tbl_username().equals("0")) {

            languages = dbhelper.get_loginlanguage();
            adapterMenuLanguage = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, languages);
            adapterMenuLanguage.setDropDownViewResource(R.layout.spinnerlist);
            imgChangeLanguage.setAdapter(adapterMenuLanguage);

            imgChangeLanguage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    imgChangeLanguage.showDropDown();
                    return false;
                }
            });

            et_username.setText(dbhelper.get_tbl_username(1));

            if (getIntent().hasExtra("currentuser")) {
                Bundle bundle = getIntent().getExtras();
                et_username.setText(bundle.getString("currentuser"));
                dbhelper.delete_data_username();
                pDialog.dismiss();
            }
            else {
                pDialog.show();
                try {
                    String[] decryptedValue = AESEnkrip.decrypt(dbhelper.get_tbl_username(5)).split(";");
                    boolean checkDecryptPassword = hashPassword.CheckPassword(decryptedValue[1], dbhelper.get_tbl_username(4));

                    if (decryptedValue[0].equals(dbhelper.get_tbl_username(0)) && checkDecryptPassword) {
                        et_username.setText(dbhelper.get_tbl_username(1));
                        et_password.setText(decryptedValue[1]);
                        btnlogin.performClick();
                    } else {
                        pDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }

        }

        //Event OnClick Tampilkan User Profile
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

        //Event OnClick Mengganti Bahasa
        imgChangeLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterMenuLanguage.getItem(position).equals("INDONESIA")) {
                    tvKeteranganBahasa.setText("ID");
                    imgChangeLanguage.setText(null);
                    if (!dbhelper.get_count_tbl_username().equals("0")) {
                        dbhelper.update_userlanguage("INDONESIA");
                    }
                } else if (adapterMenuLanguage.getItem(position).equals("CHINA")) {
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

        // Kalau click enter, ngeclick button login
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnlogin.performClick();
                }
                return false;
            }
        });

        //Event Update Version dan Company info
        generate_companyinfo();
        generate_version();
        generate_language();
    }

    public void eventClickLogin(View view) {

        if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Masukkan User ID").setConfirmText("OK").show();
        }
        else if (TextUtils.isEmpty(et_password.getText().toString().trim())
            && (et_username.getText().toString().equals(dbhelper.get_tbl_username(0))
            || TextUtils.isEmpty(et_password.getText().toString().trim())
                && et_username.getText().toString().equals(dbhelper.get_tbl_username(1))
            || TextUtils.isEmpty(et_password.getText().toString().trim())
                && et_username.getText().toString().equals(dbhelper.get_tbl_username(8)))) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Masukkan Password").setConfirmText("OK").show();
        }
        else {
            pDialog.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    url_data = url_api + "checklogin.php?userid=" +
                            et_username.getText().toString() + "&password=" + et_password.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPost = new JSONObject(response);
                                checkuser = jsonPost.getString("LOGIN");
                                if (checkuser.equals("INACTIVEUSER")) {
                                    tvDlgInfoTitle.setText("User ID tidak aktif, silahkan " +
                                            "hubungi HRD dan silahkan ulangi.");
                                    dbhelper.delete_data_username();
                                    et_username.setText(null);
                                    et_password.setText(null);
                                    imgphoto.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.border_dialog));
                                    imgphoto.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.username));
                                    pDialog.dismiss();
                                    dialoginfo.show();
                                    btnDialogInfo.setOnClickListener(v13 -> dialoginfo.dismiss());
                                } else if (checkuser.equals("CHANGEPASSWORD")) {
                                    pDialog.dismiss();
                                    dialoginsertpassword = new Dialog(LoginActivity.this);
                                    dialoginsertpassword.setContentView(R.layout.dialog_insertpassword);
                                    dialoginsertpassword.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                    Window windowInsertPassword = dialoginsertpassword.getWindow();
                                    windowInsertPassword.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    TextInputLayout layoutDlgInsertPassword = (TextInputLayout) dialoginsertpassword.findViewById(R.id.layoutDlgInsertPassword);
                                    TextInputLayout layoutDlgConfirmPassword = (TextInputLayout) dialoginsertpassword.findViewById(R.id.layoutDlgConfirmPassword);
                                    TextInputLayout layoutDlgRegistKey = (TextInputLayout) dialoginsertpassword.findViewById(R.id.layoutDlgRegistKey);
                                    EditText etDlgInsertPassword = (EditText) dialoginsertpassword.findViewById(R.id.etDlgInsertPassword);
                                    EditText etDlgConfirmPassword = (EditText) dialoginsertpassword.findViewById(R.id.etDlgConfirmPassword);
                                    EditText etDlgRegistKey = (EditText) dialoginsertpassword.findViewById(R.id.etDlgRegistKey);
                                    Button btnSubmitDlgPassword = (Button) dialoginsertpassword.findViewById(R.id.btnSubmitDlgChangePassword);
                                    Button btnBackDlgPassword = (Button) dialoginsertpassword.findViewById(R.id.btnBackDlgChangePassword);
                                    dialoginsertpassword.show();

                                    btnBackDlgPassword.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            etDlgConfirmPassword.setText(null);
                                            etDlgInsertPassword.setText(null);
                                            etDlgRegistKey.setText(null);
                                            dialoginsertpassword.dismiss();
                                        }
                                    });

                                    btnSubmitDlgPassword.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(etDlgInsertPassword.getText().toString().trim())) {
                                                layoutDlgInsertPassword.setError("Input Password Pribadi");
                                                layoutDlgConfirmPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                            } else if (TextUtils.isEmpty(etDlgConfirmPassword.getText().toString().trim())) {
                                                layoutDlgConfirmPassword.setError("Masukkan Kembali Password Pribadi");
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);
                                            } else if (TextUtils.isEmpty(etDlgRegistKey.getText().toString().trim())) {
                                                layoutDlgRegistKey.setError("Masukkan Kunci Registrasi");
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgConfirmPassword.setError(null);
                                            } else if (!etDlgInsertPassword.getText().toString()
                                                    .equals(etDlgConfirmPassword.getText().toString())) {
                                                layoutDlgInsertPassword.setError("Password does not match");
                                                layoutDlgConfirmPassword.setError("Password does not match");
                                                layoutDlgRegistKey.setError(null);
                                            } else {
                                                layoutDlgInsertPassword.setError(null);
                                                layoutDlgConfirmPassword.setError(null);
                                                layoutDlgRegistKey.setError(null);

                                                RequestQueue requestQueueChangePassword = Volley.newRequestQueue(LoginActivity.this);
                                                url_data = url_api + "dlgchangepassword.php";
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                                        url_data, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonPostChangePassword = new JSONObject(response);
                                                            if (jsonPostChangePassword.getString("CHANGEPASSWORD").equals("SUCCESS")) {
                                                                et_password.setText(null);
                                                                dialoginsertpassword.dismiss();
                                                                new SweetAlertDialog(LoginActivity.this,
                                                                    SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Merubah Password")
                                                                    .setContentText("Silahkan Login Kembali").setConfirmText("OK").show();
                                                            } else {
                                                                layoutDlgRegistKey.setError("Kunci Registrasi tidak sesuai");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        requestQueueChangePassword.stop();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(LoginActivity.this, "Error...", Toast.LENGTH_LONG).show();
                                                        error.printStackTrace();
                                                        requestQueueChangePassword.stop();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("userid", et_username.getText().toString());
                                                        params.put("password", etDlgConfirmPassword.getText().toString());
                                                        params.put("registkey", etDlgRegistKey.getText().toString());
                                                        return params;
                                                    }
                                                };
                                                requestQueueChangePassword.add(stringRequest);
                                            }
                                        }
                                    });
                                    btnBackDlgPassword.setOnClickListener(v12 -> dialoginsertpassword.dismiss());
                                } else if (checkuser.equals("SUCCESS")) {
                                    new JsonAsyncLogin().execute(response, null, null);
                                } else if (checkuser.equals("WRONGPASSWORD")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Password Salah").setConfirmText("OK").show();
                                } else {
                                    pDialog.dismiss();
                                    dialogHelper.showDialogInfo("User ID tidak terdaftar, " +
                                            "silahkan hubungi HRD dan silahkan ulangi.");
                                    et_username.setText(null);
                                    et_password.setText(null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (et_username.getText().toString().equals(dbhelper.get_tbl_username(0))
                                || et_username.getText().toString().equals(dbhelper.get_tbl_username(1))
                                || et_username.getText().toString().equals(dbhelper.get_tbl_username(8))) {

                                boolean checkPassword = hashPassword.CheckPassword(et_password.getText().toString(), dbhelper.get_tbl_username(4));
                                if (checkPassword) {
                                    pDialog.dismiss();
                                    et_password.setText(null);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Password Salah").setConfirmText("OK").show();
                                }
                            } else {
                                return_koneksi = "";
                                pDialog.dismiss();
                                dialogHelper.showDialogInfo("Koneksi Error");
                            }
                        }
                    });
                    queue.add(stringRequest);
                    handler.removeCallbacks(this);
                }
            }, 2000);
        }
    }

    private class JsonAsyncLogin extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... jsonObjectsLogin) {
            Integer insertedResult = -1;
            try {
                JSONObject jsonPost = new JSONObject(jsonObjectsLogin[0]);

                if (!jsonPost.getString("EMPCODE").equals(dbhelper.get_tbl_username(8))) {
                    dbhelper.delete_data_username();
                    dbhelper.delete_datags();
                    dbhelper.delete_menuGS02("HCM");
                    dbhelper.delete_menuGS02("WORKSHOP");
                    dbhelper.delete_menuGS02("VEHICLE");
                    dbhelper.delete_menuGS02("INVENTORY");
                    dbhelper.delete_masterdata();
                    dbhelper.delete_alltrasanction();

                    try {
                        dbhelper.insert_tblusername(
                                jsonPost.getString("USERID"),
                                jsonPost.getString("USERNAME"),
                                jsonPost.getString("USERTYPE"),
                                jsonPost.getString("USERROLE"),
                                jsonPost.getString("POSITION_ID"),
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
                                AESEnkrip.encrypt(jsonPost.getString("EMPCODE") + ";" + et_password.getText().toString() + ";"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return insertedResult;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            et_password.setText(null);
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
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            pDialog.dismiss();
        }
    }

    //Function Check Version========================================================================
    void generate_version() {
        RequestQueue queue = Volley.newRequestQueue(this);
        return_koneksi = null;
        if (systemCode != null || systemCode.equals("")) {
            url_data = url_api + "fetchdata/get_sysversion.php?systemcode=" + systemCode;
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
                                dialogHelper.showDialogInfo("Update System Tersedia\n\n" +
                                        dbhelper.get_tbl_version(7) + "\n[" +
                                        dbhelper.get_tbl_version(8) + "]\n\n" +
                                        dbhelper.get_tbl_version(9));
                                tvversion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogHelper.showDialogInfo("Update System Tersedia\n\n" +
                                                dbhelper.get_tbl_version(7) + "\n[" +
                                                dbhelper.get_tbl_version(8) + "]\n\n" +
                                                dbhelper.get_tbl_version(9));
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
            dbhelper.get_companyinfo(13);
            url_data = url_api + "fetchdata/get_companyinfo.php?systemcode=" + systemCode
                    + "&sysdate=" + currentDateTime;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonPost = new JSONObject(response.toString());

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
                            namasystem = dbhelper.get_companyinfo(3);
                            tvnamasystem.setText(jsonPost.getString("SYSTEMNAME"));
                            try {
                                bitmaplogosystem = BitmapFactory.decodeByteArray(
                                        dbhelper.get_companyimg(0), 0,
                                        dbhelper.get_companyimg(0).length);
                                imglogo.setImageBitmap(bitmaplogosystem);
                                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                                        dbhelper.get_companyimg(1), 0,
                                        dbhelper.get_companyimg(1).length);
                                BitmapDrawable backgroundLogin = new BitmapDrawable(compressedBitmap);
                                layoutBgLogin.setBackgroundDrawable(backgroundLogin);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            dbhelper.insert_companyinfo("JULONG",
                                    null, null,
                                    "LONG TECH",
                                    "http://longtech.julongindonesia.com/longtech/mobilesync/",
                                    "Pak Siswo",
                                    "siswo@mail.com",
                                    "0811",
                                    "KEM Tower 3rd & 5th Floor Jalan Landasan Pacu Barat B10 Kemayoran");

                            namasystem = dbhelper.get_companyinfo(3);
                            tvnamasystem.setText(namasystem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dbhelper.insert_companyinfo("JULONG",
                            null, null,
                            "LONG TECH",
                            "http://longtech.julongindonesia.com/longtech/mobilesync/",
                            "Pak Siswo",
                            "siswo@mail.com",
                            "0811",
                            "KEM Tower 3rd & 5th Floor Jalan Landasan Pacu Barat B10 Kemayoran");
                    namasystem = dbhelper.get_companyinfo(3);
                }
            });
            queue.add(stringRequest);
        }
    }

    public void eventLoginTest(View v) {
//        Intent intent = new Intent(LoginActivity.this, PenerimaanBBM.class);
//        startActivity(intent);
    }

    //Function Bahasa===============================================================================
    void generate_language() {
        url_data = url_api + "fetchdata/get_language.php";
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
                                        jsonObject1.getString("SEQ_NO"), "0");
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

}