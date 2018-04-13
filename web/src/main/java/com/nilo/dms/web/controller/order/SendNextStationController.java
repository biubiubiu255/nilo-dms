package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.Waybill;
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
    private ThirdDriverDao thirdDriverDao;

    @Autowired
    private HandleThirdDao handleThirdDao;

    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;



    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "waybill/send_nextStation/list";
    }


    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String list(SendThirdHead sendThirdHead){
        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();
        sendThirdHead.setType("package");
        List<SendThirdHead> sendThirdHeads = sendThirdService.queryHead(sendThirdHead, page);
        return toPaginationLayUIData(page, sendThirdHeads);
    }


    @RequestMapping(value = "/detail.html")
    public String detail(Model model, String handleNo) {
        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();
        //大包
        if (handleNo==null){
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        SendThirdHead sendThirdHead = sendThirdService.queryDetailsByHandleNo(handleNo);

        model.addAttribute("pack", sendThirdHead);
        //这里需要传入一个json，给layui解析，所有这里查询出小包列表后，包装成layui格式，jsp先格式化在变量属性里，生成静态页面时，js再解析字符串成为对象进行渲染
        model.addAttribute("smallsJson", toPaginationLayUIData(page, sendThirdHead.getList()));
        return "waybill/send_nextStation/details";
    }


    @RequestMapping(value = "/print.html")
    public String print(Model model, String loadingNo) {
        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();
        //大包
        SendThirdHead head = handleThirdDao.queryBigByHandleNo(Long.valueOf(me.getMerchantId()), loadingNo);
        if (head == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        List<Waybill> waybills = sendThirdService.querySmallsPlus(me.getMerchantId(), loadingNo);

        model.addAttribute("pack", head);
        model.addAttribute("smalls", waybills);

        return "waybill/send_nextStation/print";
    }


    //返回页面
    @RequestMapping(value = "/addLoadingPage.html", method = RequestMethod.GET)
    public String addLoadingPage(Model model) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
        List<PackageController.NextStation> list = new ArrayList<>();

        for (DistributionNetworkDO n : networkDOList) {
            PackageController.NextStation s = new PackageController.NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            list.add(s);
        }

        List<ThirdExpressDO> expressList = userService.findExpressesAll(page);


        model.addAttribute("nextStations", list);
        model.addAttribute("expressList", expressList);

        return "waybill/send_nextStation/loading_scan";
    }

    @ResponseBody
    @RequestMapping(value = "/scanSmallPack.html")
    public String scanSmallPack(String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        WaybillDO waybillDO = waybillDao.queryByOrderNo(Long.valueOf(merchantId), orderNo);

        if (StringUtil.isEmptys(waybillDO, waybillDO.getIsPackage()) || !waybillDO.getIsPackage().equals("1")){
            return toJsonErrorMsg("Order No or no packaging");
        }
        return toJsonTrueData(toPaginationLayUIData(page, waybillDO));
    }

    @ResponseBody
    @RequestMapping(value = "/addLoading.html", method = RequestMethod.POST)
    public String addLoading(String[] smallPack, SendThirdHead sendThirdHead, Integer saveStutus, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Principal me = SessionLocal.getPrincipal();
        Long merchantId = Long.valueOf(me.getMerchantId());

        //这里有两种增加方式，一种是页面之间添加，所以session-model里没有值，全在当前参数里，另外一种是站点在session-model里，driver在当前参数
        //从session取出刚刚打包好的大包发运信息（下一站点ID、名字）

        if(StringUtil.isEmpty(sendThirdHead.getNetworkCode()) || StringUtil.isEmpty(sendThirdHead.getNetworkCode())){
            if(session.getAttribute("packageInfo")==null){
                throw new DMSException(BizErrorCode.NOT_STATION_INFO);
            }
            SendThirdHead packageInfo = (SendThirdHead)session.getAttribute("packageInfo");
            if(StringUtil.isEmpty(packageInfo.getNetworkCode()) || StringUtil.isEmpty(packageInfo.getNextStation())){
                throw new DMSException(BizErrorCode.NOT_STATION_INFO);
            }else {
                sendThirdHead.setNetworkCode(packageInfo.getNetworkCode());
                sendThirdHead.setNextStation(packageInfo.getNextStation());
            }
        }


        //加上刚刚的站点信息，当前的操作信息，小包信息，合并写入系统
        sendThirdHead.setMerchantId(merchantId);
        sendThirdHead.setHandleBy(Long.valueOf(me.getUserId()));
        sendThirdHead.setHandleName(me.getUserName());
        sendThirdHead.setType("package");
        sendThirdHead.setStatus(saveStutus);

        sendThirdService.insertBigAndSmall(merchantId, sendThirdHead, smallPack);


        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getDriver.html", method = RequestMethod.POST)
    public String getDriver(String expressCode) {

        List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(expressCode);
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



    @ResponseBody
    @RequestMapping(value = "/updateStatus.html", method = RequestMethod.POST)
    public String updateStatus(String handleNo, Integer status) {
        sendThirdService.ship(handleNo);
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


        session.setAttribute("packageInfo", sendThirdHead);
        model.addAttribute("packList", toPaginationLayUIData(page, scanDetailList));
        model.addAttribute("expressList", expressList);

        return "waybill/send_nextStation/edit";
    }



}
