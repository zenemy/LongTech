package com.julong.longtech.menuvehicle;

public class ListResultCarLog {

    private String activityType;
    private String division;
    private String blokLocation;
    private String hasilKerja;
    private String satuanKerja;
    private String vehicleCode;
    private String inputDate;

    public ListResultCarLog(String activityType, String division, String blokLocation, String hasilKerja,
                            String satuanKerja, String vehicleCode, String inputDate) {

        this.activityType = activityType;
        this.division = division;
        this.blokLocation = blokLocation;
        this.hasilKerja = hasilKerja;
        this.satuanKerja = satuanKerja;
        this.vehicleCode = vehicleCode;
        this.inputDate = inputDate;

    }

    public String getActivityType() {
        return activityType;
    }

    public String getDivision() {
        return division;
    }

    public String getBlokLocation() {
        return blokLocation;
    }

    public String getHasilKerja() {
        return hasilKerja;
    }

    public String getSatuanKerja() {
        return satuanKerja;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getInputDate() {
        return inputDate;
    }
}
