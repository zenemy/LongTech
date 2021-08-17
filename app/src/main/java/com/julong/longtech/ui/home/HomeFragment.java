package com.julong.longtech.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.GPSTracker;
import com.julong.longtech.HashPassword;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;
import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;
import com.julong.longtech.menuhcm.AbsensiMandiri;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.PemeriksaanPengecekanHarian;
import com.julong.longtech.menuinventory.PermintaanBBM;
import com.julong.longtech.menuworkshop.PermintaanPerbaikan;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.DatabaseHelper.url_api;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    HashPassword hashPassword;

    DatabaseHelper dbhelper;
    public static TextView tvjabatanuser, tvnamauser;
    BubbleTabBar bubbleTabBar;
    public static ListView lvfragment;
    public static RecyclerView lvHistoryApel, lvHistoryCarLog;
    byte[] gambar1;
    AutoCompleteTextView ackendala, acMenuRiwayatHome;
    TextView tvSystemNameFragmentHome;
    EditText etdesckendala, etpanjangkendala, etlebarkendala, etluaskendala, aclokasikendala, filtertglhistory;
    Button btnsimpankendala, btnDateLvInfo;
    ImageButton btnrefresh, openDrawerBtn, imgcamkendala;
    String lat_awal, long_awal, todayDate, todayDateTime, selectedKendala;
    ScrollView scrollkendala;
    ConstraintLayout clBgMainActivity;
    LinearLayout layoutRiwayatFragment, layoutInfoFragment, linearLayoutQR, linearLayoutAbsen, linearLayoutRKH, linearLayoutP2H,
            linearLayoutCarLog, linearLayoutBBM, linearLayoutService;

    String[] arrayMenuHistory = {"APEL PAGI", "CAR LOG"};
    ArrayAdapter<String> adapterMenuHistory;

    private List<String> listKendalaCode, listKendalaName;
    ArrayAdapter<String> adapterKendala;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        dbhelper = new DatabaseHelper(getContext());
        hashPassword = new HashPassword(11);

        tvnamauser = root.findViewById(R.id.tvNamaUser);
        layoutInfoFragment = root.findViewById(R.id.layoutInfoFragment);
        btnDateLvInfo = root.findViewById(R.id.btnDateLvInfoHome);
        clBgMainActivity = root.findViewById(R.id.clBgMainActivity);
        tvjabatanuser = root.findViewById(R.id.tvJabatanUser);
        bubbleTabBar = root.findViewById(R.id.bubbleTabBar);
        openDrawerBtn =  root.findViewById(R.id.openDrawerBtn);
        scrollkendala = root.findViewById(R.id.scrollViewKendala);
        lvfragment = root.findViewById(R.id.lvInfoFragment);
        btnrefresh = root.findViewById(R.id.btnRefreshHome);
        lvHistoryApel = root.findViewById(R.id.lvHistoryHomeApel);
        lvHistoryCarLog = root.findViewById(R.id.lvHistoryHomeCarLog);
        ackendala = root.findViewById(R.id.acKategoriKendala);
        etdesckendala = root.findViewById(R.id.etDescKendala);
        btnsimpankendala = root.findViewById(R.id.btnKnedalaOk);
        imgcamkendala = root.findViewById(R.id.imgKendala);
        aclokasikendala = root.findViewById(R.id.etLokasiKendala);
        acMenuRiwayatHome = root.findViewById(R.id.acMenuRiwayatHome);
        aclokasikendala.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etpanjangkendala = root.findViewById(R.id.etPanjangKendala);
        etlebarkendala = root.findViewById(R.id.etLebarKendala);
        etluaskendala = root.findViewById(R.id.etLuasKendala);
        layoutRiwayatFragment = root.findViewById(R.id.layoutRiwayatFragment);

        filtertglhistory = root.findViewById(R.id.etDateHomeHistory);
        linearLayoutAbsen = root.findViewById(R.id.linearLayoutAbsen);
        linearLayoutRKH = root.findViewById(R.id.linearLayoutRKH);
        linearLayoutP2H = root.findViewById(R.id.linearLayoutP2H);
        linearLayoutCarLog = root.findViewById(R.id.linearLayoutCarLog);
        linearLayoutBBM = root.findViewById(R.id.linearLayoutBBM);
        linearLayoutService = root.findViewById(R.id.linearLayoutService);
        linearLayoutQR = root.findViewById(R.id.linearLayoutMyQR);

        todayDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        filtertglhistory.setText(todayDate);
        btnDateLvInfo.setText(todayDate);

        eventClickMenu();

