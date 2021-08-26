package com.julong.longtech.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.julong.longtech.menuhcm.ApelPagi;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menusetup.UploadData;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.PemeriksaanPengecekanHarian;
import com.julong.longtech.menuinventory.PermintaanBBM;
import com.julong.longtech.menuworkshop.PerintahPerbaikan;
import com.julong.longtech.menuworkshop.PermintaanPerbaikan;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;
import com.julong.longtech.menuworkshop.ProsesPerbaikan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.DatabaseHelper.url_api;
import static com.julong.longtech.menusetup.UploadData.uploadBL01;
import static com.julong.longtech.menusetup.UploadData.uploadTR01public;
import static com.julong.longtech.menusetup.UploadData.uploadTR02;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    HashPassword hashPassword;
    ActivityResultLauncher<Intent> intentLaunchActivity;

    DatabaseHelper dbhelper;
    public static TextView tvjabatanuser, tvnamauser;
    BubbleTabBar bubbleTabBar;
    public static ListView lvfragment;
    public static RecyclerView lvHistoryApel, lvHistoryCarLog;
    byte[] gambar1;
    public static AutoCompleteTextView ackendala, acMenuRiwayatHome;
    TextView tvSystemNameFragmentHome;
    EditText etdesckendala, etpanjangkendala, etlebarkendala, etluaskendala, aclokasikendala, filtertglhistory;
    Button btnsimpankendala, btnDateLvInfo;
    ImageButton btnrefresh, openDrawerBtn, imgcamkendala;
    String lat_awal, long_awal, todayDate, todayDateTime, selectedKendala;
    ScrollView scrollkendala;
    ConstraintLayout clBgMainActivity;
    public static LinearLayout layoutRiwayatFragment, layoutInfoFragment, linearLayoutQR, linearLayoutAbsen, linearLayoutRKH, linearLayoutP2H,
            linearLayoutCarLog, linearLayoutBBM, linearLayoutService, linearLayoutApel;

    List<String> arrayMenuHistory = Arrays.asList("APEL KERJA", "CAR LOG");
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
        linearLayoutApel = root.findViewById(R.id.linearLayoutApel);
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

        adapterMenuHistory = new ArrayAdapter<String>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, arrayMenuHistory);
        acMenuRiwayatHome.setAdapter(adapterMenuHistory);

        listKendalaCode = dbhelper.get_menukendala(0);
        listKendalaName = dbhelper.get_menukendala(1);
        adapterKendala = new ArrayAdapter<String>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, listKendalaName);
        ackendala.setAdapter(adapterKendala);

        ackendala.setOnItemClickListener((adapterView, view, position, l) -> selectedKendala = listKendalaCode.get(position));

        openDrawerBtn.setOnClickListener(v -> ((MainActivity) getActivity()).openDrawer());

        acMenuRiwayatHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selected = (String) adapterView.getItemAtPosition(position);

                if (selected.equals("APEL KERJA")) {
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

                if (acMenuRiwayatHome.getText().toString().equals("APEL KERJA")) {
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

        if (requestCode == 727) {
            loadlvinfohome(todayDate);
        }
    }

    private void eventClickMenu() {

        btnrefresh.setOnClickListener(view -> new AsyncUploadData().execute());

        linearLayoutQR.setOnClickListener(view -> ((MainActivity) getActivity()).eventShowQR(view));

        intentLaunchActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 3) {

                    acMenuRiwayatHome.setText(arrayMenuHistory.get(1));
                    acMenuRiwayatHome.setSelection(1);
                    loadlvinfohome(todayDate);
                    loadLvHistoryCarLog(todayDate);
                }
                if (result.getResultCode() == 4) {
                    acMenuRiwayatHome.setText(arrayMenuHistory.get(0));
                    acMenuRiwayatHome.setSelection(0);
                    loadlvinfohome(todayDate);
                    loadLvHistoryApel(todayDate);
                }
                if (result.getResultCode() == 727) {
                    loadlvinfohome(todayDate);
                }
            }
        );

        linearLayoutBBM.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PermintaanBBM.class);
            intentLaunchActivity.launch(intent);
        });

        if (dbhelper.get_tbl_username(3).equals("SPV-WS")) {
            linearLayoutService.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PerintahPerbaikan.class);
                intentLaunchActivity.launch(intent);
            });
        }

        else if (dbhelper.get_tbl_username(3).equals("SPV-TRS")) {
            linearLayoutService.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PermintaanPerbaikan.class);
                intentLaunchActivity.launch(intent);
            });
        }
        else if (dbhelper.get_tbl_username(3).equals("MEK")) {
            linearLayoutService.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProsesPerbaikan.class);
                intentLaunchActivity.launch(intent);
            });
        }

        linearLayoutApel.setOnClickListener(v -> {
            if (dbhelper.get_statusapelpagi(0).equals("1")) {
                Intent intent = new Intent(getActivity(), ApelPagi.class);
                intent.putExtra("shiftapel", dbhelper.get_statusapelpagi(2));
                intentLaunchActivity.launch(intent);
                onPause();
            } else {
                String[] arrayShiftApel = {"Shift 1", "Shift 2", "Shift 3"};
                ArrayAdapter<String> adapterShiftApel;

                Dialog dlgStartApel = new Dialog(getContext());
                dlgStartApel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dlgStartApel.setContentView(R.layout.dialog_startapel);
                dlgStartApel.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                Window windowStartApel = dlgStartApel.getWindow();
                windowStartApel.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                AutoCompleteTextView acShiftStartApel = dlgStartApel.findViewById(R.id.acShiftStartApel);
                Button btnOkStartApel = dlgStartApel.findViewById(R.id.btnOkDlgShiftApel);
                Button btnBackStartApel = dlgStartApel.findViewById(R.id.btnBackDlgShiftApel);

                btnBackStartApel.setOnClickListener(view12 -> dlgStartApel.dismiss());
                adapterShiftApel = new ArrayAdapter<>(getActivity(), R.layout.spinnerlist, R.id.spinnerItem, arrayShiftApel);
                acShiftStartApel.setAdapter(adapterShiftApel);

                btnOkStartApel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlgStartApel.dismiss();
                        Intent intent = new Intent(getActivity(), ApelPagi.class);
                        intent.putExtra("shiftapel", acShiftStartApel.getText().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLaunchActivity.launch(intent);
                        onPause();
                    }
                });
                dlgStartApel.show();
            }
        });

        linearLayoutAbsen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AbsensiMandiri.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutRKH.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RencanaKerjaHarian.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutCarLog.setOnClickListener(v -> {

            Dialog dlgStartCarLog = new Dialog(getContext());
            dlgStartCarLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlgStartCarLog.setContentView(R.layout.dlg_startcarlog);
            dlgStartCarLog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Window windowStartCarLog = dlgStartCarLog.getWindow();
            windowStartCarLog.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            AutoCompleteTextView acDlgVehicleCarLog = dlgStartCarLog.findViewById(R.id.acDlgVehicleCarLog);
            Button btnCancelDlgVehicleCarLog = dlgStartCarLog.findViewById(R.id.btnCancelDlgVehicleCarLog);
            Button btnSimpanDlgVehicleCarLog = dlgStartCarLog.findViewById(R.id.btnSimpanDlgVehicleCarLog);
            dlgStartCarLog.show();

            btnCancelDlgVehicleCarLog.setOnClickListener(view -> dlgStartCarLog.dismiss());

            List<String> listVehicleCarLog;
            ArrayAdapter<String> adapterVehicleCarLog;

            acDlgVehicleCarLog.setText(dbhelper.get_vehiclename(2, dbhelper.get_tbl_username(19)));

            listVehicleCarLog = dbhelper.get_vehiclemasterdata();
            adapterVehicleCarLog = new ArrayAdapter<>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, listVehicleCarLog);
            acDlgVehicleCarLog.setAdapter(adapterVehicleCarLog);

            acDlgVehicleCarLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    InputMethodManager keyboardMgr = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    keyboardMgr.hideSoftInputFromWindow(acDlgVehicleCarLog.getWindowToken(), 0);
                }
            });

            btnSimpanDlgVehicleCarLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgStartCarLog.dismiss();
                    String selectedCarLogVehicle = dbhelper.get_vehiclecodeonly(acDlgVehicleCarLog.getText().toString());
                    dbhelper.update_ancakcode_user(selectedCarLogVehicle);

                    Intent intent = new Intent(getActivity(), KartuKerjaVehicle.class);
                    intentLaunchActivity.launch(intent);
                }
            });


        });

        linearLayoutP2H.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PemeriksaanPengecekanHarian.class);
            intentLaunchActivity.launch(intent);
        });

        loadlvinfohome(todayDate);
    }

    private class AsyncUploadData extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {

            try { // Transaction 01
                Cursor cursorTransaction1 = dbhelper.view_upload_tr01();
                if (cursorTransaction1.moveToFirst()) {
                    do {
                        uploadTR01public(getActivity(),
                                cursorTransaction1.getInt(0), cursorTransaction1.getString(1),
                                cursorTransaction1.getString(2), cursorTransaction1.getString(3),
                                cursorTransaction1.getString(4), cursorTransaction1.getString(5),
                                cursorTransaction1.getString(6), cursorTransaction1.getString(7),
                                cursorTransaction1.getString(8), cursorTransaction1.getString(9),
                                cursorTransaction1.getString(10), cursorTransaction1.getString(11),
                                cursorTransaction1.getString(12), cursorTransaction1.getString(13),
                                cursorTransaction1.getString(14), cursorTransaction1.getString(15),
                                cursorTransaction1.getString(16), cursorTransaction1.getString(17),
                                cursorTransaction1.getString(18), cursorTransaction1.getString(19),
                                cursorTransaction1.getString(20), cursorTransaction1.getString(21),
                                cursorTransaction1.getString(22), cursorTransaction1.getString(23),
                                cursorTransaction1.getString(24), cursorTransaction1.getString(25),
                                cursorTransaction1.getString(26), cursorTransaction1.getString(27),
                                cursorTransaction1.getString(28), cursorTransaction1.getString(29),
                                cursorTransaction1.getString(30), cursorTransaction1.getString(31),
                                cursorTransaction1.getString(32), cursorTransaction1.getString(33),
                                cursorTransaction1.getString(34), cursorTransaction1.getString(35),
                                cursorTransaction1.getString(36), cursorTransaction1.getString(37),
                                cursorTransaction1.getString(38), cursorTransaction1.getString(39),
                                cursorTransaction1.getString(40)
                        );
                    } while (cursorTransaction1.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Transaction 02
            try {
                Cursor cursorTransaction2 = dbhelper.view_upload_tr02();
                if (cursorTransaction2.moveToFirst()) {
                    do {
                        uploadTR02(getActivity(),
                                cursorTransaction2.getInt(0), cursorTransaction2.getString(1),
                                cursorTransaction2.getString(2), cursorTransaction2.getString(3),
                                cursorTransaction2.getString(4), cursorTransaction2.getString(5),
                                cursorTransaction2.getString(6), cursorTransaction2.getString(7),
                                cursorTransaction2.getString(8), cursorTransaction2.getString(9),
                                cursorTransaction2.getString(10), cursorTransaction2.getString(11),
                                cursorTransaction2.getString(12), cursorTransaction2.getString(13),
                                cursorTransaction2.getString(14), cursorTransaction2.getString(15),
                                cursorTransaction2.getString(16), cursorTransaction2.getString(17),
                                cursorTransaction2.getString(18), cursorTransaction2.getString(19),
                                cursorTransaction2.getString(20), cursorTransaction2.getString(21),
                                cursorTransaction2.getString(22), cursorTransaction2.getString(23),
                                cursorTransaction2.getString(24), cursorTransaction2.getString(25),
                                cursorTransaction2.getString(26), cursorTransaction2.getString(27),
                                cursorTransaction2.getString(28), cursorTransaction2.getString(29),
                                cursorTransaction2.getString(30), cursorTransaction2.getString(31),
                                cursorTransaction2.getString(32), cursorTransaction2.getString(33),
                                cursorTransaction2.getString(34), cursorTransaction2.getString(35),
                                cursorTransaction2.getString(36), cursorTransaction2.getString(37),
                                cursorTransaction2.getString(38), cursorTransaction2.getString(39),
                                cursorTransaction2.getString(40), cursorTransaction2.getString(41),
                                cursorTransaction2.getString(42)
                        );
                    } while (cursorTransaction2.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Blob 01
            try {
                Cursor cursorImg = dbhelper.view_upload_bl01();
                if (cursorImg.moveToFirst()) {
                    do {

                        String base64blob1 = android.util.Base64.encodeToString(cursorImg.getBlob(9),  android.util.Base64.DEFAULT);
                        String base64blob2 = android.util.Base64.encodeToString(cursorImg.getBlob(10),  android.util.Base64.DEFAULT);
                        String base64blob3 = android.util.Base64.encodeToString(cursorImg.getBlob(11),  android.util.Base64.DEFAULT);
                        String base64blob4 = android.util.Base64.encodeToString(cursorImg.getBlob(12),  android.util.Base64.DEFAULT);
                        String base64blob5 = android.util.Base64.encodeToString(cursorImg.getBlob(13),  android.util.Base64.DEFAULT);

                        uploadBL01(getActivity(),
                                cursorImg.getInt(0), cursorImg.getString(1),
                                cursorImg.getString(2), cursorImg.getString(3),
                                cursorImg.getString(4), cursorImg.getString(5),
                                cursorImg.getString(6), cursorImg.getString(7),
                                cursorImg.getString(8), base64blob1, base64blob2,
                                base64blob3, base64blob4, base64blob5
                        );

                    } while (cursorImg.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public static void loadlvinfohome(String selectedDate) {

        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(lvfragment.getContext());

        List<ParamListHomeInfo> informationsHome;
        AdapterHomeInfo adapterHomeInfo;

        informationsHome = new ArrayList<>();
        informationsHome.clear();
        Cursor cursor = dbhelper.listview_infohome(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ParamListHomeInfo infoListFragment = new ParamListHomeInfo(
                        cursor.getString(cursor.getColumnIndex("menucode")),
                        cursor.getString(cursor.getColumnIndex("dataname")),
                        cursor.getString(cursor.getColumnIndex("statusupload")),
                        cursor.getString(cursor.getColumnIndex("transactiondate")),
                        cursor.getString(cursor.getColumnIndex("documentno"))
                );
                informationsHome.add(infoListFragment);
            } while (cursor.moveToNext());
        }
        adapterHomeInfo = new AdapterHomeInfo(lvfragment.getContext(), informationsHome);
        lvfragment.setAdapter(adapterHomeInfo);
        adapterHomeInfo.notifyDataSetChanged();
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