package com.julong.longtech.menusetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.MainActivity;
import com.julong.longtech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DownloadData extends AppCompatActivity {

    ConstraintLayout layoutDownloadGS, layoutDownloadMD, layoutHeaderDownload2;
    Button btnDownload;
    CheckBox checkBoxGS, checkBoxMD, checkAllDownload;
    DatabaseHelper dbhelper;
    TextView tvSubJudulDownloadGS, tvSubJudulDownloadMD;
    SweetAlertDialog progressDialog;

    Handler handler = new Handler();

    boolean isDownloadGSDone;
    boolean isDownloadMDDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        isDownloadGSDone = false;
        isDownloadMDDone = false;

        dbhelper = new DatabaseHelper(this);
        progressDialog = new SweetAlertDialog(DownloadData.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Mendownload Data");
        progressDialog.setCancelable(false);

        layoutDownloadGS = findViewById(R.id.layoutDownloadGS);
        layoutHeaderDownload2 = findViewById(R.id.clHeaderDownload2);
        layoutDownloadMD = findViewById(R.id.layoutDownloadMD);
        checkBoxGS = findViewById(R.id.checkBoxDataDownloadGS);
        checkBoxMD = findViewById(R.id.checkBoxDataDownloadMD);
        checkAllDownload = findViewById(R.id.checkAllDownload);
        tvSubJudulDownloadGS = findViewById(R.id.tvSubJudulDataDownloadGS);
        tvSubJudulDownloadMD = findViewById(R.id.tvSubJudulDataDownloadMD);

        checkAllDownload.setChecked(true);
        checkBoxGS.setChecked(true);
        checkBoxMD.setChecked(true);

        try {
            tvSubJudulDownloadGS.setText("Jumlah Data : " + dbhelper.count_datadownloadGS() + " Data");
            tvSubJudulDownloadMD.setText("Jumlah Data : " + dbhelper.count_tablemd() + " Data");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        checkAllDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkAllDownload.isChecked()) {
//                    checkBoxGS.setChecked(true);
//                    checkBoxMD.setChecked(true);
//                }
//                else {
//                    checkBoxGS.setChecked(false);
//                    checkBoxMD.setChecked(false);
//                }
//            }
//        });
//
//        layoutHeaderDownload2.setOnClickListener(v -> {
//            if (checkAllDownload.isChecked()) {
//                checkAllDownload.setChecked(false);
//            }
//            else {
//                checkAllDownload.setChecked(true);
//            }
//        });
//
//        layoutDownloadGS.setOnClickListener(v -> {
//            if (checkBoxGS.isChecked()) {
//                checkBoxGS.setChecked(false);
//            }
//            else {
//                checkBoxGS.setChecked(true);
//            }
//        });
//
//        layoutDownloadMD.setOnClickListener(v -> {
//            if (checkBoxMD.isChecked()) {
//                checkBoxMD.setChecked(false);
//            }
//            else {
//                checkBoxMD.setChecked(true);
//            }
//        });

    }

    public void eventDownloadData(View v) {
        if (!checkBoxGS.isChecked() && !checkBoxMD.isChecked()) {
            new SweetAlertDialog(DownloadData.this, SweetAlertDialog.ERROR_TYPE).setContentText("Pilih Data!").setConfirmText("OK").show();
        } else {
            if (checkBoxGS.isChecked()) {
                dbhelper.delete_datags();
                RequestQueue requestQueueDownloadDataGS = Volley.newRequestQueue(this);
                String url_data = DatabaseHelper.url_api + "fetchdata/get_generalsetup.php";
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AsyncJsonGS().execute(response, null, null);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        requestQueueDownloadDataGS.stop();
                    }
                });
                requestQueueDownloadDataGS.add(jsonRequest);
            }

            if (checkBoxMD.isChecked()) {
                dbhelper.delete_masterdata();
                RequestQueue requestQueueDownloadMD = Volley.newRequestQueue(this);
                String url_data = DatabaseHelper.url_api + "fetchdata/get_masterdata.php?userid="
                        + dbhelper.get_tbl_username(0) + "&ancakcode="+dbhelper.get_tbl_username(19);

                JsonObjectRequest jsonRequestMD = new JsonObjectRequest(Request.Method.GET, url_data, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AsyncJsonMD().execute(response, null, null);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        final SweetAlertDialog errorDlg = new SweetAlertDialog(DownloadData.this, SweetAlertDialog.ERROR_TYPE).setContentText("Periksa Jaringan!").setConfirmText("OK");
                        errorDlg.show();
                        requestQueueDownloadMD.stop();
                    }
                });
                requestQueueDownloadMD.add(jsonRequestMD);
                progressDialog.show();
            }
        }
    }

    private class AsyncJsonGS extends AsyncTask<JSONObject, Void, Integer> {

        protected Integer doInBackground(JSONObject... jsonObjectsGS) {
            try {
                JSONObject responseGS = jsonObjectsGS[0];
                JSONArray jsonArrayGS01 = responseGS.getJSONArray("DATAGS01");
                int igs01 = 0;
                while (igs01 < jsonArrayGS01.length()) {
                    JSONObject jsonObject1 = jsonArrayGS01.getJSONObject(igs01);
                    dbhelper.insert_dataGS01(jsonObject1.getString("GROUPPARAMCODE"), jsonObject1.getString("GROUPPARAMDESC"),
                            jsonObject1.getString("PARAMETERCODE"), jsonObject1.getString("PARAMETERDESC"), jsonObject1.getString("SEQ_NO"));
                    igs01++;
                }

                JSONArray jsonArrayGS06 = responseGS.getJSONArray("DATAGS06");
                int igs06 = 0;
                while (igs06 < jsonArrayGS06.length()) {
                    JSONObject jsonObjectGS06 = jsonArrayGS06.getJSONObject(igs06);
                    dbhelper.insert_dataGS06(jsonObjectGS06.getString("GROUPCOMPANYCODE"), jsonObjectGS06.getString("COMPANYID"), jsonObjectGS06.getString("COMPANYNAME"),
                            jsonObjectGS06.getString("COMPANYSITEID"), jsonObjectGS06.getString("COMPANYSITENAME"), jsonObjectGS06.getString("INITIALCODE"),
                            jsonObjectGS06.getString("FUNCTIONCODE"), jsonObjectGS06.getString("SITETYPE"), jsonObjectGS06.getString("REGION"),
                            jsonObjectGS06.getString("AREACODE"), jsonObjectGS06.getString("ADDRESS"), jsonObjectGS06.getString("CITY"),
                            jsonObjectGS06.getString("PROVINCE"), jsonObjectGS06.getString("ZIPCODE"), jsonObjectGS06.getString("TELP"));
                    igs06++;
                }

                JSONArray jsonArrayGS07 = responseGS.getJSONArray("DATAGS07");
                int igs07 = 0;
                while (igs07 < jsonArrayGS07.length()) {
                    JSONObject jsonObjectGS07 = jsonArrayGS07.getJSONObject(igs07);
                    dbhelper.insert_dataGS07(jsonObjectGS07.getString("USERID"), jsonObjectGS07.getString("USERNAME"), jsonObjectGS07.getString("USERTYPE"),
                            jsonObjectGS07.getString("USERROLE"), jsonObjectGS07.getString("EMPCODE"), jsonObjectGS07.getString("NO_TELP"), jsonObjectGS07.getString("EMAILUSER"),
                            jsonObjectGS07.getString("EMPNAME"), jsonObjectGS07.getString("POSITION_ID"), jsonObjectGS07.getString("POSITION_NAME"),
                            jsonObjectGS07.getString("COMP_ID"), jsonObjectGS07.getString("SITE_ID"), jsonObjectGS07.getString("DEPTCODE"),
                            jsonObjectGS07.getString("DIVCODE"), jsonObjectGS07.getString("GANGCODE"), jsonObjectGS07.getString("ANCAKCODE"), jsonObjectGS07.getString("SHIFTCODE"));
                    igs07++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 1;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            tvSubJudulDownloadGS.setText("Jumlah Data : " + dbhelper.count_datadownloadGS() + " Data");
        }
    }

    private class AsyncJsonMD extends AsyncTask<JSONObject, Void, Integer> {

        protected Integer doInBackground(JSONObject... jsonObjectsMD) {
            try {
                JSONObject responseMD = jsonObjectsMD[0];
                JSONArray jsonArrayMD = responseMD.getJSONArray("MASTERDATA");
                int intMD = 0;
                while (intMD < jsonArrayMD.length()) {
                    JSONObject jsonObjectMD = jsonArrayMD.getJSONObject(intMD);
                    dbhelper.insert_tablemd(jsonObjectMD.getString("DATATYPE"), jsonObjectMD.getString("SUBDATATYPE"), jsonObjectMD.getString("COMP_ID"), jsonObjectMD.getString("SITE_ID"),
                            jsonObjectMD.getString("DATE1"), jsonObjectMD.getString("DATE2"), jsonObjectMD.getString("DATE3"), jsonObjectMD.getString("DATE4"),
                            jsonObjectMD.getString("DATE5"), jsonObjectMD.getString("TEXT1"), jsonObjectMD.getString("TEXT2"), jsonObjectMD.getString("TEXT3"),
                            jsonObjectMD.getString("TEXT4"), jsonObjectMD.getString("TEXT5"), jsonObjectMD.getString("TEXT6"), jsonObjectMD.getString("TEXT7"),
                            jsonObjectMD.getString("TEXT8"), jsonObjectMD.getString("TEXT9"), jsonObjectMD.getString("TEXT10"), jsonObjectMD.getString("TEXT11"),
                            jsonObjectMD.getString("TEXT12"), jsonObjectMD.getString("TEXT13"), jsonObjectMD.getString("TEXT14"), jsonObjectMD.getString("TEXT15"),
                            jsonObjectMD.getString("TEXT16"), jsonObjectMD.getString("TEXT17"), jsonObjectMD.getString("TEXT18"), jsonObjectMD.getString("TEXT19"),
                            jsonObjectMD.getString("TEXT20"), jsonObjectMD.getString("TEXT21"), jsonObjectMD.getString("TEXT22"), jsonObjectMD.getString("TEXT23"),
                            jsonObjectMD.getString("TEXT24"), jsonObjectMD.getString("TEXT25"), jsonObjectMD.getString("TEXT26"), jsonObjectMD.getString("TEXT27"),
                            jsonObjectMD.getString("TEXT28"), jsonObjectMD.getString("TEXT29"), jsonObjectMD.getString("TEXT30"));
                    intMD++;
                }

                JSONArray jsonArrayMD2 = responseMD.getJSONArray("MASTERDATA2");
                int intMD2 = 0;
                while (intMD2 < jsonArrayMD2.length()) {
                    JSONObject jsonObjectMD2 = jsonArrayMD2.getJSONObject(intMD2);
                    dbhelper.insert_tablemd2(jsonObjectMD2.getString("DATATYPE"), jsonObjectMD2.getString("SUBDATATYPE"),
                            jsonObjectMD2.getString("ITEMDATA"), jsonObjectMD2.getString("SUBITEMDATA"), jsonObjectMD2.getString("COMP_ID"),
                            jsonObjectMD2.getString("SITE_ID"), jsonObjectMD2.getString("DATE1"), jsonObjectMD2.getString("DATE2"),
                            jsonObjectMD2.getString("DATE3"), jsonObjectMD2.getString("DATE4"), jsonObjectMD2.getString("DATE5"), jsonObjectMD2.getString("TEXT1"),
                            jsonObjectMD2.getString("TEXT2"), jsonObjectMD2.getString("TEXT3"), jsonObjectMD2.getString("TEXT4"), jsonObjectMD2.getString("TEXT5"),
                            jsonObjectMD2.getString("TEXT6"), jsonObjectMD2.getString("TEXT7"), jsonObjectMD2.getString("TEXT8"), jsonObjectMD2.getString("TEXT9"),
                            jsonObjectMD2.getString("TEXT10"), jsonObjectMD2.getString("TEXT11"), jsonObjectMD2.getString("TEXT12"), jsonObjectMD2.getString("TEXT13"),
                            jsonObjectMD2.getString("TEXT14"), jsonObjectMD2.getString("TEXT15"), jsonObjectMD2.getString("TEXT16"), jsonObjectMD2.getString("TEXT17"),
                            jsonObjectMD2.getString("TEXT18"), jsonObjectMD2.getString("TEXT19"), jsonObjectMD2.getString("TEXT20"), jsonObjectMD2.getString("TEXT21"),
                            jsonObjectMD2.getString("TEXT22"), jsonObjectMD2.getString("TEXT23"), jsonObjectMD2.getString("TEXT24"), jsonObjectMD2.getString("TEXT25"),
                            jsonObjectMD2.getString("TEXT26"), jsonObjectMD2.getString("TEXT27"), jsonObjectMD2.getString("TEXT28"), jsonObjectMD2.getString("TEXT29"),
                            jsonObjectMD2.getString("TEXT30"));
                    intMD2++;
                }

                JSONArray jsonArrayTransportMD = responseMD.getJSONArray("DATATRANSPORT");
                int intTrasport = 0;
                while (intTrasport < jsonArrayTransportMD.length()) {
                    JSONObject jsonObjectTransportMD = jsonArrayTransportMD.getJSONObject(intTrasport);
                    dbhelper.insert_transportmd(jsonObjectTransportMD.getString("DATATYPE"), jsonObjectTransportMD.getString("SUBDATATYPE"), jsonObjectTransportMD.getString("COMP_ID"), jsonObjectTransportMD.getString("SITE_ID"),
                            jsonObjectTransportMD.getString("TEXT1"), jsonObjectTransportMD.getString("TEXT2"), jsonObjectTransportMD.getString("TEXT3"), jsonObjectTransportMD.getString("TEXT4"),
                            jsonObjectTransportMD.getString("TEXT5"), jsonObjectTransportMD.getString("TEXT6"), jsonObjectTransportMD.getString("TEXT7"), jsonObjectTransportMD.getString("TEXT8"),
                            jsonObjectTransportMD.getString("TEXT9"), jsonObjectTransportMD.getString("TEXT10"), jsonObjectTransportMD.getString("TEXT11"), jsonObjectTransportMD.getString("TEXT12"),
                            jsonObjectTransportMD.getString("TEXT13"), jsonObjectTransportMD.getString("TEXT14"), jsonObjectTransportMD.getString("TEXT15"), jsonObjectTransportMD.getString("TEXT16"),
                            jsonObjectTransportMD.getString("TEXT17"), jsonObjectTransportMD.getString("TEXT18"), jsonObjectTransportMD.getString("TEXT19"));
                    intTrasport++;
                }

                JSONArray jsonArrayOrgStructure = responseMD.getJSONArray("DATAORGSTRUCTURE");
                int intOrgStructure = 0;
                while (intOrgStructure < jsonArrayOrgStructure.length()) {
                    JSONObject jsonObjectOrgStructure = jsonArrayOrgStructure.getJSONObject(intOrgStructure);
                    Log.d("ORGSTRUCUTREDATA", jsonObjectOrgStructure.getString("DATATYPE"));
                    dbhelper.insert_orgstructuremd(jsonObjectOrgStructure.getString("DATATYPE"), jsonObjectOrgStructure.getString("SUBDATATYPE"), jsonObjectOrgStructure.getString("COMP_ID"), jsonObjectOrgStructure.getString("SITE_ID"),
                            jsonObjectOrgStructure.getString("TEXT1"), jsonObjectOrgStructure.getString("TEXT2"), jsonObjectOrgStructure.getString("TEXT3"), jsonObjectOrgStructure.getString("TEXT4"),
                            jsonObjectOrgStructure.getString("TEXT5"), jsonObjectOrgStructure.getString("TEXT6"), jsonObjectOrgStructure.getString("TEXT7"), jsonObjectOrgStructure.getString("TEXT8"));
                    intOrgStructure++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 1;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            tvSubJudulDownloadMD.setText("Jumlah Data : " + dbhelper.count_tablemd() + " Data");
            progressDialog.dismissWithAnimation();
            final SweetAlertDialog errorDlg = new SweetAlertDialog(DownloadData.this, SweetAlertDialog.SUCCESS_TYPE).setContentText("Berhasil Download!").setConfirmText("OK");
            errorDlg.show();
        }
    }

}