package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;

/**
 * Created by admin on 2017/11/3.
 */
public class AbnormalOptRequest {

    private String merchantId;

    private String abnormalNo;

    private String handleType;

    private String remark;

    private boolean returnToMerchant;

    private String optBy;

    public boolean isReturnToMerchant() {
        return returnToMerchant;
    }

    public void setReturnToMerchant(boolean returnToMerchant) {
        this.returnToMerchant = returnToMerchant;
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

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }
}
