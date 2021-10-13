package com.julong.longtech.menuhcm;

public class ApelPagiList {

    private String teamCode;
    private String employeeCode;
    private String employeeName;
    private String positionName;
    private String positionCode;
    private String vehicleCode;
    private String absenValue;

    public ApelPagiList(String teamCode, String employeeCode, String employeeName,
                        String positionCode, String positionName, String vehicleCode) {
        this.teamCode = teamCode;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.positionName = positionName;
        this.positionCode = positionCode;
        this.vehicleCode = vehicleCode;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setAbsenValue(String absenValue) {
        this.absenValue = absenValue;
    }

    public String getAbsenValue() {
        return absenValue;
    }

}
