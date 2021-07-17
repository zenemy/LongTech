package com.julong.longtech.menusetup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.HashPassword;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyAccount extends AppCompatActivity {

    HashPassword hashFunction;
    Dialog dlgChangePassword;
    DatabaseHelper dbhelper;

    Button btnChangePasswordMyAccount, btnBackMyAccount, btnSimpanMyAccount;
    TextView tvNameMyAccount, tvJabatanMyAccount, tvUsernameMyAccount, tvDeptMyAccount, tvNikMyAccount;
    EditText etHeaderThemeMyAccount, etColorTextMyAccount;
    EditText etEmailUserMyAccount;
    ImageView imgFotoMyAccount;
    ConstraintLayout clHeaderMyAccount;

    String server_url, dataProses;

    private byte[] profileImg;
    //Color Picker
    private String mMaterialPreDefinedColor = "";
    private String dbThemeHex, dbSystemTextHex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        dbhelper = new DatabaseHelper(this);
        hashFunction = new HashPassword(11);

        clHeaderMyAccount = findViewById(R.id.clHeaderAkunSaya);
        btnChangePasswordMyAccount = findViewById(R.id.btnChangePasswordMyAccount);
        tvNameMyAccount = findViewById(R.id.tvFullNameMyAccount);
        tvJabatanMyAccount = findViewById(R.id.tvJabatanMyAccount);
        tvUsernameMyAccount = findViewById(R.id.tvUserIDMyAccount);
        tvNikMyAccount = findViewById(R.id.tvNikMyAccount);
        tvDeptMyAccount = findViewById(R.id.tvDeptMyAccount);
        etEmailUserMyAccount = findViewById(R.id.etEmailUserMyAccount);
        btnBackMyAccount = findViewById(R.id.btnCancelMyAccount);
        imgFotoMyAccount = findViewById(R.id.imgFotoMyAccount);
        etHeaderThemeMyAccount = findViewById(R.id.etWarnaHeaderMyAccount);
        etColorTextMyAccount = findViewById(R.id.etTextColorMyAccount);
        btnSimpanMyAccount = findViewById(R.id.btnSimpanMyAccount);

        tvNameMyAccount.setText(dbhelper.get_tbl_username(10));
        tvNikMyAccount.setText(dbhelper.get_tbl_username(8));
        tvJabatanMyAccount.setText(dbhelper.get_tbl_username(13));
        tvUsernameMyAccount.setText(dbhelper.get_tbl_username(1));
        etEmailUserMyAccount.setText(dbhelper.get_tbl_username(11));

        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(), 0, dbhelper.get_gambar_user().length);
            imgFotoMyAccount.setBackground(null);
            imgFotoMyAccount.setImageBitmap(compressedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dlgChangePassword = new Dialog(this);
        dlgChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgChangePassword.setContentView(R.layout.dlg_changepassword_fromsetting);
        dlgChangePassword.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window dialogChangePassword = dlgChangePassword.getWindow();
        dialogChangePassword.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextInputLayout layoutOldPasswordDlgSettingPassword = dlgChangePassword.findViewById(R.id.layoutOldPasswordDlgSettingPassword);
        TextInputLayout layoutNewPasswordDlgSettingPassword = dlgChangePassword.findViewById(R.id.layoutNewPasswordDlgSettingPassword);
        TextInputLayout layoutDlgConfirmDlgSettingPassword = dlgChangePassword.findViewById(R.id.layoutDlgConfirmDlgSettingPassword);
        EditText etOldPasswordDlgSettingPassword = dlgChangePassword.findViewById(R.id.etOldPasswordDlgSettingPassword);
        EditText etNewPasswordDlgSettingPassword = dlgChangePassword.findViewById(R.id.etNewPasswordDlgSettingPassword);
        EditText etDlgConfirmDlgSettingPassword = dlgChangePassword.findViewById(R.id.etDlgConfirmDlgSettingPassword);
        Button btnBackDlgChangePasswordMyAccount = dlgChangePassword.findViewById(R.id.btnBackDlgChangePasswordMyAccount);
        Button btnSubmitDlgChangePasswordMyAccount = dlgChangePassword.findViewById(R.id.btnSubmitDlgChangePasswordMyAccount);
        btnBackDlgChangePasswordMyAccount.setOnClickListener(v -> dlgChangePassword.dismiss());

        btnSubmitDlgChangePasswordMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etOldPasswordDlgSettingPassword.getText().toString().trim())) {
                    layoutOldPasswordDlgSettingPassword.setError("Masukkan Password Lama");
                    layoutNewPasswordDlgSettingPassword.setError(null);
                    layoutDlgConfirmDlgSettingPassword.setError(null);
                }
                else if (TextUtils.isEmpty(etNewPasswordDlgSettingPassword.getText().toString().trim())) {
                    layoutNewPasswordDlgSettingPassword.setError("Masukkan Password Baru");
                    layoutOldPasswordDlgSettingPassword.setError(null);
                    layoutDlgConfirmDlgSettingPassword.setError(null);
                }
                else if (TextUtils.isEmpty(etDlgConfirmDlgSettingPassword.getText().toString().trim())) {
                    layoutDlgConfirmDlgSettingPassword.setError("Konfirmasi Password Baru");
                    layoutNewPasswordDlgSettingPassword.setError(null);
                    layoutOldPasswordDlgSettingPassword.setError(null);
                }
                else {
                    layoutOldPasswordDlgSettingPassword.setError(null);
                    layoutNewPasswordDlgSettingPassword.setError(null);
                    layoutDlgConfirmDlgSettingPassword.setError(null);
                    boolean checkPassword = hashFunction.CheckPassword(etOldPasswordDlgSettingPassword.getText().toString(), dbhelper.get_tbl_username(4));
                    if (checkPassword == false) {
                        layoutOldPasswordDlgSettingPassword.setError("Password Lama tidak sesuai");
                        layoutNewPasswordDlgSettingPassword.setError(null);
                        layoutDlgConfirmDlgSettingPassword.setError(null);
                    }
                    else if (!etNewPasswordDlgSettingPassword.getText().toString().equals(etDlgConfirmDlgSettingPassword.getText().toString())) {
                        layoutNewPasswordDlgSettingPassword.setError("Password does not match");
                        layoutDlgConfirmDlgSettingPassword.setError("Password does not match");
                        layoutOldPasswordDlgSettingPassword.setError(null);
                    }
                    else {
                        layoutOldPasswordDlgSettingPassword.setError(null);
                        layoutNewPasswordDlgSettingPassword.setError(null);
                        layoutDlgConfirmDlgSettingPassword.setError(null);

                        String hashedpassword = hashFunction.HashPassword(etDlgConfirmDlgSettingPassword.getText().toString());
                        dbhelper.changepassword_myaccount(hashedpassword);
                        RequestQueue requestQueueChangePassword = Volley.newRequestQueue(getApplicationContext());
                        server_url = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/dlgchangepassword.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonPostChangePassword = new JSONObject(response.toString());
                                    if (jsonPostChangePassword.getString("CHANGEPASSWORD").equals("SUCCESS")) {
                                        etDlgConfirmDlgSettingPassword.setText(null);
                                        dlgChangePassword.dismiss();
                                        new SweetAlertDialog(MyAccount.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Merubah Password").setConfirmText("OK").show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requestQueueChangePassword.stop();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MyAccount.this, "Error ...", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                                requestQueueChangePassword.stop();
                            }
                        })  {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userid", tvUsernameMyAccount.getText().toString());
                                params.put("password", etDlgConfirmDlgSettingPassword.getText().toString());
                                return params;
                            }
                        };
                        requestQueueChangePassword.add(stringRequest);
                    }
                }
            }
        });

        btnChangePasswordMyAccount.setOnClickListener(v -> dlgChangePassword.show());

        etHeaderThemeMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] colorHexArray = getResources().getStringArray(R.array.themeColorHex);

                new MaterialColorPickerDialog.Builder(MyAccount.this).setTitle("Pick Theme").setColors(colorHexArray).setDefaultColor(mMaterialPreDefinedColor).setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        dbThemeHex = colorHex;
                        clHeaderMyAccount.getBackground().setColorFilter(Color.parseColor(colorHex), PorterDuff.Mode.SRC_ATOP);
                        setButtonBackground(btnBackMyAccount, color);
                        setButtonBackground(btnSimpanMyAccount, color);

                        if (Build.VERSION.SDK_INT >= 21) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.setStatusBarColor(Color.parseColor(colorHex));
                        }

                        if (colorHex.equals("#F6AF39")) {
                            etHeaderThemeMyAccount.setText("Orange");
                        }
                        else if (colorHex.equals("#000000")) {
                            etHeaderThemeMyAccount.setText("Black");
                        }
                        else if (colorHex.equals("#FFFFFF")) {
                            etHeaderThemeMyAccount.setText("White");
                        }
                        else if (colorHex.equals("#FF7979")) {
                            etHeaderThemeMyAccount.setText("Merah Muda");
                        }
                        else if (colorHex.equals("#DC143C")) {
                            etHeaderThemeMyAccount.setText("Merah");
                        }
                        else if (colorHex.equals("#BCA136")) {
                            etHeaderThemeMyAccount.setText("Gold");
                        }
                        else if (colorHex.equals("#3700B3")) {
                            etHeaderThemeMyAccount.setText("Blue");
                        }
                        else if (colorHex.equals("#305031")) {
                            etHeaderThemeMyAccount.setText("Dark Green");
                        }
                        else if (colorHex.equals("#686DE0")) {
                            etHeaderThemeMyAccount.setText("Light Blue");
                        }
                        else if (colorHex.equals("#30336B")) {
                            etHeaderThemeMyAccount.setText("Dark Blue");
                        }
                        else {
                            etHeaderThemeMyAccount.setText("Dark Grey");
                        }
                    }
                }).show();
            }
        });

        etColorTextMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] colorHexArray = getResources().getStringArray(R.array.themeColorHex);

                new MaterialColorPickerDialog.Builder(MyAccount.this).setTitle("Pick Theme").setColors(colorHexArray).setDefaultColor(mMaterialPreDefinedColor).setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        dbSystemTextHex = colorHex;

                        if (Build.VERSION.SDK_INT >= 21) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.setStatusBarColor(Color.parseColor(colorHex));
                        }

                        if (colorHex.equals("#F6AF39")) {
                            etColorTextMyAccount.setText("Orange");
                        }
                        else if (colorHex.equals("#000000")) {
                            etColorTextMyAccount.setText("Black");
                        }
                        else if (colorHex.equals("#FFFFFF")) {
                            etColorTextMyAccount.setText("White");
                        }
                        else if (colorHex.equals("#FF7979")) {
                            etColorTextMyAccount.setText("Merah Muda");
                        }
                        else if (colorHex.equals("#DC143C")) {
                            etColorTextMyAccount.setText("Merah");
                        }
                        else if (colorHex.equals("#BCA136")) {
                            etColorTextMyAccount.setText("Gold");
                        }
                        else if (colorHex.equals("#3700B3")) {
                            etColorTextMyAccount.setText("Blue");
                        }
                        else if (colorHex.equals("#305031")) {
                            etColorTextMyAccount.setText("Dark Green");
                        }
                        else if (colorHex.equals("#686DE0")) {
                            etColorTextMyAccount.setText("Light Blue");
                        }
                        else if (colorHex.equals("#30336B")) {
                            etColorTextMyAccount.setText("Dark Blue");
                        }
                        else {
                            etColorTextMyAccount.setText("Dark Grey");
                        }
                    }
                }).show();
            }
        });

        btnBackMyAccount.setOnClickListener(v -> finish());
    }

    public void ChangeProfilePicture(View v) {
        dataProses = "changeimg";
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).
                setFixAspectRatio(true).setRequestedSize(300, 300).setOutputCompressQuality(80).start(MyAccount.this);
    }

    private void setButtonBackground(Button btn, int color) {
        btn.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (dataProses.equals("changeimg")) {
            try {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {

                        final InputStream imageStream = getContentResolver().openInputStream(result.getUri());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, stream1);
                        profileImg = stream1.toByteArray();

                        if (profileImg.length > 500000) {
                            new SweetAlertDialog(MyAccount.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error!").setContentText("Ukuran gambar terlalu besar").setConfirmText("OK").show();
                        }
                        else {
                            dbhelper.update_profilepic(profileImg);
                            imgFotoMyAccount.setImageURI(result.getUri());
                            LoginActivity.imgphoto.setImageURI(result.getUri());
                            LoginActivity.imgphoto.setImageURI(result.getUri());
                            MainActivity.imgUserNavHeader.setImageURI(result.getUri());

                            String base64ProfileImg = android.util.Base64.encodeToString(profileImg,  android.util.Base64.DEFAULT);

                            RequestQueue requestQueueChangePassword = Volley.newRequestQueue(getApplicationContext());
                            server_url = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/uploaduserimg.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonPostChangePassword = new JSONObject(response.toString());
                                        if (jsonPostChangePassword.getString("UPLOADIMG").equals("SUCCESS")) {
                                            new SweetAlertDialog(MyAccount.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Merubah Foto").setConfirmText("OK").show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    requestQueueChangePassword.stop();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MyAccount.this, "Error ...", Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                    requestQueueChangePassword.stop();
                                }
                            })  {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("userid", tvUsernameMyAccount.getText().toString());
                                    params.put("userimg", base64ProfileImg);
                                    return params;
                                }
                            };
                            requestQueueChangePassword.add(stringRequest);

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}