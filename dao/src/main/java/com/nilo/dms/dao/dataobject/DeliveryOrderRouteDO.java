package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class DeliveryOrderRouteDO extends BaseDo<Long> {

    private Long merchantId;

    private String orderNo;

    private String memoCn;

    private String memoEn;

    private String opt;

    private String optNetwork;

    private String optBy;

    private Long optTime;

    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemoCn() {
        return memoCn;
    }

    public void setMemoCn(String memoCn) {
        this.memoCn = memoCn;
    }

    public String getMemoEn() {
        return memoEn;
    }

    public void setMemoEn(String memoEn) {
        this.memoEn = memoEn;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
