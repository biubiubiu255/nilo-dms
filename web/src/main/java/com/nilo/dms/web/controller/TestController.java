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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7.
 */
@Controller
public class TestController extends BaseController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/test2.html")
    public String test1(String content) {

        return "index";
    }


    @RequestMapping(value = "/notify.html")
    @ResponseBody
    public String notify(String content) {
        return toJsonTrueMsg();
    }

    private List<DeliveryOrder> getList() {
        List<DeliveryOrder> list = new ArrayList<>();

        DeliveryOrder d1 = new DeliveryOrder();
        d1.setOrderNo("1");
        d1.setWeight(11.1d);
        d1.setOrderType("FBK");
        list.add(d1);

        DeliveryOrder d2 = new DeliveryOrder();
        d2.setOrderNo("2");
        d2.setWeight(21.1d);
        d2.setOrderType("FBK");
        list.add(d2);

        return list;
    }
}
