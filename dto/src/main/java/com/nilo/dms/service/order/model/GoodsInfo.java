package com.nilo.dms.service.order.model;

public class GoodsInfo {

    private String goodsId;

    private String goodsDesc;

    private Integer qty;

    /** 质量状态 */
    private String quality;

    private Long unitPrice;

    private String userdefine01;

    private String userdefine02;

    private String userdefine03;

    private String userdefine04;

    private String userdefine05;

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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "goods_id='" + goodsId + '\'' +
                ", goods_desc='" + goodsDesc + '\'' +
                ", qty=" + qty +
                ", quality='" + quality + '\'' +
                ", unitPrice=" + unitPrice +
                ", userdefine01='" + userdefine01 + '\'' +
                ", userdefine02='" + userdefine02 + '\'' +
                ", userdefine03='" + userdefine03 + '\'' +
                ", userdefine04='" + userdefine04 + '\'' +
                ", userdefine05='" + userdefine05 + '\'' +
                '}';
    }
}
