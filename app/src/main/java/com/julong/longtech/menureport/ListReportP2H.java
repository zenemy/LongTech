package com.julong.longtech.menureport;

public class ListReportP2H {

    private String documentNumber;
    private String tglP2H;
    private String vehicleCode;
    private String employeeName;
    private String itemP2H;
    private String noteP2H;

    public ListReportP2H(String documentNumber, String tglP2H, String vehicleCode, String employeeName,
                            String itemP2H, String noteP2H) {

        this.documentNumber = documentNumber;
        this.tglP2H = tglP2H;
        this.vehicleCode = vehicleCode;
        this.employeeName = employeeName;
        this.itemP2H = itemP2H;
        this.noteP2H = noteP2H;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getTglP2H() {
        return tglP2H;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getItemP2H() {
        return itemP2H;
    }

    public String getNoteP2H() {
        return noteP2H;
    }
}
