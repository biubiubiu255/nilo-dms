package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendThirdService;
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
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/waybill/send_nextStation")
public class SendNextStationController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private SendThirdService sendThirdService;

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private HandleRiderDao handleRiderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;




    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "waybill/send_nextStation/list";
    }

/*

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String list(SendThirdHead sendThirdHead){
        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();

        List<SendThirdHead> list = sendThirdService.queryBigs(Long.valueOf(me.getMerchantId()), sendThirdHead, page);
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

*/
/*
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
        WaybillDO deliveryOrderDO = waybillDao.queryByOrderNo(Long.valueOf(merchantId), orderNo);
        if (deliveryOrderDO==null){
            return toJsonErrorMsg("Order does not exist");
        }

        return toJsonTrueData(toPaginationLayUIData(page, deliveryOrderDO));
    }
*//*


    @ResponseBody
    @RequestMapping(value = "/addLoading.html", method = RequestMethod.POST)
    public String addLoading(String[] smallPack, SendThirdHead sendThirdHead, Integer saveStutus, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Principal me = SessionLocal.getPrincipal();
        Long merchantId = Long.valueOf(me.getMerchantId());

        //从session取出刚刚打包好的大包发运信息（下一站点ID、名字）
        SendThirdHead packageInfo = (SendThirdHead)session.getAttribute("packageInfo");
        if(packageInfo.getNetwork_id()==null || packageInfo.getNextStation()==null){
            throw new DMSException(BizErrorCode.NOT_STATION_INFO);
        }
        sendThirdHead.setNetwork_id(packageInfo.getNetwork_id());
        sendThirdHead.setNextStation(packageInfo.getNextStation());

        //加上刚刚的站点信息，当前的操作信息，小包信息，合并写入系统
        sendThirdHead.setMerchantId(merchantId);
        sendThirdHead.setHandleBy(Long.valueOf(me.getUserId()));
        sendThirdHead.setStatus(saveStutus);
        sendThirdHead.setHandleNo(SystemConfig.getNextSerialNo(merchantId.toString(), SerialTypeEnum.LOADING_NO.getCode()));

        sendThirdHead.setHandle_time(Long.valueOf(System.currentTimeMillis()/1000).intValue());
        sendThirdService.insertBigAndSmall(merchantId, sendThirdHead, smallPack);

        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/updateStatus.html", method = RequestMethod.POST)
    public String updateStatus(String handleNo, Integer status) {
        handleRiderDao.upBigStatus(handleNo, status);
        return toJsonTrueMsg();
    }



    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String editPage(SendThirdHead sendThirdHead, String tempScanNo, Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();

        List<WaybillScanDetailsDO> scanDetailList = waybillScanDetailsDao.queryByScanNo(tempScanNo);

        String merchantId = SessionLocal.getPrincipal().getMerchantId();

        Pagination page = getPage();

        List<ThirdExpressDO> expressList = new ArrayList<ThirdExpressDO>();

        expressList = userService.findExpressesAll(page);


        //sendThirdHead.setHandleNo(SystemConfig.getNextSerialNo(merchantId, SerialTypeEnum.LOADING_NO.getCode()));
        //sendThirdHead.setStatus(0);
        //sendThirdHead.setHandleBy(Long.valueOf(SessionLocal.getPrincipal().getUserId()));

        //System.out.println("本次测试 = " + smallPack.length);


        session.setAttribute("packageInfo", sendThirdHead);
        model.addAttribute("packList", toPaginationLayUIData(page, scanDetailList));
        model.addAttribute("expressList", expressList);

        return "waybill/send_nextStation/add";
    }

    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String edit(String[] smallPack, String handleNo, Integer saveStatus, String rider) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        String merchantId = me.getMerchantId();
        if(handleNo==null || smallPack.length==0){
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST);
        }
        RiderDeliveryDO riderDeliveryDO = new RiderDeliveryDO();
        riderDeliveryDO.setHandleNo(handleNo);
        riderDeliveryDO.setMerchantId(Long.valueOf(merchantId));
        riderDeliveryDO.setRider(rider);
        riderDeliveryDO.setStatus(HandleRiderStatusEnum.getEnum(saveStatus).getCode());
        riderDeliveryService.editSmall(riderDeliveryDO, smallPack);
        riderDeliveryService.editBig(riderDeliveryDO);
        return toJsonTrueMsg();
    }*/



}
