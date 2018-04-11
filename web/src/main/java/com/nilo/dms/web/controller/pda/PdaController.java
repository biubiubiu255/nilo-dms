package com.nilo.dms.web.controller.pda;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dto.common.LoginInfo;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.order.Loading;
import com.nilo.dms.dto.order.ShipParameter;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.dto.order.WaybillHeader;
import com.nilo.dms.dto.pda.PdaRider;
import com.nilo.dms.dto.pda.PdaWaybill;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.WaybillService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private DeliveryOrderOptDao deliveryOrderOptDao;

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

        Long a = deliveryOrderOptDao.getStateByOrderNo(waybillno);
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
        waybillNos.add(waybillNo);

        waybillService.arrive(waybillNos);

        PdaWaybill pdaWaybill = this.queryByOrderNo(waybillNo);

        return toJsonTrueData(pdaWaybill);
    }

    @ResponseBody
    @RequestMapping(value = "/arrvieWeighing.html")
    public String arriveAndWeight(String waybillNo, Double weight, Double length, Double width, Double height) {

        //到件
        waybillService.arrive(Arrays.asList(waybillNo));

        Principal principal = SessionLocal.getPrincipal();
        //更新长宽高
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
        for (ThirdExpressDO e : expressDOList) {
            SendScanController.NextStation s = new SendScanController.NextStation();
            s.setCode(e.getExpressCode());
            s.setName(e.getExpressName());
            s.setType("T");
            list.add(s);
        }
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
        List<StaffDO> list = getRiderList(merchantId);

        List<PdaRider> pdaRiders = new ArrayList<PdaRider>();
        for (StaffDO s : list) {
            PdaRider pdaRider = new PdaRider();
            pdaRider.setMerchantId(s.getMerchantId());
            pdaRider.setDepartmentId(s.getDepartmentId());
            pdaRider.setUserId(s.getUserId());
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

        String[] scaned_codes = waybillnos.split(",");
        Loading loading = new Loading();
        loading.setRider(rider);

        Principal me = SessionLocal.getPrincipal();
        // 获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            if (StringUtil.isEmpty(rider)) {
                throw new IllegalArgumentException("PdaRider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if (StringUtil.isNotEmpty(rider)) {
                loading.setRider(rider);
            }
            loadingNo = loadingService.addLoading(loading);
        } catch (Exception e) {
            log.error("addLoading failed. loading:{}", loading, e);
            return toJsonErrorMsg(e.getMessage());
        }

        Waybill order = null;
        for (int i = 0; i < scaned_codes.length; i++) {
            try {
                loadingService.loadingScan(merchantId, loadingNo, scaned_codes[i], me.getUserId());
                // order = waybillService.queryByOrderNo(merchantId, scaned_codes);

            } catch (Exception e) {
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

    @RequestMapping(value = "/sendNext.html")
    @ResponseBody
    public String SendNext(String waybillnos, String nextStation, String carrier, String sendDriver, String plateNo,
                           String logisticsNo) {
        String[] scaned_codes = waybillnos.split(",");

        Loading loading = new Loading();
        loading.setNextStation(nextStation);
        loading.setRider(sendDriver);
        loading.setCarrier(carrier);
        loading.setTruckNo(plateNo);

        Principal me = SessionLocal.getPrincipal();
        // 获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            if (StringUtil.isEmpty(sendDriver)) {
                throw new IllegalArgumentException("PdaRider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if (StringUtil.isNotEmpty(sendDriver)) {
                loading.setRider(sendDriver);
            }
            loadingNo = loadingService.addLoading(loading);
        } catch (Exception e) {
            log.error("addLoading failed. loading:{}", loading, e);
            return toJsonErrorMsg(e.getMessage());
        }

        Waybill order = null;
        for (int i = 0; i < scaned_codes.length; i++) {
            try {
                loadingService.loadingScan(merchantId, loadingNo, scaned_codes[i], me.getUserId());
            } catch (Exception e) {
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
}
