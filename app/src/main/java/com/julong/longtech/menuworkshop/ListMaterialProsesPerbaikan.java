package com.julong.longtech.menuworkshop;

public class ListMaterialProsesPerbaikan {

    private String materialName;
    private int materialQty;
    private String unitMeasure;

    public ListMaterialProsesPerbaikan(String materialName, int materialQty, String unitMeasure) {
        this.materialName = materialName;
        this.materialQty = materialQty;
        this.unitMeasure = unitMeasure;
    }


    public String getMaterialName() {
        return materialName;
    }

    public int getMaterialQty() {
        return materialQty;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }


}
