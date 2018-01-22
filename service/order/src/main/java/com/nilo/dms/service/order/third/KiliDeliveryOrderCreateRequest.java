package com.nilo.dms.service.order.third;

import java.util.List;

/**
 * Created by admin on 2017/12/18.
 */
@Message(message = MessageProtocol.XML, xmlMapping = "oxm/kili_delivery_create.xml")
public class KiliDeliveryOrderCreateRequest {

    private String orderNo;

    private String orderPlatform;

    private List<KiliGoodsInfo> goodsList;

    private KiliSender sender;

    private KiliReceiver receiver;

    public static class KiliGoodsInfo{
        private String skuId;

        private String desc;

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class KiliSender{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class KiliReceiver{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
