package com.julong.longtech.menureport;

public class ListReportCarLog {

    private String documentNumber;
    private String tglCarLog;
    private String waktuCarLog;
    private String unitCarLog;
    private String firstName;
    private String lastName;
    private String employeeName;
    private String employeeCode;
    private String blokCode;
    private String activityLog;
    private String hasilPekerjaan;
    private String satuanPekerjaan;
    private String kmAwal;
    private String kmAkhir;

    public ListReportCarLog(String documentNumber, String tglCarLog, String waktuCarLog, String unitCarLog, String firstName,
                            String lastName, String employeeCode, String employeeName, String blokCode, String activityLog,
                            String hasilPekerjaan, String satuanPekerjaan, String kmAwal, String kmAkhir) {

        this.documentNumber = documentNumber;
        this.tglCarLog = tglCarLog;
        this.waktuCarLog = waktuCarLog;
        this.unitCarLog = unitCarLog;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.blokCode = blokCode;
        this.activityLog = activityLog;
        this.hasilPekerjaan = hasilPekerjaan;
        this.satuanPekerjaan = satuanPekerjaan;
        this.kmAwal = kmAwal;
        this.kmAkhir = kmAkhir;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getTglCarLog() {
        return tglCarLog;
    }

    public String getWaktuCarLog() {
        return waktuCarLog;
    }

    public String getUnitCarLog() {
        return unitCarLog;
    }

    public String getBlokCode() {
        return blokCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getActivityLog() {
        return activityLog;
    }

    public String getHasilPekerjaan() {
        return hasilPekerjaan;
    }

    public String getSatuanPekerjaan() {
        return satuanPekerjaan;
    }

    public String getKmAwal() {
        return kmAwal;
    }

    public String getKmAkhir() {
        return kmAkhir;
    }
}
