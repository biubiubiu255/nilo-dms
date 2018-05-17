package com.nilo.dms.web.controller.pda;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.common.LoginInfo;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.dto.pda.PdaRider;
import com.nilo.dms.dto.pda.PdaWaybill;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.mobile.SendScanController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pda")
public class PdaController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private WaybillService waybillService;
	@Autowired
	private LoadingService loadingService;
	@Autowired
	private UserService userService;
	@Autowired
	private ThirdExpressDao thirdExpressDao;
	@Autowired
	private DistributionNetworkDao distributionNetworkDao;
	@Autowired
	private ThirdDriverDao thirdDriverDao;
	@Autowired
	private WaybillLogDao waybillLogDao;
	@Autowired
	private WaybillDao waybillDao;
	@Autowired
	private SendThirdService sendThirdService;
	@Autowired
	private RiderDeliveryService riderDeliveryService;
	@Autowired
	private OutsourceDao outsourceDao;
	@Autowired
	private StaffDao staffDao;

	@RequestMapping(value = "/scan.html")
	public String toPage() {
		return "mobile/network/arrive_scan/arriveScan";
	}

	private PdaWaybill queryByOrderNo(String waybillNo) {

		Principal principal = SessionLocal.getPrincipal();
		Waybill delivery = waybillService.queryByOrderNo(principal.getMerchantId(), waybillNo);
		PdaWaybill pdaWaybill = new PdaWaybill();
		pdaWaybill.setWaybillNo(waybillNo);
		pdaWaybill.setReceiverCountry(delivery.getReceiverInfo().getReceiverCountry());
		pdaWaybill.setReceiverProvince(delivery.getReceiverInfo().getReceiverProvince());
		pdaWaybill.setNetworkName(delivery.getNetworkDesc());

		pdaWaybill.setWeight(delivery.getWeight() == null ? "0" : delivery.getWeight().toString());
		pdaWaybill.setWidth(delivery.getWidth() == null ? "0" : delivery.getWidth().toString());
		pdaWaybill.setLength(delivery.getLen() == null ? "0" : delivery.getLen().toString());

		pdaWaybill.setIsCod(delivery.getIsCod());
		pdaWaybill.setStatusDes(delivery.getStatusDesc());
		pdaWaybill.setGoodsTypeDes(delivery.getGoodsType());
		return pdaWaybill;
	}

	@ResponseBody
	@RequestMapping(value = "/sendCheck.html")
	public String sendCheck(String waybillno) {

		Long a = waybillLogDao.getStateByOrderNo(waybillno);
		if (a == null) {
			return toJsonErrorMsg("There is no OrderNo");
		}
		if (!(a == 20)) {
			return toJsonErrorMsg(BizErrorCode.ORDER_NO_ARRIVE.getDescription());
		}
		return toJsonTrueMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/arrive.html")
	public String arrive(String waybillNo) {

		List<String> waybillNos = new ArrayList<String>();

		String[] scaned_codes = waybillNo.split(",");
		for (String code : scaned_codes) {
			waybillNos.add(code);
		}

		waybillService.arrive(waybillNos);

		// PdaWaybill pdaWaybill = this.queryByOrderNo(waybillNo);

		return toJsonTrueMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/arrvieWeighing.html")
	public String arriveAndWeight(String waybillNo, Double weight, Double length, Double width, Double height) {

		// 到件
		waybillService.arrive(Arrays.asList(waybillNo));

		Principal principal = SessionLocal.getPrincipal();
		// 更新长宽高
		WaybillHeader header = new WaybillHeader();
		header.setMerchantId(principal.getMerchantId());
		header.setOrderNo(waybillNo);
		header.setWeight(weight);
		header.setLen(length);
		header.setWidth(width);
		header.setHeight(height);
		waybillService.updateWaybill(header);

		PdaWaybill pdaWaybill = this.queryByOrderNo(waybillNo);
		return toJsonTrueData(pdaWaybill);
	}

	@ResponseBody
	@RequestMapping(value = "/getOutsource.html")
	public String getOutsource(Model model, HttpServletRequest request) {
		Principal me = SessionLocal.getPrincipal();

		// 第三方
		List<OutsourceDO> outsourceList = outsourceDao.findAll(me.getMerchantId());

		return toJsonTrueData(outsourceList);
	}

	@ResponseBody
	@RequestMapping(value = "/getOutsourceRider.html", method = RequestMethod.POST)
	public String getOutsourceRider(String outsource) {
		Principal me = SessionLocal.getPrincipal();

		List<StaffDO> outsourceRider = staffDao.queryAllRider(Long.parseLong(me.getCompanyId()), outsource);
		List<PdaRider> pdaRiders = new ArrayList<PdaRider>();
		for (StaffDO s : outsourceRider) {
			PdaRider pdaRider = new PdaRider();
			pdaRider.setMerchantId(s.getMerchantId());
			pdaRider.setDepartmentId(s.getDepartmentId());
			pdaRider.setUserId(Long.parseLong(s.getUserId()));
			pdaRider.setStaffId(s.getStaffId());
			pdaRider.setIdandName(s.getStaffId() + "-" + s.getRealName());
			pdaRider.setRealName(s.getRealName());
			pdaRiders.add(pdaRider);
		}
		return toJsonTrueData(pdaRiders);

	}

	@ResponseBody
	@RequestMapping(value = "/getThirdExpress.html")
	public String getThirdExpress(Model model, HttpServletRequest request) {
		Principal me = SessionLocal.getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();

		// 第三方快递公司及自提点
		List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(merchantId));
		List<SendScanController.NextStation> list = new ArrayList<>();
		for (ThirdExpressDO e : expressDOList) {
			SendScanController.NextStation s = new SendScanController.NextStation();
			s.setCode(e.getExpressCode());
			s.setName(e.getExpressName());
			s.setType("T");
			list.add(s);
		}
		return toJsonTrueData(list);
	}

	@ResponseBody
	@RequestMapping(value = "/getNextStation.html")
	public String getNextStation(Model model, HttpServletRequest request) {
		Principal me = SessionLocal.getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();

		// 第三方快递公司及自提点
		List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(merchantId));
		List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));

		List<SendScanController.NextStation> list = new ArrayList<>();
		for (DistributionNetworkDO n : networkDOList) {
			SendScanController.NextStation s = new SendScanController.NextStation();
			s.setCode("" + n.getId());
			s.setName(n.getName());
			s.setType("N");
			list.add(s);
		}
		return toJsonTrueData(list);
	}

	@RequestMapping(value = "/getDriver.html")
	@ResponseBody
	public String getDriver(String code) {
		List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(code);
		List<SendScanController.Driver> list = new ArrayList<>();
		for (ThirdDriverDO d : thirdDriver) {
			SendScanController.Driver driver = new SendScanController.Driver();
			driver.setCode(d.getDriverId());
			driver.setName(d.getDriverName());
			list.add(driver);
		}
		return toJsonTrueData(list);
	}

	@ResponseBody
	@RequestMapping(value = "/getRider.html")
	public String getRider() {
		Principal me = SessionLocal.getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		List<StaffDO> list = getRiderList();

		List<PdaRider> pdaRiders = new ArrayList<PdaRider>();
		for (StaffDO s : list) {
			PdaRider pdaRider = new PdaRider();
			pdaRider.setMerchantId(s.getMerchantId());
			pdaRider.setDepartmentId(s.getDepartmentId());
			pdaRider.setUserId(Long.parseLong(s.getUserId()));
			pdaRider.setStaffId(s.getStaffId());
			pdaRider.setIdandName(s.getStaffId() + "-" + s.getRealName());
			pdaRider.setRealName(s.getRealName());
			pdaRiders.add(pdaRider);
		}
		return toJsonTrueData(pdaRiders);
	}

	@RequestMapping(value = "/riderDelivery.html")
	@ResponseBody
	public String riderDelivery(String waybillnos, String rider, String logisticsNo) {

		Principal me = SessionLocal.getPrincipal();
		String merchantId = me.getMerchantId();
		String[] scaned_codes = waybillnos.split(",");
		for (String waybillno : scaned_codes) {
			WaybillDO waybillDO = waybillDao.queryByOrderNo(Long.valueOf(merchantId), waybillno);
			if (waybillDO == null) {
				return toJsonErrorMsg("WaybillNo " + waybillno + " is wrong!");
			}
		}

		RiderDelivery riderDelivery = new RiderDelivery();
		riderDelivery.setMerchantId(Long.valueOf(merchantId));
		riderDelivery
				.setHandleNo(SystemConfig.getNextSerialNo(merchantId.toString(), SerialTypeEnum.LOADING_NO.getCode()));
		riderDelivery.setRider(rider);
		riderDelivery.setHandleBy(Long.valueOf(me.getUserId()));
		riderDelivery.setStatus(1);
		riderDeliveryService.addRiderPackAndDetail(Long.valueOf(merchantId), riderDelivery, scaned_codes);
		if (riderDelivery.getStatus().equals(HandleRiderStatusEnum.SHIP.getCode())) {
			riderDeliveryService.ship(riderDelivery.getHandleNo());
		}

		return toJsonTrueData(riderDelivery.getHandleNo());
	}

	@RequestMapping(value = "/sendNext.html")
	@ResponseBody
	// sendthirdexpress
	public String SendNext(String waybillnos, String nextStation, String carrier, String sendDriver, String plateNo,
			String logisticsNo) {
		String[] scaned_codes = waybillnos.split(",");
		for (String waybillno : scaned_codes) {
			Long a = waybillLogDao.getStateByOrderNo(waybillno);
			if (a == null) {
				return toJsonErrorMsg("WaybillNo " + waybillno + " is wrong!");
			}
			/*
			 * if (!(a == 20)) { return toJsonErrorMsg("Waybill No "+
			 * waybillno+"is not arrived!"); }
			 */
		}
		/*
		 * Loading loading = new Loading(); loading.setNextStation(nextStation);
		 * loading.setRider(sendDriver); loading.setCarrier(carrier);
		 * loading.setTruckNo(plateNo);
		 * 
		 * Principal me = SessionLocal.getPrincipal(); // 获取merchantId String merchantId
		 * = me.getMerchantId(); String loadingNo = ""; try { if
		 * (StringUtil.isEmpty(sendDriver)) { throw new
		 * IllegalArgumentException("PdaRider or Driver is empty."); }
		 * loading.setMerchantId(merchantId); loading.setLoadingBy(me.getUserId()); if
		 * (StringUtil.isNotEmpty(sendDriver)) { loading.setRider(sendDriver); }
		 * loadingNo = loadingService.addLoading(loading); } catch (Exception e) {
		 * log.error("addLoading failed. loading:{}", loading, e); return
		 * toJsonErrorMsg(e.getMessage()); }
		 * 
		 * Waybill order = null; for (int i = 0; i < scaned_codes.length; i++) { try {
		 * loadingService.loadingScan(merchantId, loadingNo, scaned_codes[i],
		 * me.getUserId()); } catch (Exception e) {
		 * log.error("loadingScan failed. orderNo:{}", scaned_codes[i], e); return
		 * toJsonErrorMsg(e.getMessage()); }
		 * 
		 * }
		 * 
		 * ShipParameter parameter = new ShipParameter();
		 * parameter.setMerchantId(merchantId); parameter.setOptBy(me.getUserId());
		 * parameter.setLoadingNo(loadingNo); parameter.setNetworkId("" +
		 * me.getNetworks().get(0)); loadingService.ship(parameter); return
		 * toJsonTrueData(loadingNo);
		 */
		Principal me = SessionLocal.getPrincipal();
		String merchantId = me.getMerchantId();
		SendThirdHead head = new SendThirdHead();
		head.setMerchantId(Long.valueOf(merchantId));
		head.setThirdExpressCode(carrier);
		head.setHandleBy(Long.valueOf(me.getUserId()));
		head.setDriver(sendDriver);
		head.setStatus(1);
		head.setType("waybill");
		head.setHandleName(me.getUserName());
		sendThirdService.insertBigAndSmall(Long.parseLong(merchantId), head, scaned_codes);
		// 如果初次写入直接是ship的话，这里再批量ship一下
		if (head.getStatus().equals(HandleRiderStatusEnum.SHIP.getCode())) {
			sendThirdService.ship(head.getHandleNo());
		}

		return toJsonTrueData(head.getHandleNo());
	}

	@RequestMapping(value = "/editPassword.html")
	@ResponseBody
	public String editPassword(String oldPassword, String newPassword, String againPassword) {
		try {
			AssertUtil.isNotBlank(oldPassword, SysErrorCode.REQUEST_IS_NULL);
			Principal me = SessionLocal.getPrincipal();
			String userId = me.getUserId();
			// 校验旧密码
			User user = userService.findByUserId(me.getMerchantId(), userId);
			if (!StringUtils.equals(DigestUtils.sha1Hex(oldPassword), user.getLoginInfo().getPassword())) {
				throw new DMSException(BizErrorCode.OLD_PASSWORD_ERROR);
			}

			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setUserId(userId);
			loginInfo.setMerchantId(me.getMerchantId());
			loginInfo.setPassword(DigestUtils.sha1Hex(newPassword));
			userService.updateLoginInfo(loginInfo);
		} catch (Exception e) {
			logger.error("changePassword failed. ", e);
			return toJsonErrorMsg("Failed:" + e.getMessage());
		}
		return toJsonTrueMsg();
	}

	@RequestMapping(value = "/submit.html")
	@ResponseBody
	public String submit(String scanedCodes) {

		try {
			String[] logisticsNoArray = scanedCodes.split(",");
			if (null != logisticsNoArray && logisticsNoArray.length > 0) {
				waybillService.arrive(Arrays.asList(logisticsNoArray));
			}
		} catch (Exception e) {
			return toJsonErrorMsg(e.getMessage());
		}

		return toJsonTrueMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/getExpress.html")
	public String getExpress() {
		Pagination page = getPage();
		List<ThirdExpressDO> expressList = userService.findExpressesAll(page);
		List<SendScanController.NextStation> list = new ArrayList<>();
		for (ThirdExpressDO e : expressList) {
			SendScanController.NextStation s = new SendScanController.NextStation();
			s.setCode(e.getExpressCode());
			s.setName(e.getExpressName());
			s.setType("T");
			list.add(s);
		}
		return toJsonTrueData(list);
	}

	@ResponseBody
	@RequestMapping(value = "/packageScan.html")
	public String packageScan(String waybillno) {

		Principal me = SessionLocal.getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		Waybill order = null;
		try {
			order = waybillService.queryByOrderNo(merchantId, waybillno);
			if (order == null)
				throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, waybillno);
		} catch (Exception e) {
			log.error("loadingScan failed. orderNo:{}", waybillno, e);
			return toJsonErrorMsg(e.getMessage());
		}
		return toJsonTrueMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/addPackage.html")
	public String addLoading(String waybillnosBill, Double weight, Double length, Double width, Double height,
			Integer nextNetworkId) {

		Principal me = SessionLocal.getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();

		List<String> orderNos = Arrays.asList(waybillnosBill.split(","));
		// 验证
		for (String waybillNo : orderNos) {
			Waybill order = null;
			try {
				order = waybillService.queryByOrderNo(merchantId, waybillNo);
				if (order == null)
					throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, waybillNo);
			} catch (Exception e) {
				log.error("loadingScan failed. orderNo:{}", waybillNo, e);
				return toJsonErrorMsg(e.getMessage());
			}
		}

		String orderNo = "";
		try {
			PackageRequest packageRequest = new PackageRequest();
			packageRequest.setOrderNos(orderNos);
			packageRequest.setMerchantId(merchantId);
			packageRequest.setOptBy(me.getUserId());
			packageRequest.setWeight(weight);
			packageRequest.setLength(length);
			packageRequest.setWidth(width);
			packageRequest.setHigh(height);
			packageRequest.setOptBy(me.getUserId());
			packageRequest.setNextNetworkId(nextNetworkId);
			if (me.getNetworks() != null && me.getNetworks().size() != 0) {
				packageRequest.setNetworkId(me.getNetworks().get(0));
			}
			orderNo = waybillService.addPackage(packageRequest);
		} catch (Exception e) {
			return toJsonErrorMsg(e.getMessage());
		}
		return toJsonTrueData(orderNo);
	}

	@ResponseBody
	@RequestMapping(value = "/scanSmallPack.html")
	public String scanSmallPack(String waybillno) {
		Principal me = SessionLocal.getPrincipal();
		// 获取merchantId
		String merchantId = me.getMerchantId();
		WaybillDO waybillDO = waybillDao.queryByOrderNo(Long.valueOf(merchantId), waybillno);
		try {
			if (StringUtil.isEmptys(waybillDO, waybillDO.getIsPackage()) || !waybillDO.getIsPackage().equals("1")) {
				return toJsonErrorMsg("Empty or not packaged");
			}
		} catch (Exception e) {
			return toJsonErrorMsg("This order has permission");
		}

		return toJsonTrueMsg();
	}

	@ResponseBody
	@RequestMapping(value = "/addLoading.html", method = RequestMethod.POST)
	public String addLoading(String smallPacks, String driver, String thirdExpressCode) {
		String[] smallPack = smallPacks.split(",");
		Principal me = SessionLocal.getPrincipal();
		Long merchantId = Long.valueOf(me.getMerchantId());
		
		//校验
		for (String waybillno : smallPack) {
			WaybillDO waybillDO = waybillDao.queryByOrderNo(Long.valueOf(merchantId), waybillno);
			if (waybillDO == null || !waybillDO.getIsPackage().equals("1")) {
				// if (StringUtil.isEmptys(waybillDO, waybillDO.getIsPackage()) ||
				// !waybillDO.getIsPackage().equals("1")) {
				return toJsonErrorMsg(waybillno + " is wrong or not packaged");
			}
		}

		List<Waybill> waybills = waybillService.queryByOrderNos(me.getMerchantId(), Arrays.asList(smallPack));

		int networkCode = waybills.get(0).getNetworkId();
		int nextStation = waybills.get(0).getNextNetworkId();
		for (Waybill waybill : waybills) {
			/*
			 * if (!waybill.isPackage()) { return toJsonErrorMsg("Waybill No " +
			 * waybill.getOrderNo() + " is not a package!"); }
			 */
			if (networkCode != waybill.getNetworkId() || nextStation != waybill.getNextNetworkId()) {
				// throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, waybill.getOrderNo());
				return toJsonErrorMsg("WaybillNo " + waybill.getOrderNo() + " is wrong!");
			}
		}

		SendThirdHead sendThirdHead = new SendThirdHead();
		sendThirdHead.setMerchantId(merchantId);
		sendThirdHead.setHandleBy(Long.valueOf(me.getUserId()));
		sendThirdHead.setHandleName(me.getUserName());
		sendThirdHead.setType("package");
		sendThirdHead.setDriver(driver);
		sendThirdHead.setThirdExpressCode(thirdExpressCode);
		sendThirdHead.setNetworkCode(networkCode + "");
		sendThirdHead.setNextStation(nextStation + "");
		sendThirdHead.setStatus(1);

		sendThirdService.insertBigAndSmall(merchantId, sendThirdHead, smallPack);

		// 如果初次写入直接是ship的话，这里再批量ship一下
		if (sendThirdHead.getStatus() != null
				&& sendThirdHead.getStatus().equals(HandleRiderStatusEnum.SHIP.getCode())) {
			sendThirdService.ship(sendThirdHead.getHandleNo());
		}

		return toJsonTrueMsg();
	}
}
