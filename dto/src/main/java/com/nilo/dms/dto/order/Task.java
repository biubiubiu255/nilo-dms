package com.nilo.dms.dto.order;

import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;

/**
 * Created by admin on 2017/10/23.
 */
public class Task {

    private String taskId;

    private TaskTypeEnum taskType;

    private String merchantId;

    private String orderNo;

    private TaskStatusEnum status;

    private String createdBy;

    private String handledBy;

    private String createdName;

    private String handledName;

    private String remark;

    private Long createdTime;

    private Long handledTime;

    private String taskDesc;

    private Integer taskRate;

    private Waybill waybill;

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getHandledName() {
        return handledName;
    }

    public void setHandledName(String handledName) {
        this.handledName = handledName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Integer getTaskRate() {
        return taskRate;
    }

    public void setTaskRate(Integer taskRate) {
        this.taskRate = taskRate;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
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

    public Long getHandledTime() {
        return handledTime;
    }

    public void setHandledTime(Long handledTime) {
        this.handledTime = handledTime;
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }
}
