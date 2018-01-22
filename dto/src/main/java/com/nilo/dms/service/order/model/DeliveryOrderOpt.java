package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.service.model.User;

/**
 * Created by admin on 2017/9/19.
 */
public class DeliveryOrderOpt {

    private String id;

    private String orderNo;

    private String merchantId;

    private String opt;

    private String optBy;

    private Integer fromStatus;

    private Integer toStatus;

    private String optName;

    private Long createdTime;

    private String remark;

    private DeliveryOrder deliveryOrder;

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getFromStatusDesc() {
        return DeliveryOrderStatusEnum.getEnum(fromStatus).getDesc();
    }

    public String getToStatusDesc() {
        return DeliveryOrderStatusEnum.getEnum(toStatus).getDesc();
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }

    public Integer getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(Integer fromStatus) {
        this.fromStatus = fromStatus;
    }

    public Integer getToStatus() {
        return toStatus;
    }

    public void setToStatus(Integer toStatus) {
        this.toStatus = toStatus;
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
}
