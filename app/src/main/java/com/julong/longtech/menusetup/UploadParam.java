package com.julong.longtech.menusetup;


public class UploadParam {

    private String code;
    private String nama;
    private Integer jumlah_data;

    public UploadParam(String code, String nama, Integer jumlah_data) {
        this.code = code;
        this.nama = nama;
        this.jumlah_data = jumlah_data;
    }

    public String getCode() {
        return code;
    }
    public String getNama() { return nama; }
    public Integer getJumlah_data() { return jumlah_data; }

}