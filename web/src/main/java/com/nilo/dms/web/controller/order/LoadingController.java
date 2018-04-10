package com.nilo.dms.web.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.model.LoadingDetails;
import com.nilo.dms.service.order.model.ShipParameter;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.web.controller.BaseController;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/loading")
public class LoadingController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoadingService loadingService;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private ThirdExpressDao thirdExpressDao;
    @Autowired
    private ThirdDriverDao thirdDriverDao;

    @RequestMapping(value = "/print.html")
    public String print(Model model, HttpServletRequest request) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = request.getParameter("loadingNo");
        //查询详情
        Loading loading = loadingService.queryByLoadingNo(merchantId, loadingNo);

        double totalAmount = 0d;
        for (LoadingDetails d : loading.getDetailsList()) {
            totalAmount = totalAmount + d.getWaybill().getNeedPayAmount();
        }
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("loading", loading);

        return "loading/print";
    }

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "loading/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String list(String loadingNo, Integer loadingStatus) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<Loading> list = loadingService.queryBy(merchantId, loadingNo, loadingStatus, page);
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/loadingScanPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model, HttpServletRequest request) {
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

        return "loading/loading_scan";
    }

    @RequestMapping(value = "/detailsPage.html", method = RequestMethod.GET)
    public String detailsPage(Model model, HttpServletRequest request) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        String loadingNo = request.getParameter("loadingNo");
        if (StringUtil.isNotEmpty(loadingNo)) {
            Loading loading = loadingService.queryByLoadingNo(merchantId, loadingNo);
            model.addAttribute("loading", loading);
        }
        model.addAttribute("riderList", getRiderList(merchantId));
        return "loading/details";
    }

    @ResponseBody
    @RequestMapping(value = "/addLoading.html")
    public String addLoading(Loading loading, String deliveryRider, String sendDriver) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            if (StringUtil.isEmpty(deliveryRider) && StringUtil.isEmpty(sendDriver)) {
                throw new IllegalArgumentException("PdaRider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if (StringUtil.isNotEmpty(deliveryRider)) {
                loading.setRider(deliveryRider);
            } else {
                loading.setRider(sendDriver);
            }
            loadingNo = loadingService.addLoading(loading);
        } catch (Exception e) {
            log.error("addLoading failed. loading:{}", loading, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(loadingNo);
    }

    @ResponseBody
    @RequestMapping(value = "/loadingScan.html")
    public String loadingScan(String orderNo, String loadingNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            //装车
            loadingService.loadingScan(merchantId, loadingNo, orderNo, me.getUserId());
        } catch (Exception e) {
            log.error("loadingScan failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/loadingDetails.html")
    public String loadingDetails(String loadingNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        if (StringUtil.isEmpty(loadingNo)) {
            return toPaginationLayUIData(page, null);
        }
        Loading loading = loadingService.queryByLoadingNo(merchantId, loadingNo);
        page.setTotalCount(loading.getDetailsList().size());
        return toPaginationLayUIData(page, loading.getDetailsList());
    }

    @ResponseBody
    @RequestMapping(value = "/ship.html")
    public String ship(String loadingNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            ShipParameter parameter = new ShipParameter();
            parameter.setMerchantId(merchantId);
            parameter.setOptBy(me.getUserId());
            parameter.setLoadingNo(loadingNo);
            parameter.setNetworkId("" + me.getNetworks().get(0));
            loadingService.ship(parameter);
        } catch (Exception e) {
            log.error("ship failed. loadingNo:{}", loadingNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteLoading.html")
    public String deleteLoading(String loadingNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            loadingService.deleteLoading(merchantId, loadingNo, me.getUserId());
        } catch (Exception e) {
            log.error("ship failed. loadingNo:{}", loadingNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteDetails.html")
    public String deleteDetails(String loadingNo, String orderNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            loadingService.deleteLoadingDetails(merchantId, loadingNo, orderNo, me.getUserId());
        } catch (Exception e) {
            log.error("ship failed. loadingNo:{}", loadingNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getThirdDriver.html")
    public String getNextStationDriver(String code) {

        List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(code);
        List<Driver> list = new ArrayList<>();
        Driver driver = new Driver();
        for (ThirdDriverDO d : thirdDriver) {
            driver = new Driver();
            driver.setCode(d.getDriverId());
            driver.setName(d.getDriverName());
            list.add(driver);
        }

        return toJsonTrueData(list);
    }

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
