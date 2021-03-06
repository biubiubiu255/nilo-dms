package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.utils.BeanUtils;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.service.order.model.LoadingDetails;
import com.nilo.dms.service.order.model.ShipParameter;
import com.nilo.dms.service.org.model.Staff;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/waybill/rider_delivery")
public class RiderDeliveryController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private RiderDeliveryService riderDeliveryService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "waybill/rider_delivery/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String list(String loadingNo, Integer status){
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        RiderDeliveryDO riderDeliveryDO = new RiderDeliveryDO();
        riderDeliveryDO.setHandleNo(loadingNo);
        riderDeliveryDO.setStatus(status);
        Pagination page = getPage();
        List<RiderDeliveryDO> list = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDeliveryDO, page);
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/detail.html")
    public String detail(Model model, String loadingNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        Pagination page = getPage();
        //大包
        RiderDeliveryDO riderDeliveryDO = new RiderDeliveryDO();
        riderDeliveryDO.setHandleNo(loadingNo);
        List<RiderDeliveryDO> list = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDeliveryDO, page);
        riderDeliveryDO = list.get(0);

        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        riderDeliverySmallDO.setRider_handle_no(loadingNo);
        List<RiderDeliverySmallDO> smalls = riderDeliveryService.queryRiderDeliveryDetail(me.getMerchantId(), riderDeliverySmallDO, page);
        model.addAttribute("pack", riderDeliveryDO);

        //这里需要传入一个json，给layui解析，所有这里查询出小包列表后，包装成layui格式，jsp先格式化在变量属性里，生成静态页面时，js再解析字符串成为对象进行渲染
        model.addAttribute("smallsJson", toPaginationLayUIData(page, smalls));
        return "waybill/rider_delivery/details";
    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, String loadingNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        Pagination page = getPage();
        //大包
        RiderDeliveryDO riderDeliveryDO = new RiderDeliveryDO();
        riderDeliveryDO.setHandleNo(loadingNo);
        //这里因为查询是自定义，也就是以有参数，就有什么查询条件查询，所有统为list，这里只需要查一个大包，也只有一条结果，但还是得取get(0)
        List<RiderDeliveryDO> list = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDeliveryDO, page);
        riderDeliveryDO = list.get(0);


        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        riderDeliverySmallDO.setRider_handle_no(loadingNo);
        List<RiderDeliverySmallDO> smalls = riderDeliveryService.queryRiderDeliveryDetail(me.getMerchantId(), riderDeliverySmallDO, page);
        model.addAttribute("pack", riderDeliveryDO);
        model.addAttribute("smalls", smalls);
        return "waybill/rider_delivery/print";
    }

    //返回页面
    @RequestMapping(value = "/addLoadingPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<StaffDO> staffList = getRiderList(merchantId);
        model.addAttribute("riderList", staffList);

        return "waybill/rider_delivery/loading_scan";
    }

    @ResponseBody
    @RequestMapping(value = "/scanSmallPack.html")
    public String scanSmallPack(String orderNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        DeliveryOrderDO deliveryOrderDO = deliveryOrderDao.queryByOrderNo(Long.valueOf(merchantId), orderNo);
        if (deliveryOrderDO==null){
            return toJsonErrorMsg(" Order does not exist");
        }

        return toJsonTrueData(toPaginationLayUIData(page, deliveryOrderDO));
    }

    @ResponseBody
    @RequestMapping(value = "/addLoading.html", method = RequestMethod.POST)
    public String addLoading(String[] smallPack, String rider) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();

        String merchantId = me.getMerchantId();
        //System.out.println("本次测试 = " + smallPack.length);
        RiderDeliveryDO riderDeliveryDO = new RiderDeliveryDO();
        riderDeliveryDO.setMerchantId(Long.valueOf(merchantId));
        riderDeliveryDO.setHandleNo(SystemConfig.getNextSerialNo(merchantId.toString(), SerialTypeEnum.LOADING_NO.getCode()));
        riderDeliveryDO.setRider(rider);
        riderDeliveryDO.setHandleBy(Long.valueOf(me.getUserId()));
        riderDeliveryService.addRiderPackAndDetail(Long.valueOf(merchantId), riderDeliveryDO, smallPack);
        return toJsonTrueMsg();
    }
    

}
