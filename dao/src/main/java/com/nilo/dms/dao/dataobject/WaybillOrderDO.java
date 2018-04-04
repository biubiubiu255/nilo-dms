package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import org.apache.commons.lang3.math.NumberUtils;

public class WaybillOrderDO {
    private Long merchantId;
    private String orderNo;
    private Integer next_network_id;
    private Integer network_id;
    private String  order_type;
    private String  reference_no;
    private String  country;
    private String  order_platform;
    private String  channel;
    private String  goods_type;
    private String  is_cod;
    private String  parent_no;
    private String  created_by;
    private String  notes;
    private Integer status;
    private Integer created_time;
    private Integer updated_time;
    private Double  weight;

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

    public Integer getNext_network_id() {
        return next_network_id;
    }

    public void setNext_network_id(Integer next_network_id) {
        this.next_network_id = next_network_id;
    }

    public Integer getNetwork_id() {
        return network_id;
    }

    public void setNetwork_id(Integer network_id) {
        this.network_id = network_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrder_platform() {
        return order_platform;
    }

    public void setOrder_platform(String order_platform) {
        this.order_platform = order_platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getIs_cod() {
        return is_cod;
    }

    public void setIs_cod(String is_cod) {
        this.is_cod = is_cod;
    }

    public String getParent_no() {
        return parent_no;
    }

    public void setParent_no(String parent_no) {
        this.parent_no = parent_no;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Integer created_time) {
        this.created_time = created_time;
    }

    public Integer getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Integer updated_time) {
        this.updated_time = updated_time;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WaybillOrderDO{" +
                "merchantId=" + merchantId +
                ", orderNo='" + orderNo + '\'' +
                ", next_network_id=" + next_network_id +
                ", network_id=" + network_id +
                ", order_type='" + order_type + '\'' +
                ", reference_no='" + reference_no + '\'' +
                ", country='" + country + '\'' +
                ", order_platform='" + order_platform + '\'' +
                ", channel='" + channel + '\'' +
                ", goods_type='" + goods_type + '\'' +
                ", is_cod='" + is_cod + '\'' +
                ", parent_no='" + parent_no + '\'' +
                ", created_by='" + created_by + '\'' +
                ", notes='" + notes + '\'' +
                ", status=" + status +
                ", created_time=" + created_time +
                ", updated_time=" + updated_time +
                ", weight=" + weight +
                '}';
    }
}
