package com.nilo.dms.web.controller.report.model;

import com.nilo.dms.service.order.model.AbnormalOrder;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.DeliveryOrderOpt;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/11/23.
 */
public class DeliveryOrderDetails implements Serializable {
    private static final long serialVersionUID = -8295855622681135044L;

    private DeliveryOrder deliveryOrder;

    private AbnormalOrder abnormalOrder;

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public AbnormalOrder getAbnormalOrder() {
        return abnormalOrder;
    }

    public void setAbnormalOrder(AbnormalOrder abnormalOrder) {
        this.abnormalOrder = abnormalOrder;
    }
}
