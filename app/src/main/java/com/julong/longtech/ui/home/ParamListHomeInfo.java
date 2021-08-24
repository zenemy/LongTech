package com.julong.longtech.ui.home;

public class ParamListHomeInfo {

    private String menucode;
    private String datatype;
    private String workstatus;
    private String transactiondate;
    private String documentnumber;

    public ParamListHomeInfo(String menucode, String datatype, String workstatus,
                             String transactiondate, String documentnumber) {
        this.menucode = menucode;
        this.datatype = datatype;
        this.workstatus = workstatus;
        this.transactiondate = transactiondate;
        this.documentnumber = documentnumber;
    }

    public String getMenucode() {
        return menucode;
    }

    public String getDatatype() {
        return datatype;
    }

    public String getWorkstatus() {
        return workstatus;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public String getDocumentnumber() {
        return documentnumber;
    }

}
