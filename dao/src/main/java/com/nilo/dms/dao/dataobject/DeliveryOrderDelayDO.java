package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DelayStatusEnum;

/**
 * Created by ronny on 2017/8/30.
 */
public class DeliveryOrderDelayDO extends BaseDo<Long> {

    private Long merchantId;

    private String orderNo;

    private String delayReason;

    private String remark;

    private Integer allowTimes;

    private Integer delayTimes;

    private Integer status;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    public Integer getStatus() {
        return status;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getAllowTimes() {
        return allowTimes;
    }

    public void setAllowTimes(Integer allowTimes) {
        this.allowTimes = allowTimes;
    }

    public Integer getDelayTimes() {
        return delayTimes;
    }

    public void setDelayTimes(Integer delayTimes) {
        this.delayTimes = delayTimes;
    }

    public String getStatusDesc() {
        return DelayStatusEnum.getEnum(status).getDesc();
    }
}