//        tvnamauser.setText(dbhelper.get_tbl_username(0));
//        tvjabatanuser.setText(dbhelper.get_tbl_username(3));

        preparedUserAppData("theme");
        preparedUserAppData("sysname");
        preparedUserAppData("bgcolor");

        adapterMenuHistory = new ArrayAdapter<String>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, arrayMenuHistory);
        acMenuRiwayatHome.setAdapter(adapterMenuHistory);

        listKendalaCode = dbhelper.get_menukendala(0);
        listKendalaName = dbhelper.get_menukendala(1);
        adapterKendala = new ArrayAdapter<String>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, listKendalaName);
        ackendala.setAdapter(adapterKendala);

        ackendala.setOnItemClickListener((adapterView, view, position, l) -> selectedKendala = listKendalaCode.get(position));

        openDrawerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        acMenuRiwayatHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selected = (String) adapterView.getItemAtPosition(position);

                if (selected.equals("APEL PAGI")) {
                    lvHistoryApel.setVisibility(View.VISIBLE);
                    lvHistoryCarLog.setVisibility(View.GONE);
                    lvHistoryApel.setAdapter(null);
                }
                else {
                    lvHistoryApel.setVisibility(View.GONE);
                    lvHistoryCarLog.setVisibility(View.VISIBLE);
                    lvHistoryCarLog.setAdapter(null);
                }

                filtertglhistory.setText(null);
            }
        });

        MaterialDatePicker<Long> datePickerLvHistory = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        MaterialDatePicker<Long> datePickerLvInfo = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        filtertglhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(acMenuRiwayatHome.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Pilih menu dahulu!", Toast.LENGTH_LONG).show();
                }
                else {
                    datePickerLvHistory.show(getParentFragmentManager(), "HISTORYHOME");
                }

            }
        });

        datePickerLvHistory.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                filtertglhistory.setText(simpleFormat.format(date));

                if (acMenuRiwayatHome.getText().toString().equals("APEL PAGI")) {
                    loadLvHistoryApel(simpleFormat.format(date));
                } else {
                    loadLvHistoryCarLog(simpleFormat.format(date));
                }
            }
        });

        btnDateLvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerLvInfo.show(getParentFragmentManager(), "INFOHOMIE");
            }
        });

        datePickerLvInfo.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                btnDateLvInfo.setText(simpleFormat.format(date));
                loadlvinfohome(simpleFormat.format(date));
            }
        });

        imgcamkendala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gambar1 = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int id) {
                switch (id) {
                    case R.id.homefragment:
                        layoutInfoFragment.setVisibility(View.VISIBLE);
                        scrollkendala.setVisibility(View.GONE);
                        layoutRiwayatFragment.setVisibility(View.GONE);
                        break;
                    case R.id.log:
                        layoutRiwayatFragment.setVisibility(View.VISIBLE);
                        layoutInfoFragment.setVisibility(View.GONE);
                        scrollkendala.setVisibility(View.GONE);
                        break;
                    case R.id.feedback:
                        layoutInfoFragment.setVisibility(View.GONE);
                        layoutRiwayatFragment.setVisibility(View.GONE);
                        scrollkendala.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        btnsimpankendala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ackendala.getText().toString().trim())) {
                    ackendala.setError("Harap Pilih Kendala");
                    aclokasikendala.setError(null);
                }
                else if (TextUtils.isEmpty(aclokasikendala.getText().toString().trim())) {
                    aclokasikendala.setError("Harap Pilih Kendala");
                    Toast.makeText(getActivity(),"Harap Input Lokasi", Toast.LENGTH_LONG).show();
                    ackendala.setError(null);
                }
                else if (gambar1 == null) {
                    Toast.makeText(getActivity(),"Harap Ambil Gambar Kendala", Toast.LENGTH_LONG).show();
                }
                else {
                    getLocation();
                    String nodocKendala = dbhelper.get_tbl_username(0) + "/KDL/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());

                    String base64Kendala = android.util.Base64.encodeToString(gambar1,  android.util.Base64.DEFAULT);

                    RequestQueue requestQueueKendala = Volley.newRequestQueue(getActivity());
                    String server_url = url_api + "dataupload/uploadkendala.php";
                    StringRequest stringRequestKendala = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPostKendala = new JSONObject(response.toString());
                                if (jsonPostKendala.getString("UPLOADKENDALA").equals("SUCCESS")) {
                                    gambar1 = null;
                                    aclokasikendala.setText(null);
                                    selectedKendala = null;
                                    ackendala.setText(null);
                                    etlebarkendala.setText(null);
                                    etpanjangkendala.setText(null);
                                    etluaskendala.setText(null);
                                    etdesckendala.setText(null);
                                    imgcamkendala.setImageResource(R.drawable.ic_menu_camera);
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Submit Kendala").setConfirmText("OK").show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            requestQueueKendala.stop();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dbhelper.insert_kendala(nodocKendala, ackendala.getText().toString(), aclokasikendala.getText().toString(),
                                    etpanjangkendala.getText().toString(), etlebarkendala.getText().toString(), etluaskendala.getText().toString(),
                                    etdesckendala.getText().toString(), lat_awal, long_awal, gambar1, 0);
                            error.printStackTrace();
                            requestQueueKendala.stop();
                        }
                    })  {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("nodoc", nodocKendala);
                            params.put("datatype", "KDL");
                            params.put("subdatatype", dbhelper.get_tbl_username(0));
                            params.put("itemdata", "HEADER");
                            params.put("subitemdata", "HEADER");
                            params.put("compid", dbhelper.get_tbl_username(14));
                            params.put("siteid", dbhelper.get_tbl_username(15));
                            params.put("date1", todayDateTime);
                            params.put("text1", selectedKendala);
                            params.put("text2", aclokasikendala.getText().toString());
                            params.put("text3", etpanjangkendala.getText().toString());
                            params.put("text4", etlebarkendala.getText().toString());
                            params.put("text5", etlebarkendala.getText().toString());
                            params.put("text6", etdesckendala.getText().toString());
                            params.put("text7", lat_awal);
                            params.put("text8", long_awal);
                            params.put("userid", dbhelper.get_tbl_username(0));
                            params.put("kendalaimg", base64Kendala);
                            return params;
                        }
                    };
                    requestQueueKendala.add(stringRequestKendala);
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photoCamera = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoCamera.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            gambar1 = stream.toByteArray();
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(gambar1, 0, gambar1.length);
            imgcamkendala.setImageBitmap(compressedBitmap);
            imgcamkendala.setBackground(null);
        }
    }

    private void preparedUserAppData(String predefinedData) {
        if (predefinedData.equals("theme")) {
            try {
                tvSystemNameFragmentHome.setTextColor(Color.parseColor(dbhelper.get_tbl_username(26)));
                btnsimpankendala.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
                btnrefresh.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(dbhelper.get_tbl_username(26))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (predefinedData.equals("sysname")) {
            try {
//                tvSystemNameFragmentHome.setText(dbhelper.get_tbl_username(25));
            } catch (Exception e) {
                e.printStackTrace();
//                tvSystemNameFragmentHome.setText("NAMA SYSTEM");
            }
        }

        if (predefinedData.equals("bgcolor")) {
            try {
                clBgMainActivity.setBackgroundColor(Color.parseColor(dbhelper.get_tbl_username(29)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void eventClickMenu() {

        linearLayoutQR.setOnClickListener(view -> ((MainActivity) getActivity()).eventShowQR(view));

        if (dbhelper.get_tbl_username(3).equals("USR")) {
            linearLayoutRKH.setVisibility(View.GONE);
        }

        if (dbhelper.get_tbl_username(3).equals("SPV")) {
            linearLayoutCarLog.setVisibility(View.GONE);
        }

        ActivityResultLauncher<Intent> intentLaunchActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == 3) {
                        acMenuRiwayatHome.setText(adapterMenuHistory.getItem(1), false);
                        loadlvinfohome(todayDate);
                        loadLvHistoryCarLog(todayDate);
                    }
                    if (result.getResultCode() == 727) {
                        loadlvinfohome(todayDate);
                    }
                }
        );

        linearLayoutAbsen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AbsensiMandiri.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutRKH.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RencanaKerjaHarian.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutCarLog.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), KartuKerjaVehicle.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutP2H.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PemeriksaanPengecekanHarian.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutBBM.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PermintaanBBM.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutService.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PermintaanPerbaikan.class);
            intentLaunchActivity.launch(intent);
        });

        loadlvinfohome(todayDate);
    }

    public static void loadlvinfohome(String selectedDate) {

        List<ParamListHomeInfo> informationsHome;
        AdapterHomeInfo adapterHomeInfo;
        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(lvfragment.getContext());

        informationsHome = new ArrayList<>();
        informationsHome.clear();
        Cursor cursor = dbhelper.listview_infohome(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ParamListHomeInfo infoListFragment = new ParamListHomeInfo(
                        cursor.getString(cursor.getColumnIndex("dataname")),
                        cursor.getString(cursor.getColumnIndex("statusupload")),
                        cursor.getString(cursor.getColumnIndex("transactiondate"))
                );
                informationsHome.add(infoListFragment);
            } while (cursor.moveToNext());
        }
        adapterHomeInfo = new AdapterHomeInfo(lvfragment.getContext(), R.layout.item_lvworkinfohome, informationsHome);
        lvfragment.setAdapter(adapterHomeInfo);
    }

    public static void loadLvHistoryApel(String selectedDate) {

        List<ListHistoryHomeApel> listApelHistories;
        HistoryHomeApelAdapter adapterLvHistory;
        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(lvHistoryApel.getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(lvHistoryApel.getContext());
        lvHistoryApel.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(lvHistoryApel.getContext(), R.drawable.divider));
        lvHistoryApel.addItemDecoration(dividerItemDecoration);

        listApelHistories = new ArrayList<>();
        listApelHistories.clear();
        final Cursor cursor = dbhelper.listview_historyapel(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ListHistoryHomeApel paramsApelHistory = new ListHistoryHomeApel(
                        cursor.getString(cursor.getColumnIndex("documentno")),
                        cursor.getString(cursor.getColumnIndex("tglapel")),
                        cursor.getString(cursor.getColumnIndex("waktuapel")),
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("jabatan")),
                        cursor.getString(cursor.getColumnIndex("jeniskehadiran")),
                        cursor.getString(cursor.getColumnIndex("absenmethod")),
                        cursor.getBlob(cursor.getColumnIndex("fotoabsen")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listApelHistories.add(paramsApelHistory);
            } while (cursor.moveToNext());
        }
        adapterLvHistory = new HistoryHomeApelAdapter(listApelHistories, lvHistoryApel.getContext());
        lvHistoryApel.setAdapter(adapterLvHistory);
    }

    public static void loadLvHistoryCarLog(String selectedDate) {

        List<ListHistoryHomeCarLog> listHistoryCarLogs;
        HistoryHomeCarLogAdapter carlogAdapter;
        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(lvHistoryCarLog.getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(lvHistoryCarLog.getContext());
        lvHistoryCarLog.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(lvHistoryCarLog.getContext(), R.drawable.divider));
        lvHistoryCarLog.addItemDecoration(dividerItemDecoration);

        listHistoryCarLogs = new ArrayList<>();
        listHistoryCarLogs.clear();
        final Cursor cursor = dbhelper.listview_historycarlog(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ListHistoryHomeCarLog paramsCarLogHistory = new ListHistoryHomeCarLog(
                        cursor.getString(cursor.getColumnIndex("documentno")),
                        cursor.getString(cursor.getColumnIndex("tglawal")),
                        cursor.getString(cursor.getColumnIndex("tglakhir")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("kmawal")),
                        cursor.getString(cursor.getColumnIndex("kmakhir")),
                        cursor.getString(cursor.getColumnIndex("kategorimuatan")),
                        cursor.getString(cursor.getColumnIndex("jenismuatan")),
                        cursor.getString(cursor.getColumnIndex("hasilkerja")),
                        cursor.getString(cursor.getColumnIndex("satuankerja")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listHistoryCarLogs.add(paramsCarLogHistory);
            } while (cursor.moveToNext());
        }
        carlogAdapter = new HistoryHomeCarLogAdapter(listHistoryCarLogs, lvHistoryCarLog.getContext());
        lvHistoryCarLog.setAdapter(carlogAdapter);
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        lat_awal = String.valueOf(latitude);
        long_awal = String.valueOf(longitude);
    }

}