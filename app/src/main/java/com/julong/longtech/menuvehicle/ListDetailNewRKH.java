package com.julong.longtech.menuvehicle;

public class ListDetailNewRKH {

        private String lokasiKerja;
        private String kegiatanKerja;
        private String targetKerja;
        private String satuanKerja;

        public ListDetailNewRKH(String lokasiKerja, String kegiatanKerja, String targetKerja, String satuanKerja) {
            this.lokasiKerja = lokasiKerja;
            this.kegiatanKerja = kegiatanKerja;
            this.targetKerja = targetKerja;
            this.satuanKerja = satuanKerja;
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
