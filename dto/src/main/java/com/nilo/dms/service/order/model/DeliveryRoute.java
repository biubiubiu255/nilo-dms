package com.nilo.dms.service.order.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/10.
 */
public class DeliveryRoute {

    private String merchantId;

    private String orderNo;

    private String traceCN;

    private String traceEN;

    private String opt;

    private String remark;

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

    public String getTraceCN() {
        return traceCN;
    }

    public void setTraceCN(String traceCN) {
        this.traceCN = traceCN;
    }

    public String getTraceEN() {
        return traceEN;
    }

    public void setTraceEN(String traceEN) {
        this.traceEN = traceEN;
    }
}
