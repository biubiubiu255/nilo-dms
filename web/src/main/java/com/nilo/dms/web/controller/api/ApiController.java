package com.nilo.dms.web.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.PaymentService;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.api.model.RequestParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ronny on 2017/8/30.
 */
@Controller
public class ApiController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WaybillOptService waybillOptService;
    @Value("#{configProperties['signKey']}")
    private String signKey;

    @RequestMapping(value = "/api.html", method = RequestMethod.GET)
    public String doGet() {
        return "api";
    }

    @RequestMapping(value = "/api.html", method = RequestMethod.POST)
    @ResponseBody
    public String doPost(RequestParam param) {

        log.info("API RequestParam:{}", param);

        param.checkParam();

        MethodEnum method = param.getMethod();
        String data = param.getData();
        String sign = param.getSign();
        String merchantId = param.getApp_id();

        Principal principal = new Principal();
        principal.setMerchantId(merchantId);
        principal.setUserId("api");
        principal.setNetworks(Arrays.asList(1));

        SessionLocal.setPrincipal(principal);

        switch (method) {
            case CREATE_WAYBILL: {
                waybillService.createWaybillRequest(merchantId, data, sign);
                break;
            }
            case UPDATE_WEIGHT: {
                JSONArray array = JSONArray.parseArray(data);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String orderNo = object.getString("waybill_number");
                    Double weight = object.getDouble("weight");
                    WaybillHeader header = new WaybillHeader();
                    header.setMerchantId(merchantId);
                    header.setOrderNo(orderNo);
                    header.setWeight(weight);
                    waybillService.updateWaybill(header);
                }

                break;
            }
            case CANCEL_WAYBILL: {
                OrderOptRequest optRequest = new OrderOptRequest();
                JSONObject jsonObject = JSON.parseObject(data);
                String orderNo = jsonObject.getString("waybill_number");
                AssertUtil.isNotBlank(orderNo, BizErrorCode.ORDER_NO_EMPTY);
                optRequest.setOrderNo(Arrays.asList(new String[]{orderNo}));
                optRequest.setOptType(OptTypeEnum.CANCEL);
                waybillService.handleOpt(optRequest);
                break;
            }
            case WAYBILL_TRACE: {
                break;
            }
            case ARRIVE_SCAN: {
                List<String> list = JSONArray.parseArray(data, String.class);
                waybillService.arrive(list);
                break;
            }
            case SIGN: {
                JSONObject jsonObject = JSON.parseObject(data);
                String orderNo = jsonObject.getString("waybill_number");
                String remark = jsonObject.getString("remark");
                waybillOptService.sign(orderNo, remark);
                break;
            }
            default:
                break;
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "api/paymentNotify.html", method = RequestMethod.POST)
    @ResponseBody
    public String paymentNotify(PaymentResult paymentResult) {

        String plainText = paymentResult.toString();
        log.info("paymentNotify Data:{}", plainText);
        String generateSign = DigestUtils.md5Hex(plainText + signKey);
        if (paymentResult.getSign() != null && paymentResult.getSign().equalsIgnoreCase(generateSign)) {

            // 支付完成
            WaybillPaymentRecord waybillPaymentRecord = new WaybillPaymentRecord();
            waybillPaymentRecord.setPaymentOrderId(paymentResult.getMerchantOrderNo());
            waybillPaymentRecord.setOrgTransId(paymentResult.getOrgTransId());
            waybillPaymentRecord.setPaymentChannel(paymentResult.getPaymentChannel());
            waybillPaymentRecord.setPaymentMethod(paymentResult.getPaymentMethod());
            waybillPaymentRecord.setPaymentType(0);
            waybillPaymentRecord.setThirdPaySn(paymentResult.getOrderId());
            waybillPaymentRecord.setStatus(1);
            paymentService.paySucess(waybillPaymentRecord);

            //返回数据
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setStatus("SUCCESS");
            paymentResponse.setErrorCode("100");
            paymentResponse.setMerchantId(paymentResult.getMerchantId());
            paymentResponse.setSignType("MD5");
            paymentResponse.setMerchantOrderNo(waybillPaymentRecord.getPaymentOrderId());
            paymentResponse.setOrderId(waybillPaymentRecord.getThirdPaySn());
            //paymentResponse.setSign(sign);
            plainText = paymentResponse.toString();
            String sign = DigestUtils.md5Hex(plainText + signKey);
            paymentResponse.setSign(sign);
            return JSON.toJSONString(paymentResponse);
        }
        return "false";
    }
}
