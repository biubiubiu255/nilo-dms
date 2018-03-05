package com.nilo.dms.service.order.model;
import com.nilo.dms.common.enums.TaskStatusEnum;

public class DeliverReport {
    private String merchantId;
    private String orderNo;
    private String referenceNo;
    private TaskStatusEnum status;
    private Integer networkId;
    private String networkDesc;
//    private Integer handledBy;
    private String handledBy;
    private Long createdTime;
    private Long handledTime;
    private String name;
    private String contactNumber;
    private String address;
    private Double needPayAmount;
    private Double deliveryFee;
    private String remark;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getNetworkDesc() {
        return networkDesc;
    }

    public void setNetworkDesc(String networkDesc) {
        this.networkDesc = networkDesc;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getHandledTime() {
        return handledTime;
    }

    public void setHandledTime(Long handledTime) {
        this.handledTime = handledTime;
    }

    public Double getNeedPayAmount() {
        return needPayAmount;
    }

    public void setNeedPayAmount(Double needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

//    public Integer getHandledBy() {
//        return handledBy;
//    }
//
//    public void setHandledBy(Integer handledBy) {
//        this.handledBy = handledBy;
//    }
    //    public void setNetworkId(Integer networkId) {
//        this.networkId = networkId;
//    }
//
    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getStatusDesc() {
        return this.status == null ? "" : this.status.getDesc();
    }

}
