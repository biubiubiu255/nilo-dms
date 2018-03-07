package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;
import com.nilo.dms.common.enums.AbnormalOrderStatusEnum;
import com.nilo.dms.common.enums.AbnormalTypeEnum;

/**
 * Created by admin on 2017/11/9.
 */
public class AbnormalOrder {

    private String orderNo;

    private String abnormalNo;

    private String remark;

    private String merchantId;

    private String createdBy;

    private String image;

    private String reason;

    private String reasonDesc;

    private AbnormalTypeEnum abnormalType;

    private AbnormalOrderStatusEnum status;

    private Long createdTime;

    private Long updatedTime;

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

    public AbnormalTypeEnum getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(AbnormalTypeEnum abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }
}
