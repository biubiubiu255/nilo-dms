package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;

/**
 * Created by admin on 2017/12/13.
 */
public class AbnormalParam {

    private AbnormalOrder abnormalOrder;

    private String optBy;

    public AbnormalOrder getAbnormalOrder() {
        return abnormalOrder;
    }

    public void setAbnormalOrder(AbnormalOrder abnormalOrder) {
        this.abnormalOrder = abnormalOrder;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }

}
