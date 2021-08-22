package com.julong.longtech.menuhistory;

public class ListHistoryRKH {

    private String documentNumber;
    private String tglPelaksanaan;
    private String employeeName;
    private String activityName;
    private String locationName;
    private String unitCode;
    private String shiftcode;
    private int isUploaded;

    public ListHistoryRKH(String documentNumber, String tglPelaksanaan, String employeeName, String activityName,
                          String locationName, String unitCode, String shiftcode, int isUploaded) {

        this.documentNumber = documentNumber;
        this.tglPelaksanaan = tglPelaksanaan;
        this.employeeName = employeeName;
        this.activityName = activityName;
        this.locationName = locationName;
        this.unitCode = unitCode;
        this.shiftcode = shiftcode;
        this.isUploaded = isUploaded;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getTglPelaksanaan() {
        return tglPelaksanaan;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getShiftcode() {
        return shiftcode;
    }

    public int getIsUploaded() {
        return isUploaded;
    }
}
