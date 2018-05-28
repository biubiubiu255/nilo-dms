package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.*;

/**
 * Created by admin on 2017/12/13.
 */
public class DeliveryFeeTemplateDO  extends BaseDo<Long> {

    private Long merchantId;

    private String name;

    private String country;

    private String customerType;

    private String customerLevel;

    private String settleType;

    private String serviceProduct;

    private String transportType;

    private String origin;

    private String destination;

    private Long expiryFrom;

    private Long expiryTo;

    private Integer status;

    private double basicFee;

    private double firstRegion;

    private double firstFee;

    private double secondRegion;

    private double secondFee;

    public Long getExpiryFrom() {
        return expiryFrom;
    }

    public void setExpiryFrom(Long expiryFrom) {
        this.expiryFrom = expiryFrom;
    }

    public Long getExpiryTo() {
        return expiryTo;
    }

    public void setExpiryTo(Long expiryTo) {
        this.expiryTo = expiryTo;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getServiceProduct() {
        return serviceProduct;
    }

    public void setServiceProduct(String serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBasicFee() {
        return basicFee;
    }

    public void setBasicFee(double basicFee) {
        this.basicFee = basicFee;
    }

    public double getFirstRegion() {
        return firstRegion;
    }

    public void setFirstRegion(double firstRegion) {
        this.firstRegion = firstRegion;
    }

    public double getFirstFee() {
        return firstFee;
    }

    public void setFirstFee(double firstFee) {
        this.firstFee = firstFee;
    }

    public double getSecondRegion() {
        return secondRegion;
    }

    public void setSecondRegion(double secondRegion) {
        this.secondRegion = secondRegion;
    }

    public double getSecondFee() {
        return secondFee;
    }

    public void setSecondFee(double secondFee) {
        this.secondFee = secondFee;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getCustomerTypeDesc(){
        return CustomerTypeEnum.getEnum(this.customerType).getDesc();
    }

    public String getCustomerLevelDesc(){
        return LevelEnum.getEnum(this.customerLevel).getDesc();
    }

    public String getSettleTypeDesc(){
        return SettleTypeEnum.getEnum(this.settleType).getDesc();
    }
    public String getServiceProductDesc(){
        return ProductTypeEnum.getEnum(this.serviceProduct).getDesc();
    }
    public String getTransportTypeDesc(){
        return TransportTypeEnum.getEnum(this.transportType).getDesc();
    }
}
