package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class SummarizeExpressDO extends BaseDo<Long> {

    private String monthDate;
    private String city;
    private String express;
    private Integer signedNum;
    private Integer fromCreatedTime;
    private Integer toCreatedTime;
    private Integer offset;
    private Integer limit;
    private Integer total;

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public Integer getSignedNum() {
        return signedNum;
    }

    public void setSignedNum(Integer signedNum) {
        this.signedNum = signedNum;
    }

    public Integer getFromCreatedTime() {
        return fromCreatedTime;
    }

    public void setFromCreatedTime(Integer fromCreatedTime) {
        this.fromCreatedTime = fromCreatedTime;
    }

    public Integer getToCreatedTime() {
        return toCreatedTime;
    }

    public void setToCreatedTime(Integer toCreatedTime) {
        this.toCreatedTime = toCreatedTime;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
