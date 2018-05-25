package com.nilo.dms.dto.system;

import com.nilo.dms.common.enums.BizFeeConfigStatusEnum;

/**
 * Created by admin on 2017/12/13.
 */
public class BizFeeConfig {

    private String merchantId;

    private String optType;

    private Double fee;

    private String remark;

    private BizFeeConfigStatusEnum status;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BizFeeConfigStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BizFeeConfigStatusEnum status) {
        this.status = status;
    }
}
