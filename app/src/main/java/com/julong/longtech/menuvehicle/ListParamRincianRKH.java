package com.julong.longtech.menuvehicle;

public class ListParamRincianRKH {

    private String satuanMuatan;
    private String lokasi;
    private String kegiatan;
    private String waktu;
    private String kilometer;
    private String targetKerja;

    public ListParamRincianRKH(String satuanMuatan, String lokasi, String kegiatan,
                               String waktu, String kilometer, String targetKerja) {

        this.satuanMuatan = satuanMuatan;
        this.lokasi = lokasi;
        this.kegiatan = kegiatan;
        this.waktu = waktu;
        this.kilometer = kilometer;
        this.targetKerja = targetKerja;
    }

    public String getSatuanMuatan() {
        return satuanMuatan;
    }
    public String getLokasi() {
        return lokasi;
    }
    public String getKegiatan() {
        return kegiatan;
    }
    public String getWaktu() {
        return waktu;
    }
    public String getKilometer() {
        return kilometer;
    }
    public String getTargetKerja() {
        return targetKerja;
    }


}
