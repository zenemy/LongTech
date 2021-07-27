package com.julong.longtech.ui.home;

public class ParamListHomeInfo {

    private String datatype;
    private String workstatus;
    private String transactiondate;

    public ParamListHomeInfo(String datatype, String workstatus, String transactiondate) {
        this.datatype = datatype;
        this.workstatus = workstatus;
        this.transactiondate = transactiondate;
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

}
