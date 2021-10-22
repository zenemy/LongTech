package com.julong.longtech.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.julong.longtech.menuhistory.HistoryAdapterRKH;
import com.julong.longtech.menuhistory.ListHistoryRKH;
import com.julong.longtech.menusetup.DividerItemDecorator;
import com.julong.longtech.menuvehicle.KartuKerjaVehicle;
import com.julong.longtech.menuvehicle.NewMethodCarLog;
import com.julong.longtech.menuvehicle.NewMethodRKH;
import com.julong.longtech.menuvehicle.PemeriksaanPengecekanHarian;
import com.julong.longtech.menuinventory.PermintaanBBM;
import com.julong.longtech.menuvehicle.VerifikasiGIS;
import com.julong.longtech.menuworkshop.PermintaanPerbaikan;
import com.julong.longtech.menuvehicle.RencanaKerjaHarian;
import com.julong.longtech.menuworkshop.LaporanPerbaikan;

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
    public static TextView tvPlaceholder;
    BubbleTabBar bubbleTabBar;
    public static ListView lvfragment;
    public static RecyclerView lvHistory;

    public static AutoCompleteTextView acMenuRiwayatHome;
    EditText etDatepickerHistory;
    Button btnDateLvInfo;
    ImageButton btnrefresh, openDrawerBtn;
    String todayDate, todayDateTime, selectedHistoryMenu;

    ConstraintLayout clBgMainActivity, layoutInfoFragment;
    public static LinearLayout layoutRiwayatFragment, linearLayoutQR, linearLayoutAbsen, linearLayoutRKH, linearLayoutP2H,
            linearLayoutCarLog, linearLayoutBBM, linearLayoutService, linearLayoutApel, linearLayoutGIS;

    List<String> arrayMenuHistoryName, arrayMenuHistoryCode;
    ArrayAdapter<String> adapterMenuHistory;

    private List<ListHistoryRKH> listHistoriesRKH;
    HistoryAdapterRKH adapterLvRKH;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        dbhelper = new DatabaseHelper(getContext());
        hashPassword = new HashPassword(11);

        layoutInfoFragment = root.findViewById(R.id.layoutInfoFragment);
        tvPlaceholder = root.findViewById(R.id.tvPlaceholderInfoHome);
        btnDateLvInfo = root.findViewById(R.id.btnDateLvInfoHome);
        clBgMainActivity = root.findViewById(R.id.clBgMainActivity);
        bubbleTabBar = root.findViewById(R.id.bubbleTabBar);
        openDrawerBtn =  root.findViewById(R.id.openDrawerBtn);
        lvfragment = root.findViewById(R.id.lvInfoFragment);
        btnrefresh = root.findViewById(R.id.btnRefreshHome);
        lvHistory = root.findViewById(R.id.lvHistoryHome);
        acMenuRiwayatHome = root.findViewById(R.id.acMenuRiwayatHome);
        layoutRiwayatFragment = root.findViewById(R.id.layoutRiwayatFragment);

        etDatepickerHistory = root.findViewById(R.id.etDateHomeHistory);
        linearLayoutApel = root.findViewById(R.id.linearLayoutApel);
        linearLayoutAbsen = root.findViewById(R.id.linearLayoutAbsen);
        linearLayoutRKH = root.findViewById(R.id.linearLayoutRKH);
        linearLayoutP2H = root.findViewById(R.id.linearLayoutP2H);
        linearLayoutCarLog = root.findViewById(R.id.linearLayoutCarLog);
        linearLayoutBBM = root.findViewById(R.id.linearLayoutBBM);
        linearLayoutService = root.findViewById(R.id.linearLayoutService);
        linearLayoutQR = root.findViewById(R.id.linearLayoutMyQR);
        linearLayoutGIS = root.findViewById(R.id.linearLayoutGIS);

        todayDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etDatepickerHistory.setText(todayDate);
        btnDateLvInfo.setText(todayDate);

        eventClickMenu();

        if (lvfragment.getAdapter().getCount() > 0) {
            tvPlaceholder.setVisibility(View.GONE);
        }

        arrayMenuHistoryName = dbhelper.get_menuInfoHome(1);
        arrayMenuHistoryCode = dbhelper.get_menuInfoHome(0);

        adapterMenuHistory = new ArrayAdapter<String>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, arrayMenuHistoryName);
        acMenuRiwayatHome.setAdapter(adapterMenuHistory);

        openDrawerBtn.setOnClickListener(v -> ((MainActivity) getActivity()).openDrawer());

        acMenuRiwayatHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedHistoryMenu = arrayMenuHistoryCode.get(position);

                etDatepickerHistory.setText(todayDate);

                if (selectedHistoryMenu.equals("010105")) {
                    loadLvHistoryApel(etDatepickerHistory.getText().toString());
                } else if (selectedHistoryMenu.equals("020203")) {
                    loadLvHistoryCarLog(etDatepickerHistory.getText().toString());
                } else if (selectedHistoryMenu.equals("020201")) {
//                    loadListViewHistoryRKH(etDatepickerHistory.getText().toString());
                }

            }
        });

        MaterialDatePicker<Long> datePickerLvHistory = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        MaterialDatePicker<Long> datePickerLvInfo = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        etDatepickerHistory.setOnClickListener(new View.OnClickListener() {
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

                etDatepickerHistory.setText(simpleFormat.format(date));

                if (selectedHistoryMenu.equals("010105")) {
                    loadLvHistoryApel(simpleFormat.format(date));
                } else if (selectedHistoryMenu.equals("020203")) {
                    loadLvHistoryCarLog(simpleFormat.format(date));
                } else if (selectedHistoryMenu.equals("020201")) {
//                    loadListViewHistoryRKH(simpleFormat.format(date));
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

        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int id) {
                switch (id) {
                    case R.id.homefragment:
                        layoutInfoFragment.setVisibility(View.VISIBLE);
                        layoutRiwayatFragment.setVisibility(View.GONE);
                        break;
                    case R.id.log:
                        layoutRiwayatFragment.setVisibility(View.VISIBLE);
                        layoutInfoFragment.setVisibility(View.GONE);
                        break;
                }

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                    loadlvinfohome(todayDate);
                    loadLvHistoryCarLog(todayDate);
                    if (lvfragment.getAdapter().getCount() > 0) {
                        tvPlaceholder.setVisibility(View.GONE);
                    }
                }
                if (result.getResultCode() == 4) {
                    loadlvinfohome(todayDate);
                    loadLvHistoryApel(todayDate);
                    if (lvfragment.getAdapter().getCount() > 0) {
                        tvPlaceholder.setVisibility(View.GONE);
                    }
                }
                if (result.getResultCode() == 727) {
                    loadlvinfohome(todayDate);
                    if (lvfragment.getAdapter().getCount() > 0) {
                        tvPlaceholder.setVisibility(View.GONE);
                    }
                }
            }
        );

        linearLayoutBBM.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PermintaanBBM.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutGIS.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), VerifikasiGIS.class);
            intentLaunchActivity.launch(intent);
        });

        if (dbhelper.get_tbl_username(3).equals("SPV-WS")) {
            linearLayoutService.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LaporanPerbaikan.class);
                intentLaunchActivity.launch(intent);
            });
        }
        else if (dbhelper.get_tbl_username(3).equals("SPV-TRS")) {
            linearLayoutService.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PermintaanPerbaikan.class);
                intentLaunchActivity.launch(intent);
            });
        }

        linearLayoutApel.setOnClickListener(v -> {
            final SweetAlertDialog startApelDlg = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
            startApelDlg.setTitleText("Mulai briefing?");
            startApelDlg.setCancelText("KEMBALI");
            startApelDlg.setConfirmText("MULAI");
            startApelDlg.showCancelButton(true);
            startApelDlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    Intent intent = new Intent(getActivity(), ApelPagi.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentLaunchActivity.launch(intent);
                    onPause();
                }
            });
            startApelDlg.show();
        });

        linearLayoutAbsen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AbsensiMandiri.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutRKH.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewMethodRKH.class);
            intentLaunchActivity.launch(intent);
        });

        linearLayoutCarLog.setOnClickListener(v -> {

            Dialog dlgStartCarLog = new Dialog(getContext());
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
            adapterVehicleCarLog = new ArrayAdapter<>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, listVehicleCarLog);
            acDlgVehicleCarLog.setAdapter(adapterVehicleCarLog);

            adapterShiftCarLog = new ArrayAdapter<String>(getContext(), R.layout.spinnerlist, R.id.spinnerItem, arrayShiftCarLog);
            acDlgShiftCarLog.setAdapter(adapterShiftCarLog);

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
                    dbhelper.update_ancakcode_user(selectedCarLogVehicle, acDlgShiftCarLog.getText().toString());

                    Intent intent = new Intent(getActivity(), NewMethodCarLog.class);
                    intentLaunchActivity.launch(intent);
                }
            });


        });

        linearLayoutP2H.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PemeriksaanPengecekanHarian.class);
            intentLaunchActivity.launch(intent);
        });

        loadlvinfohome(todayDate);

        if (lvfragment.getAdapter().getCount() > 0) {
            tvPlaceholder.setVisibility(View.GONE);
        }
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
        dbhelper = new DatabaseHelper(lvHistory.getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(lvHistory.getContext());
        lvHistory.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                lvHistory.getContext(), layoutManager.getOrientation());
        lvHistory.addItemDecoration(dividerItemDecoration);

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
        adapterLvHistory = new HistoryHomeApelAdapter(listApelHistories, lvHistory.getContext());
        lvHistory.setAdapter(adapterLvHistory);
    }

    public static void loadLvHistoryCarLog(String selectedDate) {

        List<ListHistoryHomeCarLog> listHistoryCarLogs;
        HistoryHomeCarLogAdapter carlogAdapter;
        DatabaseHelper dbhelper;
        dbhelper = new DatabaseHelper(lvHistory.getContext());

        LinearLayoutManager layoutCarLog = new LinearLayoutManager(lvHistory.getContext());
        lvHistory.setLayoutManager(layoutCarLog);

        listHistoryCarLogs = new ArrayList<>();
        listHistoryCarLogs.clear();
        final Cursor cursor = dbhelper.listview_historycarlog(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ListHistoryHomeCarLog paramsCarLogHistory = new ListHistoryHomeCarLog(
                        cursor.getString(cursor.getColumnIndex("timetr")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("divisi")),
                        cursor.getString(cursor.getColumnIndex("blok")),
                        cursor.getString(cursor.getColumnIndex("kmawal")),
                        cursor.getString(cursor.getColumnIndex("kmakhir")),
                        cursor.getString(cursor.getColumnIndex("activity")),
                        cursor.getString(cursor.getColumnIndex("hasilkerja")),
                        cursor.getString(cursor.getColumnIndex("satuankerja")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listHistoryCarLogs.add(paramsCarLogHistory);
            } while (cursor.moveToNext());
        }
        carlogAdapter = new HistoryHomeCarLogAdapter(listHistoryCarLogs, lvHistory.getContext());
        lvHistory.setAdapter(carlogAdapter);
    }

    private void loadListViewHistoryRKH(String selectedDate) {

        LinearLayoutManager layoutRKH = new LinearLayoutManager(lvHistory.getContext());
        lvHistory.setLayoutManager(layoutRKH);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                lvHistory.getContext(), layoutRKH.getOrientation());
        lvHistory.addItemDecoration(dividerItemDecoration);

        listHistoriesRKH = new ArrayList<>();
        listHistoriesRKH.clear();

        final Cursor cursor = dbhelper.listview_historyRKH(selectedDate);
        if (cursor.moveToFirst()) {
            do {
                ListHistoryRKH paramsHistoryRKH = new ListHistoryRKH(
                        cursor.getString(cursor.getColumnIndex("documentno")),
                        cursor.getString(cursor.getColumnIndex("tglinput")),
                        cursor.getString(cursor.getColumnIndex("tglpelaksannaan")),
                        cursor.getString(cursor.getColumnIndex("empname")),
                        cursor.getString(cursor.getColumnIndex("activity")),
                        cursor.getString(cursor.getColumnIndex("blok")),
                        cursor.getString(cursor.getColumnIndex("unitcode")),
                        cursor.getString(cursor.getColumnIndex("shiftcode")),
                        cursor.getInt(cursor.getColumnIndex("uploaded"))
                );
                listHistoriesRKH.add(paramsHistoryRKH);
            } while (cursor.moveToNext());
        }
        adapterLvRKH = new HistoryAdapterRKH(listHistoriesRKH, getContext());
        lvHistory.setAdapter(adapterLvRKH);
    }



}