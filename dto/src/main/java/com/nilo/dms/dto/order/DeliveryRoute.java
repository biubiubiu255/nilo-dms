package com.nilo.dms.dto.order;

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

    private String expressName;

    private String nextNetwork;

    private String optByNamePhone;

    private String rider;

    private String optBy;

    private Long optTime;

    private String phone;

    private String remark;

    private String jobId;

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

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getNextNetwork() {
        return nextNetwork;
    }

    public void setNextNetwork(String nextNetwork) {
        this.nextNetwork = nextNetwork;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getOptByNamePhone() {
        return optByNamePhone;
    }

    public void setOptByNamePhone(String optByNamePhone) {
        this.optByNamePhone = optByNamePhone;
    }
}
