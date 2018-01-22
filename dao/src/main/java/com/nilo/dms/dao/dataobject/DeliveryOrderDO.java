package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.*;

/**
 * Created by admin on 2017/9/19.
 */
public class DeliveryOrderDO extends BaseDo<Long> {

    private String orderNo;
    private String orderType;

    private String referenceNo;

    private Long orderTime;

    private String country;

    private Long merchantId;

    private String orderPlatform;

    private String serviceType;

    private String fetchType;

    private String fetchAddress;

    private Long fetchTime;

    private Long totalPrice;

    private Double weight;

    private String goodsType;

    private Integer status;

    private String userdefine01;
    private String userdefine02;
    private String userdefine03;
    private String userdefine04;
    private String userdefine05;

    private String clientType;
    private String deliveryCategoryType;
    private String customerType;
    private String customerLevel;
    private String transportType;
    private String productType;
    private String settleType;

    private Integer networkId;

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    public String getDeliveryCategoryType() {
        return deliveryCategoryType;
    }

    public void setDeliveryCategoryType(String deliveryCategoryType) {
        this.deliveryCategoryType = deliveryCategoryType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getFetchType() {
        return fetchType;
    }

    public void setFetchType(String fetchType) {
        this.fetchType = fetchType;
    }

    public String getFetchAddress() {
        return fetchAddress;
    }

    public void setFetchAddress(String fetchAddress) {
        this.fetchAddress = fetchAddress;
    }

    public Long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Long fetchTime) {
        this.fetchTime = fetchTime;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderPlatform() {
        return orderPlatform;
    }

    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getUserdefine01() {
        return userdefine01;
    }

    public void setUserdefine01(String userdefine01) {
        this.userdefine01 = userdefine01;
    }

    public String getUserdefine02() {
        return userdefine02;
    }

    public void setUserdefine02(String userdefine02) {
        this.userdefine02 = userdefine02;
    }

    public String getUserdefine03() {
        return userdefine03;
    }

    public void setUserdefine03(String userdefine03) {
        this.userdefine03 = userdefine03;
    }

    public String getUserdefine04() {
        return userdefine04;
    }

    public void setUserdefine04(String userdefine04) {
        this.userdefine04 = userdefine04;
    }

    public String getUserdefine05() {
        return userdefine05;
    }

    public void setUserdefine05(String userdefine05) {
        this.userdefine05 = userdefine05;
    }
}
