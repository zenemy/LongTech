package com.julong.longtech.menureport;

public class ListReportMintaBBM {

    private String employeeName;
    private String waktuMintaBBM;
    private String vehicleCode;
    private Integer jumlahLiter;

    public ListReportMintaBBM(String employeeName, String waktuMintaBBM, String vehicleCode, Integer jumlahLiter) {
        this.employeeName = employeeName;
        this.waktuMintaBBM = waktuMintaBBM;
        this.vehicleCode = vehicleCode;
        this.jumlahLiter = jumlahLiter;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getWaktuMintaBBM() {
        return waktuMintaBBM;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public Integer getJumlahLiter() {
        return jumlahLiter;
    }
}