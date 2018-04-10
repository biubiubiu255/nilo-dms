package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/19.
 */
public class RiderDeliveryDO extends BaseDo<Long> {

    private Long merchantId;
    private String handleNo;
    private String rider;
    private String carrier;
    private Long handleBy;
    private String handleByName;
    private String riderName;
    private Integer nextStation;
    private Integer handle_time;
    private Integer created_time;
    private Integer updated_time;
    private Integer status;
    private String remark;
    private Double weight;

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

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Long getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(Long handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleByName() {
        return handleByName;
    }

    public void setHandleByName(String handleByName) {
        this.handleByName = handleByName;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public Integer getNextStation() {
        return nextStation;
    }

    public void setNextStation(Integer nextStation) {
        this.nextStation = nextStation;
    }

    public Integer getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(Integer handle_time) {
        this.handle_time = handle_time;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "RiderDeliveryDO{" +
                "merchantId=" + merchantId +
                ", handleNo='" + handleNo + '\'' +
                ", rider='" + rider + '\'' +
                ", carrier='" + carrier + '\'' +
                ", handleBy=" + handleBy +
                ", handleByName='" + handleByName + '\'' +
                ", riderName='" + riderName + '\'' +
                ", nextStation=" + nextStation +
                ", handle_time=" + handle_time +
                ", created_time=" + created_time +
                ", updated_time=" + updated_time +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", weight=" + weight +
                '}';
    }
}
