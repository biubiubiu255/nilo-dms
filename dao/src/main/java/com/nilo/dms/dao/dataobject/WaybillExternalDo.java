package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class WaybillExternalDo extends BaseDo<Long> {
    private String orderNo;
    private String type;
    private Double weight;
    private String clientName;
    private String creator;
    private int createTime;
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

    public void getCreateTime(int create_time) {
        this.createTime = create_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    @Override
    public String toString() {
        return "WaybillForeignDo{" +
                "orderNo='" + orderNo + '\'' +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", clientName='" + clientName + '\'' +
                ", creator='" + creator + '\'' +
                ", create_time=" + createTime +
                ", notes='" + notes + '\'' +
                '}';
    }


}
