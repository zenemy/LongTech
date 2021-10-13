package com.julong.longtech;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.julong.longtech.menuhcm.AbsensiMandiri;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menuhistory.HistoryActivity;
import com.julong.longtech.menuinventory.PenerimaanBBM;
import com.julong.longtech.menureport.ReportActivity;
import com.julong.longtech.menusetup.AppSetting;
import com.julong.longtech.menusetup.DownloadData;
import com.julong.longtech.menusetup.UpdateSystem;
import com.julong.longtech.menusetup.UploadData;
import com.julong.longtech.menuvehicle.AdjustmentUnit;
import com.julong.longtech.menuvehicle.InspeksiHasilKerja;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.NewMethodRKH;
import com.julong.longtech.menuvehicle.PemeriksaanPengecekanHarian;
import com.julong.longtech.menuinventory.PengeluaranBBM;
import com.julong.longtech.menuinventory.PermintaanBBM;
import com.julong.longtech.menuworkshop.PermintaanPerbaikan;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;
import com.julong.longtech.menuworkshop.LaporanPerbaikan;
import com.julong.longtech.menuvehicle.VerifikasiGIS;
import com.julong.longtech.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.DatabaseHelper.url_api;
import static com.julong.longtech.ui.home.HomeFragment.loadLvHistoryApel;
import static com.julong.longtech.ui.home.HomeFragment.loadLvHistoryCarLog;
import static com.julong.longtech.ui.home.HomeFragment.loadlvinfohome;
import static com.julong.longtech.ui.home.HomeFragment.lvfragment;
import static com.julong.longtech.ui.home.HomeFragment.tvPlaceholder;

public class MainActivity extends AppCompatActivity {

    public static ImageView imgUserNavHeader;
    HashPassword hashPassword;

    //Private
    public static ActivityResultLauncher<Intent> intentLaunchMainActivity;
    private AppBarConfiguration mAppBarConfiguration;
    List<String> listGroupMenu;
    HashMap<String, List<String>> listMenu;
    String todayDate;

    //Class / package / Helper
    DatabaseHelper dbhelper;
    SweetAlertDialog proDialog;
    Handler handler = new Handler();

    //Object
    TextView tvUsernameNavHome, tvPositionNavHome, tvDeptCode;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    View hView;
    ConstraintLayout clnavheader;

