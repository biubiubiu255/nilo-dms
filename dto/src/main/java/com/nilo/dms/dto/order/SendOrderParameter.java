package com.nilo.dms.dto.order;

import java.util.List;

public class SendOrderParameter {
    private String merchantId;
    private String orderNo;
    private String nextNetwork;
    private List<Integer> status;
    private List<String> carrierName;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getNextNetwork() {
        return nextNetwork;
    }

    public void setNextNetwork(String nextNetwork) {
        this.nextNetwork = nextNetwork;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public List<String> getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(List<String> carrierName) {
        this.carrierName = carrierName;
    }
}
