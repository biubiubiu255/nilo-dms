package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.DeliveryFeeDetailsStatusEnum;

/**
 * Created by admin on 2017/12/13.
 */
public class DeliveryFeeDetails {

    private String merchantId;

    private String orderNo;

    private String bizType;

    private String remark;

    private DeliveryFeeDetailsStatusEnum status;

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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public DeliveryFeeDetailsStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DeliveryFeeDetailsStatusEnum status) {
        this.status = status;
    }
}
