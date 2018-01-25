package com.nilo.dms.web.controller;

import com.alibaba.fastjson.JSON;
import com.ctc.wstx.util.DataUtil;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.GoodsInfo;
import com.nilo.dms.service.order.model.ReceiverInfo;
import com.nilo.dms.service.order.model.SenderInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7.
 */
@Controller
public class TestController extends BaseController{

    @Autowired
    @Qualifier("messageProducer")
    private AbstractMQProducer messageProducer;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/test2.html")
    public String test1(String content) {

        for (int i = 1; i < 30; i++) {

            DeliveryOrder data = new DeliveryOrder();
            data.setReferenceNo("Kili_4" + i);
            data.setMerchantId("1");
            data.setOrderTime(DateUtil.getSysTimeStamp());
            data.setTotalPrice((long) i);
            data.setWeight(i+12.2d);
            data.setCountry("CN");
            data.setOrderPlatform("Test");

            data.setOrderType("DS");
            data.setServiceType(ServiceTypeEnum.ARRIVE_TODAY);
            data.setGoodsType("3C");

            ReceiverInfo receiverInfo = new ReceiverInfo();
            receiverInfo.setReceiverCountry("CN");
            receiverInfo.setReceiverPhone("18012341234");
            receiverInfo.setReceiverAddress("Mei Xi Hu");
            receiverInfo.setReceiverName("ronny");
            receiverInfo.setReceiverProvince("42");
            receiverInfo.setReceiverCity("4201");
            receiverInfo.setReceiverArea("420102");

            data.setReceiverInfo(receiverInfo);

            SenderInfo senderInfo = new SenderInfo();
            senderInfo.setSenderArea("430104");
            senderInfo.setSenderPhone("18012340000");
            senderInfo.setSenderAddress("Mei Xi Hu Office");
            senderInfo.setSenderCity("4301");
            senderInfo.setSenderProvince("43");
            senderInfo.setSenderCountry("CN");
            senderInfo.setSenderName("ronny2");

            data.setSenderInfo(senderInfo);

            List<GoodsInfo> list = new ArrayList<>();
            GoodsInfo g = new GoodsInfo();
            g.setUnitPrice(1112l);
            g.setGoodsDesc("Test Product1");
            g.setGoodsId("1");
            g.setQuality("Good");
            g.setQty(2);
            g.setUserdefine01("user define 01");

            GoodsInfo g1 = new GoodsInfo();
            g1.setUnitPrice(3333l);
            g1.setGoodsDesc("Test Product2 ");
            g1.setGoodsId("2");
            g1.setQuality("Good");
            g1.setQty(3);
            g1.setUserdefine01("123123");

            list.add(g);
            list.add(g1);

            data.setGoodsInfoList(list);

            String jsonData = JSON.toJSONString(data);
            orderService.addCreateDeliveryOrderRequest("1", jsonData, DigestUtils.md5Hex("12345678" + jsonData + "12345678"));

        }

        return "index";
    }

    @RequestMapping(value = "/notify.html")
    @ResponseBody
    public String notify(String content) {
        return toJsonTrueMsg();
    }
}
