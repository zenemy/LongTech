package com.julong.longtech.menureport;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuhistory.HistoryAdapterRKH;
import com.julong.longtech.menuhistory.HistoryApelAdapter;
import com.julong.longtech.menuhistory.ListHistoryApel;
import com.julong.longtech.menuhistory.ListHistoryRKH;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ReportActivity extends AppCompatActivity {


    DatabaseHelper dbhelper;
    SweetAlertDialog progressDialog;
    String selectedReportGroup, selectedReportMenu, selectedDate, selectedTeamCode, selectedOperator;

    EditText etDateReport;
    AutoCompleteTextView acCarLogVerified, acEmployee, acSelectReportMenu, acVehicleReport, acTeamSelect;
    TextInputLayout inputLayoutAcVehicle, inputLayoutDate, inputLayoutAcOpr, inputLayoutTeamSelect, inputLayoutVerified;
    RecyclerView lvReport;
    Button btnResetReport;

    List<ListReportCarLog> listReportCarLogs;
    AdapterReportCarLog adapterLvCarlog;

    List<ListHistoryApel> listReportApels;
    HistoryApelAdapter adapterLvApel;

    List<ListHistoryRKH> listReportRKH;
    AdapterReportRKH adapterLvRKH;

    List<ListReportP2H> listReportP2H;
    AdapterReportP2H adapterLvP2H;

    List<ListReportMintaBBM> listReportReqBBM;
    AdapterReportMintaBBM adapterReqBBM;

    List<ListReportGIS> listReportGIS;
    AdapterReportGIS adapterLvGIS;

    String[] arrayVerifiedCarLog = {"SUDAH VERIFIKASI", "BELUM VERIFIKASI"};
    ArrayAdapter<String> adapterVerified;

    ArrayList<String> listReportMenuGroup = new ArrayList<>();
    ArrayList<String> listReportMenuName = new ArrayList<>();
    ArrayList<String> listReportMenuCode = new ArrayList<>();
    ArrayAdapter<String> adapterReportMenu;

    private List<String> listTeamName, listTeamCode, listVehicleReport, listOperator;
    ArrayAdapter<String> adapterTeamSelect, adapterVehicleReport, adapterOperator;

    ActivityResultLauncher<Intent> intentLaunchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbhelper = new DatabaseHelper(this);

        lvReport = findViewById(R.id.lvReportCarLog);
        acEmployee = findViewById(R.id.acSelectEmpReport);
        acTeamSelect = findViewById(R.id.acSelectTeamReport);
        etDateReport = findViewById(R.id.etDateReportActivity);
        acCarLogVerified = findViewById(R.id.acCarLogVerifiedGIS);
        acVehicleReport = findViewById(R.id.acUnitReportActivity);
        inputLayoutAcOpr = findViewById(R.id.inputLayoutEmpReport);
        acSelectReportMenu = findViewById(R.id.acSelectReportMenu);
        inputLayoutDate = findViewById(R.id.inputLayoutDateReport);
        btnResetReport = findViewById(R.id.btnResetReportActivity);
        inputLayoutTeamSelect = findViewById(R.id.inputLayoutTeamReport);
        inputLayoutVerified = findViewById(R.id.inputLayoutAcVerifiedGIS);
        inputLayoutAcVehicle = findViewById(R.id.inputLayoutAcVehicleRpeort);

        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Loading");
        progressDialog.setCancelable(false);

        btnResetReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = null;
                selectedReportGroup = null;
                selectedReportMenu = null;
                selectedTeamCode = null;
                selectedOperator = null;
                acEmployee.setText(null);
                etDateReport.setText(null);
                acCarLogVerified.setText(null);
                acSelectReportMenu.setText(null);
                acTeamSelect.setText(null);
                acVehicleReport.setText(null);
                lvReport.setAdapter(null);
            }
        });

        if (dbhelper.get_tbl_username(2).equals("MGR") || dbhelper.get_tbl_username(3).equals("MGR")
                || dbhelper.get_tbl_username(2).equals("GIS") || dbhelper.get_tbl_username(3).equals("GIS")) {
            selectedTeamCode = null;
            inputLayoutTeamSelect.setVisibility(View.VISIBLE);
        } else {
            selectedTeamCode = dbhelper.get_tbl_username(18);
        }

        populateDropdownReportMenu(dbhelper.get_tbl_username(3));

        MaterialDatePicker<Long> reportDatepicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        etDateReport.setOnClickListener(v -> reportDatepicker.show(getSupportFragmentManager(), "REPORT_DATEPICKER"));

        // Populate dropdown Tipe Pekerjaan
        acCarLogVerified.setKeyListener(null);
        adapterVerified = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayVerifiedCarLog);
        acCarLogVerified.setAdapter(adapterVerified);

        reportDatepicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Get the offset from our timezone and UTC.
                TimeZone timeZoneUTC = TimeZone.getDefault();
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                // Create a date format, then a date object with our offset
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date(selection + offsetFromUTC);

                etDateReport.setText(simpleFormat.format(date));
                selectedDate = simpleFormat.format(date);

            }
        });

        acSelectReportMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedReportGroup = listReportMenuGroup.get(position);
                selectedReportMenu = listReportMenuCode.get(position);

                if (selectedReportMenu.equals("020202")) {
                    inputLayoutVerified.setVisibility(View.GONE);
                    inputLayoutAcOpr.setVisibility(View.GONE);
                    inputLayoutAcVehicle.setVisibility(View.VISIBLE);
                    inputLayoutDate.setPadding(6, 0, 0, 0);
                }
                else if (selectedReportMenu.equals("020203")) {
                    inputLayoutAcVehicle.setVisibility(View.GONE);
                    inputLayoutVerified.setVisibility(View.VISIBLE);
                    inputLayoutDate.setPadding(6, 0, 0, 0);

                    if (dbhelper.get_tbl_username(2).equals("SPV") || dbhelper.get_tbl_username(2).equals("GIS")) {
                        inputLayoutAcOpr.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    inputLayoutAcVehicle.setVisibility(View.GONE);
                    inputLayoutVerified.setVisibility(View.GONE);
                    inputLayoutAcOpr.setVisibility(View.GONE);
                    inputLayoutDate.setPadding(0, 0, 0, 0);
                }
            }
        });

        acTeamSelect.setOnItemClickListener(
                (adapterView, view, position, l) -> selectedTeamCode = listTeamCode.get(position));

        acEmployee.setOnItemClickListener((adapterView, view, position, l)
                -> selectedOperator = dbhelper.get_empcode(0, acEmployee.getText().toString()));

        intentLaunchActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == 727) {
                    lvReport.setAdapter(null);
                    selectedTeamCode = result.getData().getStringExtra("teamcode");
                    selectedDate = result.getData().getStringExtra("workdate");

                    inputLayoutVerified.setVisibility(View.VISIBLE);
                    acCarLogVerified.setText("SUDAH VERIFIKASI");
                    etDateReport.setText(selectedDate);
                    loadLvReportCarLogVerified(selectedTeamCode, 1);

                    adapterVerified = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayVerifiedCarLog);
                    acCarLogVerified.setAdapter(adapterVerified);
                } else if (result.getResultCode() == 82) {
                    lvReport.setAdapter(null);
                    selectedTeamCode = result.getData().getStringExtra("teamcode");
                    selectedDate = result.getData().getStringExtra("workdate");

                    inputLayoutVerified.setVisibility(View.VISIBLE);
                    acCarLogVerified.setText("BELUM VERIFIKASI");
                    etDateReport.setText(selectedDate);
                    loadLvReportCarLogUnverified(selectedTeamCode, 0);

                    adapterVerified = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, arrayVerifiedCarLog);
                    acCarLogVerified.setAdapter(adapterVerified);
                }
            }
        );
    }

    public void showReport(View v) {
        progressDialog.show();
        lvReport.setAdapter(null);
        if (selectedReportMenu == null && TextUtils.isEmpty(etDateReport.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Masukkan filter").setConfirmText("OK").show();
        }
        else if (selectedReportMenu.equals("010105")) {
            loadLvReportApel(selectedTeamCode);
        }
        else if (selectedReportMenu.equals("020201")) {
            loadLvReportRKH(selectedTeamCode);
        }
        else if (selectedReportMenu.equals("020202")) {
            loadLvReportP2H(acVehicleReport.getText().toString());
        }
        else if (selectedReportMenu.equals("020203")) {
            if (acCarLogVerified.getText().toString().equals("BELUM VERIFIKASI")) {
                loadLvReportCarLogUnverified(selectedTeamCode, 0);
            } else if (acCarLogVerified.getText().toString().equals("SUDAH VERIFIKASI")) {
                loadLvReportCarLogVerified(selectedTeamCode, 1);
            }
        }
        else if (selectedReportMenu.equals("030101")) {
            loadLvReportMintaBBM();
        }

    }

    private void populateDropdownReportMenu(String usertype) {
        progressDialog.show();
        String url_reportmenu = url_api + "fetchdata/reportmenu/get_reportmenu.php?rolecode=" + usertype;
        JsonObjectRequest jsonRequestUserType = new JsonObjectRequest(Request.Method.GET, url_reportmenu, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayTypeUser = response.getJSONArray("DROPDOWNMENUREPORT");

                    for (int i = 0; i < jsonArrayTypeUser.length(); i++) {
                        JSONObject jsonObjectTypeUser = jsonArrayTypeUser.getJSONObject(i);
                        String groupCode = jsonObjectTypeUser.getString("GROUPCODE");
                        String menuCode = jsonObjectTypeUser.getString("MENUCODE");
                        String menuName = jsonObjectTypeUser.getString("MENUNAME");

                        listReportMenuGroup.add(groupCode);
                        listReportMenuCode.add(menuCode);
                        listReportMenuName.add(menuName);

                    }

                    adapterReportMenu = new ArrayAdapter<>(ReportActivity.this, R.layout.spinnerlist, R.id.spinnerItem, listReportMenuName);
                    acSelectReportMenu.setAdapter(adapterReportMenu);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Periksa Jaringan").show();
            }
        });
        jsonRequestUserType.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(jsonRequestUserType);

        // Populate dropdown team
        listTeamCode = dbhelper.get_teamname(0);
        listTeamName = dbhelper.get_teamname(1);
        adapterTeamSelect = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listTeamName);
        acTeamSelect.setAdapter(adapterTeamSelect);

        // Populate dropdown vehicle
        listVehicleReport = dbhelper.get_vehiclecodelist();
        adapterVehicleReport = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleReport);
        acVehicleReport.setAdapter(adapterVehicleReport);

        // Populate dropdown operator
        listOperator = dbhelper.get_operatoronly();
        adapterOperator = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listOperator);
        acEmployee.setAdapter(adapterOperator);
    }

    private void loadLvReportCarLogVerified(String teamcode, int isVerified) {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportCarLogs = new ArrayList<>();
        listReportCarLogs.clear();

        Map<String, String> params = new HashMap();
        params.put("selectdate", selectedDate);
        params.put("teamcode", teamcode);
        params.put("smoothopr", selectedOperator);
        JSONObject postParameters = new JSONObject(params);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_carlog_verified.php?selectdate="+ selectedDate + "&teamcode=" + teamcode;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url_data, postParameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("REPORTCARLOG");
                    int i = 0;
                    while (i < jsonArray.length()) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ListReportCarLog paramsCarLogReport = new ListReportCarLog(
                                jsonObject1.getString("DOCUMENTNO"),
                                jsonObject1.getString("TGLINPUT"),
                                jsonObject1.getString("JAMINPUT"),
                                jsonObject1.getString("VEHICLE"),
                                jsonObject1.getString("FIRSTNAME"),
                                jsonObject1.getString("LASTNAME"),
                                jsonObject1.getString("EMPCODE"),
                                jsonObject1.getString("EMPNAME"),
                                jsonObject1.getString("LOCATION"),
                                jsonObject1.getString("ACTIVITY"),
                                jsonObject1.getString("HASILKERJA"),
                                jsonObject1.getString("SATUANKERJA"),
                                jsonObject1.getString("CATATANKERJA"),
                                jsonObject1.getString("KMAWAL"),
                                jsonObject1.getString("KMAKHIR")
                        );
                        listReportCarLogs.add(paramsCarLogReport);
                        i++;
                    }
                    adapterLvCarlog = new AdapterReportCarLog(listReportCarLogs, ReportActivity.this,
                            intentLaunchActivity, isVerified, teamcode);
                    lvReport.setAdapter(adapterLvCarlog);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Empty Data").show();
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void loadLvReportCarLogUnverified(String teamcode, int isVerified) {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportCarLogs = new ArrayList<>();
        listReportCarLogs.clear();

        Map<String, String> params = new HashMap();
        params.put("selectdate", selectedDate);
        params.put("teamcode", teamcode);
        params.put("smoothopr", selectedOperator);
        JSONObject postParameters = new JSONObject(params);

        Log.e("paramsUnverifiedCarlog", params.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_carlog_unverified.php?selectdate="+ selectedDate + "&teamcode=" + teamcode;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                url_data, postParameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("REPORTCARLOG");
                    int i = 0;
                    while (i < jsonArray.length()) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ListReportCarLog paramsCarLogReport = new ListReportCarLog(
                                jsonObject1.getString("DOCUMENTNO"),
                                jsonObject1.getString("TGLINPUT"),
                                jsonObject1.getString("JAMINPUT"),
                                jsonObject1.getString("VEHICLE"),
                                jsonObject1.getString("FIRSTNAME"),
                                jsonObject1.getString("LASTNAME"),
                                jsonObject1.getString("EMPCODE"),
                                jsonObject1.getString("EMPNAME"),
                                jsonObject1.getString("LOCATION"),
                                jsonObject1.getString("ACTIVITY"),
                                jsonObject1.getString("HASILKERJA"),
                                jsonObject1.getString("SATUANKERJA"),
                                jsonObject1.getString("CATATANKERJA"),
                                jsonObject1.getString("KMAWAL"),
                                jsonObject1.getString("KMAKHIR")
                        );
                        listReportCarLogs.add(paramsCarLogReport);
                        i++;
                    }
                    adapterLvCarlog = new AdapterReportCarLog(listReportCarLogs, ReportActivity.this,
                            intentLaunchActivity, isVerified, teamcode);
                    lvReport.setAdapter(adapterLvCarlog);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Empty Data").show();
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void loadLvReportApel(String teamcode) {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportApels = new ArrayList<>();
        listReportApels.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_apel.php?teamcode="+ teamcode + "&selectdate=" + selectedDate;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("REPORTAPEL")) {
                        JSONArray jsonArrayApel = response.getJSONArray("REPORTAPEL");
                        int i = 0;
                        while (i < jsonArrayApel.length()) {
                            JSONObject jsonObjectApel = jsonArrayApel.getJSONObject(i);

                            byte[] decodedApelImg = Base64.decode(jsonObjectApel.getString("FOTOABSEN"), Base64.DEFAULT);

                            ListHistoryApel paramsApelReport = new ListHistoryApel(
                                    jsonObjectApel.getString("DOCUMENTNO"),
                                    jsonObjectApel.getString("TGLAPEL"),
                                    jsonObjectApel.getString("JAMAPEL"),
                                    jsonObjectApel.getString("EMPNAME"),
                                    jsonObjectApel.getString("JABATAN"),
                                    jsonObjectApel.getString("JENISKEHADIRAN"),
                                    jsonObjectApel.getString("ABSENMETHOD"),
                                    decodedApelImg, 2
                            );
                            listReportApels.add(paramsApelReport);
                            i++;
                        }
                        adapterLvApel = new HistoryApelAdapter(listReportApels, ReportActivity.this);
                        lvReport.setAdapter(adapterLvApel);

                    } else {
                        lvReport.setAdapter(null);

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Empty Data").show();
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void loadLvReportRKH(String teamcode) {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportRKH = new ArrayList<>();
        listReportRKH.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_newrkh.php?teamcode="+ teamcode + "&selectdate=" + selectedDate;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("REPORTRKH")) {
                        JSONArray jsonArrayApel = response.getJSONArray("REPORTRKH");
                        int i = 0;
                        while (i < jsonArrayApel.length()) {
                            JSONObject jsonObjectApel = jsonArrayApel.getJSONObject(i);

                            ListHistoryRKH paramsReportRKH = new ListHistoryRKH(
                                    null,
                                    jsonObjectApel.getString("UNITCODE"),
                                    jsonObjectApel.getString("DIVISION"),
                                    jsonObjectApel.getString("BLOKCODE"),
                                    jsonObjectApel.getString("ACTIVITY"),
                                    jsonObjectApel.getString("TARGETKERJA"),
                                    jsonObjectApel.getString("UOM"),
                                    jsonObjectApel.getString("TGLPELAKSANAAN"),
                                    2
                            );
                            listReportRKH.add(paramsReportRKH);
                            i++;
                        }
                        adapterLvRKH = new AdapterReportRKH(listReportRKH, ReportActivity.this);
                        lvReport.setAdapter(adapterLvRKH);

                    }
                    else {
                        lvReport.setAdapter(null);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Empty Data").show();
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void loadLvReportMintaBBM() {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportReqBBM = new ArrayList<>();
        listReportReqBBM.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_permintaanbbm.php?selectdate="+selectedDate+"&warehouse="+dbhelper.get_tbl_username(17);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("REPORTMINTABBM")) {
                        JSONArray jsonArrayMintaBBM = response.getJSONArray("REPORTMINTABBM");
                        int i = 0;
                        while (i < jsonArrayMintaBBM.length()) {
                            JSONObject jsonObjectMintaBBM = jsonArrayMintaBBM.getJSONObject(i);

                            ListReportMintaBBM paramsReportMintaBBM = new ListReportMintaBBM(
                                    jsonObjectMintaBBM.getString("EMPNAME"),
                                    jsonObjectMintaBBM.getString("JAMINPUT"),
                                    jsonObjectMintaBBM.getString("UNITCODE"),
                                    jsonObjectMintaBBM.getInt("JUMLAHLITER")
                            );
                            listReportReqBBM.add(paramsReportMintaBBM);
                            i++;
                        }
                        adapterReqBBM = new AdapterReportMintaBBM(listReportReqBBM, ReportActivity.this);
                        lvReport.setAdapter(adapterReqBBM);

                    }
                    else {
                        lvReport.setAdapter(null);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Empty Data").show();
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void loadLvReportP2H(String vehicleCode) {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportP2H = new ArrayList<>();
        listReportP2H.clear();

        Map<String, String> paramsP2H = new HashMap();
        paramsP2H.put("compid", dbhelper.get_tbl_username(14));
        paramsP2H.put("selectdate", selectedDate);
        paramsP2H.put("vehiclecode", vehicleCode);
        JSONObject postParametersP2H = new JSONObject(paramsP2H);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_p2h_new.php";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url_data, postParametersP2H, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("REPORTP2H")) {
                        JSONArray jsonArrayP2H = response.getJSONArray("REPORTP2H");
                        int i = 0;
                        while (i < jsonArrayP2H.length()) {
                            JSONObject jsonObjectP2H = jsonArrayP2H.getJSONObject(i);

                            ListReportP2H paramsReportP2H = new ListReportP2H(
                                    jsonObjectP2H.getString("DOCUMENTNO"),
                                    jsonObjectP2H.getString("TGLINPUT"),
                                    jsonObjectP2H.getString("UNITCODE"),
                                    jsonObjectP2H.getString("EMPNAME"),
                                    jsonObjectP2H.getString("P2HITEM"),
                                    jsonObjectP2H.getString("P2HNOTE")
                            );
                            listReportP2H.add(paramsReportP2H);
                            i++;
                        }
                        adapterLvP2H = new AdapterReportP2H(listReportP2H, ReportActivity.this);
                        lvReport.setAdapter(adapterLvP2H);
                        progressDialog.dismiss();
                    }
                    else {
                        lvReport.setAdapter(null);
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Empty Data").show();
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }

}