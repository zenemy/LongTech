package com.julong.longtech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CountDownTimer;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "db_dsi.db", null,
                26);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Setting awal dan User
        db.execSQL("CREATE TABLE tbl_companyurl (groupcompanycode text, logocomp blob, backgroundimg blob, systemname text, urlapi text, " +
                "picname TEXT, picemail TEXT, picnotelp TEXT, compaddress TEXT, reg_type text, tdate date, headercolor text, textcolor text, lastupdate date)");

        db.execSQL("CREATE TABLE tbl_username (userid text PRIMARY KEY, username text, " +
                "usertype text, userrole text, password text, pin text, reg_status varchar, " +
                "inactive text, empcode text, no_telp text, empname text, email text, " +
                "position_id text, position_name varchar, comp_id varchar, site_id varchar, " +
                "deptcode varchar, divcode varchar, gangcode varchar, " +
                "ancakcode varchar, shiftcode varchar, lastlogin text, groupcompanycode varchar, userphoto blob, usercolorheader text, " +
                "usercolortext text, language varchar);");

        db.execSQL("CREATE TABLE gs_01 (groupparamcode TEXT, groupparamdesc TEXT, parametercode TEXT, parameterdesc TEXT, " +
                "param_ref TEXT, uom TEXT, seq_no TEXT, rate REAL, range_min REAL, range_max REAL, inactive TEXT, " +
                "tdate DATETIME, remarks TEXT, link_download TEXT);");

        db.execSQL("CREATE TABLE gs_02 (groupcode TEXT, groupparamdesc TEXT, modulecode TEXT, moduledesc TEXT, controlsystem TEXT, " +
                "doctypecode TEXT, submodulecode TEXT, submoduledesc TEXT, submoduletype TEXT, submodulestatus TEXT, seq TEXT, menuview TEXT," +
                "menudefault TEXT, reporttype TEXT, partname TEXT, inactive TEXT);");

        db.execSQL("CREATE TABLE gs_03 (reportcode TEXT, paramcode TEXT, paramcode2 TEXT, paramdesc TEXT, datatype TEXT, " +
                "subparamcode TEXT, subparamdesc TEXT, subparamvalue TEXT, dateview1 TEXT, dateview2 TEXT, querydata TEXT, isrequired TEXT);");

        db.execSQL("CREATE TABLE gs_04 (groupcode TEXT, subgroupcode TEXT, langague_desc1 TEXT, langague_desc2 TEXT, langague_desc3 TEXT, langague_desc4 TEXT);");

        db.execSQL("CREATE TABLE gs_05 (groupcompanycode TEXT, logocomp BLOB, backgroundimg BLOB, systemname TEXT, urlapi TEXT, " +
                "picname TEXT, picemail TEXT, pictelp TEXT, address TEXT, city TEXT, reg_type TEXT, tdate DATETIME);");

        db.execSQL("CREATE TABLE gs_06 (groupcompanycode TEXT, companyid TEXT, companyname TEXT, companysiteid TEXT, companysitename TEXT, initialcode TEXT, functioncode TEXT, " +
                "sitetype TEXT, region TEXT, areacode TEXT, address TEXT, city TEXT, province TEXT, zipcode TEXT, telp TEXT, fax TEXT, " +
                "email TEXT, tax_registry_code TEXT, tax_registry_date DATETIME, capitalized_ppn TEXT, npnp TEXT);");

        db.execSQL("CREATE TABLE gs_07 (userid text PRIMARY KEY, username text, " +
                "usertype text, userrole text, password text, pin text, reg_status varchar, " +
                "inactive text, empcode text, no_telp text, empname text, email text, " +
                "position_id text, position_name varchar, comp_id varchar, site_id varchar, " +
                "deptcode varchar, divcode varchar, gangcode varchar, " +
                "ancakcode varchar, shiftcode varchar, lastlogin text, groupcompanycode varchar, userphoto blob, usercolorheader text, " +
                "usercolortext text, language varchar);");

        db.execSQL("CREATE TABLE gs_08 (rolecode TEXT, roledesc TEXT, modulecode TEXT, submodulecode TEXT, authorized TEXT, comp_id TEXT, site_id TEXT);");

        db.execSQL("CREATE TABLE gs_09 (userid TEXT, modulecode TEXT, submodulecode TEXT, authorized TEXT, comp_id TEXT, site_id TEXT);");

        db.execSQL("CREATE TABLE tr_01 (documentno varchar, datatype varchar, subdatatype varchar, comp_id varchar, site_id varchar, date1 date, date2 date, date3 date, " +
                "date4 date, date5 date, text1 varchar, text2 varchar, text3 varchar, text4 varchar, text5 varchar, text6 varchar, " +
                "text7 varchar, text8 varchar, text9 varchar, text10 varchar, text11 varchar, text12 varchar, " +
                "text13 varchar, text14 varchar, text15 varchar, text16 varchar, text17 varchar, text18 varchar, text19 varchar, " +
                "text20 varchar, text21 varchar, text22 varchar, text23 varchar, text24 varchar, text25 varchar, text26 varchar, " +
                "text27 varchar, text28 varchar, text29 varchar, text30 varchar, uploaded integer);");

        db.execSQL("CREATE TABLE tr_02 (documentno varchar, datatype varchar, subdatatype varchar, itemdata varchar, subitemdata varchar, comp_id varchar, site_id varchar, date1 date, date2 date, date3 date, " +
                "date4 date, date5 date, text1 varchar, text2 varchar, text3 varchar, text4 varchar, text5 varchar, text6 varchar, " +
                "text7 varchar, text8 varchar, text9 varchar, text10 varchar, text11 varchar, text12 varchar, " +
                "text13 varchar, text14 varchar, text15 varchar, text16 varchar, text17 varchar, text18 varchar, text19 varchar, " +
                "text20 varchar, text21 varchar, text22 varchar, text23 varchar, text24 varchar, text25 varchar, text26 varchar, " +
                "text27 varchar, text28 varchar, text29 varchar, text30 varchar, uploaded integer);");

        db.execSQL("CREATE TABLE md_01 (datatype varchar, subdatatype varchar, comp_id varchar, site_id varchar, date1 date, date2 date, date3 date, " +
                "date4 date, date5 date, text1 varchar, text2 varchar, text3 varchar, text4 varchar, text5 varchar, text6 varchar, " +
                "text7 varchar, text8 varchar, text9 varchar, text10 varchar, text11 varchar, text12 varchar, " +
                "text13 varchar, text14 varchar, text15 varchar, text16 varchar, text17 varchar, text18 varchar, text19 varchar, " +
                "text20 varchar, text21 varchar, text22 varchar, text23 varchar, text24 varchar, text25 varchar, text26 varchar, " +
                "text27 varchar, text28 varchar, text29 varchar, text30 varchar);");

        db.execSQL("CREATE TABLE bl_01 (documentno varchar, datatype varchar, subdatatype varchar, itemdata varchar, subitemdata varchar, comp_id varchar, site_id varchar, " +
                "remarks varchar, blob1 blob, blob2 blob, blob3 blob, blob4 blob, blob5 blob, uploaded integer);");

        db.execSQL("CREATE TABLE tbl_version (systemcode varchar, systemname varchar, versionnumber integer, versionname varchar, remarks varchar, status varchar, " +
                "versionnumber_new integer, versionname_new varchar, tdate_new varchar, remarks_new varchar);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_username");
        db.execSQL("DROP TABLE IF EXISTS tbl_companyurl");
        db.execSQL("DROP TABLE IF EXISTS gs_01");
        db.execSQL("DROP TABLE IF EXISTS gs_02");
        db.execSQL("DROP TABLE IF EXISTS gs_03");
        db.execSQL("DROP TABLE IF EXISTS gs_04");
        db.execSQL("DROP TABLE IF EXISTS gs_05");
        db.execSQL("DROP TABLE IF EXISTS gs_06");
        db.execSQL("DROP TABLE IF EXISTS gs_07");
        db.execSQL("DROP TABLE IF EXISTS gs_08");
        db.execSQL("DROP TABLE IF EXISTS gs_09");
        db.execSQL("DROP TABLE IF EXISTS tr_01");
        db.execSQL("DROP TABLE IF EXISTS tr_02");
        db.execSQL("DROP TABLE IF EXISTS bl_01");
        db.execSQL("DROP TABLE IF EXISTS md_01");
        db.execSQL("DROP TABLE IF EXISTS tbl_version");
        onCreate(db);
    }

    public boolean generate_tbl_version(String v_versionnumber_new, String v_versionname_new, String v_tdate, String v_remarks_new) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_version");
        ContentValues contentValues = new ContentValues();
        contentValues.put("systemcode", "LONGTECH01");
        contentValues.put("systemname", "DSI SYSTEM");
        contentValues.put("versionnumber", 0);
        contentValues.put("versionname", "Version 0.1");
        contentValues.put("versionnumber_new", v_versionnumber_new);
        contentValues.put("versionname_new", v_versionname_new);
        contentValues.put("tdate_new", v_tdate);
        contentValues.put("remarks_new", v_remarks_new);

        // Insert Row
        long insert = db.insert("tbl_version", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void delete_data_username(){
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("delete from tbl_username");
    }

    public void delete_datags() {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM gs_01");
        dbwrite.execSQL("DELETE FROM gs_06");
        dbwrite.execSQL("DELETE FROM gs_07");
    }

    public void delete_menuGS02(String moduledesc) {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM gs_02 WHERE moduledesc = '"+moduledesc+"'");
    }

    public void delete_masterdata() {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM md_01");
    }

    public void update_password(String password){
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("update tbl_username set password = '"+password+"'");
    }

    public boolean generate_tbl_username(String username, String password, String empcode, String no_telp,
                                         byte[] blob1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_username");
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("empcode",empcode);
        contentValues.put("no_telp", no_telp);
        contentValues.put("empname", username);
        contentValues.put("blob1", blob1);


        // Insert Row
        long insert = db.insert("tbl_username", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_judulcolor(String colorHex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("judulcolor", colorHex);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getpickupdata(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tbl_pickup_data " +
                "where datatype = 'LOGIN' " +
                "and subdatatype = 'SETTING' " +
                "and text1 = 'SMSGATEWAY';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(i).toString();
        } else {
            return null;
        }
    }

    public boolean update_basetextcolor(String colorHex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("textcolor", colorHex);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_themecolor(String colorHex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("themecolor", colorHex);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_bgcolor(String colorHex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bgcolor", colorHex);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_profilepic(byte[] profilepic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userphoto", profilepic);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_gambar(byte[] blob1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("blob1", blob1);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_logo(byte[] blob1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("blob2", blob1);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_background(byte[] blob1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("blob3", blob1);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }


    public byte[] get_gambar_user() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT userphoto FROM tbl_username;", null);
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getBlob(0);
        } else {
            return null;
        }
    }


    public byte[] get_companyimg(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT logocomp, backgroundimg FROM tbl_companyurl;", null);
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getBlob(index);
        } else {
            return null;
        }
    }

    public String get_tbl_version(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query1 = "select systemcode, systemname, versionnumber, versionname, remarks, status," +
                " ifnull (versionnumber_new, versionnumber) versionnumber_new," +
                " ifnull (versionname_new, versionname) versionname_new," +
                " ifnull (tdate_new,'-') tdate_new, ifnull (remarks_new, '-') remarks_new from tbl_version";

        Cursor cursor = db.rawQuery(query1, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(i).toString();
        } else {
            return null;
        }
    }

    public boolean update_userlanguage(String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("language", language);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_nama_system(String nama_system) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("systemname", nama_system);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_userpphoto(byte[] userphoto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userphoto", userphoto);

        // Insert Row
        long insert = db.update("tbl_username", contentValues, null, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String get_companyinfo(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tbl_companyurl", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(i).toString();
        } else {
            return "0";
        }
    }

    public String get_tbl_username(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tbl_username", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(i).toString();
        } else {
            return "0";
        }
    }

    public String get_tblcompany(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_companyurl", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(i).toString();
        } else {
            return "0";
        }
    }

    public String get_vehiclecode(int index, String vehiclename) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1, text30 FROM md_01 WHERE text2 = '"+vehiclename+"' AND datatype = 'VEHICLE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return "0";
        }
    }

    public String get_empcode(String empname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1 FROM md_01 WHERE text2 = '"+empname+"' AND datatype = 'EMPLOYEE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    public String get_empname(String empcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text2 FROM md_01 WHERE text1 = '"+empcode+"' AND datatype = 'EMPLOYEE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    public boolean insert_tblusername(String userid, String username, String usertype, String position_name, String comp_id, String site_id, String deptcode, String divcode, String gangcode,
                                      String ancakcode, String shiftcode, String no_telp, String email, String empname, String empcode, String password, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("username", username);
        contentValues.put("usertype", usertype);
        contentValues.put("position_name", position_name);
        contentValues.put("comp_id", comp_id);
        contentValues.put("site_id", site_id);
        contentValues.put("deptcode", deptcode);
        contentValues.put("divcode", divcode);
        contentValues.put("gangcode", gangcode);
        contentValues.put("ancakcode", ancakcode);
        contentValues.put("shiftcode", shiftcode);
        contentValues.put("no_telp", no_telp);
        contentValues.put("email", email);
        contentValues.put("empname", empname);
        contentValues.put("empcode", empcode);
        contentValues.put("password", password);
        contentValues.put("language", language);

        long insert = db.insert("tbl_username", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_kendala(String nodoc, String kategorikendala, String lokasi, String panjang, String lebar,
                                  String luas, String desckendala, String latitude, String longitude, byte[] fotokendala) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "KDL");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", kategorikendala);
        contentValues.put("text2", lokasi);
        contentValues.put("text3", panjang);
        contentValues.put("text4", lebar);
        contentValues.put("text5", luas);
        contentValues.put("text6", desckendala);
        contentValues.put("text7", latitude);
        contentValues.put("text8", longitude);
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "KDL");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));
        contentValuesPhoto.put("blob1", fotokendala);
        contentValuesPhoto.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        long insertPhoto = db.insert("bl_01", null, contentValuesPhoto);
        if (insert == -1 && insertPhoto == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_absmdr(String nodoc, String kategoriabsen, String jenisabsensi, String lokasiabsen, String latitude,
                                  String longitude, byte[] fotoabsen) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "ABSMDR");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", get_tbl_username(0));
        contentValues.put("text2", kategoriabsen);
        contentValues.put("text3", jenisabsensi);
        contentValues.put("text4", lokasiabsen);
        contentValues.put("text5", latitude);
        contentValues.put("text6", longitude);
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "ABSMDR");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("subitemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));
        contentValuesPhoto.put("blob1", fotoabsen);
        contentValuesPhoto.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        long insertPhoto = db.insert("bl_01", null, contentValuesPhoto);
        if (insert == -1 && insertPhoto == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_absensimesin(String nodoc, String idabsensi, String nikemp, String kategoriabsen,
                                       String jenisabsensi, String lokasiabsen, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "ABSMSN");
        contentValues.put("subdatatype", idabsensi);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", nikemp);
        contentValues.put("text2", kategoriabsen);
        contentValues.put("text3", jenisabsensi);
        contentValues.put("text4", lokasiabsen);
        contentValues.put("text5", latitude);
        contentValues.put("text6", longitude);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_dataP2H_header(String nodoc, String codegroupvehicle, String vehiclecode, String note, String lat, String longitude, byte[] fotop2h) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "P2HVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", codegroupvehicle);
        contentValues.put("text2", vehiclecode);
        contentValues.put("text3", get_tbl_username(0));
        contentValues.put("text4", note);
        contentValues.put("text5", lat);
        contentValues.put("text6", longitude);
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "P2HVH");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("subitemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));
        contentValuesPhoto.put("blob1", fotop2h);
        contentValuesPhoto.put("uploaded", 0);


        long insert = db.insert("tr_01", null, contentValues);
        long insertPhoto = db.insert("bl_01", null, contentValuesPhoto);
        if (insert == -1 && insertPhoto == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_dataP2H_detail(String nodoc, String itemcode, String checkname, String checkedornot, String noteItemCheck) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "P2HVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("itemdata", "DETAIL1");
        contentValues.put("subitemdata", itemcode);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", checkname);
        contentValues.put("text2", checkedornot);
        contentValues.put("text3", noteItemCheck);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean delete_rkh_header(String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long insert = db.delete("tr_01", "documentno = '"+nodoc+"' and datatype = 'RKHVH'", null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_rkh_header(String nodoc, String totalrkh, String desc, String status) {

        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tglPelaksanaan = dateFormat.format(tomorrow);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RKHVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("date2", tglPelaksanaan);
        contentValues.put("text1", get_tbl_username(0));
        contentValues.put("text2", totalrkh);
        contentValues.put("text3", desc);
        contentValues.put("text4", status);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_rkh_detail1(String nodoc, String vehiclecode, String vehiclename, String shiftkerja,
                                      String driver, String helper1, String helper2, String kebutuhanBBM) {

        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tglPelaksanaan = dateFormat.format(tomorrow);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RKHVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("itemdata", "DETAIL1");
        contentValues.put("subitemdata", vehiclecode);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("date2", tglPelaksanaan);
        contentValues.put("text1", vehiclename);
        contentValues.put("text2", shiftkerja);
        contentValues.put("text3", driver);
        contentValues.put("text4", helper1);
        contentValues.put("text5", helper2);
        contentValues.put("text6", kebutuhanBBM);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_rkh_detail2(String nodoc, String vehiclecode, String vehiclename, String shiftkerja, String driver, String loadtype, String satuankerja,
                                      String lokasikerja, String kegiatanKerja,  String waktuKerja, String HMKM, String prestasiKerja, String desc) {

        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tglPelaksanaan = dateFormat.format(tomorrow);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RKHVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("itemdata", "DETAIL2");
        contentValues.put("subitemdata", vehiclecode);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("date2", tglPelaksanaan);
        contentValues.put("text1", vehiclename);
        contentValues.put("text2", shiftkerja);
        contentValues.put("text3", driver);
        contentValues.put("text4", loadtype);
        contentValues.put("text5", satuankerja);
        contentValues.put("text6", lokasikerja);
        contentValues.put("text7", kegiatanKerja);
        contentValues.put("text8", waktuKerja);
        contentValues.put("text9", HMKM);
        contentValues.put("text10", prestasiKerja);
        contentValues.put("text11", desc);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor view_tbl_uploadlist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT (CASE WHEN a.datatype LIKE '%ABS%' AND b.doctypecode LIKE '%ABS%' THEN 'ABS' " +
                "WHEN a.datatype IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') " +
                "AND b.doctypecode IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') THEN 'VH' END) AS kodeabsen, " +
                "(CASE WHEN a.datatype LIKE '%ABS%' AND b.doctypecode LIKE '%ABS%' THEN 'Transaksi Absen' WHEN a.datatype IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') " +
                "AND b.doctypecode IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') THEN 'Transaksi Vehicle' END) AS dataabsen, " +
                "SUM(CASE WHEN a.uploaded = 0 THEN 1 ELSE 0 END) AS datapending, SUM(CASE WHEN a.uploaded = 1 THEN 1 ELSE 0 END) AS uploadeddata " +
                "FROM tr_01 a INNER JOIN gs_02 b ON a.datatype = b.doctypecode " +
                "GROUP BY dataabsen HAVING datapending > 0 ORDER BY b.submodulecode", null);
        return cursor;
    }

    public Cursor listview_rkh(String nodoc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1, text2, text3, text4, text5, text6 FROM tr_02 WHERE documentno = '"+nodoc+"' AND datatype = 'RKHVH' AND itemdata = 'DETAIL1'", null);
        return cursor;
    }

    public Cursor listview_rincianrkh(String nodoc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11 FROM tr_02 WHERE documentno = '"+nodoc+"' AND datatype = 'RKHVH' AND itemdata = 'DETAIL1'", null);
        return cursor;
    }

    public boolean insert_absvh(String nodoc, String kategoriabsen, String jenisabsensi,
                                String lokasiabsen, String latitude, String longitude, byte[] fotoabsen) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "ABSVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", get_tbl_username(0));
        contentValues.put("text2", kategoriabsen);
        contentValues.put("text3", jenisabsensi);
        contentValues.put("text4", lokasiabsen);
        contentValues.put("text5", latitude);
        contentValues.put("text6", longitude);
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "ABSVH");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("subitemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));
        contentValuesPhoto.put("blob1", fotoabsen);
        contentValuesPhoto.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        long insertPhoto = db.insert("bl_01", null, contentValuesPhoto);
        if (insert == -1 && insertPhoto == -1) {
            return false;
        } else {
            return true;
        }

    }

    public String get_count_tbl_username() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM tbl_username", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    public String get_count_totalrkh(String nodoc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tr_02 WHERE documentno = '"+nodoc+"' AND datatype = 'RKHVH'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    //Check menu GS01
    public String count_dataGS01(String groupparamcode, String groupparamdesc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM gs_01 WHERE groupparamcode = '"+ groupparamcode +"' AND groupparamdesc = '"+ groupparamdesc +"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String count_datadownloadGS() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT (SELECT COUNT(*) FROM gs_01 WHERE GROUPPARAMCODE IN ('GS04', 'GS05', 'GS06', 'GS07', 'GS08', 'GS10', 'GS11')) + (SELECT count(*) FROM gs_06) + (SELECT count(*) FROM gs_07) AS total_rows;", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    public String count_tablemd() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM md_01", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    //Check menu GS02
    public String count_hcmmenuexist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM gs_02 WHERE groupcode = '01' AND groupparamdesc = 'HCM' AND modulecode = '0101'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String count_menuGS02(String groupcode, String groupparamdesc, String modulecode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM gs_02 WHERE groupcode = '" + groupcode + "' AND groupparamdesc = '" + groupparamdesc + "' AND " +
                "modulecode = '"+ modulecode +"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String check_mesinabsensi(int index, String fingerid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1, text2, text20, text28 FROM md_01 WHERE datatype = 'EMPLOYEE' AND text28 = '" + fingerid + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return null;
        }
    }

    public boolean insert_companyinfo(String groupcompanycode, byte[] logocomp, byte[] bgimage, String sysname, String urlapi, String picname, String picemail, String pictelp, String compaddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupcompanycode", groupcompanycode);
        contentValues.put("logocomp", logocomp);
        contentValues.put("backgroundimg", bgimage);
        contentValues.put("systemname", sysname);
        contentValues.put("urlapi", urlapi);
        contentValues.put("picname", picname);
        contentValues.put("picemail", picemail);
        contentValues.put("picnotelp", pictelp);
        contentValues.put("compaddress", compaddress);

        long insert = db.insert("tbl_companyurl", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<String> get_loginlanguage() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT parameterdesc FROM gs_01 WHERE groupparamcode = 'GS02' AND groupparamdesc = 'LANGUAGE' ORDER BY seq_no ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public Cursor view_upload_tr01() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT documentno, datatype, subdatatype, comp_id, site_id, IFNULL(date1, '') AS date1, IFNULL(date2, '') AS date2, " +
                "IFNULL(date3, '') AS date3, IFNULL(date4, '') AS date4, IFNULL(date5, '') AS date5, IFNULL(text1, '') AS text1, IFNULL(text2, '') AS text2, IFNULL(text3, '') AS text3, " +
                "IFNULL(text4, '') AS text4, IFNULL(text5, '') AS text5, IFNULL(text6, '') AS text6, IFNULL(text7, '') AS text7, IFNULL(text8, '') AS text8, IFNULL(text9, '') AS text9, " +
                "IFNULL(text10, '') AS text10, IFNULL(text11, '') AS text11, IFNULL(text12, '') AS text12, IFNULL(text13, '') AS text13, IFNULL(text14, '') AS text14, IFNULL(text15, '') AS text15, " +
                "IFNULL(text16, '') AS text16, IFNULL(text17, '') AS text17, IFNULL(text18, '') AS text18, IFNULL(text19, '') AS text19, IFNULL(text20, '') AS text20, IFNULL(text21, '') AS text21, " +
                "IFNULL(text22, '') AS text22, IFNULL(text23, '') AS text23, IFNULL(text24, '') AS text24, IFNULL(text25, '') AS text25, IFNULL(text26, '') AS text26, IFNULL(text27, '') AS text27, " +
                "IFNULL(text28, '') AS text28, IFNULL(text29, '') AS text29, IFNULL(text30, '') AS text30 FROM tr_01 WHERE uploaded = 0", null);
        return cursor;
    }

    public Cursor view_upload_tr02() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT documentno, datatype, subdatatype, itemdata, subitemdata, comp_id, site_id, IFNULL(date1, '') AS date1, IFNULL(date2, '') AS date2, IFNULL(date3, '') AS date3, " +
                "IFNULL(date4, '') AS date4, IFNULL(date5, '') AS date5, IFNULL(text1, '') AS text1, IFNULL(text2, '') AS text2, IFNULL(text3, '') AS text3, IFNULL(text4, '') AS text4, " +
                "IFNULL(text5, '') AS text5, IFNULL(text6, '') AS text6, IFNULL(text7, '') AS text7, IFNULL(text8, '') AS text8, IFNULL(text9, '') AS text9, IFNULL(text10, '') AS text10, \n" +
                "IFNULL(text11, '') AS text11, IFNULL(text12, '') AS text12, IFNULL(text13, '') AS text13, IFNULL(text14, '') AS text14, IFNULL(text15, '') AS text15, IFNULL(text16, '') AS text16, " +
                "IFNULL(text17, '') AS text17, IFNULL(text18, '') AS text18, IFNULL(text19, '') AS text19, IFNULL(text20, '') AS text20, IFNULL(text21, '') AS text21, IFNULL(text22, '') AS text22, " +
                "IFNULL(text23, '') AS text23, IFNULL(text24, '') AS text24, IFNULL(text25, '') AS text25, IFNULL(text26, '') AS text26, IFNULL(text27, '') AS text27, IFNULL(text28, '') AS text28, " +
                "IFNULL(text29, '') AS text29, IFNULL(text30, '') AS text30 FROM tr_02 WHERE uploaded = 0", null);
        return cursor;
    }

    public Cursor view_upload_bl01() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT documentno, datatype, subdatatype, itemdata, subitemdata, comp_id, site_id, IFNULL(remarks, '') AS remarks, IFNULL(blob1, 'a') AS blob1, IFNULL(blob2, 'a') AS blob2, " +
                "IFNULL(blob3, 'a') AS blob3, IFNULL(blob4, 'a') AS blob4, IFNULL(blob5, 'a') AS blob5 FROM bl_01 WHERE uploaded = 0", null);
        return cursor;
    }

    public List<String> get_vehiclemasterdata() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text2 FROM md_01 WHERE DATATYPE = 'VEHICLE'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_loadtype() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text4 FROM md_01 WHERE datatype = 'TRANSPORTRATE'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_itemkebun() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text2 FROM md_01 WHERE datatype = 'ORG_STRUCTURE'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_itemdivisi(String estate) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text4 FROM md_01 WHERE datatype = 'ORG_STRUCTURE' AND text2 = '"+estate+"'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_loadcategory(String loadtype) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text6 FROM md_01 WHERE datatype = 'TRANSPORTRATE' AND text4 = '"+loadtype+"'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_employee() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text2 FROM md_01 WHERE DATATYPE = 'EMPLOYEE'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_menukendala() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT parameterdesc FROM gs_01 WHERE groupparamcode = 'GS12' AND groupparamdesc = 'KENDALA' ORDER BY seq_no ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public boolean insert_dataGS01(String groupparamcode, String groupparamdesc, String parametercode, String parameterdesc, String seq_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupparamcode", groupparamcode);
        contentValues.put("groupparamdesc", groupparamdesc);
        contentValues.put("parametercode", parametercode);
        contentValues.put("parameterdesc", parameterdesc);
        contentValues.put("seq_no", seq_no);

        long insert = db.insert("gs_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_menuGS02(String groupcode, String groupparamdesc, String modulecode, String moduledesc, String controlsystem, String doctypecode, String submodulecode,
                                     String submoduledesc, String submoduletype, String seq_no, String menuview, String menudefault) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupcode", groupcode);
        contentValues.put("groupparamdesc", groupparamdesc);
        contentValues.put("modulecode", modulecode);
        contentValues.put("moduledesc", moduledesc);
        contentValues.put("controlsystem", controlsystem);
        contentValues.put("doctypecode", doctypecode);
        contentValues.put("submodulecode", submodulecode);
        contentValues.put("submoduledesc", submoduledesc);
        contentValues.put("submoduletype", submoduletype);
        contentValues.put("seq", seq_no);
        contentValues.put("menuview", menuview);
        contentValues.put("menudefault", menudefault);

        long insert = db.insert("gs_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_dataGS06(String groupcompanycode, String companyid, String companyname, String companysiteid, String companysitename, String initialcode, String functioncode,
                                   String sitetype, String region, String areacode, String address, String city, String province, String zipcode, String telp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupcompanycode", groupcompanycode);
        contentValues.put("companyid", companyid);
        contentValues.put("companyname", companyname);
        contentValues.put("companysiteid", companysiteid);
        contentValues.put("companysitename", companysitename);
        contentValues.put("initialcode", initialcode);
        contentValues.put("functioncode", functioncode);
        contentValues.put("sitetype", sitetype);
        contentValues.put("region", region);
        contentValues.put("areacode", areacode);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("province", province);
        contentValues.put("zipcode", zipcode);
        contentValues.put("telp", telp);

        long insert = db.insert("gs_06", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_dataGS07(String userid, String username, String usertype, String userrole, String empcode, String no_telp, String email, String empname, String positionid, String position_name,
                                   String comp_id, String site_id, String deptcode, String divcode, String gangcode, String ancakcode, String shiftcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("username", username);
        contentValues.put("usertype", usertype);
        contentValues.put("userrole", userrole);
        contentValues.put("empcode", empcode);
        contentValues.put("no_telp", no_telp);
        contentValues.put("email", email);
        contentValues.put("empname", empname);
        contentValues.put("position_id", positionid);
        contentValues.put("position_name", position_name);
        contentValues.put("comp_id", comp_id);
        contentValues.put("site_id", site_id);
        contentValues.put("deptcode", deptcode);
        contentValues.put("divcode", divcode);
        contentValues.put("gangcode", gangcode);
        contentValues.put("ancakcode", ancakcode);
        contentValues.put("shiftcode", shiftcode);

        long insert = db.insert("gs_07", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_tablemd(String datatype, String subdatatype, String compid, String siteid, String date1, String date2, String date3, String date4, String date5,
                                  String text1, String text2, String text3, String text4, String text5, String text6, String text7, String text8, String text9, String text10,
                                  String text11, String text12, String text13, String text14, String text15, String text16, String text17, String text18, String text19,
                                  String text20, String text21, String text22, String text23, String text24, String text25, String text26, String text27, String text28, String text29, String text30) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("datatype", datatype);
        contentValues.put("subdatatype", subdatatype);
        contentValues.put("comp_id", compid);
        contentValues.put("site_id", siteid);
        contentValues.put("date1", date1);
        contentValues.put("date2", date2);
        contentValues.put("date3", date3);
        contentValues.put("date4", date4);
        contentValues.put("date5", date5);
        contentValues.put("text1", text1);
        contentValues.put("text2", text2);
        contentValues.put("text3", text3);
        contentValues.put("text4", text4);
        contentValues.put("text5", text5);
        contentValues.put("text6", text6);
        contentValues.put("text7", text7);
        contentValues.put("text8", text8);
        contentValues.put("text9", text9);
        contentValues.put("text10", text10);
        contentValues.put("text11", text11);
        contentValues.put("text12", text12);
        contentValues.put("text13", text13);
        contentValues.put("text14", text14);
        contentValues.put("text15", text15);
        contentValues.put("text16", text16);
        contentValues.put("text17", text17);
        contentValues.put("text18", text18);
        contentValues.put("text19", text19);
        contentValues.put("text20", text20);
        contentValues.put("text21", text21);
        contentValues.put("text22", text22);
        contentValues.put("text23", text23);
        contentValues.put("text24", text24);
        contentValues.put("text25", text25);
        contentValues.put("text26", text26);
        contentValues.put("text27", text27);
        contentValues.put("text28", text28);
        contentValues.put("text29", text29);
        contentValues.put("text30", text30);

        long insert = db.insert("md_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_transportmd(String datatype, String subdatatype, String compid, String siteid, String text1, String text2, String text3, String text4,
                                      String text5, String text6, String text7, String text8, String text9, String text10, String text11, String text12,
                                      String text13, String text14, String text15, String text16, String text17) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("datatype", datatype);
        contentValues.put("subdatatype", subdatatype);
        contentValues.put("comp_id", compid);
        contentValues.put("site_id", siteid);
        contentValues.put("text1", text1);
        contentValues.put("text2", text2);
        contentValues.put("text3", text3);
        contentValues.put("text4", text4);
        contentValues.put("text5", text5);
        contentValues.put("text6", text6);
        contentValues.put("text7", text7);
        contentValues.put("text8", text8);
        contentValues.put("text9", text9);
        contentValues.put("text10", text10);
        contentValues.put("text11", text11);
        contentValues.put("text12", text12);
        contentValues.put("text13", text13);
        contentValues.put("text14", text14);
        contentValues.put("text15", text15);
        contentValues.put("text16", text16);
        contentValues.put("text17", text17);

        long insert = db.insert("md_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_orgstructuremd(String datatype, String subdatatype, String compid, String siteid, String text1, String text2, String text3, String text4,
                                      String text5, String text6, String text7, String text8) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("datatype", datatype);
        contentValues.put("subdatatype", subdatatype);
        contentValues.put("comp_id", compid);
        contentValues.put("site_id", siteid);
        contentValues.put("text1", text1);
        contentValues.put("text2", text2);
        contentValues.put("text3", text3);
        contentValues.put("text4", text4);
        contentValues.put("text5", text5);
        contentValues.put("text6", text6);
        contentValues.put("text7", text7);
        contentValues.put("text8", text8);

        long insert = db.insert("md_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String get_groupmenu(String groupparamdesc, String controlsystem) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT moduledesc FROM gs_02 WHERE groupparamdesc = '" + groupparamdesc + "'  AND controlsystem = '" + controlsystem + "' ORDER BY seq ASC", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String settingcarlog_satuanhasilkerja(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text7 FROM md_01 WHERE text6 = '"+category+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String layoutsetting_carlog(int index, String kategorimuatan) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text8, text9, text10, text11, text12, text13, text14, text15, text16, text17 FROM md_01 WHERE text6 = '"+kategorimuatan+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return null;
        }
    }

    public List<String> get_menuname(String groupparamdesc, String controlsystem) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT submoduledesc FROM gs_02 WHERE groupparamdesc = '" + groupparamdesc + "'  AND controlsystem = '" + controlsystem + "' ORDER BY CAST (seq AS UNSIGNED) ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(0));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public Boolean changepassword_myaccount(String updatedpassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", updatedpassword);

        long update = db.update("tbl_username", contentValues, null, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean change_statusuploadtr01(String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uploaded", 1);

        long update = db.update("tr_01", contentValues, "documentno = '"+nodoc+"'", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean change_statusuploadtr02(String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uploaded", 1);

        long update = db.update("tr_02", contentValues, "documentno = '"+nodoc+"'", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean change_statusuploadimg(String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uploaded", 1);

        long update = db.update("bl_01", contentValues, "documentno = '"+nodoc+"'", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updatestatusversion(String new_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", new_status);

        long update = db.update("tbl_version", contentValues, null, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

}