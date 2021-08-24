package com.julong.longtech.menuworkshop;

public class ListMekanikPerintahService {

    private String employeeName;
    private String employeeCode;


    public ListMekanikPerintahService(String employeeName, String employeeCode) {
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public String getEmployeeCode() {
        return employeeCode;
    }

}
