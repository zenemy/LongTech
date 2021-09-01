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
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;
import com.julong.longtech.menuhistory.HistoryApelAdapter;
import com.julong.longtech.menuhistory.ListHistoryApel;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.julong.longtech.DatabaseHelper.url_api;

public class ReportCarLogActivity extends AppCompatActivity {

    DatabaseHelper dbhelper;

    RecyclerView lvReportCarLog;

    private List<ListReportCarLog> listReportCarLogs;
    AdapterReportCarLog adapterLvCarlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlog_report);

        dbhelper = new DatabaseHelper(this);

        lvReportCarLog = findViewById(R.id.lvReportCarLog);

        loadLvReportCarLog();
    }

    private void loadLvReportCarLog() {

        LinearLayoutManager layoutApel = new LinearLayoutManager(this);
        lvReportCarLog.setLayoutManager(layoutApel);

        listReportCarLogs = new ArrayList<>();
        listReportCarLogs.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url_data = url_api + "fetchdata/reportmenu/report_carlog.php?selectdate=2021-08-28&teamcode=" + dbhelper.get_tbl_username(18);
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
                    adapterLvCarlog = new AdapterReportCarLog(listReportCarLogs, ReportCarLogActivity.this);
                    lvReportCarLog.setAdapter(adapterLvCarlog);
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
        requestQueue.add(jsonRequest);

    }
}