package com.nilo.dms.dao.dataobject;

public class ReportInvoiceDO {
    private Long merchantId;
    private String orderNo;
    private String orderType;
    private Double weight;
    private String network;
    private String rider;
    private String opName;
    private String clientName;
    private int createTime;

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

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ReportInvoiceDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", orderType='" + orderType + '\'' +
                ", weight=" + weight +
                ", network='" + network + '\'' +
                ", rider='" + rider + '\'' +
                ", opName='" + opName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
