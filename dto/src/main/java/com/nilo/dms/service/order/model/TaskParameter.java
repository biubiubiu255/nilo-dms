package com.nilo.dms.service.order.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2017/10/26.
 */
public class TaskParameter {

    private String merchantId;

    private List<String> taskType;

    private String handledBy;

    private String orderNo;

    private Long fromTime;

    private Long toTime;

    public Long getFromTime() {
        return fromTime;
    }

    public void setFromTime(Long fromTime) {
        this.fromTime = fromTime;
    }

    public Long getToTime() {
        return toTime;
    }

    public void setToTime(Long toTime) {
        this.toTime = toTime;
    }

    private List<Integer> status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public List<String> getTaskType() {
        return taskType;
    }
    public void setTaskType(List<String> taskType) {
        this.taskType = taskType;
    }
    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

}
