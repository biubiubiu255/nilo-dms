package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class SendReportDO extends BaseDo<Long> {
    private Long merchantId;
    private String referenceNo;
    private String orderNo;
    private String orderType;
    private Double weight;
    private Integer status;
    private String remark;
    private String phone;
    private String address;
    private String receiveName;

    private String driver;
    private String nextStation;
    private String handleName;
    private String handleNo;
    private String deliveryType;
    private String expressName;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getStatusDesc() {
        DeliveryOrderStatusEnum statusEnum = DeliveryOrderStatusEnum.getEnum(this.status);
        return statusEnum == null ? "" : statusEnum.getDesc();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getHandleNo() {
        return handleNo;
    }

    public void setHandleNo(String handleNo) {
        this.handleNo = handleNo;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    @Override
    public String toString() {
        return "SendReportDO{" +
                "merchantId=" + merchantId +
                ", referenceNo='" + referenceNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderType='" + orderType + '\'' +
                ", weight=" + weight +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", driver='" + driver + '\'' +
                ", nextStation='" + nextStation + '\'' +
                ", handleName='" + handleName + '\'' +
                ", handleNo='" + handleNo + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", expressName='" + expressName + '\'' +
                '}';
    }
}
