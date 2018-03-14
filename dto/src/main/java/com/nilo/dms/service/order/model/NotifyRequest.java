package com.nilo.dms.service.order.model;

/**
 * Created by admin on 2017/11/6.
 */
public class NotifyRequest {

    private String merchantId;

    private String appKey;

    private String orderNo;

    private String referenceNo;

    private String url;

    private String method;

    private String bizType;

    private String sign;

    private String data;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    @Override
    public String toString() {
        return "NotifyRequest{" +
                "merchantId='" + merchantId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", bizType='" + bizType + '\'' +
                ", sign='" + sign + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
