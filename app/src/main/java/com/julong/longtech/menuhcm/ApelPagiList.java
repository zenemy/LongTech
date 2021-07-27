package com.julong.longtech.menuhcm;

public class ApelPagiList {

    private String employeeName;
    private String positionName;
    private String metodeAbsen;
    private String waktuAbsem;

    public ApelPagiList(String employeeName, String positionName, String metodeAbsen, String waktuAbsem) {
        this.employeeName = employeeName;
        this.positionName = positionName;
        this.metodeAbsen = metodeAbsen;
        this.waktuAbsem = waktuAbsem;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public String getPositionName() {
        return positionName;
    }
    public String getMetodeAbsen() {
        return metodeAbsen;
    }
    public String getWaktuAbsem() {
        return waktuAbsem;
    }

}
