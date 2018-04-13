package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.RiderDelivery;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private WaybillDao waybillDao;

    @Autowired
    private HandleRiderDao handleRiderDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "waybill/rider_delivery/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String list(RiderDelivery riderDelivery) {

        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();
        List<RiderDelivery> list = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDelivery, page);
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/detail.html")
    public String detail(Model model, String loadingNo) {
        Principal me = SessionLocal.getPrincipal();

        Pagination page = getPage();
        //大包
        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setHandleNo(loadingNo);
        List<RiderDelivery> list = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDelivery, page);
        riderDelivery = list.get(0);

        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        riderDeliverySmallDO.setHandleNo(loadingNo);
        List<RiderDeliverySmallDO> smalls = riderDeliveryService.queryRiderDeliveryDetail(me.getMerchantId(), riderDeliverySmallDO, page);
        model.addAttribute("pack", riderDelivery);

        //这里需要传入一个json，给layui解析，所有这里查询出小包列表后，包装成layui格式，jsp先格式化在变量属性里，生成静态页面时，js再解析字符串成为对象进行渲染
        model.addAttribute("smallsJson", toPaginationLayUIData(page, smalls));
        return "waybill/rider_delivery/details";
    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, String loadingNo) {

        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();
        //大包
        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setHandleNo(loadingNo);
        //这里因为查询是自定义，也就是以有参数，就有什么查询条件查询，所有统为list，这里只需要查一个大包，也只有一条结果，但还是得取get(0)
        List<RiderDelivery> list = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDelivery, page);
        riderDelivery = list.get(0);


        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        riderDeliverySmallDO.setHandleNo(loadingNo);
        List<RiderDeliverySmallDO> smalls = riderDeliveryService.queryRiderDeliveryDetail(me.getMerchantId(), riderDeliverySmallDO, page);
        model.addAttribute("pack", riderDelivery);
        model.addAttribute("smalls", smalls);
        return "waybill/rider_delivery/print";
    }

    //返回页面
    @RequestMapping(value = "/addLoadingPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model) {
        List<StaffDO> staffList = getRiderList();
        model.addAttribute("riderList", staffList);
        return "waybill/rider_delivery/loading_scan";
    }

    @ResponseBody
    @RequestMapping(value = "/scanSmallPack.html")
    public String scanSmallPack(String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();

        WaybillDO waybillDO = waybillDao.queryByOrderNo(Long.valueOf(merchantId), orderNo);
        if (waybillDO == null) {
            return toJsonErrorMsg("Order does not exist");
        }
        return toJsonTrueData(toPaginationLayUIData(page, waybillDO));
    }

    @ResponseBody
    @RequestMapping(value = "/addLoading.html", method = RequestMethod.POST)
    public String addLoading(String[] smallPack, String rider, Integer saveStutus) {
        Principal me = SessionLocal.getPrincipal();

        String merchantId = me.getMerchantId();
        //System.out.println("本次测试 = " + smallPack.length);
        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setMerchantId(Long.valueOf(merchantId));
        riderDelivery.setHandleNo(SystemConfig.getNextSerialNo(merchantId.toString(), SerialTypeEnum.LOADING_NO.getCode()));
        riderDelivery.setRider(rider);
        riderDelivery.setHandleBy(Long.valueOf(me.getUserId()));
        riderDelivery.setStatus(saveStutus);
        riderDeliveryService.addRiderPackAndDetail(Long.valueOf(merchantId), riderDelivery, smallPack);
        if(riderDelivery.getStatus().equals(HandleRiderStatusEnum.SHIP.getCode())){
            riderDeliveryService.ship(riderDelivery.getHandleNo());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("handleNo", riderDelivery.getHandleNo());
        return toJsonTrueData(map);
    }

    @ResponseBody
    @RequestMapping(value = "/updateStatus.html", method = RequestMethod.POST)
    public String updateStatus(String handleNo, Integer status) {
        handleRiderDao.upBigStatus(handleNo, status);
        return toJsonTrueMsg();
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String editPage(String handleNo, Model model) {
        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setHandleNo(handleNo);

        Principal me = SessionLocal.getPrincipal();
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<WaybillDO> list = riderDeliveryService.queryRiderDeliveryDetailPlus(me.getMerchantId(), riderDelivery, page);
        String res = toJsonTrueData(list);

        List<RiderDelivery> templist = riderDeliveryService.queryRiderDelivery(me.getMerchantId(), riderDelivery, page);
        riderDelivery = templist.get(0);

        model.addAttribute("list", res);
        model.addAttribute("riderList", getRiderList());
        model.addAttribute("riderDelivery", riderDelivery);
        return "waybill/rider_delivery/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String edit(String[] smallPack, String handleNo, Integer saveStatus, String rider) {
        Principal me = SessionLocal.getPrincipal();
        String merchantId = me.getMerchantId();
        if (handleNo == null || smallPack.length == 0) {
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST);
        }
        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setHandleNo(handleNo);
        riderDelivery.setMerchantId(Long.valueOf(merchantId));
        riderDelivery.setRider(rider);
        riderDelivery.setStatus(HandleRiderStatusEnum.getEnum(saveStatus).getCode());
        riderDeliveryService.editRiderPackAndDetail(riderDelivery, smallPack);
        return toJsonTrueMsg();
    }


}
