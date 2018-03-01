package com.nilo.dms.service.order.model;

import com.nilo.dms.common.BaseDo;
import java.io.Serializable;

public class WaybillPaymentRecord extends BaseDo implements Serializable {
    private Integer id;

    private String paymentOrderId;

    private String thirdPaySn;

    private String paymentChannel;

    private String paymentMethod;

    private String orgTransId;

    private Integer paymentType;

    private Integer status;

    private String remark;

    private Long createdTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId == null ? null : paymentOrderId.trim();
    }

    public String getThirdPaySn() {
        return thirdPaySn;
    }

    public void setThirdPaySn(String thirdPaySn) {
        this.thirdPaySn = thirdPaySn == null ? null : thirdPaySn.trim();
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel == null ? null : paymentChannel.trim();
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod == null ? null : paymentMethod.trim();
    }

    public String getOrgTransId() {
        return orgTransId;
    }

    public void setOrgTransId(String orgTransId) {
        this.orgTransId = orgTransId == null ? null : orgTransId.trim();
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
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
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}