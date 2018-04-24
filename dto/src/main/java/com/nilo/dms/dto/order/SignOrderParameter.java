package com.nilo.dms.dto.order;

import java.util.List;

public class SignOrderParameter {
    private String merchantId;
    private String orderNo;
    private String nextNetwork;
    private List<Integer> status;
    private String carrierName;
    private String fromHandledTime;
    private String toHandledTime;

    public String getFromHandledTime() {
        return fromHandledTime;
    }

    public void setFromHandledTime(String fromHandledTime) {
        this.fromHandledTime = fromHandledTime;
    }

    public String getToHandledTime() {
        return toHandledTime;
    }

    public void setToHandledTime(String toHandledTime) {
        this.toHandledTime = toHandledTime;
    }

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

    public String getNextNetwork() {
        return nextNetwork;
    }

    public void setNextNetwork(String nextNetwork) {
        this.nextNetwork = nextNetwork;
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

    @Override
    public String toString() {
        return "SignOrderParameter{" +
                "merchantId='" + merchantId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", nextNetwork='" + nextNetwork + '\'' +
                ", status=" + status +
                ", carrierName='" + carrierName + '\'' +
                ", fromHandledTime='" + fromHandledTime + '\'' +
                ", toHandledTime='" + toHandledTime + '\'' +
                '}';
    }
}
