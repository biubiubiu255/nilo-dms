package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class WaybillExternalDo extends BaseDo<Long> {
    private String orderNo;
    private String type;
    private String channels;
    private Double weight;
    private String clientName;
    private String creator;
    private int createTime;
    private int receiveTime;
    private String notes;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(int receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "WaybillExternalDo{" +
                "orderNo='" + orderNo + '\'' +
                ", type='" + type + '\'' +
                ", channels='" + channels + '\'' +
                ", weight=" + weight +
                ", clientName='" + clientName + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", receiveTime=" + receiveTime +
                ", notes='" + notes + '\'' +
                '}';
    }
}
