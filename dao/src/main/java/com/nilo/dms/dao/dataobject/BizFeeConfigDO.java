package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.BizFeeConfigStatusEnum;

/**
 * Created by admin on 2017/12/13.
 */
public class BizFeeConfigDO extends BaseDo<Long> {

    private Long merchantId;

    private String optType;

    private Double fee;

    private String remark;

    private Integer status;

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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
