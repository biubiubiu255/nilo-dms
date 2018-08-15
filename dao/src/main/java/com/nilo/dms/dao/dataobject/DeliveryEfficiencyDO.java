package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class DeliveryEfficiencyDO extends BaseDo<Long> {

    private String  dateFormat;
    private Integer deliveryCount;
    private Integer durationDays;
    private Integer noSign;
    private Integer fromTime;
    private Integer toTime;
    private String  expressCode;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(Integer deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public Integer getNoSign() {
        return noSign;
    }

    public void setNoSign(Integer noSign) {
        this.noSign = noSign;
    }

    public Integer getFromTime() {
        return fromTime;
    }

    public void setFromTime(Integer fromTime) {
        this.fromTime = fromTime;
    }

    public Integer getToTime() {
        return toTime;
    }

    public void setToTime(Integer toTime) {
        this.toTime = toTime;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }
}
