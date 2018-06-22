package com.nilo.dms.dto.order;

import java.util.List;

public class SignOrderParameter {
    private String merchantId;
    private String orderNo;
    private Integer networkCode;
    private String nextNetwork;
    private String expressName;
    private String riderId;
    private String outsource;
    private List<Integer> status;
    private String carrierName;
    private Integer fromHandledTime;
    private Integer toHandledTime;
    private Integer offset;
    private Integer limit;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getNetworkCode() {
        return networkCode;
    }

    public void setNetworkCode(Integer networkCode) {
        this.networkCode = networkCode;
    }

    public String getNextNetwork() {
        return nextNetwork;
    }

    public void setNextNetwork(String nextNetwork) {
        this.nextNetwork = nextNetwork;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Integer getFromHandledTime() {
        return fromHandledTime;
    }

    public void setFromHandledTime(Integer fromHandledTime) {
        this.fromHandledTime = fromHandledTime;
    }

    public Integer getToHandledTime() {
        return toHandledTime;
    }

    public void setToHandledTime(Integer toHandledTime) {
        this.toHandledTime = toHandledTime;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOutsource() {
        return outsource;
    }

    public void setOutsource(String outsource) {
        this.outsource = outsource;
    }

    @Override
    public String toString() {
        return "SignOrderParameter{" +
                "merchantId='" + merchantId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", networkCode=" + networkCode +
                ", nextNetwork='" + nextNetwork + '\'' +
                ", expressName='" + expressName + '\'' +
                ", riderId='" + riderId + '\'' +
                ", status=" + status +
                ", carrierName='" + carrierName + '\'' +
                ", fromHandledTime=" + fromHandledTime +
                ", toHandledTime=" + toHandledTime +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
