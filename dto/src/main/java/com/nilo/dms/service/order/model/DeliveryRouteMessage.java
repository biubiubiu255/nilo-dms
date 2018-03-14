package com.nilo.dms.service.order.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/10/10.
 */
public class DeliveryRouteMessage {

    private String merchantId;

    private String[] orderNo;

    private String optType;

    private String optBy;

    private String rider;

    private String networkId;

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String[] getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String[] orderNo) {
        this.orderNo = orderNo;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getOptBy() {
        return optBy;
    }

    public void setOptBy(String optBy) {
        this.optBy = optBy;
    }

    @Override
    public String toString() {
        return "DeliveryRouteMessage{" +
                "merchantId='" + merchantId + '\'' +
                ", orderNo=" + Arrays.toString(orderNo) +
                ", optType='" + optType + '\'' +
                ", optBy='" + optBy + '\'' +
                ", rider='" + rider + '\'' +
                ", networkId='" + networkId + '\'' +
                '}';
    }
}
