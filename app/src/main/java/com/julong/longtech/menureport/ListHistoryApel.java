package com.julong.longtech.menureport;

public class ListHistoryApel {

    private String documentNumber;
    private String tglApel;
    private String waktuApel;
    private String employeeName;
    private String employeePosition;
    private String kehadiranEmp;
    private String metodeAbsen;
    private byte[] fotoApel;

    public ListHistoryApel(String documentNumber, String tglApel, String waktuApel, String employeeName,
                           String employeePosition, String kehadiranEmp, String metodeAbsen, byte[] fotoApel) {

        this.documentNumber = documentNumber;
        this.tglApel = tglApel;
        this.waktuApel = waktuApel;
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
        this.kehadiranEmp = kehadiranEmp;
        this.metodeAbsen = metodeAbsen;
        this.fotoApel = fotoApel;
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

}
