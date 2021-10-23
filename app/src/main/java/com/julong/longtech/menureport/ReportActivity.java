package com.julong.longtech.menureport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    String selectedReportGroup, selectedReportMenu, selectedDate, selectedTeamCode;

    EditText etDateReport;
    AutoCompleteTextView acSelectReportMenu, acVehicleReport, acTeamSelect;
    TextInputLayout inputLayoutAcVehicle, inputLayoutDate, inputLayoutTeamSelect;
    RecyclerView lvReport;
    Button btnResetReport;

    private List<ListReportCarLog> listReportCarLogs;
    AdapterReportCarLog adapterLvCarlog;

    private List<ListHistoryApel> listReportApels;
    HistoryApelAdapter adapterLvApel;

    private List<ListHistoryRKH> listReportRKH;
    AdapterReportRKH adapterLvRKH;

    private List<ListReportP2H> listReportP2H;
    AdapterReportP2H adapterLvP2H;

    ArrayList<String> listReportMenuGroup = new ArrayList<>();
    ArrayList<String> listReportMenuName = new ArrayList<>();
    ArrayList<String> listReportMenuCode = new ArrayList<>();
    ArrayAdapter<String> adapterReportMenu;

    private List<String> listVehicleReport;
    ArrayAdapter<String> adapterVehicleReport;

    private List<String> listTeamName, listTeamCode;
    ArrayAdapter<String> adapterTeamSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbhelper = new DatabaseHelper(this);
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Loading");
        progressDialog.setCancelable(false);

        etDateReport = findViewById(R.id.etDateReportActivity);
        acVehicleReport = findViewById(R.id.acUnitReportActivity);
        lvReport = findViewById(R.id.lvReportCarLog);
        acSelectReportMenu = findViewById(R.id.acSelectReportMenu);
        acTeamSelect = findViewById(R.id.acSelectTeamReport);
        inputLayoutAcVehicle = findViewById(R.id.inputLayoutAcVehicleRpeort);
        inputLayoutDate = findViewById(R.id.inputLayoutDateReport);
        inputLayoutTeamSelect = findViewById(R.id.inputLayoutTeamReport);
        btnResetReport = findViewById(R.id.btnResetReportActivity);

        btnResetReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = null;
                selectedReportGroup = null;
                selectedReportMenu = null;
                selectedTeamCode = null;
                etDateReport.setText(null);
                acSelectReportMenu.setText(null);
                acTeamSelect.setText(null);
                acVehicleReport.setText(null);
                lvReport.setAdapter(null);
            }
        });

        if (dbhelper.get_tbl_username(2).equals("MGR") || dbhelper.get_tbl_username(3).equals("MGR")) {
            selectedTeamCode = null;
            inputLayoutTeamSelect.setVisibility(View.VISIBLE);
        } else {
            selectedTeamCode = dbhelper.get_tbl_username(18);
        }

        populateDropdownReportMenu(dbhelper.get_tbl_username(3));

        MaterialDatePicker<Long> reportDatepicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        etDateReport.setOnClickListener(v -> reportDatepicker.show(getSupportFragmentManager(), "REPORT_DATEPICKER"));

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
                    inputLayoutAcVehicle.setVisibility(View.VISIBLE);
                    inputLayoutDate.setPadding(6, 0, 0, 0);
                }
                else {
                    inputLayoutAcVehicle.setVisibility(View.GONE);
                    inputLayoutDate.setPadding(0, 0, 0, 0);
                }
            }
        });

        acTeamSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedTeamCode = listTeamCode.get(position);
            }
        });
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
//        else if (selectedReportMenu.equals("020201")) {
//            loadLvReportRKH(selectedTeamCode);
//        }
        else if (selectedReportMenu.equals("020202")) {
            loadLvReportP2H(acVehicleReport.getText().toString());
        }
        else if (selectedReportMenu.equals("020203")) {
            loadLvReportCarLog(selectedTeamCode);
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
        Volley.newRequestQueue(getApplicationContext()).add(jsonRequestUserType);

        listTeamCode = dbhelper.get_teamname(0);
        listTeamName = dbhelper.get_teamname(1);
        adapterTeamSelect = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listTeamName);
        acTeamSelect.setAdapter(adapterTeamSelect);

        listVehicleReport = dbhelper.get_vehiclecodelist();
        adapterVehicleReport = new ArrayAdapter<String>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicleReport);
        acVehicleReport.setAdapter(adapterVehicleReport);
    }

    private void loadLvReportCarLog(String teamcode) {

        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
        lvReport.setLayoutManager(layoutReport);

        listReportCarLogs = new ArrayList<>();
        listReportCarLogs.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_carlog.php?selectdate="+ selectedDate + "&teamcode=" + teamcode;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
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
                                jsonObject1.getString("ACTIVITY"),
                                jsonObject1.getString("HASILKERJA"),
                                jsonObject1.getString("SATUANKERJA"),
                                jsonObject1.getString("KMAWAL"),
                                jsonObject1.getString("KMAKHIR")
                        );
                        listReportCarLogs.add(paramsCarLogReport);
                        i++;
                    }
                    adapterLvCarlog = new AdapterReportCarLog(listReportCarLogs, ReportActivity.this);
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
        requestQueue.add(jsonRequest);
    }

//    private void loadLvReportRKH(String teamcode) {
//
//        LinearLayoutManager layoutReport = new LinearLayoutManager(this);
//        lvReport.setLayoutManager(layoutReport);
//
//        listReportRKH = new ArrayList<>();
//        listReportRKH.clear();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url_data = url_api + "fetchdata/reportmenu/report_rkh.php?teamcode="+ teamcode + "&selectdate=" + selectedDate;
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.has("REPORTRKH")) {
//                        JSONArray jsonArrayApel = response.getJSONArray("REPORTRKH");
//                        int i = 0;
//                        while (i < jsonArrayApel.length()) {
//                            JSONObject jsonObjectApel = jsonArrayApel.getJSONObject(i);
//
//                            ListHistoryRKH paramsReportRKH = new ListHistoryRKH(
//                                    jsonObjectApel.getString("DOCUMENTNO"),
//                                    jsonObjectApel.getString("TGLINPUT"),
//                                    jsonObjectApel.getString("TGLPELAKSANAAN"),
//                                    jsonObjectApel.getString("EMPNAME"),
//                                    jsonObjectApel.getString("ACTIVITY"),
//                                    jsonObjectApel.getString("BLOK"),
//                                    jsonObjectApel.getString("UNITCODE"),
//                                    jsonObjectApel.getString("SHIFTCODE"),
//                                    2
//                            );
//                            listReportRKH.add(paramsReportRKH);
//                            i++;
//                        }
//                        adapterLvRKH = new AdapterReportRKH(listReportRKH, ReportActivity.this);
//                        lvReport.setAdapter(adapterLvRKH);
//
//                    }
//                    else {
//                        lvReport.setAdapter(null);
//                    }
//                    progressDialog.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, error -> {
//            error.printStackTrace();
//            progressDialog.dismiss();
//            new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
//                    .setContentText("Empty Data").show();
//        });
//        requestQueue.add(jsonRequest);
//    }

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
        requestQueue.add(jsonRequest);
    }

}