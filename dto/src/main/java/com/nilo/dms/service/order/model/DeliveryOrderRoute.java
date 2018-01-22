package com.nilo.dms.service.order.model;

import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
public class DeliveryOrderRoute {

    private String merchantId;

    private String orderNo;

    private String memoCN;

    private String memoEN;

    private String opt;

    private String remark;

    private Long createdTime;

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
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

    public String getMemoCN() {
        return memoCN;
    }

    public void setMemoCN(String memoCN) {
        this.memoCN = memoCN;
    }

    public String getMemoEN() {
        return memoEN;
    }

    public void setMemoEN(String memoEN) {
        this.memoEN = memoEN;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
