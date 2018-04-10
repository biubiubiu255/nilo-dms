package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dto.order.Loading;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.dto.order.ShipParameter;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mobile/send")
public class SendScanController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ThirdExpressDao thirdExpressDao;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private ThirdDriverDao thirdDriverDao;
    @Autowired
    private LoadingService loadingService;
    @Autowired
    private DeliveryOrderOptDao deliveryOrderOptDao;
//    @Autowired
//    private StaffDao staffDao;

    @RequestMapping(value = "/list.html")
    public String list(String loadingNo, Integer loadingStatus) {

//        Principal me = SessionLocal.getPrincipal();
//        //获取merchantId
//        String merchantId = me.getMerchantId();
//        Pagination page = getPage();
//        List<Loading> list = loadingService.queryBy(merchantId, loadingNo, loadingStatus, page);
        return "mobile/test";
    }

    @ResponseBody
    @RequestMapping(value = "/check.html")
    public String check(String code) {

        Long a = deliveryOrderOptDao.getStateByOrderNo(code);
        if (a == null) {
            return toJsonErrorMsg("There is no OrderNo");
        }
        if (!(a == 20)) {
            return toJsonErrorMsg("There are restrictions on this order");
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/scan.html")
    public String toPage(Model model, HttpServletRequest request) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("riderList", getRiderList(merchantId));

        //第三方快递公司及自提点
        List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(merchantId));
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));

        List<NextStation> list = new ArrayList<>();
        for (ThirdExpressDO e : expressDOList) {
            NextStation s = new NextStation();
            s.setCode(e.getExpressCode());
            s.setName(e.getExpressName());
            s.setType("T");
            list.add(s);
        }
        for (DistributionNetworkDO n : networkDOList) {
            NextStation s = new NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            s.setType("N");
            list.add(s);
        }
        model.addAttribute("nextStation", list);
        model.addAttribute("thirdCarrier", expressDOList);
        return "mobile/network/send_scan/sendScan";
    }

    @RequestMapping(value = "/getDriver.html")
    @ResponseBody
    public String getDriver(String code) {
        List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(code);
        List<Driver> list = new ArrayList<>();
        for (ThirdDriverDO d : thirdDriver) {
            Driver driver = new Driver();
            driver.setCode(d.getDriverId());
            driver.setName(d.getDriverName());
            list.add(driver);
        }
//        List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(code);
//        List<Driver> list = new ArrayList<>();
//        for(ThirdDriverDO d : thirdDriver){
//            Driver driver = new Driver();
//            driver.setCode(d.getDriverId());
//            driver.setName(d.getDriverName());
//            list.add(driver);
//        }
//        if(isInteger(code)) {
//            List<StaffDO> staffList = staffDao.queryNetworkStaff(Long.parseLong(code));
//            for(StaffDO s : staffList){
//                Driver driver = new Driver();
//                driver.setCode(""+s.getUserId());
//                driver.setName(s.getStaffId());
//                list.add(driver);
//            }
//        }
        return toJsonTrueData(list);
    }

    @RequestMapping(value = "/submit.html")
    @ResponseBody
    public String submit(String scaned_codes[], String nextStation, String carrier, String sendDriver, String plateNo, String logisticsNo) {
//        System.out.println(scaned_codes);
//        System.out.println(nextStation);
//        System.out.println(carrier);
//        System.out.println(sendDriver);
//        System.out.println(plateNo);
        Loading loading = new Loading();
        loading.setNextStation(nextStation);
        loading.setRider(sendDriver);
        loading.setCarrier(carrier);
        loading.setTruckNo(plateNo);

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
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
                //order = orderService.queryByOrderNo(merchantId, scaned_codes);

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
        return toJsonTrueMsg();
    }

//    private boolean isInteger(String str) {
//        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        return pattern.matcher(str).matches();
//    }

    public static class Driver {
        private String code;
        private String name;

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
