package com.nilo.dms.web.controller.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
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

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.ImageDao;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.web.controller.BaseController;

@Controller
@RequestMapping("/mobile/rider/COD")
public class CODSignController extends BaseController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/sign.html")
	public String sign() {
		return "mobile/rider/COD/dshkqs";
	}

	@Autowired
	private RiderOptService riderOptService;

	@Value("#{configProperties['temp_photo_file_path']}")
	private static final String path = "";

	private static final String[] suffixNameAllow = new String[] { ".jpg", ".png" };
	
	@Value("#{configProperties['signKey']}")
    private String signKey;


	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageDao imageDao;

	@RequestMapping(value = "/save.html")
	@ResponseBody

	public String save(MultipartFile file, String logisticsNo, String signer, String idNo, String danxuan) {
		System.out.println(logisticsNo);
		System.out.println(signer);
		System.out.println(idNo);
		System.out.println(danxuan);
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
		/*Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		String merchantId = me.getMerchantId();
		DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);

		// model.addAttribute("deliveryOrder", deliveryOrder);
		if (deliveryOrder == null) {
			return toJsonErrorMsg("orderNo is error");
		}
		String receiverName = deliveryOrder.getReceiverInfo().getReceiverName();*/
		HashMap<String, String> map = new HashMap<String, String>();
		//map.put("version", "1.4");
		map.put("merchantId", "test");
		map.put("signType", "MD5");
		map.put("notifyUrl", "http://47.89.177.73:8080");
		map.put("returnUrl", "http://47.89.177.73:8080");
		map.put("merchantOrderNo", "test");
		map.put("amount", "2");
		map.put("goods[0].goodsName", "test");
		map.put("goods[0].goodsQuantity", "1");
		map.put("goods[0].goodsPrice", "22");
		map.put("goods[0].goodsType", "2");
		map.put("expirationTime", "1000000");
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
			if (i == list.size()-1) {
				plainText = plainText+mapping.getKey() + "=" + mapping.getValue();
			} else {
				plainText = plainText+mapping.getKey() + "=" + mapping.getValue() + "&";
			}
		}
		String sign = DigestUtils.md5Hex(plainText+signKey);
		map.put("sign", sign);
		return toJsonTrueData(map);
	}
	
	@RequestMapping(value = "/payReturn.html" ,method = RequestMethod.GET)
	public String payReturn() {

		HttpServletRequest request = getRequest();
		String merchantOrderNo = (String)request.getParameter("merchantOrderNo");
		String orderId = (String)request.getParameter("orderId");
		String sign = (String)request.getParameter("sign");
		String signType = (String)request.getParameter("signType");
		String status = (String)request.getParameter("status");
		String merchantId = (String)request.getParameter("merchantId");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("merchantOrderNo", merchantOrderNo);
		map.put("merchantId", merchantId);
		map.put("orderId", orderId);
		map.put("signType", signType);
		map.put("status", status);

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
			if (i == list.size()-1) {
				plainText = plainText+mapping.getKey() + "=" + mapping.getValue();
			} else {
				plainText = plainText+mapping.getKey() + "=" + mapping.getValue() + "&";
			}
		}
		
		String generateSign = DigestUtils.md5Hex(plainText+signKey);
		
		if(sign!=null&&sign.equalsIgnoreCase(generateSign)) {
			//支付成功
			
			
		}else {
			//等待支付
		}
		
		 return "redirect:/ toList ";
	}

	@ResponseBody
	@RequestMapping(value = "/getDetail.html", method = RequestMethod.POST)
	public String getDetail(Model model, String orderNo) {

		if (orderNo == null) {
			return toJsonErrorMsg("错误信息");
		}
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		String merchantId = me.getMerchantId();
		DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);

		// model.addAttribute("deliveryOrder", deliveryOrder);
		if (deliveryOrder == null) {
			return toJsonErrorMsg("orderNo is error");
		}
		String amont = deliveryOrder.getNeedPayAmount().toString();
		
		return toJsonTrueData(amont);

	}
}
