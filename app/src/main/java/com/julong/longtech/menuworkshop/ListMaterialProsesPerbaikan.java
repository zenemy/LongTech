package com.julong.longtech.menuworkshop;

public class ListMaterialProsesPerbaikan {

    private String materialName;
    private String unitMeasure;

    public ListMaterialProsesPerbaikan(String materialName, String unitMeasure) {

        this.materialName = materialName;
        this.unitMeasure = unitMeasure;

    }

    public String getMaterialName() {
        return materialName;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

}
