package com.nilo.dms.dao.dataobject.QO;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class ReportRepeatQO extends BaseDo<Long> {
    private Long    merchantId;
    private String  handleNo;
    private String  orderNo;
    private Integer dispatchNum;
    private String  dispatchType;
    private String  handleName;
    private String  expressName;
    private String  referenceNo;
    private String  orderType;
    private Double  weight;
    private String  nextStation;
    private Integer nextStationCode;
    private String  phone;
    private String  address;
    private String  receiveName;
    private String  rider;
    private String  parentNo;
    private Integer status;
    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer fromCreatedTime;
    private Integer toCreatedTime;
    private Integer exportType;

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

    public Integer getDispatchNum() {
        return dispatchNum;
    }

    public void setDispatchNum(Integer dispatchNum) {
        this.dispatchNum = dispatchNum;
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
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

    public String getStatusDesc() {
        DeliveryOrderStatusEnum statusEnum = DeliveryOrderStatusEnum.getEnum(this.status.intValue());
        return statusEnum == null ? "" : statusEnum.getDesc();
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

    public Integer getNextStationCode() {
        return nextStationCode;
    }

    public void setNextStationCode(Integer nextStationCode) {
        this.nextStationCode = nextStationCode;
    }
}
