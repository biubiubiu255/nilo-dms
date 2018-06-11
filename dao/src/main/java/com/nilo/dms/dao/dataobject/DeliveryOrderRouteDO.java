package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class DeliveryOrderRouteDO extends BaseDo<Long> {

    private Long merchantId;

    private String orderNo;

    private String opt;

    private String optNetwork;

    private String expressName;

    private String nextNetwork;

    private String optBy;

    private String optByName;

    private String rider;

    private String optByNamePhone;

    private Long optTime;

    private String phone;

    private String jobId;

    private String signer;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOptNetwork() {
        return optNetwork;
    }

    public void setOptNetwork(String optNetwork) {
        this.optNetwork = optNetwork;
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

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOptByName() {
        return optByName;
    }

    public void setOptByName(String optByName) {
        this.optByName = optByName;
    }

    public String getOptByNamePhone() {
        return optByNamePhone;
    }

    public void setOptByNamePhone(String optByNamePhone) {
        this.optByNamePhone = optByNamePhone;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }
}
