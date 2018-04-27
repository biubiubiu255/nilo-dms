package com.nilo.dms.dao.dataobject.QO;

import com.nilo.dms.common.BaseDo;

public class SendReportQO extends BaseDo<Long> {
    private Long merchantId;
    private String referenceNo;
    private String orderNo;
    private String orderType;
    private Double weight;
    private Integer status;
    private String remark;
    private String phone;
    private String address;

    private String driver;
    private String nextStation;
    private String nextStationCode;
    private String handleName;
    private String handleNo;
    private String deliveryType;
    private String expressName;
    private String expressCode;

    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer fromCreatedTime;
    private Integer toCreatedTime;
    private Integer exportType;

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

    public String getNextStationCode() {
        return nextStationCode;
    }

    public void setNextStationCode(String nextStationCode) {
        this.nextStationCode = nextStationCode;
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

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFromCreatedTime() {
        return fromCreatedTime;
    }

    public void setFromCreatedTime(Integer fromCreatedTime) {
        this.fromCreatedTime = fromCreatedTime;
    }

    public Integer getToCreatedTime() {
        return toCreatedTime;
    }

    public void setToCreatedTime(Integer toCreatedTime) {
        this.toCreatedTime = toCreatedTime;
    }

    public Integer getExportType() {
        return exportType;
    }

    public void setExportType(Integer exportType) {
        this.exportType = exportType;
    }

    @Override
    public String toString() {
        return "SendReportQO{" +
                "merchantId=" + merchantId +
                ", referenceNo='" + referenceNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderType='" + orderType + '\'' +
                ", weight=" + weight +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", driver='" + driver + '\'' +
                ", nextStation='" + nextStation + '\'' +
                ", nextStationCode='" + nextStationCode + '\'' +
                ", handleName='" + handleName + '\'' +
                ", handleNo='" + handleNo + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", expressName='" + expressName + '\'' +
                ", expressCode='" + expressCode + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                ", total=" + total +
                ", fromCreatedTime=" + fromCreatedTime +
                ", toCreatedTime=" + toCreatedTime +
                ", exportType=" + exportType +
                '}';
    }
}
