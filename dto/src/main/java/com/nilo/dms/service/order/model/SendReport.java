package com.nilo.dms.service.order.model;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

public class SendReport {
    private String referenceNo;
    private String nextNetwork;
    private String network;
    private String orderNo;
    private String orderCategory;
    private Double weight;
    private String stop;
    private String carrierName;
    private Double deliveryFee;
    private DeliveryOrderStatusEnum status;
    private String remark;
    private String name;
    private String contactNumber;
    private String address;
    private ReceiverInfo receiverInfo;

    private SenderInfo senderInfo;

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getNextNetwork() {
        return nextNetwork;
    }

    public void setNextNetwork(String nextNetwork) {
        this.nextNetwork = nextNetwork;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public DeliveryOrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DeliveryOrderStatusEnum status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public ReceiverInfo getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(ReceiverInfo receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    public String getStatusDesc() {
        return this.status == null ? "" : this.status.getDesc();
    }

    @Override
    public String toString() {
        return "SendReportDO{" +
                "referenceNo='" + referenceNo + '\'' +
                ", nextNetwork='" + nextNetwork + '\'' +
                ", network='" + network + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderCategory='" + orderCategory + '\'' +
                ", weight=" + weight +
                ", stop='" + stop + '\'' +
                ", carrier_name='" + carrierName + '\'' +
                ", delivery_fee=" + deliveryFee +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", name='" + name + '\'' +
                ", contact_number='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
