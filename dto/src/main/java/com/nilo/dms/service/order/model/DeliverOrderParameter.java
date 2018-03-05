package com.nilo.dms.service.order.model;

import java.util.List;

public class DeliverOrderParameter {
    private String merchantId;
    private String orderNo;
    private List<Integer> status;
    private String carrierName;
    private String fromCreatedTime;
    private String toCreatedTime;

    public String getFromCreatedTime() {
        return fromCreatedTime;
    }

    public void setFromCreatedTime(String fromCreatedTime) {
        this.fromCreatedTime = fromCreatedTime;
    }

    public String getToCreatedTime() {
        return toCreatedTime;
    }

    public void setToCreatedTime(String toCreatedTime) {
        this.toCreatedTime = toCreatedTime;
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
}
