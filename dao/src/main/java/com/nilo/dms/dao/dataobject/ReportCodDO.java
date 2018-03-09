package com.nilo.dms.dao.dataobject;

public class ReportCodDO {
    private Long merchantId;
    private String orderNo;
    private Double weight;
    private Double money;
    private String driver;
    private String payType;
    private String orderPlatform;
    private int createTime;
    private int signTime;
    private String signName;

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    private String platform;
    private String phone;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getSignTime() {
        return signTime;
    }

    public void setSignTime(int signTime) {
        this.signTime = signTime;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "ReportCodDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", weight=" + weight +
                ", money=" + money +
                ", driver='" + driver + '\'' +
                ", payType='" + payType + '\'' +
                ", createTime=" + createTime +
                ", signTime=" + signTime +
                ", signName=" + signName +
                ", platform='" + platform + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
