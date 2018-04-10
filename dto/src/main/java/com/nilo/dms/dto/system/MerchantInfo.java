package com.nilo.dms.dto.system;

import com.nilo.dms.common.enums.CustomerTypeEnum;
import com.nilo.dms.common.enums.LevelEnum;
import com.nilo.dms.common.enums.MerchantStatusEnum;
import com.nilo.dms.common.enums.NatureTypeEnum;

/**
 * Created by ronny on 2017/8/25.
 */
public class MerchantInfo {

    private String id;

    private String merchantName;

    private String country;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private String description;

    private String website;

    private String pcLogo;

    private String appLogo;

    private String address;

    private MerchantStatusEnum status;
    private CustomerTypeEnum type;
    private NatureTypeEnum nature;
    private LevelEnum level;

    private Integer vip;

    private String licence;
    private String idCard;
    private Long createdTime;
    private Long updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MerchantStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MerchantStatusEnum status) {
        this.status = status;
    }

    public CustomerTypeEnum getType() {
        return type;
    }

    public void setType(CustomerTypeEnum type) {
        this.type = type;
    }

    public NatureTypeEnum getNature() {
        return nature;
    }

    public void setNature(NatureTypeEnum nature) {
        this.nature = nature;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPcLogo() {
        return pcLogo;
    }

    public void setPcLogo(String pcLogo) {
        this.pcLogo = pcLogo;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }

    public String getTypeDesc() {
        return this.type == null ? "" : this.type.getDesc();
    }

    public String getNatureDesc() {
        return this.nature == null ? "" : this.nature.getDesc();
    }

    public String getLevelDesc() {
        return this.level == null ? "" : this.level.getDesc();
    }


    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }
}
