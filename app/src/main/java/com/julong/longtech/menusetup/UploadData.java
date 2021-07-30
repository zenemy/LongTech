package com.julong.longtech.menusetup;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.LoginActivity;
import com.julong.longtech.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadData extends AppCompatActivity {

    Button btnUpload;
    DatabaseHelper dbHelper;
    ListView listviewUpload;
    private List<UploadParam> uploadParams;
    UploadAdapter uploadAdapter;
    SweetAlertDialog progressDialog;

    boolean isUploadDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddata);

        progressDialog = new SweetAlertDialog(UploadData.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Mengupload Data");
        progressDialog.setCancelable(false);

        dbHelper = new DatabaseHelper(this);
        btnUpload = findViewById(R.id.btnOkUpload);
        listviewUpload = findViewById(R.id.lvUpload);

        loaduploaddata();
    }

    public void uploadData(View v) throws SQLiteException {
        dbHelper = new DatabaseHelper(this);
        progressDialog.show();

        try {
            Cursor cursorTransaction1 = dbHelper.view_upload_tr01();
            if (cursorTransaction1.moveToFirst()) {
                do {
                    String nodoc = cursorTransaction1.getString(0);
                    String datatype = cursorTransaction1.getString(1);
                    String subdatatype = cursorTransaction1.getString(2);
                    String compid = cursorTransaction1.getString(3);
                    String siteid = cursorTransaction1.getString(4);
                    String date1 = cursorTransaction1.getString(5);
                    String date2 = cursorTransaction1.getString(6);
                    String date3 = cursorTransaction1.getString(7);
                    String date4 = cursorTransaction1.getString(8);
                    String date5 = cursorTransaction1.getString(9);
                    String text1 = cursorTransaction1.getString(10);
                    String text2 = cursorTransaction1.getString(11);
                    String text3 = cursorTransaction1.getString(12);
                    String text4 = cursorTransaction1.getString(13);
                    String text5 = cursorTransaction1.getString(14);
                    String text6 = cursorTransaction1.getString(15);
                    String text7 = cursorTransaction1.getString(16);
                    String text8 = cursorTransaction1.getString(17);
                    String text9 = cursorTransaction1.getString(18);
                    String text10 = cursorTransaction1.getString(19);
                    String text11 = cursorTransaction1.getString(20);
                    String text12 = cursorTransaction1.getString(21);
                    String text13 = cursorTransaction1.getString(22);
                    String text14 = cursorTransaction1.getString(23);
                    String text15 = cursorTransaction1.getString(24);
                    String text16 = cursorTransaction1.getString(25);
                    String text17 = cursorTransaction1.getString(26);
                    String text18 = cursorTransaction1.getString(27);
                    String text19 = cursorTransaction1.getString(28);
                    String text20 = cursorTransaction1.getString(29);
                    String text21 = cursorTransaction1.getString(30);
                    String text22 = cursorTransaction1.getString(31);
                    String text23 = cursorTransaction1.getString(32);
                    String text24 = cursorTransaction1.getString(33);
                    String text25 = cursorTransaction1.getString(34);
                    String text26 = cursorTransaction1.getString(35);
                    String text27 = cursorTransaction1.getString(36);
                    String text28 = cursorTransaction1.getString(37);
                    String text29 = cursorTransaction1.getString(38);
                    String text30 = cursorTransaction1.getString(39);

                    RequestQueue requestQueueAbsenData = Volley.newRequestQueue(this);
                    String server_url = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/uploaddata/uploaddata.php?upload=transaction1";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPosttr01 = new JSONObject(response.toString());
                                if (jsonPosttr01.getString("UPLOAD").equals("SUCCESS")) {
                                    dbHelper.change_statusuploadtr01(nodoc);
                                    isUploadDone = true;
                                    afterEventUpload();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            requestQueueAbsenData.stop();
                        }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            afterEventUpload();
                            error.printStackTrace();
                            requestQueueAbsenData.stop();
                        }
                    })  {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("nodoc", nodoc);
                            params.put("datatype", datatype);
                            params.put("subdatatype", subdatatype);
                            params.put("compid", compid);
                            params.put("siteid", siteid);
                            params.put("date1", date1);
                            params.put("date2", date2);
                            params.put("date3", date3);
                            params.put("date4", date4);
                            params.put("date5", date5);
                            params.put("text1", text1);
                            params.put("text2", text2);
                            params.put("text3", text3);
                            params.put("text4", text4);
                            params.put("text5", text5);
                            params.put("text6", text6);
                            params.put("text7", text7);
                            params.put("text8", text8);
                            params.put("text9", text9);
                            params.put("text10", text10);
                            params.put("text11", text11);
                            params.put("text12", text12);
                            params.put("text13", text13);
                            params.put("text14", text14);
                            params.put("text15", text15);
                            params.put("text16", text16);
                            params.put("text17", text17);
                            params.put("text18", text18);
                            params.put("text19", text19);
                            params.put("text20", text20);
                            params.put("text21", text21);
                            params.put("text22", text22);
                            params.put("text23", text23);
                            params.put("text24", text24);
                            params.put("text25", text25);
                            params.put("text26", text26);
                            params.put("text27", text27);
                            params.put("text28", text28);
                            params.put("text29", text29);
                            params.put("text30", text30);
                            params.put("userid", dbHelper.get_tbl_username(0));
                            return params;
                        }
                    };
                    requestQueueAbsenData.add(stringRequest);
                } while (cursorTransaction1.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Transaction 02
        try {
            Cursor cursorTransaction2 = dbHelper.view_upload_tr02();
            if (cursorTransaction2.moveToFirst()) {
                do {
                    String nodoctr2 = cursorTransaction2.getString(0);
                    String datatypetr2 = cursorTransaction2.getString(1);
                    String subdatatypetr2 = cursorTransaction2.getString(2);
                    String itemdatatr2 = cursorTransaction2.getString(3);
                    String subitemdatatr2 = cursorTransaction2.getString(4);
                    String compidtr2 = cursorTransaction2.getString(5);
                    String siteidtr2 = cursorTransaction2.getString(6);
                    String date1tr2 = cursorTransaction2.getString(7);
                    String date2tr2 = cursorTransaction2.getString(8);
                    String date3tr2 = cursorTransaction2.getString(9);
                    String date4tr2 = cursorTransaction2.getString(10);
                    String date5tr2 = cursorTransaction2.getString(11);
                    String text1tr2 = cursorTransaction2.getString(12);
                    String text2tr2 = cursorTransaction2.getString(13);
                    String text3tr2 = cursorTransaction2.getString(14);
                    String text4tr2 = cursorTransaction2.getString(15);
                    String text5tr2 = cursorTransaction2.getString(16);
                    String text6tr2 = cursorTransaction2.getString(17);
                    String text7tr2 = cursorTransaction2.getString(18);
                    String text8tr2 = cursorTransaction2.getString(19);
                    String text9tr2 = cursorTransaction2.getString(20);
                    String text10tr2 = cursorTransaction2.getString(21);
                    String text11tr2 = cursorTransaction2.getString(22);
                    String text12tr2 = cursorTransaction2.getString(23);
                    String text13tr2 = cursorTransaction2.getString(24);
                    String text14tr2 = cursorTransaction2.getString(25);
                    String text15tr2 = cursorTransaction2.getString(26);
                    String text16tr2 = cursorTransaction2.getString(27);
                    String text17tr2 = cursorTransaction2.getString(28);
                    String text18tr2 = cursorTransaction2.getString(29);
                    String text19tr2 = cursorTransaction2.getString(30);
                    String text20tr2 = cursorTransaction2.getString(31);
                    String text21tr2 = cursorTransaction2.getString(32);
                    String text22tr2 = cursorTransaction2.getString(33);
                    String text23tr2 = cursorTransaction2.getString(34);
                    String text24tr2 = cursorTransaction2.getString(35);
                    String text25tr2 = cursorTransaction2.getString(36);
                    String text26tr2 = cursorTransaction2.getString(37);
                    String text27tr2 = cursorTransaction2.getString(38);
                    String text28tr2 = cursorTransaction2.getString(39);
                    String text29tr2 = cursorTransaction2.getString(40);
                    String text30tr2 = cursorTransaction2.getString(41);

                    RequestQueue requestQueueAbsenData = Volley.newRequestQueue(this);
                    String server_url = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/uploaddata/uploaddata.php?upload=transaction2";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPostTR02 = new JSONObject(response.toString());

                                dbHelper.change_statusuploadtr02(nodoctr2);
                                isUploadDone = true;
                                afterEventUpload();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            requestQueueAbsenData.stop();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            afterEventUpload();
                            error.printStackTrace();
                            requestQueueAbsenData.stop();
                        }
                    })  {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> paramstr02 = new HashMap<String, String>();
                            paramstr02.put("nodoctr2", nodoctr2);
                            paramstr02.put("datatypetr2", datatypetr2);
                            paramstr02.put("subdatatypetr2", subdatatypetr2);
                            paramstr02.put("itemdatatr2", itemdatatr2);
                            paramstr02.put("subitemdatatr2", subitemdatatr2);
                            paramstr02.put("compidtr2", compidtr2);
                            paramstr02.put("siteidtr2", siteidtr2);
                            paramstr02.put("date1tr2", date1tr2);
                            paramstr02.put("date2tr2", date2tr2);
                            paramstr02.put("date3tr2", date3tr2);
                            paramstr02.put("date4tr2", date4tr2);
                            paramstr02.put("date5tr2", date5tr2);
                            paramstr02.put("text1tr2", text1tr2);
                            paramstr02.put("text2tr2", text2tr2);
                            paramstr02.put("text3tr2", text3tr2);
                            paramstr02.put("text4tr2", text4tr2);
                            paramstr02.put("text5tr2", text5tr2);
                            paramstr02.put("text6tr2", text6tr2);
                            paramstr02.put("text7tr2", text7tr2);
                            paramstr02.put("text8tr2", text8tr2);
                            paramstr02.put("text9tr2", text9tr2);
                            paramstr02.put("text10tr2", text10tr2);
                            paramstr02.put("text11tr2", text11tr2);
                            paramstr02.put("text12tr2", text12tr2);
                            paramstr02.put("text13tr2", text13tr2);
                            paramstr02.put("text14tr2", text14tr2);
                            paramstr02.put("text15tr2", text15tr2);
                            paramstr02.put("text16tr2", text16tr2);
                            paramstr02.put("text17tr2", text17tr2);
                            paramstr02.put("text18tr2", text18tr2);
                            paramstr02.put("text19tr2", text19tr2);
                            paramstr02.put("text20tr2", text20tr2);
                            paramstr02.put("text21tr2", text21tr2);
                            paramstr02.put("text22tr2", text22tr2);
                            paramstr02.put("text23tr2", text23tr2);
                            paramstr02.put("text24tr2", text24tr2);
                            paramstr02.put("text25tr2", text25tr2);
                            paramstr02.put("text26tr2", text26tr2);
                            paramstr02.put("text27tr2", text27tr2);
                            paramstr02.put("text28tr2", text28tr2);
                            paramstr02.put("text29tr2", text29tr2);
                            paramstr02.put("text30tr2", text30tr2);
                            paramstr02.put("useridtr2", dbHelper.get_tbl_username(0));
                            return paramstr02;
                        }
                    };
                    requestQueueAbsenData.add(stringRequest);
                } while (cursorTransaction2.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Blob 01
        try {
            Cursor cursorImg = dbHelper.view_upload_bl01();
            if (cursorImg.moveToFirst()) {
                do {
                    String nodoc = cursorImg.getString(0);
                    String datatype = cursorImg.getString(1);
                    String subdatatype = cursorImg.getString(2);
                    String itemdata = cursorImg.getString(3);
                    String subitemdata = cursorImg.getString(4);
                    String compid = cursorImg.getString(5);
                    String siteid = cursorImg.getString(6);
                    String remarks = cursorImg.getString(7);
                    String base64blob1 = android.util.Base64.encodeToString(cursorImg.getBlob(8),  android.util.Base64.DEFAULT);
                    String base64blob2 = android.util.Base64.encodeToString(cursorImg.getBlob(9),  android.util.Base64.DEFAULT);
                    String base64blob3 = android.util.Base64.encodeToString(cursorImg.getBlob(10),  android.util.Base64.DEFAULT);
                    String base64blob4 = android.util.Base64.encodeToString(cursorImg.getBlob(11),  android.util.Base64.DEFAULT);
                    String base64blob5 = android.util.Base64.encodeToString(cursorImg.getBlob(12),  android.util.Base64.DEFAULT);

                    RequestQueue requestQueueAbsenImg = Volley.newRequestQueue(this);
                    String server_url = "http://longtech.julongindonesia.com:8889/longtech/mobilesync/uploaddata/uploaddata.php?upload=blob1";
                    StringRequest stringRequestImg = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonPostImg = new JSONObject(response.toString());

                                dbHelper.change_statusuploadimg(nodoc);
                                isUploadDone = true;
                                afterEventUpload();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            requestQueueAbsenImg.stop();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            afterEventUpload();
                            error.printStackTrace();
                            requestQueueAbsenImg.stop();
                        }
                    })  {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> paramsImg = new HashMap<String, String>();
                            paramsImg.put("nodocimg", nodoc);
                            paramsImg.put("datatypeimg", datatype);
                            paramsImg.put("subdatatypeimg", subdatatype);
                            paramsImg.put("itemdataimg", itemdata);
                            paramsImg.put("subitemdataimg", subitemdata);
                            paramsImg.put("compidimg", compid);
                            paramsImg.put("siteidimg", siteid);
                            paramsImg.put("remarks", remarks);
                            paramsImg.put("blob1", base64blob1);
                            paramsImg.put("blob2", base64blob2);
                            paramsImg.put("blob3", base64blob3);
                            paramsImg.put("blob4", base64blob4);
                            paramsImg.put("blob5", base64blob5);
                            paramsImg.put("useridimg", dbHelper.get_tbl_username(0));
                            return paramsImg;
                        }
                    };
                    requestQueueAbsenImg.add(stringRequestImg);
                } while (cursorImg.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loaduploaddata() {

        listviewUpload = findViewById(R.id.lvUpload);
        uploadParams = new ArrayList<>();
        uploadParams.clear();
        final Cursor cursor = dbHelper.view_tbl_uploadlist();
        if (cursor.moveToFirst()) {
            do {
                UploadParam uploadParam = new UploadParam(
                        cursor.getString(cursor.getColumnIndex("kodeabsen")),
                        cursor.getString(cursor.getColumnIndex("dataabsen")),
                        cursor.getInt(cursor.getColumnIndex("datapending"))
                );
                uploadParams.add(uploadParam);
            } while (cursor.moveToNext());
        }
        uploadAdapter = new UploadAdapter(this, R.layout.upload_list, uploadParams);
        listviewUpload.setAdapter(uploadAdapter);
    }

    void afterEventUpload() {
        if (isUploadDone) {
            progressDialog.dismiss();
            new SweetAlertDialog(UploadData.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Upload").setConfirmText("OK").show();
            loaduploaddata();
        } else {
            new SweetAlertDialog(UploadData.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error!").setContentText("Error! Silahkan Coba Lagi").setConfirmText("COBA LAGI").show();
        }
    }


}