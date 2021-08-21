package com.julong.longtech.menuvehicle;

public class ListParamRKH {

    private String vehicleCode;
    private String shiftKerja;
    private String driverName;
    private boolean checked;

    public ListParamRKH(String vehicleCode, String shiftKerja, String driverName) {
        this.vehicleCode = vehicleCode;
        this.shiftKerja = shiftKerja;
        this.driverName = driverName;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
