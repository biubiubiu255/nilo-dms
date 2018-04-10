package com.nilo.dms.dto.system;

import com.nilo.dms.common.enums.SMSConfigStatusEnum;

/**
 * Created by admin on 2017/12/1.
 */
public class SMSConfig {

    private String merchantId;

    private String smsType;

    private String content;

    private String remark;

    private SMSConfigStatusEnum status;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SMSConfigStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SMSConfigStatusEnum status) {
        this.status = status;
    }
}
