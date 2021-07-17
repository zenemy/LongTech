package com.julong.longtech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.julong.longtech.menuhcm.AbsensiBekerjaUnit;
import com.julong.longtech.menuhcm.AbsensiMandiri;
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menusetup.AppSetting;
import com.julong.longtech.menusetup.DownloadData;
import com.julong.longtech.menusetup.UpdateSystem;
import com.julong.longtech.menusetup.UploadData;
import com.julong.longtech.menuvehicle.InspeksiHasilKerja;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuhcm.MesinAbsensi;
import com.julong.longtech.menuvehicle.PemeriksaanPengecekanHarian;
import com.julong.longtech.menuinventory.PengeluaranBBM;
import com.julong.longtech.menuworkshop.PerintahPerbaikan;
import com.julong.longtech.menuinventory.PermintaanBBM;
import com.julong.longtech.menuworkshop.PermintaanPerbaikan;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;
import com.julong.longtech.menuworkshop.SelesaiPerbaikanBA;
import com.julong.longtech.menuvehicle.VerifikasiGIS;
import com.julong.longtech.menuhcm.BiodataKaryawan;
import com.julong.longtech.menusetup.MyAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    List<String> listGroupMenu;
    HashMap<String, List<String>> listMenu;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    private AppBarConfiguration mAppBarConfiguration;

    ProgressDialog pDialog;
    Handler handler = new Handler();

    View hView;
    ConstraintLayout clnavheader;
    public static ImageView imgUserNavHeader;
    TextView tvUsernameNavHome, tvPositionNavHome;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DatabaseHelper(this);
        generate_hcmmenu();
        generate_inventorymenu();
        generate_vehiclemenu();
        generate_workshopmenu();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);

        imgUserNavHeader = hView.findViewById(R.id.userPhotoNavHeader);
        clnavheader = hView.findViewById(R.id.clnavheader);
        tvUsernameNavHome = hView.findViewById(R.id.tv_viewusername);
        tvPositionNavHome = hView.findViewById(R.id.tv_viewposition);
        tvUsernameNavHome.setText(dbhelper.get_tbl_username(10));
        tvPositionNavHome.setText(dbhelper.get_tbl_username(13));

        try {
            clnavheader.getBackground().setColorFilter(Color.parseColor(dbhelper.get_tbl_username(26)), PorterDuff.Mode.SRC_ATOP);
            if (Build.VERSION.SDK_INT >= 21) {
                Window statusbar = getWindow();
                statusbar.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                statusbar.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                statusbar.setStatusBarColor(Color.parseColor(dbhelper.get_tbl_username(26)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(dbhelper.get_gambar_user(), 0, dbhelper.get_gambar_user().length);
            imgUserNavHeader.setImageBitmap(compressedBitmap);
            imgUserNavHeader.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        expandableListView = findViewById(R.id.expandableListView);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (groupPosition == 0 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, BiodataKaryawan.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 0 && childPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, MesinAbsensi.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 0 && childPosition == 2) {
                    Intent intent = new Intent(MainActivity.this, AbsensiBekerjaUnit.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 0 && childPosition == 3) {
                    Intent intent = new Intent(MainActivity.this, AbsensiMandiri.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 0 && childPosition == 4) {
                    Intent intent = new Intent(MainActivity.this, ApelPagi.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 1 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, PermintaanPerbaikan.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 1 && childPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, PerintahPerbaikan.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 1 && childPosition == 2) {
                    Intent intent = new Intent(MainActivity.this, SelesaiPerbaikanBA.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                // Vehicle Menu
                if (groupPosition == 2 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, RencanaKerjaHarian.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 2 && childPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, PemeriksaanPengecekanHarian.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 2 && childPosition == 2) {
                    Intent intent = new Intent(MainActivity.this, KartuKerjaVehicle.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 2 && childPosition == 3) {

                }

                if (groupPosition == 2 && childPosition == 4) {

                }

                if (groupPosition == 2 && childPosition == 5) {

                }

                if (groupPosition == 2 && childPosition == 6) {

                }

                if (groupPosition == 2 && childPosition == 7) {

                }

                if (groupPosition == 2 && childPosition == 8) {

                }

                if (groupPosition == 2 && childPosition == 9) {
                    Intent intent = new Intent(MainActivity.this, VerifikasiGIS.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 2 && childPosition == 10) {
                    Intent intent = new Intent(MainActivity.this, InspeksiHasilKerja.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                // INVENTORY MENU
                if (groupPosition == 3 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, PermintaanBBM.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 3 && childPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, PengeluaranBBM.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 4 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 5 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, DownloadData.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 5 && childPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, UploadData.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 6 && childPosition == 0) {
                    Intent intent = new Intent(MainActivity.this, MyAccount.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 6 && childPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, AppSetting.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                if (groupPosition == 6 && childPosition == 2) {
                    Intent intent = new Intent(MainActivity.this, UpdateSystem.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onPause();
                }

                return false;
            }
        });


        final SweetAlertDialog proDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        proDialog.getProgressHelper().setBarColor(Color.parseColor("#305031"));
        proDialog.setTitleText("Loading Data");
        proDialog.setCancelable(false);
        proDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                prepareListData();
                expandableListAdapter = new com.julong.longtech.ExpandableListAdapter(MainActivity.this, listGroupMenu, listMenu, 1);
                expandableListView.setAdapter(expandableListAdapter);
                proDialog.dismiss();
                handler.removeCallbacks(this);
            }
        }, 1500);


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

        listGroupMenu.add(dbhelper.get_groupmenu("HCM", "HCM"));
        List<String> MENUHCM = dbhelper.get_menuname("HCM", "HCM");
        listMenu.put(listGroupMenu.get(0), MENUHCM);

        listGroupMenu.add(dbhelper.get_groupmenu("OPERASIONAL", "WSA"));
        List<String> MENUWORKSHOP = dbhelper.get_menuname("OPERASIONAL", "WSA");
        listMenu.put(listGroupMenu.get(1), MENUWORKSHOP);

        listGroupMenu.add(dbhelper.get_groupmenu("OPERASIONAL", "VHA"));
        List<String> MENUVEHICLE = dbhelper.get_menuname("OPERASIONAL", "VHA");
        listMenu.put(listGroupMenu.get(2), MENUVEHICLE);

        listGroupMenu.add(dbhelper.get_groupmenu("DISTRIBUTION", "INV"));
        List<String> MENUINVENTORY = dbhelper.get_menuname("DISTRIBUTION", "INV");
        listMenu.put(listGroupMenu.get(3), MENUINVENTORY);

        listGroupMenu.add("LAPORAN");
        List<String> REPORT = Arrays.asList("LAPORAN");
        listMenu.put(listGroupMenu.get(4), REPORT);

        listGroupMenu.add("UPLOAD & DOWNLOAD");
        List<String> UPLOADDOWNLOAD =  Arrays.asList("DOWNLOAD DATA","UPLOAD DATA");
        listMenu.put(listGroupMenu.get(5), UPLOADDOWNLOAD);

        listGroupMenu.add("PENGATURAN");
        if (dbhelper.get_tbl_username(2).equals("ASU")) {
            List<String> SETUP = Arrays.asList("AKUN", "PROFILE SYSTEM", "PEMBARUAN SYSTEM");
            listMenu.put(listGroupMenu.get(6), SETUP);
        }
        else {
            List<String> SETUP = Arrays.asList("AKUN", "PEMBARUAN SYSTEM");
            listMenu.put(listGroupMenu.get(6), SETUP);
        }

    }

    void generate_hcmmenu() {
        String url_data = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/fetchdata/get_menuhcm.php";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("DATAHCM");
                            int i = 0;
                            if (dbhelper.count_hcmmenuexist().equals("0")) {
                                while (i < jsonArray.length()) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    dbhelper.insert_menuGS02(jsonObject1.getString("GROUPCODE"), jsonObject1.getString("GROUPPARAMDESC"), jsonObject1.getString("MODULECODE"),
                                            jsonObject1.getString("MODULEDESC"), jsonObject1.getString("CONTROLSYSTEM"), jsonObject1.getString("DOCTYPECODE"),
                                            jsonObject1.getString("SUBMODULECODE"), jsonObject1.getString("SUBMODULEDESC"), jsonObject1.getString("SUBMODULETYPE"),
                                            jsonObject1.getString("SEQ"), jsonObject1.getString("MENUVIEW"), jsonObject1.getString("MENUDEFAULT"));
                                    i++;
                                }
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

    void generate_workshopmenu() {
        String url_data = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/fetchdata/get_workshopmenu.php";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("DATAWORKSHOP");
                            int i = 0;
                            if (dbhelper.count_menuGS02("02", "OPERASIONAL", "0201").equals("0")) {
                                while (i < jsonArray.length()) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    dbhelper.insert_menuGS02(jsonObject1.getString("GROUPCODE"), jsonObject1.getString("GROUPPARAMDESC"), jsonObject1.getString("MODULECODE"),
                                            jsonObject1.getString("MODULEDESC"), jsonObject1.getString("CONTROLSYSTEM"), jsonObject1.getString("DOCTYPECODE"),
                                            jsonObject1.getString("SUBMODULECODE"), jsonObject1.getString("SUBMODULEDESC"), jsonObject1.getString("SUBMODULETYPE"),
                                            jsonObject1.getString("SEQ"), jsonObject1.getString("MENUVIEW"), jsonObject1.getString("MENUDEFAULT"));
                                    i++;
                                }
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

    void generate_vehiclemenu() {
        String url_data = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/fetchdata/get_vehiclemenu.php";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("DATAVEHICLE");
                            int i = 0;
                            if (dbhelper.count_menuGS02("02", "OPERASIONAL", "0202").equals("0")) {
                                while (i < jsonArray.length()) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    dbhelper.insert_menuGS02(jsonObject1.getString("GROUPCODE"), jsonObject1.getString("GROUPPARAMDESC"), jsonObject1.getString("MODULECODE"),
                                            jsonObject1.getString("MODULEDESC"), jsonObject1.getString("CONTROLSYSTEM"), jsonObject1.getString("DOCTYPECODE"),
                                            jsonObject1.getString("SUBMODULECODE"), jsonObject1.getString("SUBMODULEDESC"), jsonObject1.getString("SUBMODULETYPE"),
                                            jsonObject1.getString("SEQ"), jsonObject1.getString("MENUVIEW"), jsonObject1.getString("MENUDEFAULT"));
                                    i++;
                                }
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

    void generate_inventorymenu() {
        String url_data = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/fetchdata/get_inventorymenu.php";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("DATAINVENTORY");
                            int i = 0;
                            if (dbhelper.count_menuGS02("03", "DISTRIBUTION", "0301").equals("0")) {
                                while (i < jsonArray.length()) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    dbhelper.insert_menuGS02(jsonObject1.getString("GROUPCODE"), jsonObject1.getString("GROUPPARAMDESC"), jsonObject1.getString("MODULECODE"),
                                            jsonObject1.getString("MODULEDESC"), jsonObject1.getString("CONTROLSYSTEM"), jsonObject1.getString("DOCTYPECODE"),
                                            jsonObject1.getString("SUBMODULECODE"), jsonObject1.getString("SUBMODULEDESC"), jsonObject1.getString("SUBMODULETYPE"),
                                            jsonObject1.getString("SEQ"), jsonObject1.getString("MENUVIEW"), jsonObject1.getString("MENUDEFAULT"));
                                    i++;
                                }
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

    @Override
    public void onBackPressed() {
        final SweetAlertDialog warningExitDlg = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        warningExitDlg.setTitleText("Anda yakin akan keluar?");
        warningExitDlg.setCancelText("KEMBALI");
        warningExitDlg.setConfirmText("YA");
        warningExitDlg.showCancelButton(true);
        warningExitDlg.setConfirmClickListener(sDialog -> finish());
        warningExitDlg.show();
    }
}