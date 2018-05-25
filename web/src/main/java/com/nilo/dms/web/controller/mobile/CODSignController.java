package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.PaidTypeEnum;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.service.FileService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.PaymentService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.dto.order.WaybillHeader;
import com.nilo.dms.dto.order.WaybillPaymentOrder;
import com.nilo.dms.dto.order.WaybillPaymentRecord;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping("/mobile/rider/COD")
public class CODSignController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("#{configProperties['temp_photo_file_path']}")
    private static final String path = "";
    @Value("#{configProperties['signKey']}")
    private String signKey;
    @Value("#{configProperties['lipapayMerchantId']}")
    private String lipapayMerchantId;
    @Value("#{configProperties['notifyUrl']}")
    private String notifyUrl;
    @Value("#{configProperties['returnUrl']}")
    private String returnUrl;

    private static final String[] suffixNameAllow = new String[]{".jpg", ".png"};

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/sign.html")
    public String sign() {
        return "mobile/rider/COD/dshkqs";
    }

    @RequestMapping(value = "/cashSave.html")
    @ResponseBody
    public String cashSave(String logisticsNo, Integer paidType) {

        Principal me = SessionLocal.getPrincipal();
        String merchantId = me.getMerchantId();
        WaybillHeader header = new WaybillHeader();
        header.setMerchantId(merchantId);
        header.setOrderNo(logisticsNo);
        header.setPaidType(PaidTypeEnum.getEnum(paidType));
        waybillService.updateWaybill(header);
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/onlineProblemSave.html")
    @ResponseBody
    public String onlineProblemSave(MultipartFile file, String logisticsNo, Integer paidType, String remark) {

        Principal me = SessionLocal.getPrincipal();

        // 写入图片 与签收图片一致
        try {
            if (file != null) {
                fileService.uploadSignImage(me.getMerchantId(), me.getUserId(), logisticsNo, file.getBytes());
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }

        String merchantId = me.getMerchantId();
        Waybill waybill = waybillService.queryByOrderNo(merchantId, logisticsNo);
        // 订单付款标识更新
        WaybillHeader header = new WaybillHeader();
        header.setMerchantId(merchantId);
        header.setOrderNo(logisticsNo);
        header.setPaidType(PaidTypeEnum.getEnum(paidType));
        waybillService.updateWaybill(header);

        // 新增payment order记录
        List<String> waybillNos = new ArrayList<String>();
        waybillNos.add(logisticsNo);
        WaybillPaymentOrder paymentOrder = new WaybillPaymentOrder();

        paymentOrder.setId(IdWorker.getInstance().nextId() + "");
        paymentOrder.setNetworkId(me.getNetworks().get(0));
        paymentOrder.setPriceAmount(new BigDecimal(waybill.getNeedPayAmount()));
        paymentOrder.setRemark(remark);
        paymentOrder.setWaybillCount(1);
        paymentOrder.setPaymentTime(System.currentTimeMillis());
        paymentOrder.setCreatedTime(System.currentTimeMillis());
        paymentOrder.setCreatedBy(me.getUserId());

        paymentService.savePaymentOrder(paymentOrder, waybillNos);
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/toPay.html")
    public RedirectView toPay(String orderNo) {

        String payUrl = "http://sandbox.lipapay.com/api/excashier.html?version=1.4&merchantId=test&signType=MD5";
        String sign = "";
        String notifyUrl = "";
        String returnUrl = "";
        // String merchantOrderNo = orderId;
        float amount = 0.1f;
        String expirationTime = "2018-02-25";
        String sourceType = "B";
        String currency = "1.4";

        payUrl = payUrl
                + "&sign=1&notifyUrl=api/paymentNotify.html&returnUrl=111&merchantOrderNo=orderId&amount=0.1&expirationTime=2018-02-25&sourceType=B&currency=1.4";
        RedirectView redirectView = new RedirectView("http://sandbox.lipapay.com/api/excashier.html", true, false,
                false);
        redirectView.setContentType("application/x-www-form-urlencoded");
        Properties attributes = new Properties();
        attributes.setProperty("version", "1.4");
        redirectView.setAttributes(attributes);
        // redirectView.setRequestContextAttribute("application/x-www-form-urlencoded");
        return redirectView;
    }

    @ResponseBody
    @RequestMapping(value = "/redyToPay.html", method = RequestMethod.POST)
    public String redyToPay(String orderNo) {

        if (orderNo == null) {
            return toJsonErrorMsg("错误信息");
        }
        Principal me = SessionLocal.getPrincipal();
        String merchantId = me.getMerchantId();

        WaybillHeader header = new WaybillHeader();
        header.setMerchantId(merchantId);
        header.setOrderNo(orderNo);
        header.setPaidType(PaidTypeEnum.ONLINE);
        waybillService.updateWaybill(header);


        Waybill waybill = waybillService.queryByOrderNo(merchantId, orderNo);

        List<String> waybillNos = new ArrayList<String>();
        waybillNos.add(orderNo);
        WaybillPaymentOrder paymentOrder = new WaybillPaymentOrder();
        paymentOrder.setId(IdWorker.getInstance().nextId() + "");
        paymentOrder.setNetworkId(me.getNetworks().get(0));
        paymentOrder.setPriceAmount(new BigDecimal(waybill.getNeedPayAmount()));
        paymentOrder.setWaybillCount(1);
        paymentService.savePaymentOrder(paymentOrder, waybillNos);

        HashMap<String, String> map = new HashMap<String, String>();
        // map.put("version", "1.4");
        map.put("merchantId", lipapayMerchantId);
        map.put("signType", "MD5");
        map.put("notifyUrl", notifyUrl);
        map.put("returnUrl", returnUrl);
        map.put("merchantOrderNo", paymentOrder.getId());
        int amount = (int) paymentOrder.getPriceAmount().doubleValue() * 100;
        map.put("amount", amount + "");
        map.put("goods[0].goodsName", orderNo);
        map.put("goods[0].goodsQuantity", "1");
        map.put("goods[0].goodsPrice", amount + "");
        map.put("goods[0].goodsType", "2");
        map.put("expirationTime", 1000 * 60 * 30 + "");
        map.put("sourceType", "B");
        map.put("currency", "KES");

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            // 升序排序
            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        String plainText = "";
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, String> mapping = list.get(i);
            if (i == list.size() - 1) {
                plainText = plainText + mapping.getKey() + "=" + mapping.getValue();
            } else {
                plainText = plainText + mapping.getKey() + "=" + mapping.getValue() + "&";
            }
        }
        String sign = DigestUtils.md5Hex(plainText + signKey);
        map.put("sign", sign);
        return toJsonTrueData(map);
    }

    @RequestMapping(value = "/payReturn.html", method = RequestMethod.GET)
    public String payReturn() {

        Principal me = SessionLocal.getPrincipal();
        HttpServletRequest request = getRequest();
        String merchantOrderNo = (String) request.getParameter("merchantOrderNo");
        String orderId = (String) request.getParameter("orderId");
        String sign = (String) request.getParameter("sign");
        String signType = (String) request.getParameter("signType");
        String status = (String) request.getParameter("status");
        String merchantId = (String) request.getParameter("merchantId");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("merchantOrderNo", merchantOrderNo);
        map.put("merchantId", merchantId);
        map.put("orderId", orderId);
        map.put("signType", signType);
        map.put("status", status);

        String logisticsNo = "";

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            // 升序排序
            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        String plainText = "";
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, String> mapping = list.get(i);
            if (i == list.size() - 1) {
                plainText = plainText + mapping.getKey() + "=" + mapping.getValue();
            } else {
                plainText = plainText + mapping.getKey() + "=" + mapping.getValue() + "&";
            }
        }

        String generateSign = DigestUtils.md5Hex(plainText + signKey);

        if (sign != null && sign.equalsIgnoreCase(generateSign)) {
            // 支付完成
            WaybillPaymentRecord waybillPaymentRecord = new WaybillPaymentRecord();
            waybillPaymentRecord.setPaymentOrderId(merchantOrderNo);
            waybillPaymentRecord.setThirdPaySn(orderId);
            waybillPaymentRecord.setCreatedTime(System.currentTimeMillis());
            if ("SUCCESS".equalsIgnoreCase(status)) {
                waybillPaymentRecord.setStatus(1);
            } else {
                waybillPaymentRecord.setStatus(0);
            }
            paymentService.payRerun(waybillPaymentRecord);
            List<String> orderNos = paymentService.getOrderNosByPayOrderId(merchantOrderNo);

            if (orderNos != null) {
                logisticsNo = orderNos.get(0);
            }

            // 支付成功
            if (waybillPaymentRecord.getStatus() == 1) {
                return "redirect:/mobile/rider/sign/toSign.html?logisticsNo=" + logisticsNo;
            }
        } else {
            // 等待成功
            return "redirect:/mobile/rider/sign/toSign.html?logisticsNo=" + logisticsNo;
        }
        // 支付异常
        return "redirect:/mobile/rider/sign/toSign.html?logisticsNo=" + logisticsNo;
    }

    @ResponseBody
    @RequestMapping(value = "/getDetail.html", method = RequestMethod.POST)
    public String getDetail(Model model, String orderNo) {

        if (orderNo == null) {
            return toJsonErrorMsg("错误信息");
        }
        Principal me = SessionLocal.getPrincipal();
        String merchantId = me.getMerchantId();
        Waybill waybill = waybillService.queryByOrderNo(merchantId, orderNo);

        // model.addAttribute("waybill", waybill);
        if (waybill == null) {
            return toJsonErrorMsg("orderNo is error");
        }
        String amont = waybill.getNeedPayAmount().toString();

        return toJsonTrueData(amont);

    }
}
