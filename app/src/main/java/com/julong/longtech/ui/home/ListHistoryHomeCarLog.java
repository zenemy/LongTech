package com.julong.longtech.ui.home;

public class ListHistoryHomeCarLog {

    private String timeInput;
    private String vehicleCode;
    private String division;
    private String blokLocation;
    private String kmAwal;
    private String kmAkhir;
    private String jenisAktifitas;
    private String hasilPekerjaan;
    private String satuanPekerjaan;
    private int isUploaded;

    public ListHistoryHomeCarLog(String timeInput, String vehicleCode, String division, String blokCode, String kmAwal,
                                 String kmAkhir, String jenisAktifitas, String hasilPekerjaan, String satuanPekerjaan, int isUploaded) {


        this.timeInput = timeInput;
        this.vehicleCode = vehicleCode;
        this.division = division;
        this.blokLocation = blokCode;
        this.kmAwal = kmAwal;
        this.kmAkhir = kmAkhir;
        this.jenisAktifitas = jenisAktifitas;
        this.hasilPekerjaan = hasilPekerjaan;
        this.satuanPekerjaan = satuanPekerjaan;
        this.isUploaded = isUploaded;
    }


    public String getTimeInput() {
        return timeInput;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getDivision() {
        return division;
    }

    public String getBlokLocation() {
        return blokLocation;
    }

    public String getKmAwal() {
        return kmAwal;
    }

    public String getKmAkhir() {
        return kmAkhir;
    }

    public String getActivity() {
        return jenisAktifitas;
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
