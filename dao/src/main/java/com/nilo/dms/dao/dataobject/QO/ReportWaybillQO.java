package com.nilo.dms.dao.dataobject.QO;

import com.nilo.dms.dao.dataobject.ReportWaybillDO;

import java.util.List;

public class ReportWaybillQO extends ReportWaybillDO {

    private Integer offset;
    private Integer limit;
    private Integer total;

    private Integer fromOrderTime;
    private Integer toOrderTime;

    private Integer fromOrderCreatedTime;
    private Integer toOrderCreatedTime;

    private Integer fromFirstArriveTime;
    private Integer toFirstArriveTime;

    private Integer fromLastDeliverTime;
    private Integer toLastDeliverTime;

    private Integer fromSignTime;
    private Integer toSignTime;
    private Integer fromCreatedTime;
    private Integer toCreatedTime;
    private Integer exportType;
    private List<Integer> networks;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFromOrderTime() {
        return fromOrderTime;
    }

    public void setFromOrderTime(Integer fromOrderTime) {
        this.fromOrderTime = fromOrderTime;
    }

    public Integer getToOrderTime() {
        return toOrderTime;
    }

    public void setToOrderTime(Integer toOrderTime) {
        this.toOrderTime = toOrderTime;
    }

    public Integer getFromOrderCreatedTime() {
        return fromOrderCreatedTime;
    }

    public void setFromOrderCreatedTime(Integer fromOrderCreatedTime) {
        this.fromOrderCreatedTime = fromOrderCreatedTime;
    }

    public Integer getToOrderCreatedTime() {
        return toOrderCreatedTime;
    }

    public void setToOrderCreatedTime(Integer toOrderCreatedTime) {
        this.toOrderCreatedTime = toOrderCreatedTime;
    }

    public Integer getFromFirstArriveTime() {
        return fromFirstArriveTime;
    }

    public void setFromFirstArriveTime(Integer fromFirstArriveTime) {
        this.fromFirstArriveTime = fromFirstArriveTime;
    }

    public Integer getToFirstArriveTime() {
        return toFirstArriveTime;
    }

    public void setToFirstArriveTime(Integer toFirstArriveTime) {
        this.toFirstArriveTime = toFirstArriveTime;
    }

    public Integer getFromLastDeliverTime() {
        return fromLastDeliverTime;
    }

    public void setFromLastDeliverTime(Integer fromLastDeliverTime) {
        this.fromLastDeliverTime = fromLastDeliverTime;
    }

    public Integer getToLastDeliverTime() {
        return toLastDeliverTime;
    }

    public void setToLastDeliverTime(Integer toLastDeliverTime) {
        this.toLastDeliverTime = toLastDeliverTime;
    }

    public Integer getFromSignTime() {
        return fromSignTime;
    }

    public void setFromSignTime(Integer fromSignTime) {
        this.fromSignTime = fromSignTime;
    }

    public Integer getToSignTime() {
        return toSignTime;
    }

    public void setToSignTime(Integer toSignTime) {
        this.toSignTime = toSignTime;
    }

    public Integer getFromCreatedTime() {
        return fromCreatedTime;
    }

    public void setFromCreatedTime(Integer fromCreatedTime) {
        this.fromCreatedTime = fromCreatedTime;
    }

    public Integer getToCreatedTime() {
        return toCreatedTime;
    }

    public void setToCreatedTime(Integer toCreatedTime) {
        this.toCreatedTime = toCreatedTime;
    }

    public Integer getExportType() {
        return exportType;
    }

    public void setExportType(Integer exportType) {
        this.exportType = exportType;
    }

    public List<Integer> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Integer> networks) {
        this.networks = networks;
    }
}
