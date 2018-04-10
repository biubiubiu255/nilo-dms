package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/arriveScan")
public class ArriveScanController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WaybillService waybillService;
    @Autowired
    private WaybillScanDao waybillScanDao;
    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;

    @RequestMapping(value = "/scanPage.html", method = RequestMethod.GET)
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

    @ResponseBody
    @RequestMapping(value = "/scanList.html")
    public String scanList(String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DeliveryOrder> list = new ArrayList<>();
        Pagination pagination = new Pagination(0, 100);

        if (StringUtil.isEmpty(scanNo)) return toPaginationLayUIData(pagination, list);

        List<WaybillScanDetailsDO> scanDetailsDOList = waybillScanDetailsDao.queryByScanNo(scanNo);
        if (scanDetailsDOList == null) return toPaginationLayUIData(pagination, list);

        List<String> orderNos = new ArrayList<>();
        for (WaybillScanDetailsDO details : scanDetailsDOList) {
            orderNos.add(details.getOrderNo());
        }
        list = waybillService.queryByOrderNos(merchantId, orderNos);

        for (DeliveryOrder o : list) {
            for (WaybillScanDetailsDO d : scanDetailsDOList) {
                if (StringUtil.equals(d.getOrderNo(), o.getOrderNo()) && d.getWeight() != null) {
                    o.setWeight(d.getWeight());
                    break;
                }
            }
        }

        pagination.setTotalCount(list.size());
        return toPaginationLayUIData(pagination, list);
    }

    @ResponseBody
    @RequestMapping(value = "/scan.html")
    public String scan(String orderNo, String scanNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        DeliveryOrder deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);
        if (deliveryOrder == null) throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, orderNo);

        WaybillScanDetailsDO query = waybillScanDetailsDao.queryBy(orderNo, scanNo);
        if (query != null) throw new DMSException(BizErrorCode.ALREADY_SCAN, orderNo);
        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        waybillScanDetailsDao.insert(scanDetailsDO);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/updateWeight.html")
    public String updateWeight(String orderNo, String scanNo, String weight) {

        if (!NumberUtils.isNumber(weight)) {
            return toJsonErrorMsg(BizErrorCode.WEIGHT_MORE_THAN_0.getDescription());
        }
        Double w = Double.parseDouble(weight);
        if (w <= 0) {
            return toJsonErrorMsg(BizErrorCode.WEIGHT_MORE_THAN_0.getDescription());

        }

        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        scanDetailsDO.setWeight(w);
        waybillScanDetailsDao.update(scanDetailsDO);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteDetails.html")
    public String deleteDetails(String orderNo, String scanNo) {


        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        waybillScanDetailsDao.deleteBy(orderNo, scanNo);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/arrive.html")
    public String arrive(String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            waybillService.arrive(merchantId, scanNo, "" + me.getNetworks().get(0), me.getUserId());
        } catch (Exception e) {
            log.error("arrive failed. scanNo:{}", scanNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();

    }
}
