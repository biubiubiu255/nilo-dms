package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/arriveScan")
public class ArriveScanController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderOptLogService orderOptLogService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WaybillScanDao waybillScanDao;
    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;

    @RequestMapping(value = "/arriveScanPage.html", method = RequestMethod.GET)
    public String arriveScanPage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        WaybillScanDO scanDO = new WaybillScanDO();
        scanDO.setMerchantId(Long.parseLong(merchantId));
        scanDO.setScanBy(me.getUserId());
        scanDO.setScanNo("" + IdWorker.getInstance().nextId());
        waybillScanDao.insert(scanDO);

        model.addAttribute("scanNo", scanDO.getScanNo());

        return "arrive_scan/arrive_scan";
    }


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "arrive_scan/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(String orderNo, String fromTime, String toTime) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        Long fromTimeL = null;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeL = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        Long toTimeL = null;
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeL = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }
        List<DeliveryOrderOpt> list = orderOptLogService.queryBy(merchantId, OptTypeEnum.ARRIVE_SCAN.getCode(), orderNo, me.getUserId(), fromTimeL, toTimeL, page);
        return toPaginationLayUIData(page, list);
    }

    @ResponseBody
    @RequestMapping(value = "/scanList.html")
    public String scanList(String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DeliveryOrder> list = new ArrayList<>();
        Pagination pagination = new Pagination(0, 100);

        if (StringUtil.isEmpty(scanNo)) return toPaginationLayUIData(pagination, list);

        List<WaybillScanDetailsDO> scanDetailsDOList = waybillScanDetailsDao.queryByScanNo( scanNo);
        if (scanDetailsDOList == null) return toPaginationLayUIData(pagination, list);

        List<String> orderNos = new ArrayList<>();
        for (WaybillScanDetailsDO details : scanDetailsDOList) {
            orderNos.add(details.getOrderNo());
        }
        list = orderService.queryByOrderNos(merchantId, orderNos);
        pagination.setTotalCount(list.size());
        return toPaginationLayUIData(pagination, list);
    }

    @ResponseBody
    @RequestMapping(value = "/scan.html")
    public String scan(String orderNo, String scanNo) {

        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        waybillScanDetailsDao.insert(scanDetailsDO);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/updateWeight.html")
    public String updateWeight(String orderNo, String scanNo, Double weight) {


        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        scanDetailsDO.setWeight(weight);
        waybillScanDetailsDao.update(scanDetailsDO);
        return toJsonTrueMsg();
    }
    @ResponseBody
    @RequestMapping(value = "/deleteDetails.html")
    public String deleteDetails(String orderNo, String scanNo) {


        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        waybillScanDetailsDao.deleteBy(orderNo,scanNo);
        return toJsonTrueMsg();
    }
    @ResponseBody
    @RequestMapping(value = "/arrive.html")
    public String arrive(String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            orderService.arrive(merchantId, scanNo, me.getUserId());
        } catch (Exception e) {
            log.error("arrive failed. scanNo:{}", scanNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();

    }
}
