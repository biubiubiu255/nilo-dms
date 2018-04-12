package com.nilo.dms.dto.order;


import com.alibaba.fastjson.annotation.JSONField;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.PaidTypeEnum;
import com.nilo.dms.dto.util.Excel;

/**
 * Created by ronny on 2017/9/15.
 */
public class WaybillHeader {

    @Excel(name = "OrderNo", order = 1)
    private String orderNo;
    @Excel(name = "Type", order = 2)
    private String orderType;
    private String orderTypeDesc;
    private String referenceNo;
    private Long orderTime;
    private String createdBy;
    private Long createdTime;
    private Long updatedTime;
    private String country;
    private String merchantId;
    private String orderPlatform;
    private Double totalPrice;
    private DeliveryOrderStatusEnum status;
    private Double weight;
    private String goodsType;
    private String warehouseId;
    private String stopId;
    private String stop;
    private String channel;
    private String channelStation;
    private String orderCategory;
    private String carrierId;
    private String carrierName;
    private String relationOrderNo;
    private Double deliveryFee;
    private String isCod;
    private String notes;
    private String remark;

    private Double height;
    private Double len;
    private Double width;
    private boolean isPackage;
    private String parentNo;
    private Integer networkId;
    private String networkDesc;
    private Integer nextNetworkId;
    private String nextNetworkDesc;

    private Double needPayAmount;
    private Double alreadyPaid;
    private Double billNo;
    private Double accountNo;
    private Integer printTimes;

    private String allocatedRider;
    private PaidTypeEnum paidType;


    public PaidTypeEnum getPaidType() {
        return paidType;
    }

    public void setPaidType(PaidTypeEnum paidType) {
        this.paidType = paidType;
    }

    public String getAllocatedRider() {
        return allocatedRider;
    }

    public void setAllocatedRider(String allocatedRider) {
        this.allocatedRider = allocatedRider;
    }

    public Double getNeedPayAmount() {
        return needPayAmount == null ? 0d : needPayAmount;
    }

    @JSONField(name = "need_pay_amount")
    public void setNeedPayAmount(Double needPayAmount) {
        this.needPayAmount = needPayAmount;
    }

    public Double getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(Double alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public Double getBillNo() {
        return billNo;
    }

    public void setBillNo(Double billNo) {
        this.billNo = billNo;
    }

    public Double getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Double accountNo) {
        this.accountNo = accountNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getNetworkDesc() {
        return networkDesc;
    }

    public void setNetworkDesc(String networkDesc) {
        this.networkDesc = networkDesc;
    }

    public String getNextNetworkDesc() {
        return nextNetworkDesc;
    }

    public void setNextNetworkDesc(String nextNetworkDesc) {
        this.nextNetworkDesc = nextNetworkDesc;
    }

    public boolean isPrinted() {
        return printTimes == null ? false : true;
    }

    public Integer getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(Integer printTimes) {
        this.printTimes = printTimes;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    public Integer getNextNetworkId() {
        return nextNetworkId;
    }

    public void setNextNetworkId(Integer nextNetworkId) {
        this.nextNetworkId = nextNetworkId;
    }

    public boolean isPackage() {
        return isPackage;
    }

    public void setPackage(boolean aPackage) {
        isPackage = aPackage;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public Double getHeight() {
        return height;
    }

    @JSONField(name = "height")
    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLen() {
        return len;
    }

    @JSONField(name = "lenth")
    public void setLen(Double len) {
        this.len = len;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    @JSONField(name = "warehouse_id")
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }


    public String getChannel() {
        return channel;
    }

    @JSONField(name = "delivery_type")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelStation() {
        return channelStation;
    }

    public void setChannelStation(String channelStation) {
        this.channelStation = channelStation;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    @JSONField(name = "carrier_name")
    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getRelationOrderNo() {
        return relationOrderNo;
    }

    public void setRelationOrderNo(String relationOrderNo) {
        this.relationOrderNo = relationOrderNo;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    @JSONField(name = "shipping_fee")
    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }


    public String getIsCod() {
        return isCod;
    }

    @JSONField(name = "is_pod")
    public void setIsCod(String isCod) {
        this.isCod = isCod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    @JSONField(name = "weight")
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGoodsType() {
        return goodsType;
    }

    @JSONField(name = "goods_type_id")
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public DeliveryOrderStatusEnum getStatus() {
        return status;
    }

    @JSONField(name = "order_status")
    public void setStatus(DeliveryOrderStatusEnum status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    @JSONField(name = "order_amount")
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    @JSONField(name = "waybill_number")
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    @JSONField(name = "logistics_type")
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public String getReferenceNo() {
        return referenceNo;
    }

    @JSONField(name = "client_order_sn")
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    @JSONField(name = "add_time")
    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderPlatform() {
        return orderPlatform;
    }

    @JSONField(name = "client_name")
    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getStatusDesc() {
        return this.status == null ? "" : this.status.getDesc();
    }

}
