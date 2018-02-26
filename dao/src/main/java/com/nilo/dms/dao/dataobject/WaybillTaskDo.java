package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class WaybillTaskDo  extends BaseDo<Long> {
    private String orderNo;
    private String isCOD;
    private Double needPayAmount;
    private Double alreadyPaid;
    private String handledBy;
    private Long handledTime;
    private Integer status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIsCOD() {
        return isCOD;
    }

    public void setIsCOD(String isCOD) {
        this.isCOD = isCOD;
    }

    public Double getNeedPayAmount() {
        return needPayAmount;
    }

    public void setNeedPayAmount(Double needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public Double getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(Double alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public Long getHandledTime() {
        return handledTime;
    }

    public void setHandledTime(Long handledTime) {
        this.handledTime = handledTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WaybillTaskDo{" +
                "orderNo='" + orderNo + '\'' +
                ", idCOD='" + isCOD + '\'' +
                ", needPayAmount=" + needPayAmount +
                ", alreadyPaid=" + alreadyPaid +
                ", handledBy='" + handledBy + '\'' +
                ", handledTime=" + handledTime +
                ", status=" + status +
                '}';
    }
}
