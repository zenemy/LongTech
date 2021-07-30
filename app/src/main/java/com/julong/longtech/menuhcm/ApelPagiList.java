package com.julong.longtech.menuhcm;

public class ApelPagiList {

    private String employeeName;
    private String employeeCode;
    private String positionCode;
    private String positionName;
    private String unitCode;
    private String shiftcode;
    private String metodeAbsen;
    private String waktuAbsem;
    private String itemData;
    private byte[] imgApel;

    public ApelPagiList(String employeeName, String employeeCode, String positionCode, String positionName, String unitCode,
                        String shiftcode, String metodeAbsen, String waktuAbsem, String itemData, byte[] imgApel) {
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.positionCode = positionCode;
        this.positionName = positionName;
        this.unitCode = unitCode;
        this.shiftcode = shiftcode;
        this.metodeAbsen = metodeAbsen;
        this.waktuAbsem = waktuAbsem;
        this.itemData = itemData;
        this.imgApel = imgApel;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public String getEmployeeCode() {
        return employeeCode;
    }
    public String getPositionCode() {
        return positionCode;
    }
    public String getPositionName() {
        return positionName;
    }
    public String getUnitCode() {
        return unitCode;
    }
    public String getShiftcode() {
        return shiftcode;
    }
    public String getMetodeAbsen() {
        return metodeAbsen;
    }
    public String getWaktuAbsem() {
        return waktuAbsem;
    }
    public String getItemData() {
        return itemData;
    }
    public byte[] getImgApel() {
        return imgApel;
    }

}
