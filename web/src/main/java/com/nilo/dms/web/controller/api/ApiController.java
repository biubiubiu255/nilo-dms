package com.nilo.dms.web.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.AbnormalParam;
import com.nilo.dms.service.order.model.SignForOrderParam;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.api.model.OPEnum;
import com.nilo.dms.web.controller.api.model.RequestParam;

/**
 * Created by ronny on 2017/8/30.
 */
@Controller
public class ApiController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RiderOptService riderOptService;

    @RequestMapping(value = "/api.html", method = RequestMethod.GET)
    public String doGet() {
        return "api";
    }

    @RequestMapping(value = "/api.html", method = RequestMethod.POST)
    @ResponseBody
    public String doPost(RequestParam param) {

        param.checkParam();

        Map<String, String> resultData = new HashMap<>();

        OPEnum op = param.getOp();
        String data = param.getData();
        String sign = param.getSign();
        String merchantId = param.getMerchantId();

        switch (op) {
            case CREATE_DELIVERY_ORDER: {
                String orderNo = orderService.addCreateDeliveryOrderRequest(merchantId, data, sign);
                resultData.put("orderNo", orderNo);
                break;
            }
            case ARRIVE_SCAN: {
                break;
            }
            case SIGN: {
                SignForOrderParam signForOrderParam = JSON.parseObject(data, SignForOrderParam.class);
                signForOrderParam.setMerchantId(merchantId);
                riderOptService.signForOrder(signForOrderParam);
                break;
            }
            case ABNORMAL: {
                AbnormalParam abnormalParam = JSON.parseObject(data, AbnormalParam.class);
                abnormalParam.getAbnormalOrder().setMerchantId(merchantId);
                riderOptService.abnormal(abnormalParam);
                break;
            }
            default:
                break;
        }
        return toJsonTrueData(resultData);
    }


    @RequestMapping(value = "api/login.html", method = RequestMethod.POST)
    @ResponseBody
    public String login( LoginInfo loginInfo) {

    	Map<String, String> resultData = new HashMap<>();
    	resultData.put("userName", loginInfo.getUserName());
    	
        return toJsonTrueData(resultData);
    }
    
}
