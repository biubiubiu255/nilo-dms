package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.OptTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/3.
 */
public class OrderOptRequest {

    private String merchantId;

    private List<String> orderNo;

    private OptTypeEnum optType;

    private String optBy;

    private String remark;

    private Map<String,String> params;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<String> getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(List<String> orderNo) {
        this.orderNo = orderNo;
    }

    public OptTypeEnum getOptType() {
        return optType;
    }

    public void setOptType(OptTypeEnum optType) {
        this.optType = optType;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }
}
