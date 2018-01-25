package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/12/13.
 */
public class WaybillScanDetailsDO extends BaseDo<Long> {

    private String scanNo;

    private String orderNo;

    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getScanNo() {
        return scanNo;
    }

    public void setScanNo(String scanNo) {
        this.scanNo = scanNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
