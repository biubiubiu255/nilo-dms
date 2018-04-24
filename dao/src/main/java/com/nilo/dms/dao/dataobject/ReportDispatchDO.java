package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class ReportDispatchDO extends BaseDo<Long> {
    private String handleNo;
    private String orderNo;
    private String referenceNo;
    private String country;
    private String orderType;
    private String address;


    private Double weight;
    private Double len;
    private Double high;
    private Double width;
    private Long handleBy;
    private String handleName;

    public String getHandleNo() {
        return handleNo;
    }

    public void setHandleNo(String handleNo) {
        this.handleNo = handleNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLen() {
        return len;
    }

    public void setLen(Double len) {
        this.len = len;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ReportDispatchDO{" +
                "handleNo='" + handleNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", country='" + country + '\'' +
                ", orderType='" + orderType + '\'' +
                ", weight=" + weight +
                ", len=" + len +
                ", high=" + high +
                ", width=" + width +
                ", handleBy=" + handleBy +
                ", handleName='" + handleName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
