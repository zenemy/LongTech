package com.julong.longtech.menuvehicle;

public class ListParamGIS {

    private String latKoordinat;
    private String longKoordinat;
    private String tagTime;

    public ListParamGIS(String latKoordinat, String longKoordinat, String tagTime) {
        this.latKoordinat = latKoordinat;
        this.longKoordinat = longKoordinat;
        this.tagTime = tagTime;

    }

    public String getLatKoordinat() {
        return latKoordinat;
    }
    public String getLongKoordinat() {
        return longKoordinat;
    }
    public String getTagTime() {
        return tagTime;
    }



}
