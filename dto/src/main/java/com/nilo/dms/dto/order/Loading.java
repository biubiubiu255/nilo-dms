package com.nilo.dms.dto.order;

import com.nilo.dms.common.enums.LoadingStatusEnum;
import com.nilo.dms.common.enums.LoadingTypeEnum;
import com.nilo.dms.common.utils.StringUtil;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public class Loading {

    private String merchantId;

    private LoadingTypeEnum loadingType;

    private String loadingNo;

    private String carrier;

    private String rider;

    private String truckType;

    private String truckNo;

    private Long loadingFromTime;

    private Long loadingToTime;

    private String loadingBy;

    private LoadingStatusEnum status;

    private String remark;

    private String nextStation;

    private String riderName;

    private String loadingName;

    public LoadingTypeEnum getLoadingType() {
        if (StringUtil.isNotEmpty(nextStation)) return LoadingTypeEnum.SEND;
        return LoadingTypeEnum.DELIVERY;
    }

    public void setLoadingType(LoadingTypeEnum loadingType) {
        this.loadingType = loadingType;
    }

    public String getLoadingName() {
        return loadingName;
    }

    public void setLoadingName(String loadingName) {
        this.loadingName = loadingName;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    private List<LoadingDetails> detailsList;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getLoadingNo() {
        return loadingNo;
    }

    public void setLoadingNo(String loadingNo) {
        this.loadingNo = loadingNo;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public Long getLoadingFromTime() {
        return loadingFromTime;
    }

    public void setLoadingFromTime(Long loadingFromTime) {
        this.loadingFromTime = loadingFromTime;
    }

    public Long getLoadingToTime() {
        return loadingToTime;
    }

    public void setLoadingToTime(Long loadingToTime) {
        this.loadingToTime = loadingToTime;
    }

    public String getLoadingBy() {
        return loadingBy;
    }

    public void setLoadingBy(String loadingBy) {
        this.loadingBy = loadingBy;
    }

    public LoadingStatusEnum getStatus() {
        return status;
    }

    public void setStatus(LoadingStatusEnum status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<LoadingDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<LoadingDetails> detailsList) {
        this.detailsList = detailsList;
    }

    public String getStatusDesc() {
        return this.status.getDesc();
    }

}
