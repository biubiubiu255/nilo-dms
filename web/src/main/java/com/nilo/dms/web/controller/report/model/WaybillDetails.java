package com.nilo.dms.web.controller.report.model;

import com.nilo.dms.dto.order.AbnormalOrder;
import com.nilo.dms.dto.order.Waybill;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/23.
 */
public class WaybillDetails implements Serializable {
    private static final long serialVersionUID = -8295855622681135044L;

    private Waybill deliveryOrder;

    private AbnormalOrder abnormalOrder;

    public Waybill getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(Waybill deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public AbnormalOrder getAbnormalOrder() {
        return abnormalOrder;
    }

    public void setAbnormalOrder(AbnormalOrder abnormalOrder) {
        this.abnormalOrder = abnormalOrder;
    }
}