    private List<String> listKendalaCode, listKendalaName;
    ArrayAdapter<String> adapterKendala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbhelper = new DatabaseHelper(this);
        hashPassword = new HashPassword(11);

        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);

        imgUserNavHeader = hView.findViewById(R.id.userPhotoNavHeader);
        clnavheader = hView.findViewById(R.id.clnavheader);
        tvUsernameNavHome = hView.findViewById(R.id.tv_viewusername);
        tvPositionNavHome = hView.findViewById(R.id.tv_viewposition);
        tvDeptCode = hView.findViewById(R.id.tvDeptCode);
        tvUsernameNavHome.setText(dbhelper.get_tbl_username(10));
        tvPositionNavHome.setText(dbhelper.get_tbl_username(13));
        tvDeptCode.setText(dbhelper.get_tbl_username(17));
        expandableListView = findViewById(R.id.expandableListView);

        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        generate_gs02menu();

        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(), 0, dbhelper.get_gambar_user().length);
            imgUserNavHeader.setImageBitmap(compressedBitmap);
            imgUserNavHeader.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        intentLaunchMainActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == 1) {
                        if (dbhelper.count_tablemd().equals("0")) {
                            final SweetAlertDialog warningExitDlg = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                            warningExitDlg.setContentText("Download data dahulu!");
                            warningExitDlg.setConfirmText("YA");
                            warningExitDlg.showCancelButton(false);
                            warningExitDlg.setCancelable(false);
                            warningExitDlg.setConfirmClickListener(sweetAlertDialog -> {
                                warningExitDlg.dismiss();
                                Intent intentDownload = new Intent(MainActivity.this, DownloadData.class);
                                intentLaunchMainActivity.launch(intentDownload);
                            });
                            warningExitDlg.show();
                        }
                    }

                    if (result.getResultCode() == 2) {
                        loadlvinfohome(todayDate);
                        loadLvHistoryCarLog(todayDate);
                        loadLvHistoryApel(todayDate);
                        tvPlaceholder.setVisibility(View.GONE);
                    }

                    if (result.getResultCode() == 3) {
                        loadlvinfohome(todayDate);
                        loadLvHistoryCarLog(todayDate);
                        tvPlaceholder.setVisibility(View.GONE);

                    }

                    if (result.getResultCode() == 4) {
                        loadlvinfohome(todayDate);
                        loadLvHistoryApel(todayDate);
                        tvPlaceholder.setVisibility(View.GONE);
                    }
                    if (result.getResultCode() == 727) {
                        loadlvinfohome(todayDate);
                        if (lvfragment.getAdapter().getCount() > 0) {
                            tvPlaceholder.setVisibility(View.GONE);
                        }
                    }
                }
        );



        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    String menuGroupCode = dbhelper.get_menucode(listGroupMenu.get(groupPosition),
                            listMenu.get(listGroupMenu.get(groupPosition)).get(childPosition), 0);
                    String menuSubCode = dbhelper.get_menucode(listGroupMenu.get(groupPosition),
                            listMenu.get(listGroupMenu.get(groupPosition)).get(childPosition), 1);
                try {

                    if (menuGroupCode.equals("0101") && menuSubCode.equals("010104")) {
                        Intent intent = new Intent(MainActivity.this, AbsensiMandiri.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0101") && menuSubCode.equals("010105")) {
                        if (dbhelper.get_statusapelpagi(0).equals("1")) {
                            Intent intent = new Intent(MainActivity.this, ApelPagi.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentLaunchMainActivity.launch(intent);
                            onPause();
                        }
                        else {
                            final SweetAlertDialog startApelDlg = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                            startApelDlg.setTitleText("Mulai apel pagi?");
                            startApelDlg.setCancelText("KEMBALI");
                            startApelDlg.setConfirmText("MULAI");
                            startApelDlg.showCancelButton(true);
                            startApelDlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, ApelPagi.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intentLaunchMainActivity.launch(intent);
                                    onPause();
                                }
                            });
                            startApelDlg.show();
                        }
                    }

                    if (menuGroupCode.equals("0201") && menuSubCode.equals("020101")) {
                        Intent intent = new Intent(MainActivity.this, PermintaanPerbaikan.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0201") && menuSubCode.equals("020102")) {
                        Intent intent = new Intent(MainActivity.this, LaporanPerbaikan.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }


                    // Vehicle Menu
                    if (menuGroupCode.equals("0202") && menuSubCode.equals("020201")) {
                        Intent intent = new Intent(MainActivity.this, NewMethodRKH.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0202") && menuSubCode.equals("020202")) {
                        Intent intent = new Intent(MainActivity.this, PemeriksaanPengecekanHarian.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0202") && menuSubCode.equals("020203")) {

                        Dialog dlgStartCarLog = new Dialog(MainActivity.this);
                        dlgStartCarLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dlgStartCarLog.setContentView(R.layout.dlg_startcarlog);
                        dlgStartCarLog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        Window windowStartCarLog = dlgStartCarLog.getWindow();
                        windowStartCarLog.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        AutoCompleteTextView acDlgVehicleCarLog = dlgStartCarLog.findViewById(R.id.acDlgVehicleCarLog);
                        AutoCompleteTextView acDlgShiftCarLog = dlgStartCarLog.findViewById(R.id.acDlgShiftCarLog);
                        Button btnCancelDlgVehicleCarLog = dlgStartCarLog.findViewById(R.id.btnCancelDlgVehicleCarLog);
                        Button btnSimpanDlgVehicleCarLog = dlgStartCarLog.findViewById(R.id.btnSimpanDlgVehicleCarLog);
                        dlgStartCarLog.show();

                        btnCancelDlgVehicleCarLog.setOnClickListener(view -> dlgStartCarLog.dismiss());

                        List<String> listVehicleCarLog;
                        ArrayAdapter<String> adapterVehicleCarLog;

                        String[] arrayShiftCarLog = {"Shift 1", "Shift 2", "Shift 3"};
                        ArrayAdapter<String> adapterShiftCarLog;

                        acDlgVehicleCarLog.setText(dbhelper.get_vehiclename(2, dbhelper.get_tbl_username(19)));
                        acDlgShiftCarLog.setText(dbhelper.get_tbl_username(20));

                        listVehicleCarLog = dbhelper.get_vehiclemasterdata();
                        adapterVehicleCarLog = new ArrayAdapter<>(MainActivity.this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleCarLog);
                        acDlgVehicleCarLog.setAdapter(adapterVehicleCarLog);

                        adapterShiftCarLog = new ArrayAdapter<>(MainActivity.this, R.layout.spinnerlist, R.id.spinnerItem, arrayShiftCarLog);
                        acDlgShiftCarLog.setAdapter(adapterShiftCarLog);


                        acDlgVehicleCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                                keyboardMgr.hideSoftInputFromWindow(acDlgVehicleCarLog.getWindowToken(), 0);
                            }
                        });

                        btnSimpanDlgVehicleCarLog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dlgStartCarLog.dismiss();
                                String selectedCarLogVehicle = dbhelper.get_vehiclecodeonly(acDlgVehicleCarLog.getText().toString());
                                dbhelper.update_ancakcode_user(selectedCarLogVehicle, acDlgShiftCarLog.getText().toString());

                                Intent intent = new Intent(MainActivity.this, KartuKerjaVehicle.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intentLaunchMainActivity.launch(intent);
                                onPause();
                            }
                        });
                    }

                    if (groupPosition == 2 && childPosition == 3) {

                    }

                    if (groupPosition == 2 && childPosition == 4) {

                    }

                    if (menuGroupCode.equals("0202") && menuSubCode.equals("020206")) {
                        Intent intent = new Intent(MainActivity.this, AdjustmentUnit.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0202") && menuSubCode.equals("020207")) {
                        Intent intent = new Intent(MainActivity.this, VerifikasiGIS.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0202") && menuSubCode.equals("020208")) {
                        Intent intent = new Intent(MainActivity.this, InspeksiHasilKerja.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    // INVENTORY MENU
                    if (menuGroupCode.equals("0301") && menuSubCode.equals("030101")) {
                        Intent intent = new Intent(MainActivity.this, PermintaanBBM.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                    if (menuGroupCode.equals("0301") && menuSubCode.equals("030103")) {
                        Intent intent = new Intent(MainActivity.this, PenerimaanBBM.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }


                    if (menuGroupCode.equals("0301") && menuSubCode.equals("030102")) {
                        Intent intent = new Intent(MainActivity.this, PengeluaranBBM.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchMainActivity.launch(intent);
                        onPause();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (menuGroupCode.equals("0401") && menuSubCode.equals("040101")) {
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (menuGroupCode.equals("0401") && menuSubCode.equals("040102")) {
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (menuGroupCode.equals("0501") && menuSubCode.equals("050101")) {
                    Intent intent = new Intent(MainActivity.this, DownloadData.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentLaunchMainActivity.launch(intent);
                    onPause();
                }

                if (menuGroupCode.equals("0501") && menuSubCode.equals("050102")) {
                    Intent intent = new Intent(MainActivity.this, UploadData.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentLaunchMainActivity.launch(intent);
                    onPause();
                }

                return false;
            }
        });

    }

    public void eventShowQR(View v) {
        //Insert Password before show QR
        Dialog dlgInsertPasswordQR = new Dialog(MainActivity.this);
        dlgInsertPasswordQR.setContentView(R.layout.dialog_passwordqr);
        dlgInsertPasswordQR.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlgInsertPasswordQR.setCanceledOnTouchOutside(false);
        Window windowInsertPassowrd = dlgInsertPasswordQR.getWindow();
        windowInsertPassowrd.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        EditText etPasswordDlgQR = dlgInsertPasswordQR.findViewById(R.id.etPasswordDlgQR);
        Button btnOkPasswordQR = dlgInsertPasswordQR.findViewById(R.id.btnOkPasswordQR);
        Button btnBackPasswordQR = dlgInsertPasswordQR.findViewById(R.id.btnBackPasswordQR);
        btnBackPasswordQR.setOnClickListener(view12 -> dlgInsertPasswordQR.dismiss());
        dlgInsertPasswordQR.show();

        etPasswordDlgQR.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnOkPasswordQR.performClick();
                }
                return false;
            }
        });

        btnOkPasswordQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkPassword = hashPassword.CheckPassword(etPasswordDlgQR.getText().toString(), dbhelper.get_tbl_username(4));
                if (checkPassword) {
                    dlgInsertPasswordQR.dismiss();

                    //Show QR if success
                    Dialog dialogMyQR = new Dialog(MainActivity.this);
                    dialogMyQR.setContentView(R.layout.dialog_myqr);
                    dialogMyQR.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialogMyQR.setCanceledOnTouchOutside(false);
                    Window windowQR = dialogMyQR.getWindow();
                    windowQR.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvEmpNameDialogQR = dialogMyQR.findViewById(R.id.tvEmpNameDialogQR);
                    ImageView imgQrEmployee = dialogMyQR.findViewById(R.id.imgQrEmployee);
                    Button btnRefreshQR = dialogMyQR.findViewById(R.id.btnRefreshQR);
                    Button btnBackDlgQR = dialogMyQR.findViewById(R.id.btnBackDlgQR);
                    tvEmpNameDialogQR.setText(dbhelper.get_tbl_username(10));
                    dialogMyQR.show();

                    btnBackDlgQR.setOnClickListener(view1 -> dialogMyQR.dismiss());
                    QRCodeWriter writer = new QRCodeWriter();

                    try {
                        String hashedValue = hashPassword.HashPassword(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) + dbhelper.get_tbl_username(8));
                        String finalValueQR = hashedValue + "longtech" + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        BitMatrix bitMatrix = writer.encode(finalValueQR, BarcodeFormat.QR_CODE, 512, 512);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        imgQrEmployee.setImageBitmap(bmp);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            btnRefreshQR.setVisibility(View.VISIBLE);
                            imgQrEmployee.setAlpha(50);

                            handler.removeCallbacks(this);
                        }
                    }, 300000);

                    btnRefreshQR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogMyQR.dismiss();
                            eventShowQR(v);
                        }
                    });
                } else {
                    dlgInsertPasswordQR.dismiss();
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Password Salah").setConfirmText("OK").show();
                }
            }
        });
    }

    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void prepareListData () {
        listGroupMenu = new ArrayList<String>();
        listMenu = new HashMap<String, List<String>>();

        int menuCount = 0;

        listGroupMenu.add(dbhelper.get_groupmenu("HCM", "HCM"));
        List<String> MENUHCM = dbhelper.get_menuname("HCM", "HCM");
        listMenu.put(listGroupMenu.get(menuCount), MENUHCM);
        menuCount++;

        if (dbhelper.get_groupmenu("OPERASIONAL", "WSA") != null) {
            listGroupMenu.add(dbhelper.get_groupmenu("OPERASIONAL", "WSA"));
            List<String> MENUWORKSHOP = dbhelper.get_menuname("OPERASIONAL", "WSA");
            listMenu.put(listGroupMenu.get(menuCount), MENUWORKSHOP);
            menuCount++;
        }

        if (dbhelper.get_groupmenu("OPERASIONAL", "VHA") != null) {
            listGroupMenu.add(dbhelper.get_groupmenu("OPERASIONAL", "VHA"));
            List<String> MENUVEHICLE = dbhelper.get_menuname("OPERASIONAL", "VHA");
            listMenu.put(listGroupMenu.get(menuCount), MENUVEHICLE);
            menuCount++;
        }

        if (dbhelper.get_groupmenu("DISTRIBUTION", "INV") != null) {
            listGroupMenu.add(dbhelper.get_groupmenu("DISTRIBUTION", "INV"));
            List<String> MENUINVENTORY = dbhelper.get_menuname("DISTRIBUTION", "INV");
            listMenu.put(listGroupMenu.get(menuCount), MENUINVENTORY);
            menuCount++;
        }

        listGroupMenu.add("REPORT");
        List<String> REPORT = dbhelper.get_menuname("REPORT", "RPRT");
        listMenu.put(listGroupMenu.get(menuCount), REPORT);
        menuCount++;

        listGroupMenu.add("UPLOAD & DOWNLOAD");
        List<String> UPLOADDOWNLOAD =  dbhelper.get_menuname("DATA", "SYNC");
        listMenu.put(listGroupMenu.get(menuCount), UPLOADDOWNLOAD);
        menuCount++;

//        listGroupMenu.add("PENGATURAN");
//        if (dbhelper.get_tbl_username(2).equals("ASU")) {
//            List<String> SETUP = Arrays.asList("AKUN", "PROFILE SYSTEM", "PEMBARUAN SYSTEM");
//            listMenu.put(listGroupMenu.get(6), SETUP);
//        }
//        else {
//            List<String> SETUP = Arrays.asList("AKUN", "PEMBARUAN SYSTEM");
//            listMenu.put(listGroupMenu.get(6), SETUP);
//        }

    }

    private void generate_gs02menu() {
        proDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        proDialog.setTitleText("Loading Data");
        proDialog.setCancelable(false);
        proDialog.show();

        //  Fetch menu from API
        String url_data = url_api + "fetchdata/get_menugs02.php?rolecode=" + dbhelper.get_tbl_username(3);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
            (Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // Background process fetching each menu data
                    new AsyncJsonMenuGS().execute(response, null, null);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                    prepareListData();
                    expandableListAdapter = new com.julong.longtech.ExpandableListAdapter(MainActivity.this, listGroupMenu, listMenu, 1);
                    expandableListView.setAdapter(expandableListAdapter);
                    proDialog.dismiss();

                    if (dbhelper.check_menufragment("010105").equals("0")) {
                        HomeFragment.linearLayoutApel.setVisibility(View.GONE);
                    }
                    if (dbhelper.check_menufragment("020201").equals("0")) {
                        HomeFragment.linearLayoutRKH.setVisibility(View.GONE);
                    }
                    if (dbhelper.check_menufragment("020202").equals("0")) {
                        HomeFragment.linearLayoutP2H.setVisibility(View.GONE);
                    }
                    if (dbhelper.check_menufragment("020203").equals("0")) {
                        HomeFragment.linearLayoutCarLog.setVisibility(View.GONE);
                    }
                    if (dbhelper.check_menufragment("020207").equals("0")) {
                        HomeFragment.linearLayoutGIS.setVisibility(View.GONE);
                    }
                    if (dbhelper.check_menufragment("030101").equals("0")) {
                        HomeFragment.linearLayoutBBM.setVisibility(View.GONE);
                    }

                    if (dbhelper.check_menufragment("020101").equals("0")
                            && dbhelper.check_menufragment("020102").equals("0")
                            && dbhelper.check_menufragment("020104").equals("0")) {
                        HomeFragment.linearLayoutService.setVisibility(View.GONE);
                    }

                    if (dbhelper.count_tablemd().equals("0")) {
                        final SweetAlertDialog warningExitDlg = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                        warningExitDlg.setContentText("Download data dahulu!");
                        warningExitDlg.setConfirmText("YA");
                        warningExitDlg.showCancelButton(false);
                        warningExitDlg.setCancelable(false);
                        warningExitDlg.setConfirmClickListener(sweetAlertDialog -> {
                            warningExitDlg.dismiss();
                            Intent intentDownload = new Intent(MainActivity.this, DownloadData.class);
                            intentLaunchMainActivity.launch(intentDownload);
                        });
                        warningExitDlg.show();
                    }
                }
            });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private class AsyncJsonMenuGS extends AsyncTask<JSONObject, Void, Integer> {

        @Override
        protected  void onPreExecute() {

            dbhelper.delete_menuGS02("HCM");
            dbhelper.delete_menuGS02("WORKSHOP");
            dbhelper.delete_menuGS02("VEHICLE");
            dbhelper.delete_menuGS02("INVENTORY");
            dbhelper.delete_menuGS02("REPORT");
            dbhelper.delete_menuGS02("UPLOAD & DOWNLOAD");

        }

        protected Integer doInBackground(JSONObject... jsonObjectsGS) {
            try {
                JSONObject responseGS = jsonObjectsGS[0];

                // Generate Menu HCM
                JSONArray jsonArrayHCM = responseGS.getJSONArray("DATAHCM");
                int iHCM = 0;
                while (iHCM < jsonArrayHCM.length()) {
                    JSONObject jsonObjectHCM = jsonArrayHCM.getJSONObject(iHCM);
                    dbhelper.insert_menuGS02(jsonObjectHCM.getString("GROUPCODE"), jsonObjectHCM.getString("GROUPPARAMDESC"), jsonObjectHCM.getString("MODULECODE"),
                            jsonObjectHCM.getString("MODULEDESC"), jsonObjectHCM.getString("CONTROLSYSTEM"), jsonObjectHCM.getString("DOCTYPECODE"),
                            jsonObjectHCM.getString("SUBMODULECODE"), jsonObjectHCM.getString("SUBMODULEDESC"), jsonObjectHCM.getString("SUBMODULETYPE"),
                            jsonObjectHCM.getString("SEQ"), jsonObjectHCM.getString("MENUVIEW"), jsonObjectHCM.getString("MENUDEFAULT"));
                    iHCM++;
                }

                // Generate Menu Workshop
                if (responseGS.has("DATAWORKSHOP")) {
                    JSONArray jsonArrayWS = responseGS.getJSONArray("DATAWORKSHOP");
                    if (jsonArrayWS != null || jsonArrayWS.length() > 0)  {
                        int iWS = 0;
                        while (iWS < jsonArrayWS.length()) {
                            JSONObject jsonObjectWS = jsonArrayWS.getJSONObject(iWS);
                            dbhelper.insert_menuGS02(jsonObjectWS.getString("GROUPCODE"), jsonObjectWS.getString("GROUPPARAMDESC"), jsonObjectWS.getString("MODULECODE"),
                                    jsonObjectWS.getString("MODULEDESC"), jsonObjectWS.getString("CONTROLSYSTEM"), jsonObjectWS.getString("DOCTYPECODE"),
                                    jsonObjectWS.getString("SUBMODULECODE"), jsonObjectWS.getString("SUBMODULEDESC"), jsonObjectWS.getString("SUBMODULETYPE"),
                                    jsonObjectWS.getString("SEQ"), jsonObjectWS.getString("MENUVIEW"), jsonObjectWS.getString("MENUDEFAULT"));
                            iWS++;
                        }
                    }
                }

                // Generate Menu Vehicle
                if (responseGS.has("DATAVEHICLE")) {
                    JSONArray jsonArrayVH = responseGS.getJSONArray("DATAVEHICLE");
                    if (jsonArrayVH != null || jsonArrayVH.length() > 0)  {
                        int iVH = 0;
                        while (iVH < jsonArrayVH.length()) {
                            JSONObject jsonObjectVH = jsonArrayVH.getJSONObject(iVH);
                            dbhelper.insert_menuGS02(jsonObjectVH.getString("GROUPCODE"), jsonObjectVH.getString("GROUPPARAMDESC"), jsonObjectVH.getString("MODULECODE"),
                                    jsonObjectVH.getString("MODULEDESC"), jsonObjectVH.getString("CONTROLSYSTEM"), jsonObjectVH.getString("DOCTYPECODE"),
                                    jsonObjectVH.getString("SUBMODULECODE"), jsonObjectVH.getString("SUBMODULEDESC"), jsonObjectVH.getString("SUBMODULETYPE"),
                                    jsonObjectVH.getString("SEQ"), jsonObjectVH.getString("MENUVIEW"), jsonObjectVH.getString("MENUDEFAULT"));
                            iVH++;
                        }
                    }
                }


                // Generate Menu Inventory
                if (responseGS.has("DATAINVENTORY")) {
                    JSONArray jsonArrayINV = responseGS.getJSONArray("DATAINVENTORY");
                    if (jsonArrayINV != null || jsonArrayINV.length() > 0)  {
                        int iNV = 0;
                        while (iNV < jsonArrayINV.length()) {
                            JSONObject jsonObjectINV = jsonArrayINV.getJSONObject(iNV);
                            dbhelper.insert_menuGS02(jsonObjectINV.getString("GROUPCODE"), jsonObjectINV.getString("GROUPPARAMDESC"), jsonObjectINV.getString("MODULECODE"),
                                    jsonObjectINV.getString("MODULEDESC"), jsonObjectINV.getString("CONTROLSYSTEM"), jsonObjectINV.getString("DOCTYPECODE"),
                                    jsonObjectINV.getString("SUBMODULECODE"), jsonObjectINV.getString("SUBMODULEDESC"), jsonObjectINV.getString("SUBMODULETYPE"),
                                    jsonObjectINV.getString("SEQ"), jsonObjectINV.getString("MENUVIEW"), jsonObjectINV.getString("MENUDEFAULT"));
                            iNV++;
                        }
                    }
                }

                if (!dbhelper.get_tbl_username(3).equals("OPR")) {
                    dbhelper.insert_menuGS02("04", "REPORT", "0401",
                        "REPORT", "RPRT", "RPRTON",
                        "040101", "Laporan Pekerjaan", null,
                        "1", null, null);
                }

                dbhelper.insert_menuGS02("05", "DATA", "0501",
                        "UPLOAD & DOWNLOAD", "SYNC", "FETCHDT",
                        "050101", "Download Data", null,
                        "1", null, null);

                dbhelper.insert_menuGS02("05", "DATA", "0501",
                        "UPLOAD & DOWNLOAD", "SYNC", "STOREDT",
                        "050102", "Upload Data", null,
                        "2", null, null);

                prepareListData();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if (dbhelper.check_menufragment("010105").equals("0")) {
                HomeFragment.linearLayoutApel.setVisibility(View.GONE);
            }
            if (dbhelper.check_menufragment("020201").equals("0")) {
                HomeFragment.linearLayoutRKH.setVisibility(View.GONE);
            }
            if (dbhelper.check_menufragment("020202").equals("0")) {
                HomeFragment.linearLayoutP2H.setVisibility(View.GONE);
            }
            if (dbhelper.check_menufragment("020203").equals("0")) {
                HomeFragment.linearLayoutCarLog.setVisibility(View.GONE);
            }
            if (dbhelper.check_menufragment("020207").equals("0")) {
                HomeFragment.linearLayoutGIS.setVisibility(View.GONE);
            }
            if (dbhelper.check_menufragment("030101").equals("0")) {
                HomeFragment.linearLayoutBBM.setVisibility(View.GONE);
            }
            if (dbhelper.check_menufragment("020101").equals("0")
                    && dbhelper.check_menufragment("020102").equals("0")
                    && dbhelper.check_menufragment("020104").equals("0")) {
                HomeFragment.linearLayoutService.setVisibility(View.GONE);
            }

            expandableListAdapter = new com.julong.longtech.ExpandableListAdapter(MainActivity.this, listGroupMenu, listMenu, 1);
            expandableListView.setAdapter(expandableListAdapter);
            proDialog.dismiss();

            if (dbhelper.count_tablemd().equals("0")) {
                final SweetAlertDialog warningExitDlg = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                warningExitDlg.setContentText("Download data dahulu!");
                warningExitDlg.setConfirmText("YA");
                warningExitDlg.showCancelButton(false);
                warningExitDlg.setCancelable(false);
                warningExitDlg.setConfirmClickListener(sweetAlertDialog -> {
                    warningExitDlg.dismiss();
                    Intent intentDownload = new Intent(MainActivity.this, DownloadData.class);
                    intentLaunchMainActivity.launch(intentDownload);
                });
                warningExitDlg.show();
            }
        }
    }

    public void logoutApp(View v) {

        final SweetAlertDialog warningExitDlg = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        warningExitDlg.setTitleText("Keluar dari akun ini?");
        warningExitDlg.setCancelText("KEMBALI");
        warningExitDlg.setConfirmText("YA");
        warningExitDlg.showCancelButton(true);
        warningExitDlg.setConfirmClickListener(sDialog  -> {
            sDialog.dismiss();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            loginIntent.addCategory( Intent.CATEGORY_HOME );
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginIntent.putExtra("currentuser", dbhelper.get_tbl_username(1));
            startActivity(loginIntent);
        });
        warningExitDlg.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            loadlvinfohome(todayDate);
            loadLvHistoryCarLog(todayDate);
            loadLvHistoryApel(todayDate);
            if (lvfragment.getAdapter().getCount() > 0) {
                tvPlaceholder.setVisibility(View.GONE);
            }
        }

        if (requestCode == 727) {
            loadlvinfohome(todayDate);
            if (lvfragment.getAdapter().getCount() > 0) {
                tvPlaceholder.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onBackPressed() {
        final SweetAlertDialog warningExitDlg = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        warningExitDlg.setTitleText("Anda yakin akan keluar?");
        warningExitDlg.setCancelText("KEMBALI");
        warningExitDlg.setConfirmText("YA");
        warningExitDlg.showCancelButton(true);
        warningExitDlg.setConfirmClickListener(sDialog  -> {
            sDialog.dismiss();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        });
        warningExitDlg.show();
    }
}