package com.julong.longtech.menuhistory;

public class ListHistoryRKH {

    private String itemdata;
    private String unitCode;
    private String division;
    private String blokCode;
    private String activityName;
    private String targetKerja;
    private String satuanKerja;
    private String inputTime;
    private int isUploaded;

    public ListHistoryRKH(String itemdata, String unitCode, String division, String blokCode, String activityName, String targetKerja,
                          String satuanKerja, String inputTime, int isUploaded) {

        this.itemdata = itemdata;
        this.unitCode = unitCode;
        this.division = division;
        this.blokCode = blokCode;
        this.activityName = activityName;
        this.targetKerja = targetKerja;
        this.satuanKerja = satuanKerja;
        this.inputTime = inputTime;
        this.isUploaded = isUploaded;
    }

    public String getItemdata() {
        return itemdata;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getDivision() {
        return division;
    }

    public String getBlokCode() {
        return blokCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getTargetKerja() {
        return targetKerja;
    }

    public String getSatuanKerja() {
        return satuanKerja;
    }

    public String getInputTime() {return inputTime;}

    public int getIsUploaded() {
        return isUploaded;
    }
}
