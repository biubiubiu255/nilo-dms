package com.nilo.dms.web.controller.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
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
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
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
    private OrderService orderService;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private ThirdExpressDao thirdExpressDao;
    @Autowired
    private ThirdDriverDao thirdDriverDao;
    @Autowired
    private StaffDao staffDao;

    @RequestMapping(value = "/print.html")
    public String print(Model model, HttpServletRequest request) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = request.getParameter("loadingNo");
        //查询详情
        Loading loading = loadingService.queryByLoadingNo(merchantId, loadingNo);
        
        String temp_str="";     
        Date dt = new Date();     
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        temp_str=sdf.format(dt); 
        
        model.addAttribute("date_str",temp_str);
        model.addAttribute("loading", loading);
        
        return "loading/print";
    }
    
    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "loading/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String list(String loadingNo,Integer loadingStatus) {

        Principal me = (Principal)SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<Loading> list = loadingService.queryBy(merchantId,loadingNo,loadingStatus,page);
        return toPaginationLayUIData(page,list);
    }

    @RequestMapping(value = "/loadingScanPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model, HttpServletRequest request) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("riderList", getRiderList(merchantId));

        //第三方快递公司及自提点
        List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(merchantId));
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));

        List<NextStation> list = new ArrayList<>();
        for(ThirdExpressDO e : expressDOList){
            NextStation s = new NextStation();
            s.setCode(e.getExpressCode());
            s.setName(e.getExpressName());
            s.setType("T");
            list.add(s);
        }
        for(DistributionNetworkDO n:networkDOList){
            NextStation s = new NextStation();
            s.setCode(""+n.getId());
            s.setName(n.getName());
            s.setType("N");
            list.add(s);
        }
        model.addAttribute("nextStation",list);
        model.addAttribute("thirdCarrier",expressDOList);

        return "loading/loading_scan";
    }

    @RequestMapping(value = "/detailsPage.html", method = RequestMethod.GET)
    public String detailsPage(Model model, HttpServletRequest request) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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
    public String addLoading(Loading loading,String deliveryRider,String sendDriver) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            if(StringUtil.isEmpty(deliveryRider) && StringUtil.isEmpty(sendDriver)){
                throw new IllegalArgumentException("Rider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if(StringUtil.isNotEmpty(deliveryRider)) {
                loading.setRider(deliveryRider);
            }else{
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

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            loadingService.ship(merchantId, loadingNo, me.getUserId());
        } catch (Exception e) {
            log.error("ship failed. loadingNo:{}", loadingNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
    @ResponseBody
    @RequestMapping(value = "/deleteLoading.html")
    public String deleteLoading(String loadingNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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
        for(ThirdDriverDO d : thirdDriver){
            driver = new Driver();
            driver.setCode(d.getDriverId());
            driver.setName(d.getDriverName());
            list.add(driver);
        }

        return toJsonTrueData(list);
    }
    private  boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static class Driver{
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

    public static class NextStation{
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
