package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class ReportDispatchDO extends BaseDo<Long> {

    private Long   merchantId;
    private String handleNo;
    private String orderNo;
    private String referenceNo;
    private String country;
    private String orderType;

    private Double weight;
    private Double len;
    private Double high;
    private Double width;
    private Long handleBy;
    private String handleName;
    private String rider;
    private String phone;
    private String address;

    private Integer status;

    private String statusDesc;

    public Integer getStatus() {
        return status;
    }
    public String getStatusDesc() {
        DeliveryOrderStatusEnum s =DeliveryOrderStatusEnum.getEnum(this.status);
        return  s== null?"":s.getDesc();
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getHandleNo() {
        return handleNo;
    }

    public void setHandleNo(String handleNo) {
        this.handleNo = handleNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Double getLen() {
        return len;
    }

    public void setLen(Double len) {
        this.len = len;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
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

    @Override
    public String toString() {
        return "ReportDispatchDO{" +
                "merchantId=" + merchantId +
                ", handleNo='" + handleNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", country='" + country + '\'' +
                ", orderType='" + orderType + '\'' +
                ", weight=" + weight +
                ", len=" + len +
                ", high=" + high +
                ", width=" + width +
                ", handleBy=" + handleBy +
                ", handleName='" + handleName + '\'' +
                ", rider='" + rider + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
