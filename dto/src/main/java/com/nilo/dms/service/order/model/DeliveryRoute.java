package com.nilo.dms.service.order.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/10.
 */
public class DeliveryRoute {

    private String merchantId;

    private String orderNo;

    private String opt;

    private String optNetwork;

    private String networkDesc;

    private String optByName;

    private String optBy;

    private Long optTime;

    private String phone;

    private String remark;

    private Long createdTime;

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

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getOptNetwork() {
        return optNetwork;
    }

    public void setOptNetwork(String optNetwork) {
        this.optNetwork = optNetwork;
    }

    public String getNetworkDesc() {
        return networkDesc;
    }

    public void setNetworkDesc(String networkDesc) {
        this.networkDesc = networkDesc;
    }

    public String getOptByName() {
        return optByName;
    }

    public void setOptByName(String optByName) {
        this.optByName = optByName;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }

    public Long getOptTime() {
        return optTime;
    }

    public void setOptTime(Long optTime) {
        this.optTime = optTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
