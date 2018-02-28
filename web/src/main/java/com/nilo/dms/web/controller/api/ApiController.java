package com.nilo.dms.web.controller.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.OrderOptRequest;
import com.nilo.dms.service.order.model.PaymentResponse;
import com.nilo.dms.service.order.model.PaymentResult;
import com.nilo.dms.service.order.model.SignForOrderParam;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.api.model.RequestParam;

/**
 * Created by ronny on 2017/8/30.
 */
@Controller
public class ApiController extends BaseController {


    private final Logger log = LoggerFactory.getLogger(getClass());

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

        MethodEnum method = param.getMethod();
        String data = param.getData();
        String sign = param.getSign();
        String merchantId = param.getApp_id();

        log.info("API Data:{}", data);

        switch (method) {
            case CREATE_WAYBILL: {
                orderService.addCreateDeliveryOrderRequest(merchantId, data, sign);
                break;
            }
            case CANCEL_WAYBILL: {
                OrderOptRequest optRequest = new OrderOptRequest();
                JSONObject jsonObject = JSON.parseObject(data);
                String orderNo = jsonObject.getString("waybill_number");
                AssertUtil.isNotBlank(orderNo, BizErrorCode.ORDER_NO_EMPTY);
                optRequest.setOrderNo(Arrays.asList(new String[]{orderNo}));
                optRequest.setMerchantId(merchantId);
                optRequest.setOptBy("api");
                optRequest.setOptType(OptTypeEnum.CANCEL);
                orderService.handleOpt(optRequest);
                break;
            }
            case WAYBILL_TRACE: {
                break;
            }
            case ARRIVE_SCAN: {
                List<String> list = JSONArray.parseArray(data,String.class);
                orderService.waybillNoListArrive(list,"api",merchantId);
                break;
            }
            case SIGN: {
                SignForOrderParam signForOrderParam = JSON.parseObject(data, SignForOrderParam.class);
                signForOrderParam.setMerchantId(merchantId);
                riderOptService.signForOrder(signForOrderParam);
                break;
            }
            default:
                break;
        }
        return toJsonTrueData(resultData);
    }


    @RequestMapping(value = "api/paymentNotify.html", method = RequestMethod.POST)
    @ResponseBody
    public String paymentNotify(PaymentResult paymentResult) {

        PaymentResponse paymentResponse = new PaymentResponse();
        return JSON.toJSONString(paymentResponse);
    }
    
    

}
