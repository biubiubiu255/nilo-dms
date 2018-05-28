package com.nilo.dms.dto.handle;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/19.
 */
public class SendThirdDetail extends BaseDo<Long> {

    private Long merchantId;
    private String thirdHandleNo;
    private String orderNo;
    private Double weight;
    private Double len;
    private Double height;
    private Double width;
    private Integer status;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getThirdHandleNo() {
        return thirdHandleNo;
    }

    public void setThirdHandleNo(String thirdHandleNo) {
        this.thirdHandleNo = thirdHandleNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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



    @Override
    public String toString() {
        return "SendThirdDetail{" +
                "merchantId=" + merchantId +
                ", thirdHandleNo='" + thirdHandleNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", weight=" + weight +
                ", len=" + len +
                ", height=" + height +
                ", width=" + width +
                ", status=" + status +
                '}';
    }
}
