package com.nilo.dms.service.order.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ronny on 2017/9/27.
 */
public class ReceiverInfo {

    private String receiverName;

    private String receiverPhone;

    private String receiverCountry;

    private String receiverProvince;

    private String receiverCity;

    private String receiverArea;

    private String receiverAddress;


    public String getReceiverName() {
        return receiverName;
    }
    @JSONField(name = "name")
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }
    @JSONField(name = "phone_number")
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }
    @JSONField(name = "country")
    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }
    @JSONField(name = "province")
    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }
    @JSONField(name = "city")
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverArea() {
        return receiverArea;
    }

    public void setReceiverArea(String receiverArea) {
        this.receiverArea = receiverArea;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }
    @JSONField(name = "address")
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
}
