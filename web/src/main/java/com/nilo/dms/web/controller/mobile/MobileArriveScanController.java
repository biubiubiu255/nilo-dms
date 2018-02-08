package com.nilo.dms.web.controller.mobile;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nilo.dms.common.Principal;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.web.controller.BaseController;

@Controller
@RequestMapping("/mobile/arrive")
public class MobileArriveScanController extends BaseController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/scan.html")
	public String toPage() {
		return "mobile/network/arrive_scan/arriveScan";
	}

	
	@RequestMapping(value = "/submit.html")
	@ResponseBody
	public String submit(String scanedCodes,String logisticsNos) {
//		for (int i=0;i<logisticsNos.length;i++){
//			System.out.println(logisticsNos[i]);
//		}
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		String arriveBy = me.getUserId();
		
		String[] logisticsNoArray = scanedCodes.split(",");
		if (null != logisticsNoArray && logisticsNoArray.length > 0) {
			orderService.waybillNoListArrive(Arrays.asList(logisticsNoArray), arriveBy, merchantId);
		}
		return toJsonTrueMsg();
	}
}
