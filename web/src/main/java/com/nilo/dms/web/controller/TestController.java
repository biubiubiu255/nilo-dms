package com.nilo.dms.web.controller;

import com.nilo.dms.service.order.model.DeliveryOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
@Controller
public class TestController extends BaseController {

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
