package com.nilo.dms.service.order.model;

import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
public class QueryAbnormalOrderParameter {
    private String merchantId;

    private String orderNo;

    private String fromCreatedTime;

    private String toCreatedTime;

    private List<Integer> status;

    private String referenceNo;

    private List<String> abnormalType;

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

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public List<String> getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(List<String> abnormalType) {
        this.abnormalType = abnormalType;
    }
}
