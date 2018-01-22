package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/19.
 */
public class DeliveryOrderGoodsDO extends BaseDo<Long> {

    private Long merchantId;

    private String orderNo;

    private String goodsId;

    private String goodsDesc;

    private Integer qty;

    private String quality;

    private Long unitPrice;

    private Long totalPrice;

    private Integer status;

    private String userdefine01;

    private String userdefine02;

    private String userdefine03;

    private String userdefine04;

    private String userdefine05;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getUserdefine01() {
        return userdefine01;
    }

    public void setUserdefine01(String userdefine01) {
        this.userdefine01 = userdefine01;
    }

    public String getUserdefine02() {
        return userdefine02;
    }

    public void setUserdefine02(String userdefine02) {
        this.userdefine02 = userdefine02;
    }

    public String getUserdefine03() {
        return userdefine03;
    }

    public void setUserdefine03(String userdefine03) {
        this.userdefine03 = userdefine03;
    }

    public String getUserdefine04() {
        return userdefine04;
    }

    public void setUserdefine04(String userdefine04) {
        this.userdefine04 = userdefine04;
    }

    public String getUserdefine05() {
        return userdefine05;
    }

    public void setUserdefine05(String userdefine05) {
        this.userdefine05 = userdefine05;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
