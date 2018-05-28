package com.nilo.dms.dto.handle;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class HandleRefuse extends BaseDo<Long> {

    private Long merchantId;

    private String orderNo;

    private String reason;

    private String nextWork;

    private Integer networkId;

    private String handleName;

    private Long handleBy;

    private String remark;

    private Integer status;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNextWork() {
        return nextWork;
    }

    public void setNextWork(String nextWork) {
        this.nextWork = nextWork;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
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

    @Override
    public String toString() {
        return "HandleRefuse{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", reason='" + reason + '\'' +
                ", nextWork='" + nextWork + '\'' +
                ", networkId=" + networkId +
                ", handleName='" + handleName + '\'' +
                ", handleBy=" + handleBy +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                '}';
    }
}
