package com.julong.longtech.ui.home;

public class ListHistoryHomeP2H {

    private String vehicleName;
    private String itemsP2H;
    private String notes;

    public ListHistoryHomeP2H(String vehicleName, String itemsP2H, String notes) {
        this.vehicleName = vehicleName;
        this.itemsP2H = itemsP2H;
        this.notes = notes;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getItemsP2H() {
        return itemsP2H;
    }

    public String getNotes() {
        return notes;
    }
}
