package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/waybill/third_express_delivery")
public class ThirdExpressDeliveryController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private SendThirdService sendThirdService;
    @Autowired
    private WaybillService waybillService;
    @Autowired
    private ThirdDriverDao thirdDriverDao;
    @Autowired
    private ThirdExpressDao thirdExpressDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "waybill/third_express_delivery/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String list(String handleNo, Integer status) {

        Pagination page = getPage();
        //构建查询条件
        SendThirdHead head = new SendThirdHead();
        head.setHandleNo(handleNo);
        head.setStatus(status);
        head.setType("waybill");
        List<SendThirdHead> list = sendThirdService.queryHead(head, page);
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/detail.html")
    public String detail(Model model, String loadingNo) {
        Principal me = SessionLocal.getPrincipal();

        SendThirdHead head = sendThirdService.queryDetailsByHandleNo(loadingNo);
        if (head == null) {
            throw new IllegalArgumentException();
        }
        model.addAttribute("pack", head);
        Pagination page = getPage();
        model.addAttribute("list", toPaginationLayUIData(page, head.getList()));

        List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(me.getMerchantId()));
        model.addAttribute("expressList", expressDOList);

        List<ThirdDriverDO> driverList = thirdDriverDao.findByExpressCode(head.getThirdExpressCode());
        model.addAttribute("driverList", driverList);

        return "waybill/third_express_delivery/details";
    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, String loadingNo) {
        Principal me = SessionLocal.getPrincipal();

        SendThirdHead head = sendThirdService.queryDetailsByHandleNo(loadingNo);
        List<Waybill> waybills = sendThirdService.querySmallsPlus(me.getMerchantId(), loadingNo);

        model.addAttribute("pack", head);
        model.addAttribute("smalls", waybills);
        //List<Waybill> smalls = sendThirdService.querySmallsPlus(me.getMerchantId(), loadingNo);
        //model.addAttribute("smalls", smalls);
        return "waybill/third_express_delivery/print";
    }

    //返回页面
    @RequestMapping(value = "/addLoadingPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model) {
        Principal me = SessionLocal.getPrincipal();

        List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(me.getMerchantId()));
        model.addAttribute("expressList", expressDOList);
        return "waybill/third_express_delivery/loading_scan";
    }

    @ResponseBody
    @RequestMapping(value = "/scanSmallPack.html")
    public String scanSmallPack(String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        Waybill waybillDO = waybillService.queryByOrderNo(merchantId, orderNo);
        if (waybillDO == null) {
            return toJsonErrorMsg("Order does not exist");
        }
        return toJsonTrueData(toPaginationLayUIData(page, waybillDO));
    }

    @ResponseBody
    @RequestMapping(value = "/addLoading.html", method = RequestMethod.POST)
    public String addLoading(String rider, String[] smallPack, String thirdExpressCode, Integer status) {
        Principal me = SessionLocal.getPrincipal();

        String merchantId = me.getMerchantId();
        SendThirdHead head = new SendThirdHead();
        head.setMerchantId(Long.valueOf(merchantId));
        head.setThirdExpressCode(thirdExpressCode);
        head.setHandleBy(Long.valueOf(me.getUserId()));
        head.setDriver(rider);
        head.setStatus(status);
        head.setType("thirdExpress");
        head.setHandleName(me.getUserName());
        sendThirdService.insertBigAndSmall(Long.parseLong(merchantId), head, smallPack);
        //如果初次写入直接是ship的话，这里再批量ship一下
        if(head.getStatus().equals(HandleRiderStatusEnum.SHIP.getCode())){
            sendThirdService.ship(head.getHandleNo());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("handleNo", head.getHandleNo());
        return toJsonTrueData(map);
    }

    @ResponseBody
    @RequestMapping(value = "/ship.html", method = RequestMethod.POST)
    public String updateStatus(String handleNo) {
        sendThirdService.ship(handleNo);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String edit(String[] smallPack, String handleNo, Integer saveStatus, String rider) {

        SendThirdHead head = new SendThirdHead();
        head.setHandleName(handleNo);
        head.setDriver(rider);
        sendThirdService.edit(head, smallPack);
        if (saveStatus == 1) {
            sendThirdService.ship(handleNo);
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getThirdDriver.html")
    public String getNextStationDriver(String code) {

        List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(code);
        List<LoadingController.Driver> list = new ArrayList<>();
        LoadingController.Driver driver = new LoadingController.Driver();
        for (ThirdDriverDO d : thirdDriver) {
            driver = new LoadingController.Driver();
            driver.setCode(d.getDriverId());
            driver.setName(d.getDriverName());
            list.add(driver);
        }

        return toJsonTrueData(list);
    }

}
