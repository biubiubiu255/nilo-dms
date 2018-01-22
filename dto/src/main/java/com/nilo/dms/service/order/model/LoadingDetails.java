package com.nilo.dms.service.order.model;

/**
 * Created by admin on 2017/10/31.
 */
public class LoadingDetails {
    private Integer num;

    private String loadingNo;

    private String orderNo;

    private Long loadingBy;

    private Integer status;

    private DeliveryOrder deliveryOrder;

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getLoadingNo() {
        return loadingNo;
    }

    public void setLoadingNo(String loadingNo) {
        this.loadingNo = loadingNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getLoadingBy() {
        return loadingBy;
    }

    public void setLoadingBy(Long loadingBy) {
        this.loadingBy = loadingBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoadingDetails{" +
                "num=" + num +
                ", loadingNo='" + loadingNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", loadingBy=" + loadingBy +
                ", status=" + status +
                '}';
    }
}
