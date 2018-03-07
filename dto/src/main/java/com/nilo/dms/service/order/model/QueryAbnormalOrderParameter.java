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

    private List<String> abnormalType;

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public List<String> getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(List<String> abnormalType) {
        this.abnormalType = abnormalType;
    }
}
