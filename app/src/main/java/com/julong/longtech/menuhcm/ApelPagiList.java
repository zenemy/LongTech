package com.julong.longtech.menuhcm;

public class ApelPagiList {

    private String empcode;
    private String empname;
    private String jabatan;
    private String ancak;
    private String time;
    private String tipeabsen;

    public ApelPagiList(String empcode, String empname, String jabatan, String ancak, String time, String tipeabsen, String gantiancak) {

        this.empcode = empcode;
        this.empname = empname;
        this.jabatan = jabatan;
        this.ancak = ancak;
        this.tipeabsen = tipeabsen;
        this.time = time;
    }

    public String getEmpcode() {
        return empcode;
    }
    public String getEmpname() {
        return empname;
    }
    public String getJabatan() {
        return jabatan;
    }
    public String getAncak() {
        return ancak;
    }
    public String gettime() {
        return time;
    }
    public String getTipeabsen() {
        return tipeabsen;
    }

}
