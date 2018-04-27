package com.nilo.dms.dao.dataobject;

public class SignReportDO {
    private Long merchantId;
    private String orderNo;
    private String referenceNo;
    private Double weight;
    private Double needPayAmount;
    private Double alreadyPaid;
    private String handleBy;
    private Long handleTime;
    private String sName;
    private String rName;
    private String contactNumber;
    private String address;
    private String remark;

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

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getNeedPayAmount() {
        return needPayAmount;
    }

    public void setNeedPayAmount(Double needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public Double getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(Double alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public String getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(String handleBy) {
        this.handleBy = handleBy;
    }

    public Long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SignReportDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", weight=" + weight +
                ", needPayAmount=" + needPayAmount +
                ", alreadyPaid=" + alreadyPaid +
                ", handleBy='" + handleBy + '\'' +
                ", handleTime=" + handleTime +
                ", sName='" + sName + '\'' +
                ", rName='" + rName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
