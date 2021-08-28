package com.julong.longtech.menuworkshop;

public class ListMaterialProsesPerbaikan {

    private String materialCode;
    private String materialName;
    private String unitMeasure;
    private String editTextValue;
    private boolean isSelected;

    public ListMaterialProsesPerbaikan(String materialCode, String materialName, String unitMeasure) {
        this.materialCode = materialCode;
        this.materialName = materialName;
        this.unitMeasure = unitMeasure;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

    public String getEditTextValue() {
        return editTextValue;
    }

    public boolean getChecked() {
        return isSelected;
    }

    public void setChecked(boolean isChecked) {
        isSelected = isChecked;
    }

}
