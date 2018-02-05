package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.web.controller.BaseController;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String submit(String[] arrWaybillNo) {
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		String arriveBy = me.getUserId();

		if (null != arrWaybillNo && arrWaybillNo.length > 0) {
			orderService.waybillNoListArrive(Arrays.asList(arrWaybillNo), arriveBy, merchantId);
		}
		return toJsonTrueMsg();
	}
}
