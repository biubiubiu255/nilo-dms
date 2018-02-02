package com.nilo.dms.service.order.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ronny on 2017/9/27.
 */
public class SenderInfo {


    private String senderName;

    private String senderPhone;

    private String senderCountry;

    private String senderProvince;

    private String senderCity;

    private String senderArea;

    private String senderAddress;


    public String getSenderName() {
        return senderName;
    }
    @JSONField(name = "sender_contactname")
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }
    @JSONField(name = "sender_contactnumber")
    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderCountry() {
        return senderCountry;
    }
    @JSONField(name = "sender_country")
    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderProvince() {
        return senderProvince;
    }
    @JSONField(name = "sender_province")
    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }


    public String getSenderCity() {
        return senderCity;
    }
    @JSONField(name = "sender_city")
    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderArea() {
        return senderArea;
    }

    public void setSenderArea(String senderArea) {
        this.senderArea = senderArea;
    }


    public String getSenderAddress() {
        return senderAddress;
    }
    @JSONField(name = "sender_address")
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
}
