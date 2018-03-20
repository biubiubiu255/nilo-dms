package com.nilo.dms.web.controller.pda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.service.model.pda.PdaRider;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.service.order.model.ShipParameter;
import org.apache.shiro.SecurityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderService orderService;
    @Autowired
    private LoadingService loadingService;

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

	@ResponseBody
	@RequestMapping(value = "/getRider.html")
	public String getRider() {
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		//获取merchantId
		String merchantId = me.getMerchantId();
        List<StaffDO> list = getRiderList(merchantId);

        List<PdaRider> pdaRiders = new ArrayList<PdaRider>();
        for(StaffDO s:list){
            PdaRider pdaRider = new PdaRider();
            pdaRider.setMerchantId(s.getMerchantId());
            pdaRider.setDepartmentId(s.getDepartmentId());
            pdaRider.setUserId(s.getUserId());
            pdaRider.setStaffId(s.getStaffId());
            pdaRider.setIdandName(s.getStaffId() +"-"+ s.getRealName());
            pdaRider.setRealName(s.getRealName());
            pdaRiders.add(pdaRider);
        }
		return toJsonTrueData(pdaRiders);
	}
    @RequestMapping(value = "/riderDelivery.html")
    @ResponseBody
    public String riderDelivery(String scaned_codes[],String rider,String logisticsNo ) {
        Loading loading = new Loading();
        loading.setRider(rider);

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            if(StringUtil.isEmpty(rider)){
                throw new IllegalArgumentException("PdaRider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if(StringUtil.isNotEmpty(rider)) {
                loading.setRider(rider);
            }
            loadingNo = loadingService.addLoading(loading);
        } catch (Exception e) {
            log.error("addLoading failed. loading:{}", loading, e);
            return toJsonErrorMsg(e.getMessage());
        }

        DeliveryOrder order = null;
        for (int i=0; i<scaned_codes.length; i++){
            try {
                loadingService.loadingScan(merchantId, loadingNo, scaned_codes[i], me.getUserId());
                //order = orderService.queryByOrderNo(merchantId, scaned_codes);

            }catch (Exception e) {
                log.error("loadingScan failed. orderNo:{}", scaned_codes[i], e);
                return toJsonErrorMsg(e.getMessage());
            }

        }

        ShipParameter parameter = new ShipParameter();
        parameter.setMerchantId(merchantId);
        parameter.setOptBy(me.getUserId());
        parameter.setLoadingNo(loadingNo);
        parameter.setNetworkId("" + me.getNetworks().get(0));
        loadingService.ship(parameter);
        return toJsonTrueData(loadingNo);

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
