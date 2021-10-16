package com.julong.longtech.menuvehicle;

public class ListDetailNewRKH {

    private String division;
    private String lokasiKerja;
    private String kegiatanKerja;
    private String targetKerja;
    private String satuanKerja;

    public ListDetailNewRKH(String division, String lokasiKerja, String kegiatanKerja, String targetKerja, String satuanKerja) {
        this.division = division;
        this.lokasiKerja = lokasiKerja;
        this.kegiatanKerja = kegiatanKerja;
        this.targetKerja = targetKerja;
        this.satuanKerja = satuanKerja;
    }

    public String getDivision() {
        return division;
    }

    public String getLokasiKerja() {
            return lokasiKerja;
        }

    public String getKegiatanKerja() {
        return kegiatanKerja;
    }

    public String getTargetKerja() {
        return targetKerja;
    }

    public String getSatuanKerja() {  return satuanKerja; }


}
