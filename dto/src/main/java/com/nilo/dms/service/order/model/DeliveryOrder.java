package com.nilo.dms.service.order.model;


import com.alibaba.fastjson.annotation.JSONField;
import com.nilo.dms.common.enums.*;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public class DeliveryOrder {

    private String orderNo;

    private String orderType;

    private String orderTypeDesc;

    private String referenceNo;

    private Long orderTime;

    private String createdBy;
    private Long createdTime;

    private Long updatedTime;

    private String country;

    private String merchantId;

    private ServiceTypeEnum serviceType;

    private String orderPlatform;

    private Double totalPrice;

    private ReceiverInfo receiverInfo;

    private SenderInfo senderInfo;

    private List<GoodsInfo> goodsInfoList;

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

    private Double high;
    private Double length;
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

    public String getAllocatedRider() {
        return allocatedRider;
    }

    public void setAllocatedRider(String allocatedRider) {
        this.allocatedRider = allocatedRider;
    }

    public Double getNeedPayAmount() {
        return needPayAmount;
    }

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
        return printTimes==null?false:true;
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

    public Double getHigh() {
        return high;
    }
    @JSONField(name = "height")
    public void setHigh(Double high) {
        this.high = high;
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
    @JSONField(name = "is_pickup")
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

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
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

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }
    @JSONField(name = "sender_info")
    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
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
    @JSONField(name = "order_platform")
    public void setOrderPlatform(String orderPlatform) {
        this.orderPlatform = orderPlatform;
    }

    public ReceiverInfo getReceiverInfo() {
        return receiverInfo;
    }
    @JSONField(name = "receiver_info")
    public void setReceiverInfo(ReceiverInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }


    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }
    @JSONField(name = "order_items_list")
    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
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

    public String getServiceTypeDesc() {
        return this.serviceType == null ? "" : this.serviceType.getDesc();
    }

}
