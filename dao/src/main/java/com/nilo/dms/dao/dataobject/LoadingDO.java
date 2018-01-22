package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by ronny on 2017/8/30.
 */
public class LoadingDO extends BaseDo<Long> {

    private Long merchantId;

    private String loadingNo;

    private String carrier;

    private String rider;

    private String truckType;

    private String truckNo;

    private Long loadingFromTime;

    private Long loadingToTime;

    private Long loadingBy;

    private Integer status;

    private String remark;

    private String nextStation;

    private String userdefine01;

    private String userdefine02;

    private String userdefine03;

    private String userdefine04;

    private String userdefine05;

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserdefine01() {
        return userdefine01;
    }

    public void setUserdefine01(String userdefine01) {
        this.userdefine01 = userdefine01;
    }

    public String getUserdefine02() {
        return userdefine02;
    }

    public void setUserdefine02(String userdefine02) {
        this.userdefine02 = userdefine02;
    }

    public String getUserdefine03() {
        return userdefine03;
    }

    public void setUserdefine03(String userdefine03) {
        this.userdefine03 = userdefine03;
    }

    public String getUserdefine04() {
        return userdefine04;
    }

    public void setUserdefine04(String userdefine04) {
        this.userdefine04 = userdefine04;
    }

    public String getUserdefine05() {
        return userdefine05;
    }

    public void setUserdefine05(String userdefine05) {
        this.userdefine05 = userdefine05;
    }
}
