package com.nilo.dms.dao.dataobject.QO;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

import java.util.List;

public class ReportArriveQO {
    private Long merchantId;
    private String orderNo;
    private String  scanNetwork;
    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer fromCreatedTime;
    private Long toCreatedTime;
    private Integer exportType;
    private Integer status;
    private List<Integer> networks;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getScanNetwork() {
        return scanNetwork;
    }

    public void setScanNetwork(String scanNetwork) {
        this.scanNetwork = scanNetwork;
    }

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

    public Integer getFromCreatedTime() {
        return fromCreatedTime;
    }

    public void setFromCreatedTime(Integer fromCreatedTime) {
        this.fromCreatedTime = fromCreatedTime;
    }

    public Long getToCreatedTime() {
        return toCreatedTime;
    }

    public void setToCreatedTime(Long toCreatedTime) {
        this.toCreatedTime = toCreatedTime;
    }

    public Integer getExportType() {
        return exportType;
    }

    public void setExportType(Integer exportType) {
        this.exportType = exportType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Integer> networks) {
        this.networks = networks;
    }
}
