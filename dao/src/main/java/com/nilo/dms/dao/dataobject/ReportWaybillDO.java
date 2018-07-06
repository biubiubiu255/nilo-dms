package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class ReportWaybillDO{

    private Integer id;

    private String orderNo;

    private String orderType;

    private String referenceNo;

    private Long orderTime;

    private Integer orderStatus;

    private Integer orderCreatedTime;

    private Integer firstArriveTime;

    private Integer firstArriveNetworkId;

    private String firstArriveNetworkName;

    private Integer lastDeliverTime;

    private Integer lastDeliverNetworkId;

    private String lastDeliverNetworkName;

    private String lastDeliverExpressCode;

    private Integer signTime;

    private Long signHandleById;

    private String signHandleByName;

    private Integer signNetworkId;

    private String signNetworkName;

    private Integer createdTime;

    private String signOutsourceName;

    private String signOutsourceCode;

    private Long lastDeliverRiderId;

    private String lastDeliverRiderName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo == null ? null : referenceNo.trim();
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatusDesc(){
        DeliveryOrderStatusEnum statusEnum = DeliveryOrderStatusEnum.getEnum(this.orderStatus.intValue());
        return statusEnum == null ? "" : statusEnum.getDesc();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderCreatedTime() {
        return orderCreatedTime;
    }

    public void setOrderCreatedTime(Integer orderCreatedTime) {
        this.orderCreatedTime = orderCreatedTime;
    }

    public Integer getFirstArriveTime() {
        return firstArriveTime;
    }

    public void setFirstArriveTime(Integer firstArriveTime) {
        this.firstArriveTime = firstArriveTime;
    }

    public Integer getFirstArriveNetworkId() {
        return firstArriveNetworkId;
    }

    public void setFirstArriveNetworkId(Integer firstArriveNetworkId) {
        this.firstArriveNetworkId = firstArriveNetworkId;
    }

    public String getFirstArriveNetworkName() {
        return firstArriveNetworkName;
    }

    public void setFirstArriveNetworkName(String firstArriveNetworkName) {
        this.firstArriveNetworkName = firstArriveNetworkName == null ? null : firstArriveNetworkName.trim();
    }

    public Integer getLastDeliverTime() {
        return lastDeliverTime;
    }

    public void setLastDeliverTime(Integer lastDeliverTime) {
        this.lastDeliverTime = lastDeliverTime;
    }

    public Integer getLastDeliverNetworkId() {
        return lastDeliverNetworkId;
    }

    public void setLastDeliverNetworkId(Integer lastDeliverNetworkId) {
        this.lastDeliverNetworkId = lastDeliverNetworkId;
    }

    public String getLastDeliverNetworkName() {
        return lastDeliverNetworkName;
    }

    public void setLastDeliverNetworkName(String lastDeliverNetworkName) {
        this.lastDeliverNetworkName = lastDeliverNetworkName == null ? null : lastDeliverNetworkName.trim();
    }

    public String getLastDeliverExpressCode() {
        return lastDeliverExpressCode;
    }

    public void setLastDeliverExpressCode(String lastDeliverExpressCode) {
        this.lastDeliverExpressCode = lastDeliverExpressCode == null ? null : lastDeliverExpressCode.trim();
    }

    public Integer getSignTime() {
        return signTime;
    }

    public void setSignTime(Integer signTime) {
        this.signTime = signTime;
    }

    public Long getSignHandleById() {
        return signHandleById;
    }

    public void setSignHandleById(Long signHandleById) {
        this.signHandleById = signHandleById;
    }

    public String getSignHandleByName() {
        return signHandleByName;
    }

    public void setSignHandleByName(String signHandleByName) {
        this.signHandleByName = signHandleByName == null ? null : signHandleByName.trim();
    }

    public Integer getSignNetworkId() {
        return signNetworkId;
    }

    public void setSignNetworkId(Integer signNetworkId) {
        this.signNetworkId = signNetworkId;
    }

    public String getSignNetworkName() {
        return signNetworkName;
    }

    public void setSignNetworkName(String signNetworkName) {
        this.signNetworkName = signNetworkName == null ? null : signNetworkName.trim();
    }

    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    public String getSignOutsourceName() {
        return signOutsourceName;
    }

    public void setSignOutsourceName(String signOutsourceName) {
        this.signOutsourceName = signOutsourceName == null ? null : signOutsourceName.trim();
    }

    public String getSignOutsourceCode() {
        return signOutsourceCode;
    }

    public void setSignOutsourceCode(String signOutsourceCode) {
        this.signOutsourceCode = signOutsourceCode == null ? null : signOutsourceCode.trim();
    }

    public Long getLastDeliverRiderId() {
        return lastDeliverRiderId;
    }

    public void setLastDeliverRiderId(Long lastDeliverRiderId) {
        this.lastDeliverRiderId = lastDeliverRiderId;
    }

    public String getLastDeliverRiderName() {
        return lastDeliverRiderName;
    }

    public void setLastDeliverRiderName(String lastDeliverRiderName) {
        this.lastDeliverRiderName = lastDeliverRiderName == null ? null : lastDeliverRiderName.trim();
    }

}
