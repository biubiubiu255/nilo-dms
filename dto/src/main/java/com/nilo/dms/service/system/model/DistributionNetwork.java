package com.nilo.dms.service.system.model;

import com.nilo.dms.common.enums.DistributionStatusEnum;
import com.nilo.dms.common.enums.DistributionTypeEnum;

/**
 * Created by admin on 2017/12/5.
 */
public class DistributionNetwork {

    private String id;

    private String merchantId;

    private String merchantName;

    private String name;

    private String code;

    private String address;

    private DistributionTypeEnum type;

    private String country;

    private String province;

    private String city;

    private String area;

    private String remark;

    private DistributionStatusEnum status;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DistributionTypeEnum getType() {
        return type;
    }

    public void setType(DistributionTypeEnum type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public DistributionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DistributionStatusEnum status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }

    public String getProvinceDesc() {
    	return "需要修改";
        //return RedisUtil.hget(Constant.ADDRESS + country, province);
    }

    public String getCityDesc() {
    	return "需要修改";
        //return RedisUtil.hget(Constant.ADDRESS + country, city);
    }

    public String getAreaDesc() {
    	return "需要修改";
        //return RedisUtil.hget(Constant.ADDRESS + country, area);
    }
}
