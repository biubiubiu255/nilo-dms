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

    private String countryId;

    private String provinceId;

    private String cityId;

    private String areaId;

    public String getCountryId() {
        return countryId;
    }
    @JSONField(name = "country_id")
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getProvinceId() {
        return provinceId;
    }
    @JSONField(name = "province_id")
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }
    @JSONField(name = "city_id")
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }
    @JSONField(name = "area_id")
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getSenderName() {
        return senderName;
    }
    @JSONField(name = "contact_name")
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }
    @JSONField(name = "contact_number")
    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderCountry() {
        return senderCountry;
    }
    @JSONField(name = "country")
    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderProvince() {
        return senderProvince;
    }
    @JSONField(name = "province")
    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }


    public String getSenderCity() {
        return senderCity;
    }
    @JSONField(name = "city")
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
