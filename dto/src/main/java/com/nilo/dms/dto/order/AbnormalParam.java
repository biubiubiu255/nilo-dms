package com.nilo.dms.dto.order;

import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;

/**
 * Created by admin on 2017/12/13.
 */
public class AbnormalParam {

    private String merchantId;
    private String abnormalNo;
    private String orderNo;
    private String reason;
    private String abnormalType;
    private String optBy;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAbnormalNo() {
        return abnormalNo;
    }

    public void setAbnormalNo(String abnormalNo) {
        this.abnormalNo = abnormalNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(String abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }

}
