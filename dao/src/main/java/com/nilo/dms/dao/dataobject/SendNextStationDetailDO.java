package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/19.
 */
public class SendNextStationDetailDO extends BaseDo<Long> {

    private Long merchantId;
    private String third_handle_no;
    private String order_no;
    private Double weight;
    private Double len;
    private Double height;
    private Double width;
    private Integer status;
    private Integer created_time;
    private Integer updated_time;
    private String version;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getThird_handle_no() {
        return third_handle_no;
    }

    public void setThird_handle_no(String third_handle_no) {
        this.third_handle_no = third_handle_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLen() {
        return len;
    }

    public void setLen(Double len) {
        this.len = len;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
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

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SendNextStationDetailDO{" +
                "merchantId=" + merchantId +
                ", third_handle_no='" + third_handle_no + '\'' +
                ", order_no='" + order_no + '\'' +
                ", weight=" + weight +
                ", len=" + len +
                ", height=" + height +
                ", width=" + width +
                ", status=" + status +
                ", created_time=" + created_time +
                ", updated_time=" + updated_time +
                ", version='" + version + '\'' +
                '}';
    }
}
