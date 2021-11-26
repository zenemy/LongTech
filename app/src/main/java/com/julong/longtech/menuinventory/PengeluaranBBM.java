package com.julong.longtech.menuinventory;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.DatabaseHelper.url_api;

public class PengeluaranBBM extends AppCompatActivity {

    String selectedVehicle, nodocKeluarBBM;

    DatabaseHelper dbhelper;

    AutoCompleteTextView acVehicleKeluarBBM;
    Button btnBackKeluarBBM;

    LinearLayout layoutIntentReq, layoutIntentInfoReq;
    View dividerIntentReq;
    EditText etTodayDate, etLiterBBM, etNoteKeluarBBM, etPemohonBBM, etReqLiter;

    List<String> listVehicle;
    ArrayAdapter<String> adapterVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran_bbm);

        dbhelper = new DatabaseHelper(this);

        etLiterBBM = findViewById(R.id.etLiterKeluarBBM);
        etNoteKeluarBBM = findViewById(R.id.etNoteKeluarBBM);
        etReqLiter = findViewById(R.id.etInfoJumlahMintaBBM);
        etTodayDate = findViewById(R.id.etTodayDateKeluarBBM);
        etPemohonBBM = findViewById(R.id.etInfoPemohonMintaBBM);
        acVehicleKeluarBBM = findViewById(R.id.acVehicleKeluarBBM);
        layoutIntentReq = findViewById(R.id.layoutIntentReqMintaBBM);
        btnBackKeluarBBM = findViewById(R.id.btnCancelPengeluaranBBM);
        dividerIntentReq = findViewById(R.id.dividerIntentReqMintaBBM);
        layoutIntentInfoReq = findViewById(R.id.layoutIntentInfoReqBBM);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            layoutIntentReq.setVisibility(View.VISIBLE);
            dividerIntentReq.setVisibility(View.VISIBLE);
            layoutIntentInfoReq.setVisibility(View.VISIBLE);
            etReqLiter.setText(String.valueOf(extras.getInt("literjml")));
            etPemohonBBM.setText(extras.getString("requester"));
            selectedVehicle = extras.getString("unitcode");
            acVehicleKeluarBBM.setText(dbhelper.get_vehiclename(2, selectedVehicle));
            acVehicleKeluarBBM.setDropDownHeight(0);
            acVehicleKeluarBBM.setKeyListener(null);
        }

        String todayDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        etTodayDate.setText(todayDate);
        btnBackKeluarBBM.setOnClickListener(view -> finish());

        listVehicle = dbhelper.get_vehiclemasterdata();
        adapterVehicle = new ArrayAdapter<>(this, R.layout.spinnerlist, R.id.spinnerItem, listVehicle);
        acVehicleKeluarBBM.setAdapter(adapterVehicle);

        acVehicleKeluarBBM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedVehicle = dbhelper.get_vehiclecodeonly(adapterVehicle.getItem(position));
                InputMethodManager keyboardMgr = (InputMethodManager) getSystemService(PengeluaranBBM.INPUT_METHOD_SERVICE);
                keyboardMgr.hideSoftInputFromWindow(acVehicleKeluarBBM.getWindowToken(), 0);
            }
        });

    }

    public void submitPengeluaranBBM(View v) {
        if (selectedVehicle == null ||
                TextUtils.isEmpty(etLiterBBM.getText().toString().trim())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Lengkapi data").setConfirmText("OK").show();
        }
        else {
            nodocKeluarBBM = dbhelper.get_tbl_username(0) + "/SIVBBM/" + new SimpleDateFormat("ddMMyy/HHmmss", Locale.getDefault()).format(new Date());
            String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            RequestQueue requestQueueChangePassword = Volley.newRequestQueue(this);
            String url_uploadbbm = url_api + "dataupload/uploadpengeluarabbm.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uploadbbm, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonPostUploadBBM = new JSONObject(response);
                        if (jsonPostUploadBBM.getString("UPLOADBBM").equals("SUCCESS")) {
                            dbhelper.insert_pengeluaranbbm(nodocKeluarBBM, selectedVehicle, etLiterBBM.getText().toString(), etNoteKeluarBBM.getText().toString(), 1);
                            SweetAlertDialog dlgFinish = new SweetAlertDialog(PengeluaranBBM.this, SweetAlertDialog.SUCCESS_TYPE);
                            dlgFinish.setCancelable(false);
                            dlgFinish.setTitleText("Berhasil Simpan");
                            dlgFinish.setConfirmClickListener(sweetAlertDialog -> {
                                Intent loginIntent = new Intent(PengeluaranBBM.this, MainActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                            });
                            dlgFinish.setConfirmText("OK").show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestQueueChangePassword.stop();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    requestQueueChangePassword.stop();
                    dbhelper.insert_pengeluaranbbm(nodocKeluarBBM, selectedVehicle, etLiterBBM.getText().toString(), etNoteKeluarBBM.getText().toString(), 0);
                    SweetAlertDialog dlgFinish = new SweetAlertDialog(PengeluaranBBM.this, SweetAlertDialog.SUCCESS_TYPE);
                    dlgFinish.setCancelable(false);
                    dlgFinish.setTitleText("Berhasil Simpan");
                    dlgFinish.setConfirmClickListener(sweetAlertDialog -> {
                        Intent loginIntent = new Intent(PengeluaranBBM.this, MainActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                    });
                    dlgFinish.setConfirmText("OK").show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nodoc", nodocKeluarBBM);
                    params.put("datatype", "SIVBBM");
                    params.put("subdatatype", dbhelper.get_tbl_username(0));
                    params.put("compid", dbhelper.get_tbl_username(14));
                    params.put("siteid", dbhelper.get_tbl_username(15));
                    params.put("date1", savedate);
                    params.put("text1", selectedVehicle);
                    params.put("text2", etLiterBBM.getText().toString());
                    params.put("text3", etNoteKeluarBBM.getText().toString());
                    params.put("userid", dbhelper.get_tbl_username(0));
                    return params;
                }
            };
            requestQueueChangePassword.add(stringRequest);

        }
    }

    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(727, backIntent);
        finish();
    }
}