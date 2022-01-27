package com.julong.longtech.menuhcm;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.HashPassword;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.julong.longtech.menuvehicle.InspeksiHasilKerja;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ApelPagi extends AppCompatActivity {

    public static int dataProcess, REQUEST_IMAGE_CAPTURE = 1;
    public static byte[] byteGambarApel;
    public static String selectedEmp, selectedUnit, selectedItemData, nodocApel, latApel, longApel;
    DatabaseHelper dbhelper;

    ActivityResultLauncher<Intent> intentLaunchCameraHasil;

    List<ApelPagiList> listApels;
    public static ApelPagiAdapter adapterApel;

    EditText etKemandoranApel, etLokasiApel;
    TextView tvHeaderApel;
    ImageView imgFotoApel;
    public static RecyclerView lvAnggotaApel;
    public static Button btnActionApel;
    Button btnBackApelPagi;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apelpagi);

        dbhelper = new DatabaseHelper(this);
        String apelDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());

        // Declare design ID
        etLokasiApel = findViewById(R.id.etLokasiApel);
        imgFotoApel = findViewById(R.id.imgCaptureApel);
        tvHeaderApel = findViewById(R.id.tvHeaderApelPagi);
        etKemandoranApel = findViewById(R.id.etKemandoranApel);
        lvAnggotaApel = findViewById(R.id.lvAnggotaApelPagi);
        btnBackApelPagi = findViewById(R.id.btnBackApelPagi);

        btnBackApelPagi.setOnClickListener(view -> onBackPressed());

        // Get selected shift value from intent
        etKemandoranApel.setText(dbhelper.get_infokemandoranapel(0, dbhelper.get_tbl_username(18)));
        tvHeaderApel.setText("BRIEFING " + apelDate);

        intentLaunchCameraHasil = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    getLocation();

                    Bundle bundleFoto = result.getData().getExtras();
                    Bitmap photoCamera = (Bitmap) bundleFoto.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byteGambarApel = stream.toByteArray();
                    imgFotoApel.setImageBitmap(photoCamera);
                    imgFotoApel.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgFotoApel.setForeground(null);

                }

            }
        );

        imgFotoApel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byteGambarApel = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    intentLaunchCameraHasil.launch(takePictureIntent);
                }
            }
        });

        loadListViewEmp();

    }

    public void submitBriefing(View v) {
        if (byteGambarApel == null) {
            Snackbar.make(v, "Harap foto kegiatan briefing", Snackbar.LENGTH_LONG).setAnchorView(btnBackApelPagi)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).setAction("OKAY", null).show();
        }
        else {
            nodocApel = dbhelper.get_tbl_username(0) + "/ABSAPL/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
            dbhelper.updateselesai_apelpagi(nodocApel, etLokasiApel.getText().toString(), latApel, longApel, byteGambarApel);

            SweetAlertDialog dlgSuccess = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            dlgSuccess.setTitleText("APEL SELESAI");
            dlgSuccess.setCancelable(false);
            dlgSuccess.setConfirmClickListener(sweetAlertDialog -> {
                Intent backIntent = new Intent();
                setResult(4, backIntent);
                finish();
            });
            dlgSuccess.show();

            new Handler().postDelayed(() -> {
                Intent backIntent = new Intent();
                setResult(4, backIntent);
                finish();
            }, 2000);
        }
    }

    // Load RecylerView of team member
    public void loadListViewEmp() {
        LinearLayoutManager layoutApel = new LinearLayoutManager(this);
        lvAnggotaApel.setLayoutManager(layoutApel);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                lvAnggotaApel.getContext(), layoutApel.getOrientation());
        lvAnggotaApel.addItemDecoration(dividerItemDecoration);

        listApels = new ArrayList<>();
        listApels.clear();
        final Cursor cursor = dbhelper.listview_apelpagi_anggota();
        if (cursor.moveToFirst()) {
            do {
                ApelPagiList paramsApel = new ApelPagiList(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(4), cursor.getString(3), cursor.getString(5));
                listApels.add(paramsApel);
            } while (cursor.moveToNext());
        }
        adapterApel = new ApelPagiAdapter(listApels, this);
        lvAnggotaApel.setAdapter(adapterApel);
    }

    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latApel = String.valueOf(latitude);
            longApel = String.valueOf(longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onBackPressed() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'ABSAPL' AND itemdata = 'DETAIL2' " +
                "AND date(date1) = date('now', 'localtime') AND uploaded IS NULL");
        finish();
    }
}