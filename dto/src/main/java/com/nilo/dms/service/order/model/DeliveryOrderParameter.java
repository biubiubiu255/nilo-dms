package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.FetchTypeEnum;

import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
public class DeliveryOrderParameter {
    private String merchantId;

    private String orderNo;

    private String referenceNo;

    private String isPackage;

    private List<String> orderType;

    private String fromCreatedTime;

    private String toCreatedTime;

    private List<Integer> status;

    private String nextStation;

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
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

    public List<String> getOrderType() {
        return orderType;
    }

    public void setOrderType(List<String> orderType) {
        this.orderType = orderType;
    }
}
