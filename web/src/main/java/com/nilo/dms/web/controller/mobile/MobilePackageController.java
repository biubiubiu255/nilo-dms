package com.nilo.dms.web.controller.mobile;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.PackageRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.PackageController.NextStation;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/11/22.
 */
@Controller
@RequestMapping("/mobile/package")
public class MobilePackageController extends BaseController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private DistributionNetworkDao distributionNetworkDao;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/packing.html")
	public String toPackage(Model model) {
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
		List<NextStation> list = new ArrayList<>();

		for (DistributionNetworkDO n : networkDOList) {
			NextStation s = new NextStation();
			s.setCode("" + n.getId());
			s.setName(n.getName());
			list.add(s);
		}

		model.addAttribute("nextStation", list);

		return "mobile/network/package/packing";
	}
	
	@RequestMapping(value = "/list.html")
	public String toList(Model model) {
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
		List<NextStation> list = new ArrayList<>();

		for (DistributionNetworkDO n : networkDOList) {
			NextStation s = new NextStation();
			s.setCode("" + n.getId());
			s.setName(n.getName());
			list.add(s);
		}

		model.addAttribute("nextStation", list);

		return "mobile/network/package/list";
	}

	@RequestMapping(value = "/submit.html")
	@ResponseBody
	public String submit(String scaned_codes[], int nextStation, Double weight, Double length, Double width,
			Double high) {
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		String orderNo = "";

		List<String> orderNos = new ArrayList<>();
		for (int i = 0; i < scaned_codes.length; i++) {
			orderNos.add(scaned_codes[i]);
		}

		PackageRequest packageRequest = new PackageRequest();
		packageRequest.setOrderNos(orderNos);
		packageRequest.setMerchantId(merchantId);
		packageRequest.setOptBy(me.getUserId());
		packageRequest.setNextNetworkId(nextStation);
		packageRequest.setWeight(weight);
		packageRequest.setLength(length);
		packageRequest.setWidth(width);
		packageRequest.setHigh(high);
		if (me.getNetworks() != null && me.getNetworks().size() != 0) {
			packageRequest.setNetworkId(me.getNetworks().get(0));
		}
		orderNo = orderService.addPackage(packageRequest);
		return toJsonTrueMsg();

		// for (int i=0;i<scaned_codes.length;i++){
		// System.out.println(scaned_codes[i]);
		// }
		// System.out.println(nextStation);
		// System.out.println(weight);
		// System.out.println(length);
		// System.out.println(width);
		// System.out.println(high);
		// return "true";
	}

	public static class NextStation {
		private String code;
		private String name;
		private String type;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
