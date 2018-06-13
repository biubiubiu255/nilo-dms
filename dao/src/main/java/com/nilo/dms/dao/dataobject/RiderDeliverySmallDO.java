package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/19.
 */
public class RiderDeliverySmallDO extends BaseDo<Long> {

    private Long merchantId;
    private String handleNo;
    private String orderNo;
    private Double weight;
    private Double length;
    private Double height;
    private Double width;
    private Long handleBy;
    private String handleByName;
    private Integer status;
    private Integer created_time;
    private Integer updated_time;
    private String version;

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

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }



    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Integer created_time) {
        this.created_time = created_time;
    }

    public Integer getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Integer updated_time) {
        this.updated_time = updated_time;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getHandleByName() {
        return handleByName;
    }

    public void setHandleByName(String handleByName) {
        this.handleByName = handleByName;
    }

    @Override
    public String toString() {
        return "RiderDeliverySmallDO{" +
                "merchantId=" + merchantId +
                ", handleNo='" + handleNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", weight=" + weight +
                ", length=" + length +
                ", width=" + width +
                ", handleBy=" + handleBy +
                ", status=" + status +
                ", created_time=" + created_time +
                ", updated_time=" + updated_time +
                ", version='" + version + '\'' +
                '}';
    }
}
