package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.PaymentService;
import com.nilo.dms.service.order.model.WaybillPaymentOrder;
import com.nilo.dms.service.order.model.WaybillPaymentRecord;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping("/mobile/rider/Batch")
public class BatchTransferController extends BaseController {

	@Value("#{configProperties['signKey']}")
	private String signKey;
	@Value("#{configProperties['lipapayMerchantId']}")
	private String lipapayMerchantId;
	@Value("#{configProperties['notifyUrl']}")
	private String notifyUrl;
	@Value("#{configProperties['batchReturnUrl']}")
	private String returnUrl;

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = "/toPay.html")
	public String toPay(Model model) {
		Principal me = SessionLocal.getPrincipal();
		List<WaybillTaskDo> list = paymentService.queryNeedPayOrderByRider(me.getUserId());
		model.addAttribute("list", list);
		return "mobile/rider/BatchTransfer/toPay";
	}

	@RequestMapping(value = "/readyToPay.html")
	@ResponseBody
	public String readyToPay(Model model, String[] orderNos) {
		if (orderNos == null || orderNos.length==0) {
			return toJsonErrorMsg("please select an order");
		}
		Principal me = SessionLocal.getPrincipal();
		String merchantId = me.getMerchantId();
		List<String> waybillNos = Arrays.asList(orderNos);
		if(waybillNos.size()==0) {
			return toJsonErrorMsg("please select an order");
		}
		WaybillPaymentOrder paymentOrder = paymentService.savePaymentOrderByWaybill(Long.parseLong(merchantId),
				me.getNetworks().get(0),me.getUserId(), waybillNos);

		HashMap<String, String> map = new HashMap<String, String>();
		// map.put("version", "1.4");
		map.put("merchantId", lipapayMerchantId);
		map.put("signType", "MD5");
		map.put("notifyUrl", notifyUrl);
		map.put("returnUrl", returnUrl);
		map.put("merchantOrderNo", paymentOrder.getId());
		int amount = (int) paymentOrder.getPriceAmount().doubleValue();
		map.put("amount", amount + "");
		map.put("goods[0].goodsName", "batch pay");
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
			waybillPaymentRecord.setCreatedBy(me.getUserId());
			if ("SUCCESS".equalsIgnoreCase(status)) {
				waybillPaymentRecord.setStatus(1);
			} else {
				waybillPaymentRecord.setStatus(0);
			}
			paymentService.payRerun(waybillPaymentRecord);
			//List<String> orderNos = paymentService.getOrderNosByPayOrderId(merchantOrderNo);
			/*
			if (orderNos != null) {
				logisticsNo = orderNos.get(0);
			}*/

			// 支付成功
			if (waybillPaymentRecord.getStatus() == 1) {
				return "redirect:/mobile/rider/Batch/toPay.html";
			}
		} else {
			// 等待成功
			return "redirect:/mobile/rider/Batch/toPay.html";
		}
		// 支付异常
		return "redirect:/mobile/rider/Batch/toPay.html";
	}

}
