package com.nilo.dms.web.controller.pda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.visitor.functions.Hex;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.service.model.pda.PdaWaybill;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.web.controller.BaseController;

@Controller
@RequestMapping("/pda")
public class PdaController extends BaseController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliveryOrderOptDao deliveryOrderOptDao;

	@RequestMapping(value = "/scan.html")
	public String toPage() {
		return "mobile/network/arrive_scan/arriveScan";
	}

	@ResponseBody
	@RequestMapping(value = "/arrive.html")
	public String arrive(String waybillNo) {

		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		List<String> waybillNos = new ArrayList<String>();
		waybillNos.add(waybillNo);
		String arriveBy = me.getUserId();
		String merchantId = me.getMerchantId();
		String netWorkId = me.getNetworks().get(0)+"";
		
		orderService.waybillNoListArrive(waybillNos, arriveBy, merchantId,netWorkId);
		
		DeliveryOrder delivery = orderService.queryByOrderNo(merchantId, waybillNo);
		
		PdaWaybill pdaWaybill= new PdaWaybill();
		pdaWaybill.setWaybillNo(waybillNo);
		pdaWaybill.setReceiverCountry(delivery.getReceiverInfo().getReceiverCountry());
		pdaWaybill.setReceiverProvince(delivery.getReceiverInfo().getReceiverProvince());
		pdaWaybill.setNetworkName(delivery.getNetworkDesc());
		
		pdaWaybill.setWeight(delivery.getWeight()==null?"0":delivery.getWeight().toString());
		pdaWaybill.setWidth(delivery.getWidth()==null?"0":delivery.getWidth().toString());
		pdaWaybill.setLength(delivery.getLength()==null?"0":delivery.getLength().toString());

		pdaWaybill.setIsCod(delivery.getIsCod());
		pdaWaybill.setStatusDes(delivery.getStatusDesc());
		pdaWaybill.setGoodsTypeDes(delivery.getGoodsType());
		return toJsonTrueData(pdaWaybill);
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
		String netWorkId = ""+me.getNetworks().get(0);
		try {
			String[] logisticsNoArray = scanedCodes.split(",");
			if (null != logisticsNoArray && logisticsNoArray.length > 0) {
				orderService.waybillNoListArrive(Arrays.asList(logisticsNoArray), arriveBy, merchantId,netWorkId);
			}
		}catch (Exception e){
			return toJsonErrorMsg(e.getMessage());
		}

		return toJsonTrueMsg();
	}
}
