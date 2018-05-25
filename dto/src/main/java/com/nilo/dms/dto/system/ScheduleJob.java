package com.nilo.dms.dto.system;

import com.nilo.dms.common.enums.ScheduleStatusEnum;

/**
 * Created by admin on 2017/12/1.
 */
public class ScheduleJob {

    private String merchantId;

    private String jobName;

    private String className;

    private String cornExpression;

    private ScheduleStatusEnum status;

    private String remark;

    private Long createdTime;

    private Long updatedTime;

    private String version;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCornExpression() {
        return cornExpression;
    }

    public void setCornExpression(String cornExpression) {
        this.cornExpression = cornExpression;
    }

    public ScheduleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatusEnum status) {
        this.status = status;
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

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
