package com.julong.longtech.menusetup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.julong.longtech.R;
import com.julong.longtech.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadData extends AppCompatActivity {

    public static String todayDate;
    Button btnUpload;
    DatabaseHelper dbHelper;
    public static ListView listviewUpload;
    SweetAlertDialog progressDialog;

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

        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        loaduploaddata(this);
    }

    public void uploadData(View v) throws SQLiteException {
        dbHelper = new DatabaseHelper(this);
        progressDialog.show();
        new AsyncUploadData().execute();
    }

    private class AsyncUploadData extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {

            try { // Transaction 01
                Cursor cursorTransaction1 = dbHelper.view_upload_tr01();
                if (cursorTransaction1.moveToFirst()) {
                    do {
                        uploadTR01private(
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
                Cursor cursorTransaction2 = dbHelper.view_upload_tr02();
                if (cursorTransaction2.moveToFirst()) {
                    do {
                        uploadTR02(UploadData.this,
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
                Cursor cursorImg = dbHelper.view_upload_bl01();
                if (cursorImg.moveToFirst()) {
                    do {

                        String base64blob1 = android.util.Base64.encodeToString(cursorImg.getBlob(9),  android.util.Base64.DEFAULT);
                        String base64blob2 = android.util.Base64.encodeToString(cursorImg.getBlob(10),  android.util.Base64.DEFAULT);
                        String base64blob3 = android.util.Base64.encodeToString(cursorImg.getBlob(11),  android.util.Base64.DEFAULT);
                        String base64blob4 = android.util.Base64.encodeToString(cursorImg.getBlob(12),  android.util.Base64.DEFAULT);
                        String base64blob5 = android.util.Base64.encodeToString(cursorImg.getBlob(13),  android.util.Base64.DEFAULT);

                        uploadBL01(UploadData.this,
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
            progressDialog.dismiss();
            new SweetAlertDialog(UploadData.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil Upload").setConfirmText("OK").show();
        }
    }

    private void uploadTR01private(int id, String nodoc, String datatype, String subdatatype, String compid, String siteid, String date1,
                            String date2, String date3, String date4, String date5, String text1, String text2, String text3,
                            String text4, String text5, String text6, String text7, String text8, String text9, String text10,
                            String text11, String text12, String text13, String text14, String text15, String text16,
                            String text17, String text18, String text19, String text20, String text21, String text22,
                            String text23, String text24, String text25, String text26, String text27, String text28,
                            String text29, String text30) {
        RequestQueue requestQueueTR01 = Volley.newRequestQueue(this);
        String server_url = DatabaseHelper.url_api + "dataupload/uploadTR01.php";
        StringRequest stringRequestTR01 = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonPostTR01 = new JSONObject(response);
                            if (jsonPostTR01.getString("UPLOAD").equals("SUCCESS")) {
                                dbHelper.change_statusuploadtr01(id, nodoc);
                                loaduploaddata(UploadData.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        requestQueueTR01.stop();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
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
        requestQueueTR01.add(stringRequestTR01);
    }

    public static void uploadTR01public(Activity context, int id, String nodoc, String datatype, String subdatatype, String compid, String siteid, String date1,
                                  String date2, String date3, String date4, String date5, String text1, String text2, String text3,
                                  String text4, String text5, String text6, String text7, String text8, String text9, String text10,
                                  String text11, String text12, String text13, String text14, String text15, String text16,
                                  String text17, String text18, String text19, String text20, String text21, String text22,
                                  String text23, String text24, String text25, String text26, String text27, String text28,
                                  String text29, String text30) {

        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(context);

        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        RequestQueue requestQueueTR01 = Volley.newRequestQueue(context);
        String server_url = DatabaseHelper.url_api + "dataupload/uploadTR01.php";
        StringRequest stringRequestTR01 = new StringRequest(Request.Method.POST, server_url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonPostTR01 = new JSONObject(response);
                        if (jsonPostTR01.getString("UPLOAD").equals("SUCCESS")) {
                            dbHelper.change_statusuploadtr01(id, nodoc);
                            HomeFragment.loadlvinfohome(todayDate);
                            HomeFragment.loadLvHistoryCarLog(todayDate);
                            HomeFragment.loadLvHistoryApel(todayDate);
                            HomeFragment.loadLvHistoryRKH(todayDate);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(context, "Cek koneksi jaringan", Toast.LENGTH_LONG).show();
                    requestQueueTR01.stop();
                }
            }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
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
        requestQueueTR01.add(stringRequestTR01);
    }

    public static void uploadTR02(Activity context, int id, String nodoc, String datatype, String subdatatype,
        String itemdata, String subitemdata, String compid, String siteid, String date1, String date2, String date3,
        String date4, String date5, String text1, String text2, String text3, String text4, String text5,
        String text6, String text7, String text8, String text9, String text10, String text11, String text12,
        String text13, String text14, String text15, String text16, String text17, String text18,
        String text19, String text20, String text21, String text22, String text23, String text24,
        String text25, String text26, String text27, String text28, String text29, String text30) {

        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(context);

        RequestQueue requestQueueTR02 = Volley.newRequestQueue(context);
        String server_url = DatabaseHelper.url_api + "dataupload/uploadTR02.php";
        StringRequest stringRequestTR02 = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonPostTR02 = new JSONObject(response);

                    if (jsonPostTR02.getString("UPLOAD").equals("SUCCESS")) {
                        dbHelper.change_statusuploadtr02(id, nodoc);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    error.printStackTrace();
                    requestQueueTR02.stop();
                }
            })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramstr02 = new HashMap<String, String>();
                paramstr02.put("nodoc", nodoc);
                paramstr02.put("datatype", datatype);
                paramstr02.put("subdatatype", subdatatype);
                paramstr02.put("itemdata", itemdata);
                paramstr02.put("subitemdata", subitemdata);
                paramstr02.put("compid", compid);
                paramstr02.put("siteid", siteid);
                paramstr02.put("date1", date1);
                paramstr02.put("date2", date2);
                paramstr02.put("date3", date3);
                paramstr02.put("date4", date4);
                paramstr02.put("date5", date5);
                paramstr02.put("text1", text1);
                paramstr02.put("text2", text2);
                paramstr02.put("text3", text3);
                paramstr02.put("text4", text4);
                paramstr02.put("text5", text5);
                paramstr02.put("text6", text6);
                paramstr02.put("text7", text7);
                paramstr02.put("text8", text8);
                paramstr02.put("text9", text9);
                paramstr02.put("text10", text10);
                paramstr02.put("text11", text11);
                paramstr02.put("text12", text12);
                paramstr02.put("text13", text13);
                paramstr02.put("text14", text14);
                paramstr02.put("text15", text15);
                paramstr02.put("text16", text16);
                paramstr02.put("text17", text17);
                paramstr02.put("text18", text18);
                paramstr02.put("text19", text19);
                paramstr02.put("text20", text20);
                paramstr02.put("text21", text21);
                paramstr02.put("text22", text22);
                paramstr02.put("text23", text23);
                paramstr02.put("text24", text24);
                paramstr02.put("text25", text25);
                paramstr02.put("text26", text26);
                paramstr02.put("text27", text27);
                paramstr02.put("text28", text28);
                paramstr02.put("text29", text29);
                paramstr02.put("text30", text30);
                paramstr02.put("userid", dbHelper.get_tbl_username(0));
                return paramstr02;
            }
        };
        requestQueueTR02.add(stringRequestTR02);
    }

    public static void uploadBL01(Activity context, int id, String nodoc, String datatype, String subdatatype, String itemdata, String subitemdata,
                            String compid, String siteid, String remarks, String base64Blob1, String base64Blob2,
                            String base64Blob3, String base64Blob4, String base64Blob5) {

        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(context);

        RequestQueue requestQueueBlob = Volley.newRequestQueue(context);
        String server_url = DatabaseHelper.url_api + "dataupload/uploadBL01.php";
        StringRequest stringRequestImg = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonPostBL02 = new JSONObject(response);

                    if (jsonPostBL02.getString("UPLOAD").equals("SUCCESS")) {
                        dbHelper.change_statusuploadimg(id, nodoc);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    requestQueueBlob.stop();
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
                paramsImg.put("blob1", base64Blob1);
                paramsImg.put("blob2", base64Blob2);
                paramsImg.put("blob3", base64Blob3);
                paramsImg.put("blob4", base64Blob4);
                paramsImg.put("blob5", base64Blob5);
                paramsImg.put("useridimg", dbHelper.get_tbl_username(0));
                return paramsImg;
            }
        };
        requestQueueBlob.add(stringRequestImg);
    }

    public static void loaduploaddata(Activity context) {

        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(context);

        List<UploadParam> uploadParams;
        UploadAdapter uploadAdapter;

        listviewUpload = (ListView) context.findViewById(R.id.lvUpload);

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
        uploadAdapter = new UploadAdapter(context, R.layout.upload_list, uploadParams);
        listviewUpload.setAdapter(uploadAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        setResult(2, backIntent);
        finish();
    }

}