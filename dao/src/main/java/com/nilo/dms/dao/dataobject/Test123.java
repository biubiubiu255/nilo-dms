package com.nilo.dms.dao.dataobject;

public class Test123 {
    private String waybillNumber;
    private String carrierSite;
    private String detainedType;
    private String remarks;

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public String getCarrierSite() {
        return carrierSite;
    }

    public void setCarrierSite(String carrierSite) {
        this.carrierSite = carrierSite;
    }

    public String getDetainedType() {
        return detainedType;
    }

    public void setDetainedType(String detainedType) {
        this.detainedType = detainedType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Test123{" +
                "waybillNumber='" + waybillNumber + '\'' +
                ", carrierSite='" + carrierSite + '\'' +
                ", detainedType='" + detainedType + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
