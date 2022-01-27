package com.julong.longtech.menureport;

public class ListReportGIS {

    private String driverName;
    private String blokCode;
    private String unitCarLog;
    private String activity;
    private String hasilPekerjaan;
    private String satuanPekerjaan;
    private String timeInput;
    private int isUploaded;

    public ListReportGIS(String driverName, String blokCode, String unitCarLog, String activity,
                         String hasilPekerjaan, String satuanPekerjaan, String timeInput, int isUploaded) {

        this.driverName = driverName;
        this.blokCode = blokCode;
        this.unitCarLog = unitCarLog;
        this.timeInput = timeInput;
        this.activity = activity;
        this.hasilPekerjaan = hasilPekerjaan;
        this.satuanPekerjaan = satuanPekerjaan;
        this.isUploaded = isUploaded;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getBlokCode() {
        return blokCode;
    }

    public String getUnitCarLog() {
        return unitCarLog;
    }

    public String getActivity() {
        return activity;
    }

    public String getTimeInput() {
        return timeInput;
    }

    public String getHasilPekerjaan() {
        return hasilPekerjaan;
    }

    public String getSatuanPekerjaan() {
        return satuanPekerjaan;
    }

    public int getIsUploaded() {
        return isUploaded;
    }
}
