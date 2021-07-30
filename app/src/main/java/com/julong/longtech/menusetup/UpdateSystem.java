package com.julong.longtech.menusetup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateSystem extends AppCompatActivity {

    TextView tvRemarksUpdateSystem, tvInfoVersionUpdateSystem;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatesystem);

        dbhelper = new DatabaseHelper(this);

        tvRemarksUpdateSystem = findViewById(R.id.tvRemarksUpdateSystem);
        tvInfoVersionUpdateSystem = findViewById(R.id.tvAppVersionInfo);
        generate_version();
    }

    void generate_version() {
        String url_data = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/dsi_version.php?systemcode=LONGTECH01";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonPost = new JSONObject(response.toString());
                    dbhelper.insert_tbl_version(jsonPost.getString("VERSIONNUMBER"),
                            jsonPost.getString("VERSIONNAME"),
                            jsonPost.getString("TDATE"),
                            jsonPost.getString("REMARKS"), jsonPost.getString("link_download"));
                    String status_update;
                    try {
                        if (!dbhelper.get_tbl_version(2).equals(dbhelper.get_tbl_version(6))) {
                            status_update = "NEW";
                            dbhelper.updatestatusversion(status_update);
                            tvRemarksUpdateSystem.setText(dbhelper.get_tbl_version(9));
                            tvInfoVersionUpdateSystem.setText(dbhelper.get_tbl_version(3));

                        } else {
                            status_update = "NO";
                            dbhelper.updatestatusversion(status_update);
                            tvRemarksUpdateSystem.setText(dbhelper.get_tbl_version(9));
                            tvInfoVersionUpdateSystem.setText(dbhelper.get_tbl_version(3));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String status_update;
                status_update = "NO";
                dbhelper.updatestatusversion(status_update);
                tvInfoVersionUpdateSystem.setTextColor(0xff305031);
                tvInfoVersionUpdateSystem.setText(dbhelper.get_tbl_version(3));

            }
        });
        queue.add(stringRequest);
    }
}