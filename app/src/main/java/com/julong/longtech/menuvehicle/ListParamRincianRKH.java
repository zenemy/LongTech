package com.julong.longtech.menuvehicle;

public class ListParamRincianRKH {

    private String vehicleName;
    private String shiftKerja;
    private String driverName;
    private String helper1Name;
    private String helper2Name;
    private String kebutuhanBBM;

    public ListParamRincianRKH(String vehicleName, String shiftKerja, String driverName, String helper1Name, String helper2Name, String kebutuhanBBM) {
        this.vehicleName = vehicleName;
        this.shiftKerja = shiftKerja;
        this.driverName = driverName;
        this.helper1Name = helper1Name;
        this.helper2Name = helper2Name;
        this.kebutuhanBBM = kebutuhanBBM;
    }

    public String getVehiclename() {
        return vehicleName;
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
    public String getKebutuhanBBM() {
        return kebutuhanBBM;
    }


}
