package com.julong.longtech.ui.home;

public class ListHistoryHomeCarLog {

    private String documentNumber;
    private String waktuAwal;
    private String unitCarLog;
    private String kmAwal;
    private String kmAkhir;
    private String kategoriMuatan;
    private String jenisMuatan;
    private String hasilPekerjaan;
    private String satuanPekerjaan;
    private int isUploaded;

    public ListHistoryHomeCarLog(String documentNumber, String waktuAwal, String unitCarLog, String kmAwal,
                                 String kmAkhir, String kategoriMuatan, String jenisMuatan,
                                 String hasilPekerjaan, String satuanPekerjaan, int isUploaded) {

        this.documentNumber = documentNumber;
        this.waktuAwal = waktuAwal;
        this.unitCarLog = unitCarLog;
        this.kmAwal = kmAwal;
        this.kmAkhir = kmAkhir;
        this.jenisMuatan = jenisMuatan;
        this.kategoriMuatan = kategoriMuatan;
        this.hasilPekerjaan = hasilPekerjaan;
        this.satuanPekerjaan = satuanPekerjaan;
        this.isUploaded = isUploaded;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getWaktuAwal() {
        return waktuAwal;
    }

    public String getUnitCarLog() {
        return unitCarLog;
    }

    public String getKmAwal() {
        return kmAwal;
    }

    public String getKmAkhir() {
        return kmAkhir;
    }

    public String getJenisMuatan() {
        return jenisMuatan;
    }

    public String getKategoriMuatan() {
        return kategoriMuatan;
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
