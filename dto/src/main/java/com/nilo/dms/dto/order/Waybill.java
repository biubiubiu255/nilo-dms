package com.nilo.dms.dto.order;


import com.alibaba.fastjson.annotation.JSONField;
import com.nilo.dms.dto.util.Excel;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public class Waybill extends WaybillHeader {

    @Excel(subType = true)
    private ReceiverInfo receiverInfo;

    private SenderInfo senderInfo;

    private List<GoodsInfo> goodsInfoList;

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

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    @JSONField(name = "sender_info")
    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
    }
}
