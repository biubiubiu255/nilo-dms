package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * User: Alvin
 * Date: 2018/06/07 16:46
 * Just go for it and give it a try
 */
public class WaybillDeliveryDeatilDO extends BaseDo<Integer> {
    private Long merchantId;
    private String handleNo;
    private String orderNo;

    private Double weight;
    private Double len;
    private Double high;
    private Double width;

    private String rider;
    private String driver;
    private String network;
    private String nextStation;
    private String expressName;
    private Integer handleBy;
    private Integer handleTime;
    private String  parentNo;
    private Integer status;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getHandleNo() {
        return handleNo;
    }

    public void setHandleNo(String handleNo) {
        this.handleNo = handleNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLen() {
        return len;
    }

    public void setLen(Double len) {
        this.len = len;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public Integer getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Integer handleBy) {
        this.handleBy = handleBy;
    }

    public Integer getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Integer handleTime) {
        this.handleTime = handleTime;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WaybillDeliveryDeatilDO{" +
                "merchantId=" + merchantId +
                ", handleNo='" + handleNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", weight=" + weight +
                ", len=" + len +
                ", high=" + high +
                ", width=" + width +
                ", rider='" + rider + '\'' +
                ", driver='" + driver + '\'' +
                ", network='" + network + '\'' +
                ", nextStation='" + nextStation + '\'' +
                ", expressName='" + expressName + '\'' +
                ", handleBy=" + handleBy +
                ", handleTime=" + handleTime +
                ", parentNo='" + parentNo + '\'' +
                ", status=" + status +
                '}';
    }
}
