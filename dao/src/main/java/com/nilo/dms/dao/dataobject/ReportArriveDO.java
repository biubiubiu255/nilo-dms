package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class ReportArriveDO {
    private Long merchantId;
    private String orderNo;
    private String lastNetwork;
    private Double weight;
    private String scanNetwork;
    private int scanTime;
    private String recipients;
    private String address;
    private String phone;
    private Integer status;
    public String getStatusDesc() {
        DeliveryOrderStatusEnum statusEnum = DeliveryOrderStatusEnum.getEnum(this.status);
        return statusEnum == null ? "" : statusEnum.getDesc();
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
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

    public String getLastNetwork() {
        return lastNetwork;
    }

    public void setLastNetwork(String lastNetwork) {
        this.lastNetwork = lastNetwork;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }


    public String getScanNetwork() {
        return scanNetwork;
    }

    public void setScanNetwork(String scanNetwork) {
        this.scanNetwork = scanNetwork;
    }

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "ReportArriveDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", lastNetwork='" + lastNetwork + '\'' +
                ", weight=" + weight +

                ", scanNetwork='" + scanNetwork + '\'' +
                ", scanTime=" + scanTime +
                ", recipients='" + recipients + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
