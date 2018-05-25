package com.nilo.dms.dto.order;

import java.io.Serializable;

/**
 * Created by admin on 2017/10/18.
 */
public class CreateDeliverOrderMessage implements Serializable{

    private static final long serialVersionUID = 7957743559688705691L;
    private Long requestId;
    private String optBy;
    private String orderNo;
    private String merchantId;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "CreateDeliverOrderMessage{" +
                "requestId=" + requestId +
                ", optBy='" + optBy + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", merchantId='" + merchantId + '\'' +
                '}';
    }
}
