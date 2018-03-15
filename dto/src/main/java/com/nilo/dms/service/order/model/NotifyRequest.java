package com.nilo.dms.service.order.model;

/**
 * Created by admin on 2017/11/6.
 */
public class NotifyRequest {

    private String appKey;

    private String url;

    private String method;

    private String bizType;

    private String sign;

    private String data;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
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

    @Override
    public String toString() {
        return "NotifyRequest{" +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", bizType='" + bizType + '\'' +
                ", sign='" + sign + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
