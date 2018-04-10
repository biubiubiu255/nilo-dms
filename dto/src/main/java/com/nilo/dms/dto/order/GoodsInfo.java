package com.nilo.dms.dto.order;

import com.alibaba.fastjson.annotation.JSONField;

public class GoodsInfo {

    private String goodsId;

    private String goodsDesc;

    private Integer qty;

    private String quality;

    private Double unitPrice;

    public String getGoodsId() {
        return goodsId;
    }

    @JSONField(name = "sku")
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    @JSONField(name = "goods_name")
    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Integer getQty() {
        return qty;
    }

    @JSONField(name = "goods_num")
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    @JSONField(name = "goods_price")
    public void setUnitPrice(Double unitPrice) {
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
                '}';
    }
}
