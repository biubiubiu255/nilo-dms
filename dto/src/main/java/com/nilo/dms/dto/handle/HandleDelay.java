package com.nilo.dms.dto.handle;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DelayStatusEnum;

/**
 * Created by ronny on 2017/8/30.
 */
public class HandleDelay extends BaseDo<Long> {

    private Long merchantId;

    private String orderNo;

    private String reason;

    private String reasonId;

    private String handleBy;

    private String handleName;

    private String remark;

    private Integer status;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(String handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getStatusDesc() {
        return DelayStatusEnum.getEnum(status).getDesc();
    }
}
