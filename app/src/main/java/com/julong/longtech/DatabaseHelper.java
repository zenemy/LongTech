package com.julong.longtech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CountDownTimer;
import android.os.StrictMode;

import com.julong.longtech.menuhcm.ApelPagi;

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

    public static String url_api = "http://longtech.julongindonesia.com/longtech/mobilesync/";
    public static String systemCode = "LONGTECH01";
    public static String systemName = "LONG TECH";
    public static int versionNumber = 1;
    public static String versionName = "Version 0.10";

    public DatabaseHelper(Context context) {
        super(context, "db_dsi.db", null, 37);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Setting awal dan User
        db.execSQL("CREATE TABLE tbl_companyurl (groupcompanycode text, logocomp blob, backgroundimg blob, systemname text, urlapi text, " +
                "picname TEXT, picemail TEXT, picnotelp TEXT, compaddress TEXT, reg_type text, tdate date, headercolor text, textcolor text, lastupdate datetime)");

        db.execSQL("CREATE TABLE tbl_username (userid text PRIMARY KEY, username text, " +
                "usertype text, userrole text, password text, encryptedpassword text, reg_status varchar, " +
                "inactive text, empcode text, no_telp text, empname text, email text, " +
                "position_id text, position_name varchar, comp_id varchar, site_id varchar, " +
                "deptcode varchar, divcode varchar, gangcode varchar, " +
                "ancakcode varchar, shiftcode varchar, lastlogin text, groupcompanycode varchar, userphoto blob, usercolorheader text, " +
                "usercolortext text, language varchar, kmhm varchar);");

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

        db.execSQL("CREATE TABLE gs_08 (rolecode TEXT, roledesc TEXT, modulecode TEXT, submodulecode TEXT, " +
                "authorized TEXT, authorized_report TEXT, comp_id TEXT, site_id TEXT);");

        db.execSQL("CREATE TABLE gs_09 (userid TEXT, modulecode TEXT, submodulecode TEXT, authorized TEXT, comp_id TEXT, site_id TEXT);");

        db.execSQL("CREATE TABLE tr_01 (trid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, documentno varchar, datatype varchar, " +
                "subdatatype varchar, comp_id varchar, site_id varchar, date1 date, date2 date, date3 date, " +
                "date4 date, date5 date, text1 varchar, text2 varchar, text3 varchar, text4 varchar, text5 varchar, text6 varchar, " +
                "text7 varchar, text8 varchar, text9 varchar, text10 varchar, text11 varchar, text12 varchar, " +
                "text13 varchar, text14 varchar, text15 varchar, text16 varchar, text17 varchar, text18 varchar, text19 varchar, " +
                "text20 varchar, text21 varchar, text22 varchar, text23 varchar, text24 varchar, text25 varchar, text26 varchar, " +
                "text27 varchar, text28 varchar, text29 varchar, text30 varchar, uploaded integer);");

        db.execSQL("CREATE TABLE tr_02 (trid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, documentno varchar, datatype varchar, " +
                "subdatatype varchar, itemdata varchar, subitemdata varchar, comp_id varchar, site_id varchar, date1 date, date2 date, date3 date, " +
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
                "text27 varchar, text28 varchar, text29 varchar, text30 varchar, webid integer, webparentid integer, webparentitemid integer);");

        db.execSQL("CREATE TABLE md_02 (datatype varchar, subdatatype varchar, itemdata varchar, subitemdata varchar, comp_id varchar, " +
                "site_id varchar, date1 date, date2 date, date3 date, date4 date, date5 date, text1 varchar, text2 varchar, text3 varchar, " +
                "text4 varchar, text5 varchar, text6 varchar, text7 varchar, text8 varchar, text9 varchar, text10 varchar, text11 varchar, " +
                "text12 varchar, text13 varchar, text14 varchar, text15 varchar, text16 varchar, text17 varchar, text18 varchar, text19 varchar, " +
                "text20 varchar, text21 varchar, text22 varchar, text23 varchar, text24 varchar, text25 varchar, text26 varchar, " +
                "text27 varchar, text28 varchar, text29 varchar, text30 varchar, webid integer, webparentid integer, webparentitemid integer);");

        db.execSQL("CREATE TABLE bl_01 (blid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, documentno varchar, datatype varchar, " +
                "subdatatype varchar, itemdata varchar, subitemdata varchar, comp_id varchar, site_id varchar, " +
                "remarks varchar, blob1 blob, blob2 blob, blob3 blob, blob4 blob, blob5 blob, uploaded integer);");

        db.execSQL("CREATE TABLE tbl_version (systemcode varchar, systemname varchar, versionnumber integer, versionname varchar, remarks varchar, status varchar, " +
                "versionnumber_new integer, versionname_new varchar, tdate_new varchar, remarks_new varchar, link_download varchar);");
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
        db.execSQL("DROP TABLE IF EXISTS md_02");
        db.execSQL("DROP TABLE IF EXISTS tbl_version");
        onCreate(db);
    }

    public boolean insert_tbl_version(String v_versionnumber_new, String v_versionname_new, String v_tdate,
                                      String v_remarks_new, String link_download) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_version");
        ContentValues contentValues = new ContentValues();
        contentValues.put("systemcode", systemCode);
        contentValues.put("systemname", systemName);
        contentValues.put("versionnumber", versionNumber);
        contentValues.put("versionname", versionName);
        contentValues.put("versionnumber_new", v_versionnumber_new);
        contentValues.put("versionname_new", v_versionname_new);
        contentValues.put("tdate_new", v_tdate);
        contentValues.put("remarks_new", v_remarks_new);
        contentValues.put("link_download", link_download);

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
        dbwrite.execSQL("DELETE FROM tbl_username");
    }

    public void delete_datags() {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM gs_01");
        dbwrite.execSQL("DELETE FROM gs_06");
        dbwrite.execSQL("DELETE FROM gs_08");
    }

    public void delete_alltrasanction() {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM tr_01");
        dbwrite.execSQL("DELETE FROM tr_02");
        dbwrite.execSQL("DELETE FROM bl_01");
    }

    public void delete_menuGS02(String moduledesc) {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM gs_02 WHERE moduledesc = '"+moduledesc+"'");
    }

    public void delete_masterdata() {
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM md_01");
        dbwrite.execSQL("DELETE FROM md_02");
    }

    public void update_password(String password){
        SQLiteDatabase dbwrite = this.getWritableDatabase();
        dbwrite.execSQL("update tbl_username set password = '"+password+"'");
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
            return cursor.getString(i);
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
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_username", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            if (cursor.isNull(i)) {
                return null;
            } else {
                return cursor.getString(i);
            }
        } else {
            return "0";
        }
    }

    public String get_infokehadiran_apel(String employeeCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gs.parameterdesc FROM tr_02 tr " +
                "INNER JOIN gs_01 gs ON gs.parametercode = tr.text4 " +
                "WHERE gs.groupparamcode = 'GS14' AND tr.datatype = 'ABSAPL' " +
                "AND tr.subitemdata = '"+employeeCode+"' AND DATE(tr.date1) = DATE('now', 'localtime');", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            if (cursor.isNull(0)) {
                return "N/A";
            } else {
                return cursor.getString(0);
            }
        } else {
            return "N/A";
        }
    }

    public String get_infokemandoranapel(int i, String gangcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT mdr.text1, mdr.text2, emp.empname, emp.empcode, mdr.text8 AS krani, " +
                "mdr.text9 AS mandor, mdr.text10 AS mandor1, mdr.text11 AS asisten FROM tbl_username emp " +
                "INNER JOIN md_01 mdr ON mdr.SUBDATATYPE = emp.gangcode " +
                "WHERE emp.gangcode = '"+gangcode+"';", null);
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

    public String get_vehiclecodegroup(int index, String vehiclename) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1, text30 FROM md_01 WHERE (text2 = '"+vehiclename+"' OR text1 = '"+vehiclename+"') AND datatype = 'VEHICLE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return "0";
        }
    }

    public String get_vehiclename(int index, String vehiclecode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text2, text30, text1 || ' - ' || text2 FROM md_01 WHERE text1 = '"+vehiclecode+"' AND datatype = 'VEHICLE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return "0";
        }
    }

    public String get_vehiclecodeonly(String vehicle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1 FROM md_01 WHERE text1 || ' - ' || text2 = '"+vehicle+"' AND datatype = 'VEHICLE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }

    public String get_empcode(int index, String employee) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text1, text2 FROM md_01 WHERE text1 || ' - ' || text2 = '"+employee+"' AND datatype = 'EMPLOYEE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return "0";
        }
    }

    public String get_position_ancakcode(int index, String employee) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT md1.text1, md1.text19, md2.text1 FROM md_01 md1 LEFT JOIN md_02 md2 " +
                "ON md2.subitemdata = md1.text1 AND md2.datatype = 'GANG' WHERE md1.text1 || ' - ' || md1.text2 = '"+employee+"' AND md1.datatype = 'EMPLOYEE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
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

    public boolean insert_tblusername(String userid, String username, String usertype, String userrole, String position_id,
                                      String position_name, String comp_id, String site_id, String deptcode, String divcode,
                                      String gangcode, String ancakcode, String shiftcode, String no_telp, String email,
                                      String empname, String empcode, String password, String encryptedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("username", username);
        contentValues.put("usertype", usertype);
        contentValues.put("userrole", userrole);
        contentValues.put("position_id", position_id);
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
        contentValues.put("encryptedpassword", encryptedPassword);

        long insert = db.insert("tbl_username", null, contentValues);
        if (insert == -1) {
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
        contentValues.put("text1", get_tbl_username(18));
        contentValues.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValues.put("text3", get_tbl_username(0));
        contentValues.put("text4", kategoriabsen);
        contentValues.put("text5", jenisabsensi);
        contentValues.put("text6", lokasiabsen);
        contentValues.put("text8", latitude);
        contentValues.put("text9", longitude);
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

    public boolean insert_pengeluaranbbm(String nodoc, String vehiclecode, String terimaLiter, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "SIVBBM");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", vehiclecode);
        contentValues.put("text2", terimaLiter);
        contentValues.put("text3", notes);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_penerimaanbbm(String nodoc, String vehiclecode, String gudangcode, String terimaLiter,
                                        String kurirCode, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RCVBBM");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", vehiclecode);
        contentValues.put("text2", gudangcode);
        contentValues.put("text3", terimaLiter);
        contentValues.put("text4", kurirCode);
        contentValues.put("text5", notes);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_permintaanbbm(String nodoc, String vehiclecode, String gudangcode, String lastKMHM,
                                        String totalmintaBBM, String notes, byte[] fotobensin) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "PRBBM");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", vehiclecode);
        contentValues.put("text2", gudangcode);
        contentValues.put("text3", lastKMHM);
        contentValues.put("text4", totalmintaBBM);
        contentValues.put("text5", notes);
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "PRBBM");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("subitemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));
        contentValuesPhoto.put("blob1", fotobensin);
        contentValuesPhoto.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        long insertPhoto = db.insert("bl_01", null, contentValuesPhoto);
        if (insert == -1 && insertPhoto == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_kegiataninspeksi(String nodoc, String kebun, String divisi, String lokasi, String resultInspeksi,
                                           String nextAction, String latitude, String longitude, byte[] fotoInspeksi) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "IHKVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", kebun);
        contentValues.put("text2", divisi);
        contentValues.put("text3", lokasi);
        contentValues.put("text4", resultInspeksi);
        contentValues.put("text5", nextAction);
        contentValues.put("text6", latitude);
        contentValues.put("text7", longitude);
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "IHKVH");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("subitemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));
        contentValuesPhoto.put("blob1", fotoInspeksi);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        long insertPhoto = db.insert("bl_01", null, contentValuesPhoto);
        if (insert == -1 && insertPhoto == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_absensimesin(String nodoc, String idabsensi, String nikemp, String kategoriabsen,
                                       String jenisabsensi, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "ABSMSN");
        contentValues.put("subdatatype", idabsensi);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", get_tbl_username(18));
        contentValues.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValues.put("text3", nikemp);
        contentValues.put("text4", kategoriabsen);
        contentValues.put("text5", jenisabsensi);
        contentValues.put("text8", latitude);
        contentValues.put("text9", longitude);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_permintaanperbaikan(String nodoc, String vehiclecode, String tipeperbaikan,
                                              String blok, String detailloc, String latitude,
                                              String longitude, byte[] fotoservice) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValuesTR01 = new ContentValues();
        contentValuesTR01.put("documentno", nodoc);
        contentValuesTR01.put("datatype", "SRWS");
        contentValuesTR01.put("subdatatype", get_tbl_username(8));
        contentValuesTR01.put("comp_id", get_tbl_username(14));
        contentValuesTR01.put("site_id", get_tbl_username(15));
        contentValuesTR01.put("date1", savedate);
        contentValuesTR01.put("text1", get_tbl_username(18));
        contentValuesTR01.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValuesTR01.put("text3", vehiclecode);
        contentValuesTR01.put("text4", tipeperbaikan);
        contentValuesTR01.put("text5", blok);
        contentValuesTR01.put("text6", detailloc);
        contentValuesTR01.put("text7", latitude);
        contentValuesTR01.put("text8", longitude);
        contentValuesTR01.put("uploaded", 0);

        ContentValues contentValuesBL = new ContentValues();
        if (fotoservice != null) {
            contentValuesBL.put("documentno", nodoc);
            contentValuesBL.put("datatype", "SRWS");
            contentValuesBL.put("subdatatype", get_tbl_username(8));
            contentValuesBL.put("itemdata", "HEADER");
            contentValuesBL.put("subitemdata", "HEADER");
            contentValuesBL.put("comp_id", get_tbl_username(14));
            contentValuesBL.put("site_id", get_tbl_username(15));
            contentValuesBL.put("blob1", fotoservice);
            contentValuesBL.put("uploaded", 0);
        }


        long insertTR = db.insert("tr_01", null, contentValuesTR01);
        if (fotoservice != null) {
            db.insert("bl_01", null, contentValuesBL);
        }
        if (insertTR == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insert_prosesperbaikan_header(String nodoc, String dateETA, String vehiclecode, String statusSubmit, String activityDesc,
                                                 String latitude, String longitude, String lokasiService) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValuesTR01 = new ContentValues();
        contentValuesTR01.put("documentno", nodoc);
        contentValuesTR01.put("datatype", "PSWS");
        contentValuesTR01.put("subdatatype", get_tbl_username(8));
        contentValuesTR01.put("comp_id", get_tbl_username(14));
        contentValuesTR01.put("site_id", get_tbl_username(15));
        contentValuesTR01.put("date1", savedate);
        contentValuesTR01.put("date2", dateETA);
        contentValuesTR01.put("text1", statusSubmit);
        contentValuesTR01.put("text2", vehiclecode);
        contentValuesTR01.put("text3", activityDesc);
        contentValuesTR01.put("text4", latitude);
        contentValuesTR01.put("text5", longitude);
        contentValuesTR01.put("text6", lokasiService);
        contentValuesTR01.put("uploaded", 0);

        long insertTR = db.insert("tr_01", null, contentValuesTR01);
        if (insertTR == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_prosesperbaikan_detailmaterial(String nodoc, String materialCode,
                                                         String materialQty, String unitOfMeasure) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "PSWS");
        contentValues.put("subdatatype", get_tbl_username(8));
        contentValues.put("itemdata", "DETAIL2");
        contentValues.put("subitemdata", materialCode);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", materialCode);
        contentValues.put("text2", materialQty);
        contentValues.put("text3", unitOfMeasure);

        long insertTR = db.insert("tr_02", null, contentValues);
        if (insertTR == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_detail_laporanperbaikan(String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'PSWS' AND itemdata = 'DETAIL1' AND uploaded IS NULL AND (text3 = '' OR text3 IS NULL)");

        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("date1", savedate);
        contentValues.putNull("text3");
        contentValues.put("uploaded", 0);

        ContentValues contentValuesMaterial = new ContentValues();
        contentValuesMaterial.put("documentno", nodoc);
        contentValuesMaterial.put("date1", savedate);
        contentValuesMaterial.put("uploaded", 0);

        long update = db.update("tr_02", contentValues, "datatype = 'PSWS' AND itemdata = 'DETAIL1' AND uploaded IS NULL", null);
        long updateMaterial = db.update("tr_02", contentValuesMaterial, "datatype = 'PSWS' AND itemdata = 'DETAIL2' AND uploaded IS NULL", null);
        if (update == -1 && updateMaterial == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String get_menucode(String moduledesc, String submoduledesc, int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT modulecode, submodulecode FROM gs_02 WHERE " +
                "moduledesc = '" + moduledesc + "' AND submoduledesc = '" + submoduledesc + "';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(i);
        } else {
            return null;
        }
    }

    public boolean insert_verifikasigis_header(String nodoc, String tglunitkerja, String vehiclecode, String drivercode, String lokasi,
                                       String kegiatan, String satuankerja, String hasilverifikasi, String catatanSOP) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "GISVH");
        contentValues.put("subdatatype", get_tbl_username(8));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("date3", tglunitkerja);
        contentValues.put("text1", vehiclecode);
        contentValues.put("text2", drivercode);
        contentValues.put("text3", lokasi);
        contentValues.put("text4", kegiatan);
        contentValues.put("text5", satuankerja);
        contentValues.put("text6", hasilverifikasi);
        contentValues.put("text7", catatanSOP);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insert_verifikasigis_detail(String nodoc, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "GISVH");
        contentValues.put("subdatatype", get_tbl_username(8));
        contentValues.put("itemdata", "DETAIL1");
        contentValues.put("subitemdata", "DETAIL1");
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", latitude);
        contentValues.put("text2", longitude);

        long insert = db.insert("tr_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String get_kmhmunit(String vehiclecode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT IFNULL(text28, '') AS kmhm FROM md_01 WHERE " +
                "datatype = 'VEHICLE' AND subdatatype = '" + vehiclecode + "';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_statuscheckinout_absmdr(String documentno) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text4 FROM tr_01 WHERE datatype = 'ABSMDR' " +
                "AND documentno = '"+documentno+"';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public boolean update_ancakcode_user(String vehiclecode, String shiftcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ancakcode", vehiclecode);
        contentValues.put("shiftcode", shiftcode);

        long update = db.update("tbl_username", contentValues, null, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update_kmhm(String kmhm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("kmhm", kmhm);

        long update = db.update("tbl_username", contentValues, null, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int getExistingEmpBriefing(String employeeCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tr_02 WHERE subitemdata = '"+employeeCode+"' " +
                "AND DATE(date1) = DATE('now', 'localtime') AND uploaded IS NULL", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    public void insert_briefing_anggota(String empcode, String jabatancode,
                                        String unitcode, String absenMethod,
                                        String absencode, String keteranganAbsen,
                                        String latitudeApel, String longitudeApel) {
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();

        if (getExistingEmpBriefing(empcode) == 0) {
            ContentValues contentValuesInsert = new ContentValues();
            contentValuesInsert.put("datatype", "ABSAPL");
            contentValuesInsert.put("subdatatype", get_tbl_username(0));
            contentValuesInsert.put("itemdata", "DETAIL2");
            contentValuesInsert.put("subitemdata", empcode);
            contentValuesInsert.put("comp_id", get_tbl_username(14));
            contentValuesInsert.put("site_id", get_tbl_username(15));
            contentValuesInsert.put("date1", savedate);
            contentValuesInsert.put("text1", jabatancode);
            contentValuesInsert.put("text2", unitcode);
            contentValuesInsert.put("text3", absenMethod);
            contentValuesInsert.put("text4", absencode);
            contentValuesInsert.put("text5", keteranganAbsen);
            contentValuesInsert.put("text6", latitudeApel);
            contentValuesInsert.put("text7", longitudeApel);
            db.insert("tr_02", null, contentValuesInsert);
        } else {
            ContentValues contentValuesUpdate = new ContentValues();
            contentValuesUpdate.put("date1", savedate);
            contentValuesUpdate.put("text3", absenMethod);
            contentValuesUpdate.put("text4", absencode);
            db.update("tr_02", contentValuesUpdate, "datatype = 'ABSAPL' AND " +
                "subitemdata = '"+empcode+"' AND DATE(date1) = DATE('now', 'localtime') AND uploaded IS NULL", null);
        }


    }

    public Boolean updatesubmit_perintahservice(String vehicleCode, String estimasiHari) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'WOWS' AND uploaded IS NULL AND (text3 = '' OR text3 IS NULL)");
        ContentValues contentValuesTR01 = new ContentValues();
        contentValuesTR01.put("text2", vehicleCode);
        contentValuesTR01.put("text3", estimasiHari);
        contentValuesTR01.put("uploaded", 0);

        ContentValues contentValuesTR02 = new ContentValues();
        contentValuesTR02.put("uploaded", 0);

        long updateTR01 = db.update("tr_01", contentValuesTR01,
                "datatype = 'WOWS' AND uploaded IS NULL", null);
        long updateTR02 = db.update("tr_02", contentValuesTR02,
                "datatype = 'WOWS' AND uploaded IS NULL", null);
        if (updateTR01 == -1 && updateTR02 == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_mekanik_laporanservice(String gangcode, String empcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        contentValues.put("datatype", "PSWS");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("itemdata", "DETAIL1");
        contentValues.put("subitemdata", empcode);
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", gangcode);
        contentValues.put("text2", empcode);
        contentValues.putNull("text3");

        long insert = db.insert("tr_02", null, contentValues);
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

    public boolean insert_adjustmentunit(String nodoc, String unitstatus, String vehiclecode, String empcode, String note, String kmhm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RUNVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", unitstatus);
        contentValues.put("text2", vehiclecode);
        contentValues.put("text3", empcode);
        contentValues.put("text4", note);
        contentValues.put("text5", kmhm);
        contentValues.put("uploaded", 0);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_rkh_header(String nodoc, String tglPelaksanaan, String vehicleType) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RKHVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date2", tglPelaksanaan);
        contentValues.put("text1", get_tbl_username(18));
        contentValues.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValues.put("text3", vehicleType);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_rkh_detail1(String nodoc, String vehiclecode, String tglPelaksanaan, String shiftkerja,
                                      String driver, String helper1, String helper2, String kebutuhanBBM) {

        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

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
        contentValues.put("text1", vehiclecode);
        contentValues.put("text2", shiftkerja);
        contentValues.put("text3", driver);
        contentValues.put("text4", helper1);
        contentValues.put("text5", helper2);
        contentValues.put("text6", kebutuhanBBM);

        long insert = db.insert("tr_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_newrkh_header(String nodoc, String tglPelaksanaan, String vehicleType) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RKHVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date2", tglPelaksanaan);
        contentValues.put("text1", get_tbl_username(18));
        contentValues.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValues.put("text3", vehicleType);

        long insert = db.insert("tr_01", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_newrkh_detail(String nodoc, String tglPelaksanaan, String vehicleType) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "RKHVH");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date2", tglPelaksanaan);
        contentValues.put("text1", get_tbl_username(18));
        contentValues.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValues.put("text3", vehicleType);

        long insert = db.insert("tr_01", null, contentValues);
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
                "AND b.doctypecode IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') THEN 'VH' " +
                "WHEN a.datatype IN('SRWS', 'WOWS', 'PSWS', 'BAWS') " +
                "AND b.doctypecode IN('SRWS', 'WOWS', 'PSWS', 'BAWS') THEN 'WS'" +
                "WHEN a.datatype IN('PRBBM', 'SIVBBM', 'RCVBBM') " +
                "AND b.doctypecode IN('PRBBM', 'SIVBBM', 'RCVBBM') THEN 'INV' END) AS kodeabsen, " +
                "(CASE WHEN a.datatype LIKE '%ABS%' AND b.doctypecode LIKE '%ABS%' THEN 'Transaksi Absen' WHEN a.datatype IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') " +
                "AND b.doctypecode IN('P2HVH', 'RKHVH', 'GISVH', 'IHKVH', 'CARLOG', 'VDA', 'APPVH', 'RUNVH', 'CHGVH', 'ADJVH') THEN 'Transaksi Vehicle' " +
                "WHEN a.datatype IN('SRWS', 'WOWS', 'PSWS', 'BAWS') " +
                "AND b.doctypecode IN('SRWS', 'WOWS', 'PSWS', 'BAWS') THEN 'Transaksi Workshop'" +
                "WHEN a.datatype IN('PRBBM', 'SIVBBM', 'RCVBBM') " +
                "AND b.doctypecode IN('PRBBM', 'SIVBBM', 'RCVBBM') THEN 'Transaksi Inventory' END) AS dataabsen, " +
                "SUM(CASE WHEN a.uploaded = 0 THEN 1 ELSE 0 END) AS datapending, SUM(CASE WHEN a.uploaded = 1 THEN 1 ELSE 0 END) AS uploadeddata " +
                "FROM tr_01 a INNER JOIN gs_02 b ON a.datatype = b.doctypecode " +
                "GROUP BY dataabsen HAVING datapending > 0 ORDER BY b.submodulecode", null);
        return cursor;
    }

    public Cursor listview_koordinatgis(String nodoc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT time(date1), text1, text2 FROM tr_02 WHERE datatype = 'GISVH' AND documentno = '"+nodoc+"'", null);
        return cursor;
    }

    public Cursor listview_historyRKH(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT tr2.documentno, strftime('%d-%m-%Y', tr2.date1) AS tglinput, " +
                "strftime('%d', tr2.date2) || ' ' || CASE strftime('%m', date(tr2.date2)) WHEN '01' THEN 'January' " +
                "WHEN '02' THEN 'Febuary' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' " +
                "WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' " +
                "WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' " +
                "ELSE '' END || ' ' || strftime('%Y', tr2.date2) AS tglpelaksannaan, mdemp.text2 AS empname, " +
                "mdactivity.text6 AS activity, mdblok.text2 AS blok, tr2.text1 AS unitcode, " +
                "tr2.text2 AS shiftcode, tr2.uploaded AS uploaded FROM tr_02 tr2 " +
                "LEFT JOIN md_01 mdemp ON mdemp.text1 = tr2.text3 AND mdemp.datatype = 'EMPLOYEE' " +
                "LEFT JOIN md_01 mdactivity ON mdactivity.text5 = tr2.text5 AND mdactivity.datatype = 'TRANSPORTRATE' " +
                "LEFT JOIN md_01 mdblok ON mdblok.text1 = tr2.text4 AND mdblok.datatype = 'FIELDCROP' " +
                "WHERE tr2.datatype = 'RKHVH' AND DATE(tr2.date1) = DATE('"+date+"') and tr2.uploaded IS NOT NULL", null);
        return cursor;
    }

    public List<String> get_listmaterialmd() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT md.text1 FROM md_01 md WHERE md.datatype = 'MATERIAL'";
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

    public String get_single_materialcode(String materialname, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT md.subdatatype AS materialcode, md.text6 AS satuan " +
                "FROM md_01 md WHERE md.datatype = 'MATERIAL' AND md.text1 = '"+materialname+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index);
        } else {
            return null;
        }
    }

    public Cursor listview_material() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT md.text1 AS material, tr2.text2 AS qty, gs.parameterdesc AS uom FROM tr_02 tr2 " +
                "INNER JOIN md_01 md ON tr2.text1 = md.subdatatype INNER JOIN gs_01 gs ON tr2.text3 = gs.parametercode " +
                "WHERE tr2.datatype = 'PSWS' AND md.datatype = 'MATERIAL' AND gs.groupparamcode = 'GS07' AND tr2.uploaded IS NULL", null);
        return cursor;
    }

    public Cursor listview_historyapel(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tr2.documentno, date(tr2.date1) AS tglapel, time(tr2.date1) AS waktuapel, md1.text2 AS empname, " +
                "md1.text20 AS jabatan, gs1.parameterdesc AS jeniskehadiran, tr2.text3 AS absenmethod, bl1.blob1 AS fotoabsen, tr2.uploaded AS uploaded FROM tr_02 tr2 " +
                "LEFT JOIN md_01 md1 ON md1.text1 = tr2.subitemdata LEFT JOIN gs_01 gs1 ON gs1.parametercode = tr2.text4 AND gs1.groupparamcode = 'GS14' LEFT JOIN bl_01 bl1 " +
                "ON tr2.subitemdata = bl1.subitemdata WHERE md1.datatype = 'EMPLOYEE' AND tr2.datatype = 'ABSAPL' " +
                "AND DATE(tr2.date1) = DATE('"+date+"') AND tr2.uploaded IS NOT NULL;", null);
        return cursor;
    }

    public Cursor listview_historycarlog(String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT tr.documentno, strftime('%d-%m-%Y %H:%M', tr.date1) AS tglawal, " +
                "tr.text1 AS unitcode, md.text4 AS jenismuatan, " +
                "md.text6 AS kategorimuatan, tr.text2 AS kmawal, tr.text18 AS kmakhir, tr.text16 AS hasilkerja, " +
                "md.text7 AS satuankerja, tr.uploaded AS uploaded FROM tr_01 tr LEFT JOIN md_01 md ON tr.text4 = md.text5 " +
                "AND md.datatype = 'TRANSPORTRATE' WHERE tr.datatype = 'CARLOG' " +
                "AND DATE(tr.date1) = DATE('"+date+"') AND tr.uploaded IS NOT NULL;", null);
        return cursor;
    }

    public Cursor listview_rkh(String nodoc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT subitemdata, text2, text3 FROM tr_02 " +
                "WHERE documentno = '"+nodoc+"' AND datatype = 'RKHVH' AND itemdata = 'DETAIL1' " +
                "AND uploaded IS NULL ORDER BY subitemdata, text2", null);
        return cursor;
    }

    public Cursor listview_rincianrkh(String nodoc, String unitcode, String shiftcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text4, text5, text6, text7, text8, text9 FROM tr_02 " +
                "WHERE documentno = '"+nodoc+"' AND itemdata = 'DETAIL2' and subitemdata = '"+unitcode+"' " +
                "AND text2 = '"+shiftcode+"' AND uploaded IS NULL", null);
        return cursor;
    }

    public Cursor listview_infohome(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gs.submodulecode AS menucode, " +
                "gs.submoduledesc AS dataname, IFNULL(tr.uploaded, '') AS statusupload, " +
                "IFNULL(strftime('%H:%M', tr.date1), '') AS transactiondate, tr.documentno FROM tr_01 tr " +
                "INNER JOIN gs_02 gs ON tr.datatype = gs.doctypecode " +
                "WHERE DATE(date1) = DATE('"+date+"') OR date1 IS NULL;", null);
        return cursor;
    }

    public Cursor listview_mekanik_mintaservice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT md.text2 AS empname, tr2.text2 AS empcode " +
                "FROM tr_02 tr2 LEFT JOIN md_01 md ON tr2.text2 = md.text1 " +
                "WHERE tr2.datatype = 'PSWS' AND tr2.itemdata = 'DETAIL1' AND md.datatype = 'EMPLOYEE' AND tr2.uploaded IS NULL", null);
        return cursor;
    }

    public Cursor listview_apelpagi_anggota() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT mdgang.subdatatype AS teamcode, " +
            "mdemp.text1 AS empcode, mdemp.text2 AS empname, mdemp.text20 AS position_name, " +
            "mdemp.text19 AS positioncode, '' AS vehicle FROM md_01 mdemp INNER JOIN " +
            "md_01 mdgang ON mdgang.text10 = mdemp.text1 WHERE mdemp.datatype = 'EMPLOYEE' " +
            "AND mdgang.datatype = 'GANG' AND mdgang.subdatatype = '"+get_tbl_username(18)+"' " +
            "UNION ALL " +
            "SELECT mdgang.subdatatype AS teamcode, mdemp.text1 AS empcode, mdemp.text2 AS empname, " +
            "mdemp.text20 AS position_name, mdemp.text19 AS positioncode, '' AS vehicle " +
            "FROM md_01 mdemp INNER JOIN md_01 mdgang ON mdgang.text9 = mdemp.text1 " +
            "WHERE mdemp.datatype = 'EMPLOYEE' AND mdgang.datatype = 'GANG' " +
            "UNION ALL " +
            "SELECT mdgang.subdatatype AS teamcode, mdemp.text1 AS empcode, mdemp.text2 AS empname, " +
            "mdemp.text20 AS position_name, mdemp.text19 AS positioncode, mdgang.text1 AS vehicle " +
            "FROM md_01 mdemp INNER JOIN md_02 mdgang ON mdgang.subitemdata = mdemp.text1 " +
            "WHERE mdemp.datatype = 'EMPLOYEE' AND mdgang.datatype = 'GANG' AND mdemp.text1 NOT IN " +
            "(SELECT subitemdata FROM tr_02 WHERE datatype = 'ABSAPL' AND date(date1) = " +
                "date('now', 'localtime') AND uploaded IS NOT NULL);", null);
        return cursor;
    }

    public boolean insert_carlog(String nodoc, String kmawal, String jenismuatan, String kategorimuatan,
                                 String tujuankebun, String tujuandivisi, String tujuanlokasi,
                                 String tujuankegiatan, String satuanmuata, String ritasemuata,
                                 String hasilkerja, String keterangan, String latitude,
                                 String longitude, String kmakhir, byte[] fotohasilkerja, byte[] fotokm) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("documentno", nodoc);
        contentValues.put("datatype", "CARLOG");
        contentValues.put("subdatatype", get_tbl_username(0));
        contentValues.put("comp_id", get_tbl_username(14));
        contentValues.put("site_id", get_tbl_username(15));
        contentValues.put("date1", savedate);
        contentValues.put("text1", get_tbl_username(19));
        contentValues.put("text2", kmawal);
        contentValues.put("text3", jenismuatan);
        contentValues.put("text4", kategorimuatan);
        contentValues.putNull("text5");
        contentValues.putNull("text6");
        contentValues.put("text10", tujuankebun);
        contentValues.put("text11", tujuandivisi);
        contentValues.put("text12", tujuanlokasi);
        contentValues.put("text13", tujuankegiatan);
        contentValues.put("text14", satuanmuata);
        contentValues.put("text15", ritasemuata);
        contentValues.put("text16", hasilkerja);
        contentValues.put("text17", keterangan);
        contentValues.put("text18", kmakhir);
        contentValues.put("text19", latitude);
        contentValues.put("text20", longitude);
        contentValues.put("text21", get_tbl_username(20));
        contentValues.put("uploaded", 0);

        ContentValues contentValuesPhoto = new ContentValues();
        contentValuesPhoto.put("documentno", nodoc);
        contentValuesPhoto.put("datatype", "CARLOG");
        contentValuesPhoto.put("subdatatype", get_tbl_username(0));
        contentValuesPhoto.put("itemdata", "HEADER");
        contentValuesPhoto.put("subitemdata", "HEADER");
        contentValuesPhoto.put("comp_id", get_tbl_username(14));
        contentValuesPhoto.put("site_id", get_tbl_username(15));

        if (fotohasilkerja != null) {
            contentValuesPhoto.put("blob1", fotohasilkerja);
        }

        contentValuesPhoto.put("blob2", fotokm);
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

    public String check_menufragment(String submodulecode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM gs_02 WHERE submodulecode = '"+submodulecode+"';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return "0";
        }
    }
    public String get_count_totalkoordinatgis(String nodoc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tr_02 WHERE datatype = 'GISVH' AND itemdata = 'DETAIL1' AND documentno = '"+nodoc+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0);
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
            return cursor.getString(0);
        } else {
            return null;
        }
    }

    public String count_datadownloadGS() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT (SELECT COUNT(*) FROM gs_01 WHERE GROUPPARAMCODE IN ('GS04', 'GS05', 'GS06', 'GS07', 'GS08', 'GS10', 'GS11', 'GS14', 'GS15', 'GS16', 'GS17')) + (SELECT count(*) FROM gs_06) + (SELECT count(*) FROM gs_08) AS total_rows;", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0);
        } else {
            return "0";
        }
    }

    public String count_tablemd() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT (SELECT COUNT(*) FROM md_01) + (SELECT COUNT(*) FROM md_02) AS total_rows;", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0);
        } else {
            return "0";
        }
    }

    public boolean insert_companyinfo(String groupcompanycode, byte[] logocomp, byte[] bgimage, String sysname, String urlapi, String picname, String picemail, String pictelp, String compaddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_companyurl");
        ContentValues contentValues = new ContentValues();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        contentValues.put("groupcompanycode", groupcompanycode);
        contentValues.put("logocomp", logocomp);
        contentValues.put("backgroundimg", bgimage);
        contentValues.put("systemname", sysname);
        contentValues.put("urlapi", urlapi);
        contentValues.put("picname", picname);
        contentValues.put("picemail", picemail);
        contentValues.put("picnotelp", pictelp);
        contentValues.put("compaddress", compaddress);
        contentValues.put("lastupdate", savedate);

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

    public Cursor view_prepareunit_rkh(String selectedVehicleGroup) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "";
        if (get_tbl_username(12).equals("AST")) {
            query = "SELECT DISTINCT md2.text1 AS unitcode, md2.text2 AS shiftcode, md2.subitemdata AS drivercode " +
                    "FROM md_02 md2 INNER JOIN md_01 md1 ON md1.text1 = md2.text1 AND md1.datatype = 'VEHICLE' " +
                    "WHERE md1.text30 = '"+selectedVehicleGroup+"'";
        } else {
            query = "SELECT DISTINCT md2.text1 AS unitcode, md2.text2 AS shiftcode, md2.subitemdata AS drivercode " +
                    "FROM md_02 md2 INNER JOIN md_01 md1 ON md1.text1 = md2.text1 AND md1.datatype = 'VEHICLE' " +
                    "WHERE md2.subdatatype = '"+get_tbl_username(18)+"' AND md1.text30 = '"+selectedVehicleGroup+"'";
        }
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor view_preparemekanik_service() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT  md2.subitemdata AS empcode, md2.text1 AS gangcode FROM md_02 md2 " +
                "INNER JOIN tbl_username u ON u.gangcode = md2.subdatatype INNER JOIN md_01 md1emp ON md1emp.text1 = md2.subitemdata " +
                "INNER JOIN md_01 md1post ON md1post.subdatatype = md1emp.text19 WHERE md1emp.datatype = 'EMPLOYEE'", null);
        return cursor;
    }

    public Cursor view_upload_tr01() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT trid, documentno, datatype, subdatatype, comp_id, site_id, IFNULL(date1, '') AS date1, " +
                "IFNULL(date2, '') AS date2, IFNULL(date3, '') AS date3, IFNULL(date4, '') AS date4, IFNULL(date5, '') AS date5, " +
                "IFNULL(text1, '') AS text1, IFNULL(text2, '') AS text2, IFNULL(text3, '') AS text3, IFNULL(text4, '') AS text4, " +
                "IFNULL(text5, '') AS text5, IFNULL(text6, '') AS text6, IFNULL(text7, '') AS text7, IFNULL(text8, '') AS text8, " +
                "IFNULL(text9, '') AS text9, IFNULL(text10, '') AS text10, IFNULL(text11, '') AS text11, IFNULL(text12, '') AS text12, " +
                "IFNULL(text13, '') AS text13, IFNULL(text14, '') AS text14, IFNULL(text15, '') AS text15, IFNULL(text16, '') AS text16, " +
                "IFNULL(text17, '') AS text17, IFNULL(text18, '') AS text18, IFNULL(text19, '') AS text19, IFNULL(text20, '') AS text20, " +
                "IFNULL(text21, '') AS text21, IFNULL(text22, '') AS text22, IFNULL(text23, '') AS text23, IFNULL(text24, '') AS text24, " +
                "IFNULL(text25, '') AS text25, IFNULL(text26, '') AS text26, IFNULL(text27, '') AS text27, IFNULL(text28, '') AS text28, " +
                "IFNULL(text29, '') AS text29, IFNULL(text30, '') AS text30 FROM tr_01 WHERE uploaded = 0", null);
        return cursor;
    }

    public Cursor view_upload_tr02() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT trid, documentno, datatype, subdatatype, itemdata, subitemdata, comp_id, site_id, " +
                "IFNULL(date1, '') AS date1, IFNULL(date2, '') AS date2, IFNULL(date3, '') AS date3, IFNULL(date4, '') AS date4, " +
                "IFNULL(date5, '') AS date5, IFNULL(text1, '') AS text1, IFNULL(text2, '') AS text2, IFNULL(text3, '') AS text3, " +
                "IFNULL(text4, '') AS text4, IFNULL(text5, '') AS text5, IFNULL(text6, '') AS text6, IFNULL(text7, '') AS text7, " +
                "IFNULL(text8, '') AS text8, IFNULL(text9, '') AS text9, IFNULL(text10, '') AS text10, IFNULL(text11, '') AS text11, " +
                "IFNULL(text12, '') AS text12, IFNULL(text13, '') AS text13, IFNULL(text14, '') AS text14, IFNULL(text15, '') AS text15, " +
                "IFNULL(text16, '') AS text16, IFNULL(text17, '') AS text17, IFNULL(text18, '') AS text18, IFNULL(text19, '') AS text19, " +
                "IFNULL(text20, '') AS text20, IFNULL(text21, '') AS text21, IFNULL(text22, '') AS text22, IFNULL(text23, '') AS text23, " +
                "IFNULL(text24, '') AS text24, IFNULL(text25, '') AS text25, IFNULL(text26, '') AS text26, IFNULL(text27, '') AS text27, " +
                "IFNULL(text28, '') AS text28, IFNULL(text29, '') AS text29, IFNULL(text30, '') AS text30 FROM tr_02 WHERE uploaded = 0", null);
        return cursor;
    }

    public Cursor view_upload_bl01() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT blid, documentno, datatype, subdatatype, itemdata, subitemdata, comp_id, site_id, IFNULL(remarks, '') AS remarks, IFNULL(blob1, '') AS blob1, " +
                "IFNULL(blob2, '') AS blob2, IFNULL(blob3, '') AS blob3, IFNULL(blob4, '') AS blob4, IFNULL(blob5, '') AS blob5 FROM bl_01 WHERE uploaded = 0", null);
        return cursor;
    }

    public List<String> get_gudangmd(int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT TEXT1, TEXT2 FROM md_01 WHERE datatype = 'STORE'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_vehiclemasterdata() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text1 || ' - ' || text2 AS vehicle FROM md_01 WHERE DATATYPE = 'VEHICLE' AND length(text30) > 0";
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

    public List<String> get_vehiclecodelist() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "";
        if (get_tbl_username(3).equals("SPV-TRS")) {
            query = "SELECT DISTINCT text1 FROM md_02 WHERE DATATYPE = 'GANG'";
        } else {
            query = "SELECT DISTINCT text1 FROM md_01 WHERE DATATYPE = 'VEHICLE' AND length(text30) > 0";
        }
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

    //Get Name based on its code

    public String get_single_vehicletypegroup(String vehiclename) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT parametercode FROM gs_01 " +
                "WHERE groupparamcode = 'GS06' AND parameterdesc = '"+vehiclename+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_single_activitysap(String activityCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text1 FROM md_01 WHERE datatype = 'ACTIVITYSAP' " +
                "AND subdatatype = '"+activityCode+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_singlelokasi(String kodelokasi) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text2 FROM md_01 WHERE datatype = 'FIELDCROP' AND text1 = '"+kodelokasi+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_singlelokasiCode(String namalokasi) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text1 FROM md_01 WHERE datatype = 'FIELDCROP' AND text2 = '"+namalokasi+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_singlekegiatanname(String kegiatancode, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT md.text6, md.text7, gs.parameterdesc FROM md_01 md LEFT JOIN gs_01 gs " +
                "ON md.text3 = gs.parametercode WHERE md.datatype = 'TRANSPORTRATE' " +
                "AND gs.groupparamdesc = 'LOADTYPE' AND md.subdatatype = '"+kegiatancode+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index).toString();
        } else {
            return null;
        }
    }

    public String get_singlekegiatancode(String kegiatanname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT subdatatype FROM md_01 WHERE datatype = 'TRANSPORTRATE' AND text6 = '"+kegiatanname+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_singletransportratecode(String kegiatanname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT subdatatype FROM md_01 WHERE datatype = 'TRANSPORTRATE' AND text6 = '"+kegiatanname+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_singletransportratename(String kegiatancode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text6 FROM md_01 WHERE datatype = 'TRANSPORTRATE' AND subdatatype = '"+kegiatancode+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_singlekegiatanCarLog(String kegiatancode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT parametercode FROM gs_01 WHERE groupparamcode = 'GS16' AND parameterdesc = '"+kegiatancode+"'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }
    //Finish get Name based on its code



    public String get_statusrkh(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*), documentno, date(date2) AS tglpelaksanaan, text3 AS vehicletype," +
                "text4 AS location, text5 AS kegiatan FROM tr_01 WHERE datatype = 'RKHVH' AND uploaded IS NULL", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            if (cursor.isNull(index)) {
                return null;
            } else {
                return cursor.getString(index);
            }
        } else {
            return null;
        }
    }

    public String get_statusapelpagi(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tr_01 WHERE datatype = 'ABSAPL' " +
                "AND date(date1) = date('now', 'localtime')", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index);
        } else {
            return null;
        }
    }

    public String check_existingapel(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*), documentno, text6 AS location, IFNULL(text7, '') AS catatan, " +
                "IFNULL(uploaded, '') AS uploaded, text10 AS shiftcode FROM tr_01 WHERE datatype = 'ABSAPL' " +
                "AND date(date1) = date('now', 'localtime')", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index);
        } else {
            return null;
        }
    }


    public String get_statusverifikasigis(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT COUNT(*), documentno, DATE(date3) AS tglunit, " +
                "text1 AS kebun, text2 AS divisi, text3 AS lokasi, text4 AS kegiatan, text5 AS satuankerja, " +
                "text6 AS hasilkerja, text7 AS catatansop FROM tr_01 WHERE datatype = 'GISVH' AND uploaded IS NULL;", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(index);
        } else {
            return null;
        }
    }

    public String get_unitP2H_fragmentinfo(String documentno) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text2 FROM tr_01 WHERE datatype = 'P2HVH' AND documentno = '"+documentno+"';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String get_unitcarlog_fragmentinfo(String documentno) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text1 FROM tr_01 WHERE datatype = 'CARLOG' AND documentno = '"+documentno+"';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public List<String> get_loadtype(int index, String vehiclegroup) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text3, text4 FROM md_01 WHERE datatype = 'TRANSPORTRATE' " +
                "AND text1 = '"+vehiclegroup+"' ORDER BY subdatatype ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_vehiclegroup_filterkemandoran() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT gs1.parameterdesc FROM gs_01 gs1 INNER JOIN md_01 md1 ON md1.text30 = gs1.parametercode " +
                "INNER JOIN md_02 md2 ON md1.text1 = md2.text1 WHERE gs1.groupparamcode = 'GS06' " +
                "AND md1.datatype = 'VEHICLE' AND md2.subdatatype = '"+get_tbl_username(18)+"';";
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

    public List<String> get_all_transport() {
        ArrayList<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text6 FROM md_01 WHERE datatype = 'TRANSPORTRATE'";
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

    public List<String> get_transportactivity(String vehiclegroup) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text6 FROM md_01 WHERE datatype = 'TRANSPORTRATE' AND text1 = '"+vehiclegroup+"'";
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

    public List<String> get_tujuankegiatancarlog(int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT parametercode, parameterdesc FROM gs_01 WHERE groupparamcode = 'GS16' ORDER BY CAST(seq_no AS UNSIGNED)";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_itemkebun(int index) {
        ArrayList<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text1, text2 FROM md_01 WHERE datatype = 'ORG_STRUCTURE' AND text7 NOT IN ('MGR') ORDER BY text2 ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_itemdivisi(String estate, int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text3, text4 FROM md_01 WHERE datatype = 'ORG_STRUCTURE' AND text2 = '"+estate+"' ORDER BY text4 ASC;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_activitysap_filtered(String vehiclegroup) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT md.text1 FROM md_01 md LEFT JOIN gs_01 gs ON md.text2 = gs.parametercode " +
                "AND gs.groupparamcode = 'GS05' WHERE datatype = 'ACTIVITYSAP' " +
                "AND text4 = '"+vehiclegroup+"';";
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

    public List<String> get_fieldcrop(int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT text1, text2 FROM md_01 WHERE datatype = 'FIELDCROP'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_fieldcrop_filtered(String kebun, String divisi, int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT text1, text2 FROM md_01 WHERE datatype = 'FIELDCROP' " +
                "AND TEXT4 = '"+kebun+"' AND TEXT5 = '"+divisi+"' ORDER BY CAST(TEXT3 AS UNSIGNED);";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_loadcategory(String vehiclegroup, String loadtype, int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text5, text6 FROM md_01 WHERE datatype = 'TRANSPORTRATE' AND text1 = '"+vehiclegroup+"' AND text3 = '"+loadtype+"'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public List<String> get_employee() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text1 || ' - ' || text2 AS employee FROM md_01 WHERE DATATYPE = 'EMPLOYEE'";
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

    public List<String> get_operatoronly() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT text1 || ' - ' || text2 AS employee FROM md_01 " +
                "WHERE DATATYPE = 'EMPLOYEE' AND text20 = 'DRIVER / OPERATOR'";
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

    public List<String> get_teamname(int index) {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT subdatatype, text1 FROM md_01 WHERE datatype = 'GANG'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                dataList.add(cursor.getString(index));
            }

            while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

    public boolean insert_dataGS01(String groupparamcode, String groupparamdesc, String parametercode, String parameterdesc, String seq_no, String inactive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupparamcode", groupparamcode);
        contentValues.put("groupparamdesc", groupparamdesc);
        contentValues.put("parametercode", parametercode);
        contentValues.put("parameterdesc", parameterdesc);
        contentValues.put("seq_no", seq_no);
        contentValues.put("inactive", inactive);

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

    public boolean insert_dataGS08(String rolecode, String roledesc, String modulecode,
                                   String submodulecode, String authorized, String authorized_report) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rolecode", rolecode);
        contentValues.put("roledesc", roledesc);
        contentValues.put("modulecode", modulecode);
        contentValues.put("submodulecode", submodulecode);
        contentValues.put("authorized", authorized);
        contentValues.put("authorized_report", authorized_report);

        long insert = db.insert("gs_08", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_tablemd(String datatype, String subdatatype, String compid, String siteid, String date1, String date2, String date3, String date4, String date5,
                                  String text1, String text2, String text3, String text4, String text5, String text6, String text7, String text8, String text9, String text10,
                                  String text11, String text12, String text13, String text14, String text15, String text16, String text17, String text18, String text19,
                                  String text20, String text21, String text22, String text23, String text24, String text25, String text26, String text27, String text28,
                                  String text29, String text30) {
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

    public boolean insert_tablemd2(String datatype, String subdatatype, String itemdata, String subitemdata, String compid, String siteid, String date1,
                                   String date2, String date3, String date4, String date5, String text1, String text2, String text3, String text4, String text5,
                                   String text6, String text7, String text8, String text9, String text10, String text11, String text12, String text13, String text14,
                                   String text15, String text16, String text17, String text18, String text19, String text20, String text21, String text22,
                                   String text23, String text24, String text25, String text26, String text27, String text28, String text29, String text30) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("datatype", datatype);
        contentValues.put("subdatatype", subdatatype);
        contentValues.put("itemdata", itemdata);
        contentValues.put("subitemdata", subitemdata);
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

        long insert = db.insert("md_02", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insert_transportmd(String datatype, String subdatatype, String compid, String siteid, String text1, String text2, String text3, String text4,
                                      String text5, String text6, String text7, String text8, String text9, String text10, String text11, String text12,
                                      String text13, String text14, String text15, String text16, String text17, String text18, String text19) {
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
        contentValues.put("text18", text18);
        contentValues.put("text19", text19);

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

    public String settingcarlog_satuanhasilkerja(String loadActivity) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT text7 FROM md_01 WHERE text5 = '"+loadActivity+"' AND datatype = 'TRANSPORTRATE'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String layoutsetting_carlog(int index, String activityLoad) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text8, text9, text10, text11, text12, text13, text14, text15, text16, text17 FROM md_01 WHERE text5 = '"+activityLoad+"'", null);
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

    public Boolean updatestatus_verifikasigis(String nodoc, String hasilkerja, byte[] fotogis, String catatanSOP) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValuesTR01 = new ContentValues();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        contentValuesTR01.put("date2", savedate);
        contentValuesTR01.put("text6", hasilkerja);
        contentValuesTR01.put("text7", catatanSOP);
        contentValuesTR01.put("uploaded", 0);

        ContentValues contentValuesTR02 = new ContentValues();
        contentValuesTR02.put("uploaded", 0);

        ContentValues contentValuesFoto = new ContentValues();
        contentValuesFoto.put("documentno", nodoc);
        contentValuesFoto.put("datatype", "GISVH");
        contentValuesFoto.put("subdatatype", get_tbl_username(0));
        contentValuesFoto.put("itemdata", "HEADER");
        contentValuesFoto.put("subitemdata", "HEADER");
        contentValuesFoto.put("comp_id", get_tbl_username(14));
        contentValuesFoto.put("site_id", get_tbl_username(15));
        contentValuesFoto.put("blob1", fotogis);

        long update = db.update("tr_01", contentValuesTR01, "documentno = '"+nodoc+"'", null);
        long updateTR02 = db.update("tr_02", contentValuesTR02, "documentno = '"+nodoc+"'", null);
        db.insert("bl_01", null, contentValuesFoto);
        if (update == -1 && updateTR02 == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean update_headerLokasiRKH(String nodoc, String vehiclegroup, String lokasicode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("text4", lokasicode);


        long update = db.update("tr_01", contentValues, "documentno = '"+nodoc+"' AND " +
                "datatype = 'RKHVH' AND text3 = '"+vehiclegroup+"' AND uploaded IS NULL", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean update_headerKegiatanRKH(String nodoc, String vehiclegroup, String activitycode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("text5", activitycode);

        long update = db.update("tr_01", contentValues, "documentno = '"+nodoc+"' AND " +
                "datatype = 'RKHVH' AND text3 = '"+vehiclegroup+"' AND uploaded IS NULL", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean update_checkedRKH(String nodoc, String unitcode, String shiftcode, String empcode,
                                     String lokasiCode, String kegiatanCode, String checkvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValuesTR02 = new ContentValues();
        contentValuesTR02.put("text4", lokasiCode);
        contentValuesTR02.put("text5", kegiatanCode);
        contentValuesTR02.put("text7", checkvalue);

        long update = db.update("tr_02", contentValuesTR02, "documentno = '"+nodoc+"' AND " +
                "text1 = '"+unitcode+"' AND text2 = '"+shiftcode+"' AND text3 = '"+empcode+"'", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateCheckedMekanik(String mekanikCode, String checkvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValuesTR02 = new ContentValues();
        contentValuesTR02.put("text3", checkvalue);

        long update = db.update("tr_02", contentValuesTR02, "datatype = 'PSWS' " +
                "AND itemdata = 'DETAIL1' AND text2 = '"+mekanikCode+"' AND uploaded IS NULL", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getCheckRKH(String nodoc, String unitcode, String shiftcode, String empcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT IFNULL(text7, '') AS rkhcheck FROM tr_02 WHERE documentno ='"+nodoc+"' " +
                "AND text1 = '"+unitcode+"' AND text2 = '"+shiftcode+"' AND text3 = '"+empcode+"';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
    }

    public String getCheckMekanik(String empcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT IFNULL(text3, '') AS mekanikcek FROM tr_02 " +
                "WHERE datatype = 'PSWS' AND text2 ='"+empcode+"' AND uploaded IS NULL", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            return cursor.getString(0).toString();
        } else {
            return null;
        }
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




    public Boolean updateselesai_rkh(String nodoc, String note, String vehiclegroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        db.execSQL("DELETE FROM tr_02 WHERE datatype = 'RKHVH' AND documentno = '"+nodoc+"' AND (text7 = '' OR text7 IS NULL) AND uploaded IS NULL");
        ContentValues contentValuesTR01 = new ContentValues();
        contentValuesTR01.put("date1", savedate);
        contentValuesTR01.put("text6", note);

        contentValuesTR01.put("uploaded", 0);

        ContentValues contentValuesTR02 = new ContentValues();
        contentValuesTR02.putNull("text7");
        contentValuesTR02.put("uploaded", 0);

        long updateTR01 = db.update("tr_01", contentValuesTR01, "documentno = '"+nodoc+"' " +
                "AND text3 = '"+vehiclegroup+"' AND uploaded IS NULL", null);
        long updateTR02 = db.update("tr_02", contentValuesTR02, "documentno = '"+nodoc+"' " +
                "AND uploaded IS NULL", null);
        if (updateTR01 == -1 && updateTR02 == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateselesai_apelpagi(String nodoc, String lokasiAbsen, String latitude,
                                          String longitude, byte[] fotorame) {
        SQLiteDatabase db = this.getWritableDatabase();
        String savedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        ContentValues contentValuesTR01 = new ContentValues();
        contentValuesTR01.put("documentno", nodoc);
        contentValuesTR01.put("datatype", "ABSAPL");
        contentValuesTR01.put("subdatatype", get_tbl_username(0));
        contentValuesTR01.put("comp_id", get_tbl_username(14));
        contentValuesTR01.put("site_id", get_tbl_username(15));
        contentValuesTR01.put("date1", savedate);
        contentValuesTR01.put("text1", get_tbl_username(18));
        contentValuesTR01.put("text2", get_infokemandoranapel(1, get_tbl_username(18)));
        contentValuesTR01.put("text3", get_infokemandoranapel(3, get_tbl_username(18)));
        contentValuesTR01.put("text4", get_infokemandoranapel(6, get_tbl_username(18)));
        contentValuesTR01.put("text5", get_infokemandoranapel(4, get_tbl_username(18)));
        contentValuesTR01.put("text6", lokasiAbsen);
        contentValuesTR01.put("text8", latitude);
        contentValuesTR01.put("text9", longitude);
        contentValuesTR01.put("uploaded", 0);

        ContentValues contentValuesTR02 = new ContentValues();
        contentValuesTR02.put("documentno", nodoc);
        contentValuesTR02.put("uploaded", 0);

        ContentValues contentValuesFoto = new ContentValues();
        contentValuesFoto.put("documentno", nodoc);
        contentValuesFoto.put("datatype", "ABSAPL");
        contentValuesFoto.put("subdatatype", get_tbl_username(0));
        contentValuesFoto.put("itemdata", "HEADER");
        contentValuesFoto.put("subitemdata", "HEADER");
        contentValuesFoto.put("comp_id", get_tbl_username(14));
        contentValuesFoto.put("site_id", get_tbl_username(15));
        contentValuesFoto.put("blob1", fotorame);
        contentValuesFoto.put("uploaded", 0);

        long insertTR01 = db.insert("tr_01", null, contentValuesTR01);
        long updateTR02 = db.update("tr_02", contentValuesTR02, "datatype = 'ABSAPL' AND documentno IS NULL AND uploaded IS NULL", null);
        long insertBL = db.insert("bl_01", null, contentValuesFoto);

        db.execSQL("DELETE FROM tr_02 WHERE documentno = '"+nodoc+"' AND date1 IS NULL");
        if (insertTR01 == -1 && updateTR02 == -1 && insertBL == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean change_statusuploadtr01(int trid, String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uploaded", 1);

        long update = db.update("tr_01", contentValues, "trid = "+ trid +" AND documentno = '"+nodoc+"'", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean change_statusuploadtr02(int trid, String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uploaded", 1);

        long update = db.update("tr_02", contentValues, "trid = "+trid+" AND documentno = '"+nodoc+"'", null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean change_statusuploadimg(int blid, String nodoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uploaded", 1);

        long update = db.update("bl_01", contentValues, "blid = "+blid+" AND documentno = '"+nodoc+"'", null);
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