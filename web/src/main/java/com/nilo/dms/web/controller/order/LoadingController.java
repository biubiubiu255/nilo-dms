package com.nilo.dms.web.controller.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.nilo.dms.dao.UserInfoDao;
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
    private UserInfoDao userInfoDao;

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
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制     
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
    public String list(String loadingNo, Integer loadingStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<Loading> list = loadingService.queryBy(merchantId, loadingNo, loadingStatus, page);
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/loadingScanPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model, HttpServletRequest request) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("riderList", getRiderList(merchantId));
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
    public String addLoading(Loading loading) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
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
        DeliveryOrder order = null;
        try {
            //装车
            loadingService.loadingScan(merchantId, loadingNo, orderNo, me.getUserId());
            order = orderService.queryByOrderNo(merchantId, orderNo);
        } catch (Exception e) {
            log.error("loadingScan failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(order);
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
}
