package com.julong.longtech.ui.home;

public class ListHistoryHomeApel {

    private String documentNumber;
    private String tglApel;
    private String waktuApel;
    private String employeeName;
    private String employeePosition;
    private String kehadiranEmp;
    private String metodeAbsen;
    private byte[] fotoApel;
    private int isUploaded;

    public ListHistoryHomeApel(String documentNumber, String tglApel, String waktuApel, String employeeName,
                           String employeePosition, String kehadiranEmp, String metodeAbsen, byte[] fotoApel,
                            int isUploaded) {

        this.documentNumber = documentNumber;
        this.tglApel = tglApel;
        this.waktuApel = waktuApel;
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
        this.kehadiranEmp = kehadiranEmp;
        this.metodeAbsen = metodeAbsen;
        this.fotoApel = fotoApel;
        this.isUploaded = isUploaded;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getTglApel() {
        return tglApel;
    }

    public String getWaktuApel() {
        return waktuApel;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmpPosition() {
        return employeePosition;
    }

    public String getKehadiranEmp() {
        return kehadiranEmp;
    }

    public String getMetodeAbsen() {
        return metodeAbsen;
    }

    public byte[] getFotoApel() {
        return fotoApel;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

}
