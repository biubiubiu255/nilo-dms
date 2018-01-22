package com.nilo.dms.service.order.model;


import com.nilo.dms.common.enums.*;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public class DeliveryOrder {

    private String orderNo;

    private String orderType;

    private String referenceNo;

    private Long orderTime;

    private Long createdTime;

    private Long updatedTime;

    private String country;

    private String merchantId;

    private ServiceTypeEnum serviceType;

    private DeliveryCategoryTypeEnum categoryType;

    private String fetchType;

    private Long fetchTime;

    private String fetchAddress;

    private String orderPlatform;

    private Long totalPrice;

    private ReceiverInfo receiverInfo;

    private SenderInfo senderInfo;

    private List<GoodsInfo> goodsInfoList;

    private DeliveryOrderStatusEnum status;

    private Double weight;

    private String goodsType;

    private DeliveryCategoryTypeEnum deliveryCategoryType;

    private CustomerTypeEnum customerType;

    private LevelEnum customerLevel;

    private TransportTypeEnum transportType;

    private ProductTypeEnum productType;

    private SettleTypeEnum settleType;

    //扩展字段
    private String userdefine01;
    private String userdefine02;
    private String userdefine03;
    private String userdefine04;
    private String userdefine05;

    private ClientTypeEnum clientType;

    public Long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Long fetchTime) {
        this.fetchTime = fetchTime;
    }

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
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

    public DeliveryOrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DeliveryOrderStatusEnum status) {
        this.status = status;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
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

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderPlatform() {
        return orderPlatform;
    }

    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    public ReceiverInfo getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(ReceiverInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
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

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public ClientTypeEnum getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeEnum clientType) {
        this.clientType = clientType;
    }

    public DeliveryCategoryTypeEnum getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(DeliveryCategoryTypeEnum categoryType) {
        this.categoryType = categoryType;
    }

    public DeliveryCategoryTypeEnum getDeliveryCategoryType() {
        return deliveryCategoryType;
    }

    public void setDeliveryCategoryType(DeliveryCategoryTypeEnum deliveryCategoryType) {
        this.deliveryCategoryType = deliveryCategoryType;
    }

    public CustomerTypeEnum getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerTypeEnum customerType) {
        this.customerType = customerType;
    }

    public LevelEnum getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(LevelEnum customerLevel) {
        this.customerLevel = customerLevel;
    }

    public TransportTypeEnum getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportTypeEnum transportType) {
        this.transportType = transportType;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public SettleTypeEnum getSettleType() {
        return settleType;
    }

    public void setSettleType(SettleTypeEnum settleType) {
        this.settleType = settleType;
    }

    public String getStatusDesc() {
        return this.status == null ? "" : this.status.getDesc();
    }

    public String getServiceTypeDesc() {
        return this.serviceType == null ? "" : this.serviceType.getDesc();
    }

}
