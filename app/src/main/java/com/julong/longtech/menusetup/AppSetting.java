package com.julong.longtech.menusetup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppSetting extends AppCompatActivity {

    DatabaseHelper dbhelper;
    DialogHelper dialogHelper;
    Handler handler = new Handler();

    //Color Picker
    private String mMaterialPreDefinedColor = "";
    private String dbThemeHex, dbTextBaseColor, dbSystemTextHex, dbBgColorHex, timeSatellite;

    private byte[] gambar1, gambar2, gambar3;
    int v_data_proses;
    ConstraintLayout clHeaderAppSetting, clBgAppSetting;
    ImageView takeprofilePicture, previewimgprofile, previewimglogo, takeimagelogo, takeimagebackground, previewimgbackground;
    TextView tvBgImg, btnPreviewLogin, imgInsertLogo, previewtvnamasystem, tvGantiSandi, tvHapusUser, tvJudulLoginPage, tvSilahkanRegist,
            tvCompNameAppSystem, tvPicNameAppSystem, tvContactPicAppSystem, tvPicEmailAppSystem, tvCompAddressAppSystem;
    EditText etChangeUsername, etChangeSystemName, etEmailUser, tvSystemTextColorPicker, tvThemeColorPicker;
    Button btnsimpan, btnBackAppSetting;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        dbhelper = new DatabaseHelper(this);
        dialogHelper = new DialogHelper(this);
        clBgAppSetting = findViewById(R.id.clBgAppSetting);
        tvGantiSandi = findViewById(R.id.tvGantiKataSandi);
        tvHapusUser = findViewById(R.id.tvHapusUser);
        tvJudulLoginPage = findViewById(R.id.tvJudulLoginPage);
        tvSilahkanRegist = findViewById(R.id.tvSilahkanRegist);
        previewimgprofile  = findViewById(R.id.previewProfilePhoto);
        previewimglogo = findViewById(R.id.previewLogoAplikasi);
        clHeaderAppSetting = findViewById(R.id.clHeaderAppSetting);
        tvThemeColorPicker = findViewById(R.id.tvChangeColorTheme);
        tvSystemTextColorPicker = findViewById(R.id.tvSystemTextColor);
        etChangeSystemName = findViewById(R.id.etNamaSystem);
        takeimagelogo = findViewById(R.id.selectLogoImg);
        btnPreviewLogin = findViewById(R.id.btnPreviewLogin);
        takeimagebackground = findViewById(R.id.selectBgImg);
        previewimgbackground = findViewById(R.id.backgroundAplikasi);
        previewtvnamasystem = findViewById(R.id.tvPreviewNamaSystem);
        btnsimpan = findViewById(R.id.submitSetting);
        tvCompNameAppSystem = findViewById(R.id.tvCompNameAppSystem);
        tvPicNameAppSystem = findViewById(R.id.tvPicNameAppSystem);
        tvContactPicAppSystem = findViewById(R.id.tvContactPicAppSystem);
        tvPicEmailAppSystem = findViewById(R.id.tvPicEmailAppSystem);
        tvCompAddressAppSystem = findViewById(R.id.tvCompAddressAppSystem);
        btnBackAppSetting = findViewById(R.id.btnCanceltSetting);

        tvCompNameAppSystem.setText(dbhelper.get_tblcompany(3));
        tvPicNameAppSystem.setText(dbhelper.get_tblcompany(5));
        tvContactPicAppSystem.setText(dbhelper.get_tblcompany(7));
        tvPicEmailAppSystem.setText(dbhelper.get_tblcompany(6));
        tvCompAddressAppSystem.setText(dbhelper.get_tblcompany(8));

        preparedUserAppData("sysname");
        preparedUserAppData("setcolor");
        preparedUserAppData("theme");
        preparedUserAppData("imglogo");
        preparedUserAppData("imgbg");

        etChangeSystemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                previewtvnamasystem.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvThemeColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] colorHexArray = getResources().getStringArray(R.array.themeColorHex);

                new MaterialColorPickerDialog.Builder(AppSetting.this).setTitle("Pick Theme").setColors(colorHexArray).setDefaultColor(mMaterialPreDefinedColor).setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        dbThemeHex = colorHex;
                        clHeaderAppSetting.getBackground().setColorFilter(Color.parseColor(colorHex), PorterDuff.Mode.SRC_ATOP);
                        btnPreviewLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorHex)));
                        setButtonBackground(btnsimpan, color);
                        setButtonBackground(btnBackAppSetting, color);

                        if (Build.VERSION.SDK_INT >= 21) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.setStatusBarColor(Color.parseColor(colorHex));
                        }

                        if (colorHex.equals("#F6AF39")) {
                            tvThemeColorPicker.setText("Orange");
                        }
                        else if (colorHex.equals("#000000")) {
                            tvThemeColorPicker.setText("Black");
                        }
                        else if (colorHex.equals("#FFFFFF")) {
                            tvThemeColorPicker.setText("White");
                        }
                        else if (colorHex.equals("#FF7979")) {
                            tvThemeColorPicker.setText("Merah Muda");
                        }
                        else if (colorHex.equals("#DC143C")) {
                            tvThemeColorPicker.setText("Merah");
                        }
                        else if (colorHex.equals("#BCA136")) {
                            tvThemeColorPicker.setText("Gold");
                        }
                        else if (colorHex.equals("#3700B3")) {
                            tvThemeColorPicker.setText("Blue");
                        }
                        else if (colorHex.equals("#305031")) {
                            tvThemeColorPicker.setText("Dark Green");
                        }
                        else if (colorHex.equals("#686DE0")) {
                            tvThemeColorPicker.setText("Light Blue");
                        }
                        else if (colorHex.equals("#30336B")) {
                            tvThemeColorPicker.setText("Dark Blue");
                        }
                        else {
                            tvThemeColorPicker.setText("Dark Grey");
                        }
                    }
                }).show();
            }
        });

        tvSystemTextColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] colorHexArray = getResources().getStringArray(R.array.themeColorHex);
                new MaterialColorPickerDialog.Builder(AppSetting.this).setTitle("Pick Color for System Name").setColors(colorHexArray).setDefaultColor(mMaterialPreDefinedColor).setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        dbSystemTextHex = colorHex;
                        previewtvnamasystem.setTextColor(Color.parseColor(colorHex));

                        if (colorHex.equals("#F6AF39")) {
                            tvSystemTextColorPicker.setText("Orange");
                        }
                        else if (colorHex.equals("#000000")) {
                            tvSystemTextColorPicker.setText("Black");
                        }
                        else if (colorHex.equals("#FFFFFF")) {
                            tvSystemTextColorPicker.setText("White");
                        }
                        else if (colorHex.equals("#FF7979")) {
                            tvSystemTextColorPicker.setText("Merah Muda");
                        }
                        else if (colorHex.equals("#DC143C")) {
                            tvSystemTextColorPicker.setText("Merah");
                        }
                        else if (colorHex.equals("#BCA136")) {
                            tvSystemTextColorPicker.setText("Gold");
                        }
                        else if (colorHex.equals("#3700B3")) {
                            tvSystemTextColorPicker.setText("Blue");
                        }
                        else if (colorHex.equals("#305031")) {
                            tvSystemTextColorPicker.setText("Dark Green");
                        }
                        else if (colorHex.equals("#686DE0")) {
                            tvSystemTextColorPicker.setText("Light Blue");
                        }
                        else if (colorHex.equals("#30336B")) {
                            tvSystemTextColorPicker.setText("Dark Blue");
                        }
                        else {
                            tvSystemTextColorPicker.setText("Dark Grey");
                        }
                    }
                }).show();
            }
        });

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.v_dlg_title = "Apakah anda yakin akan menyimpan perubahan?";
                LoginActivity.v_dlg_btn1 = "YA";
                LoginActivity.v_dlg_btn2 = "TIDAK";
                dialogHelper.showDialogYesNo();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 500);

                        if (LoginActivity.v_rtn_dlg_string.equals("CANCEL") ||
                                LoginActivity.v_rtn_dlg_string.equals("NO")) {
                            LoginActivity.v_rtn_dlg_string = "";
                            handler.removeCallbacks(this);
                        }

                        if (LoginActivity.v_rtn_dlg_string.equals("OK")) {
                            LoginActivity.v_rtn_dlg_string = "";
                            handler.removeCallbacks(this);

                            if (!previewtvnamasystem.getText().toString().equals("")) {
                                dbhelper.update_nama_system(previewtvnamasystem.getText().toString());
                            }
                            if (dbBgColorHex != null) {
                                dbhelper.update_bgcolor(dbBgColorHex);
                            }
                            if (dbThemeHex != null) {
                                dbhelper.update_themecolor(dbThemeHex);
                            }
                            if (dbTextBaseColor != null) {
                                dbhelper.update_basetextcolor(dbTextBaseColor);
                            }
                            if (dbSystemTextHex != null) {
                                dbhelper.update_judulcolor(dbSystemTextHex);
                            }
                            if (gambar1 != null && gambar1.length > 0 ) {
                                dbhelper.update_gambar(gambar1);
                            }
                            if (gambar2 != null && gambar2.length > 0) {
                                dbhelper.update_logo(gambar2);
                            }
                            if (gambar3 != null && gambar3.length > 0) {
                                dbhelper.update_background(gambar3);
                            }

                            Intent intent = new Intent(AppSetting.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }, 500);

            }
        });

        takeimagelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_data_proses = 3;
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).
                        setFixAspectRatio(true).setAspectRatio(1, 1).start(AppSetting.this);
            }
        });

        takeimagebackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_data_proses = 4;
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).
                        setFixAspectRatio(true).setAspectRatio(9, 16).start(AppSetting.this);
            }
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (v_data_proses == 2) {
            try {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {

                        final InputStream imageStream = getContentResolver().openInputStream(result.getUri());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, stream1);
                        gambar1 = stream1.toByteArray();

                        if (gambar1.length > 500000) {
                            LoginActivity.v_dlg_title = "Ukuran Gambar Terlalu Besar";
                            LoginActivity.v_dlg_btn1 = "OK";
                            dialogHelper.showDialogInfo();
                        }
                        else {
                            previewimgprofile.setImageURI(result.getUri());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (v_data_proses == 3) {
            try {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        final InputStream imageStream = getContentResolver().openInputStream(result.getUri());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, stream1);
                        gambar2 = stream1.toByteArray();

                        if (gambar2.length > 500000) {
                            LoginActivity.v_dlg_title = "Ukuran Gambar Terlalu Besar";
                            LoginActivity.v_dlg_btn1 = "OK";
                            dialogHelper.showDialogInfo();
                        }
                        else {
                            previewimglogo.setImageURI(result.getUri());
                            takeimagelogo.setImageURI(result.getUri());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (v_data_proses == 4) {
            try {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        final InputStream imageStream = getContentResolver().openInputStream(result.getUri());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, stream1);
                        gambar3 = stream1.toByteArray();

                        if (gambar3.length > 500000) {
                            LoginActivity.v_dlg_title = "Ukuran Gambar Terlalu Besar";
                            LoginActivity.v_dlg_btn1 = "OK";
                            dialogHelper.showDialogInfo();
                        }
                        else {
                            previewimgbackground.setImageURI(result.getUri());
                            takeimagebackground.setImageURI(result.getUri());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void preparedUserAppData(String predefinedData) {

        if (predefinedData.equals("setcolor")) {
            //Theme Color
            try {
                if (dbhelper.get_tbl_username(26).equals("")) {
                    tvThemeColorPicker.setText("Dark Green");
                } else if (dbhelper.get_tbl_username(26).equals("#000000")) {
                    tvThemeColorPicker.setText("Black");
                } else if (dbhelper.get_tbl_username(26).equals("#FFFFFF")) {
                    tvThemeColorPicker.setText("White");
                } else if (dbhelper.get_tbl_username(26).equals("#F6AF39")) {
                    tvThemeColorPicker.setText("Orange");
                } else if (dbhelper.get_tbl_username(26).equals("#FF7979")) {
                    tvThemeColorPicker.setText("Merah Muda");
                } else if (dbhelper.get_tbl_username(26).equals("#FF7979")) {
                    tvThemeColorPicker.setText("Gold");
                } else if (dbhelper.get_tbl_username(26).equals("3700B3")) {
                    tvThemeColorPicker.setText("Blue");
                } else if (dbhelper.get_tbl_username(26).equals("#305031")) {
                    tvThemeColorPicker.setText("Dark Green");
                } else if (dbhelper.get_tbl_username(26).equals("#686DE0")) {
                    tvThemeColorPicker.setText("Light Blue");
                } else if (dbhelper.get_tbl_username(26).equals("#30336B")) {
                    tvThemeColorPicker.setText("Dark Blue");
                } else if (dbhelper.get_tbl_username(26).equals("#3E3E3E")) {
                    tvThemeColorPicker.setText("Dark Gray");
                }
                else {
                    tvThemeColorPicker.setText("Dark Green");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Judul Color
            try {
                if (dbhelper.get_tbl_username(28).equals("")) {
                    tvSystemTextColorPicker.setText("White");
                } else if (dbhelper.get_tbl_username(28).equals("#000000")) {
                    tvSystemTextColorPicker.setText("Black");
                } else if (dbhelper.get_tbl_username(28).equals("#FFFFFF")) {
                    tvSystemTextColorPicker.setText("White");
                } else if (dbhelper.get_tbl_username(28).equals("#F6AF39")) {
                    tvSystemTextColorPicker.setText("Orange");
                } else if (dbhelper.get_tbl_username(28).equals("#FF7979")) {
                    tvSystemTextColorPicker.setText("Merah Muda");
                } else if (dbhelper.get_tbl_username(28).equals("#EFECEC")) {
                    tvSystemTextColorPicker.setText("Light Gray");
                } else if (dbhelper.get_tbl_username(28).equals("#FF7979")) {
                    tvSystemTextColorPicker.setText("Gold");
                } else if (dbhelper.get_tbl_username(28).equals("3700B3")) {
                    tvSystemTextColorPicker.setText("Blue");
                } else if (dbhelper.get_tbl_username(28).equals("#305031")) {
                    tvSystemTextColorPicker.setText("Dark Green");
                } else if (dbhelper.get_tbl_username(28).equals("#686DE0")) {
                    tvSystemTextColorPicker.setText("Light Blue");
                } else if (dbhelper.get_tbl_username(28).equals("#30336B")) {
                    tvSystemTextColorPicker.setText("Dark Blue");
                } else if (dbhelper.get_tbl_username(28).equals("#3E3E3E")) {
                    tvSystemTextColorPicker.setText("Dark Gray");
                }
            } catch (Exception e) {
                e.printStackTrace();
                tvSystemTextColorPicker.setText("White");
            }

        }

        try {
            if (predefinedData.equals("sysname")) {
                previewtvnamasystem.setText(dbhelper.get_tbl_username(25));
                etChangeSystemName.setText(dbhelper.get_tbl_username(25));
                etEmailUser.setText(dbhelper.get_tbl_username(6));
                tvSilahkanRegist.setTextColor(Color.parseColor(dbhelper.get_tbl_username(27)));
                tvHapusUser.setTextColor(Color.parseColor(dbhelper.get_tbl_username(27)));
                tvJudulLoginPage.setTextColor(Color.parseColor(dbhelper.get_tbl_username(27)));
                tvGantiSandi.setTextColor(Color.parseColor(dbhelper.get_tbl_username(27)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (predefinedData.equals("theme") && Build.VERSION.SDK_INT >= 21) {
                clHeaderAppSetting.getBackground().setColorFilter(Color.parseColor(dbhelper.get_tbl_username(26)), PorterDuff.Mode.SRC_ATOP);
                btnsimpan.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
                btnPreviewLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
                clBgAppSetting.setBackgroundColor(Color.parseColor(dbhelper.get_tbl_username(29)));

                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.parseColor(dbhelper.get_tbl_username(26)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (predefinedData.equals("imglogo")) {
                Bitmap compressedBitmapLogo = BitmapFactory.decodeByteArray(dbhelper.get_companyimg(0), 0, dbhelper.get_companyimg(0).length);
                previewimglogo.setImageBitmap(compressedBitmapLogo);
                takeimagelogo.setImageBitmap(compressedBitmapLogo);
                takeimagelogo.setBackground(null);
                previewimglogo.setBackground(null);
                gambar2 = dbhelper.get_companyimg(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (predefinedData.equals("imgbg")) {
                Bitmap compressedBitmapLogo = BitmapFactory.decodeByteArray(dbhelper.get_companyimg(1), 0, dbhelper.get_companyimg(1).length);
                previewimgbackground.setImageBitmap(compressedBitmapLogo);
                takeimagebackground.setImageBitmap(compressedBitmapLogo);
                takeimagebackground.setBackground(null);
                previewimgbackground.setBackground(null);
                gambar3 = dbhelper.get_companyimg(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setButtonBackground(Button btn, int color) {
        btn.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker (AppSetting.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Date date = new Date(gps.getElapsedRealtimeNanos());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));

        String myDate = sdf.format(date);
        tvThemeColorPicker.setText(myDate);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 512;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}