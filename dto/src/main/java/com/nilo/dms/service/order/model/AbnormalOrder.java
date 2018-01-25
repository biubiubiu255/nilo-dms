package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;
import com.nilo.dms.common.enums.AbnormalOrderStatusEnum;

/**
 * Created by admin on 2017/11/9.
 */
public class AbnormalOrder {

    private String referenceNo;

    private String orderNo;

    private String abnormalNo;

    private String remark;

    private String merchantId;

    private String createdBy;

    private String image;

    private String abnormalType;

    private String abnormalTypeDesc;

    private AbnormalHandleTypeEnum handleType;

    private AbnormalOrderStatusEnum status;

    private Long createdTime;

    private Long updatedTime;

    private String handleBy;

    private String handleRemark;

    private Long handleTime;

    public Long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(String handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
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

    public AbnormalOrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AbnormalOrderStatusEnum status) {
        this.status = status;
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

    public String getAbnormalNo() {
        return abnormalNo;
    }

    public void setAbnormalNo(String abnormalNo) {
        this.abnormalNo = abnormalNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(String abnormalType) {
        this.abnormalType = abnormalType;
    }

    public AbnormalHandleTypeEnum getHandleType() {
        return handleType;
    }

    public void setHandleType(AbnormalHandleTypeEnum handleType) {
        this.handleType = handleType;
    }

    public String getHandleTypeDesc() {
        return this.handleType.getDesc();
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }

    public String getAbnormalTypeDesc() {
        return abnormalTypeDesc;
    }

    public void setAbnormalTypeDesc(String abnormalTypeDesc) {
        this.abnormalTypeDesc = abnormalTypeDesc;
    }
}
