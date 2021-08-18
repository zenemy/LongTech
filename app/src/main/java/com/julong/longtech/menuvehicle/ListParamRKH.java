package com.julong.longtech.menuvehicle;

public class ListParamRKH {

    private String vehicleCode;
    private String shiftKerja;
    private String driverName;
    private String helper1Name;
    private String helper2Name;
    private int kebutuhanBBM;

    public ListParamRKH(String vehicleCode, String shiftKerja, String driverName, String helper1Name, String helper2Name, int kebutuhanBBM) {
        this.vehicleCode = vehicleCode;
        this.shiftKerja = shiftKerja;
        this.driverName = driverName;
        this.helper1Name = helper1Name;
        this.helper2Name = helper2Name;
        this.kebutuhanBBM = kebutuhanBBM;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getShiftkerja() {
        return shiftKerja;
    }
    public String getDrivername() {
        return driverName;
    }
    public String getHelper1Name() {
        return helper1Name;
    }
    public String getHelper2Name() {
        return helper2Name;
    }
    public int getKebutuhanBBM() {
        return kebutuhanBBM;
    }


}
