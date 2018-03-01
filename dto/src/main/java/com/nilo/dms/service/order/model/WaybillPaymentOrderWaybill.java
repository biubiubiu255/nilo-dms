package com.nilo.dms.service.order.model;



import com.nilo.dms.common.BaseDo;
import java.io.Serializable;

public class WaybillPaymentOrderWaybill extends BaseDo implements Serializable {
    private Integer id;

    private String paymentOrderId;

    private String waybillOrderNo;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId == null ? null : paymentOrderId.trim();
    }

    public String getWaybillOrderNo() {
        return waybillOrderNo;
    }

    public void setWaybillOrderNo(String waybillOrderNo) {
        this.waybillOrderNo = waybillOrderNo == null ? null : waybillOrderNo.trim();
    }
}