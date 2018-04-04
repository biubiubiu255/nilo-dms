package com.nilo.dms.dao.dataobject;

public class ReportInvoiceQueryDO {
    private Long merchantId;
    private String orderNo;
    private Integer sTime_creat;
    private Integer eTime_creat;
    private Integer sTime_sign;
    private Integer eTime_sign;
    private String rider;
    private String payType;
    private String orderPlatform;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getsTime_creat() {
        return sTime_creat;
    }

    public void setsTime_creat(Integer sTime_creat) {
        this.sTime_creat = sTime_creat;
    }

    public Integer geteTime_creat() {
        return eTime_creat;
    }

    public void seteTime_creat(Integer eTime_creat) {
        this.eTime_creat = eTime_creat;
    }

    public Integer getsTime_sign() {
        return sTime_sign;
    }

    public void setsTime_sign(Integer sTime_sign) {
        this.sTime_sign = sTime_sign;
    }

    public Integer geteTime_sign() {
        return eTime_sign;
    }

    public void seteTime_sign(Integer eTime_sign) {
        this.eTime_sign = eTime_sign;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderPlatform() {
        return orderPlatform;
    }

    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    @Override
    public String toString() {
        return "ReportInvoiceQueryDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", sTime_creat=" + sTime_creat +
                ", eTime_creat=" + eTime_creat +
                ", sTime_sign=" + sTime_sign +
                ", eTime_sign=" + eTime_sign +
                ", rider='" + rider + '\'' +
                ", payType='" + payType + '\'' +
                ", orderPlatform='" + orderPlatform + '\'' +
                '}';
    }
}
