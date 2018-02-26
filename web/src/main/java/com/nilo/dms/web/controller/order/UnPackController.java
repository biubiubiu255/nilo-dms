package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.DeliveryOrderParameter;
import com.nilo.dms.service.order.model.PackageRequest;
import com.nilo.dms.service.order.model.UnpackRequest;
import com.nilo.dms.web.controller.BaseController;
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
@RequestMapping("/order/unpack")
public class UnPackController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private WaybillScanDao waybillScanDao;
    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;

    private static final String PACKAGE = "package";


    @RequestMapping(value = "/unpackScanPage.html", method = RequestMethod.GET)
    public String unpackScanPage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        WaybillScanDO scanDO = new WaybillScanDO();
        scanDO.setMerchantId(Long.parseLong(merchantId));
        scanDO.setScanBy(me.getUserId());
        scanDO.setScanNo("" + IdWorker.getInstance().nextId());
        waybillScanDao.insert(scanDO);

        model.addAttribute("scanNo", scanDO.getScanNo());

        return "unpack/unpack";
    }

    @ResponseBody
    @RequestMapping(value = "/scanList.html")
    public String scanList(String scanNo, String packageNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<UnpackInfo> list = new ArrayList<>();
        Pagination pagination = new Pagination(0, 100);

        if (StringUtil.isEmpty(scanNo)) return toPaginationLayUIData(pagination, list);

        List<WaybillScanDetailsDO> scanDetailsDOList = waybillScanDetailsDao.queryByScanNo(scanNo);
        List<DeliveryOrder> orderList = orderService.queryByPackageNo(merchantId, packageNo);
        if (orderList == null) throw new DMSException(BizErrorCode.PACKAGE_NO_ERROR);
        for (DeliveryOrder o : orderList) {
            UnpackInfo i = new UnpackInfo();
            i.setOrderNo(o.getOrderNo());
            i.setOrderType(o.getOrderType());
            i.setReferenceNo(o.getReferenceNo());
            i.setWeight(o.getWeight());
            for (WaybillScanDetailsDO d : scanDetailsDOList) {
                if (StringUtil.equals(d.getOrderNo(), o.getOrderNo())) {
                    i.setArrived(true);
                    break;
                }
            }
            list.add(i);
        }

        pagination.setTotalCount(list.size());
        return toPaginationLayUIData(pagination, list);
    }

    @ResponseBody
    @RequestMapping(value = "/scan.html")
    public String scanner(String orderNo, String scanNo, String type) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);
        if (deliveryOrder == null) throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, orderNo);
        if (StringUtil.equals(type, PACKAGE) && !deliveryOrder.isPackage()) {
            throw new DMSException(BizErrorCode.PACKAGE_NO_ERROR);
        }
        WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
        scanDetailsDO.setScanNo(scanNo);
        scanDetailsDO.setOrderNo(orderNo);
        waybillScanDetailsDao.insert(scanDetailsDO);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/unpack.html")
    public String unpack( String scanNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<WaybillScanDetailsDO> scanDetailList = waybillScanDetailsDao.queryByScanNo(scanNo);
        List<String> orderNos = new ArrayList<>();
        for (WaybillScanDetailsDO d : scanDetailList) {
            orderNos.add(d.getOrderNo());
        }
        UnpackRequest request = new UnpackRequest();
        request.setMerchantId(merchantId);
        request.setOptBy(me.getUserId());
        request.setNetworkId(me.getNetworks().get(0));
        request.setOrderNos(orderNos);
        orderService.unpack(request);

        return toJsonTrueMsg();
    }
    
    public static class UnpackInfo {
        private String orderNo;
        private Double weight;
        private String referenceNo;
        private String orderType;
        private boolean arrived;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public boolean isArrived() {
            return arrived;
        }

        public void setArrived(boolean arrived) {
            this.arrived = arrived;
        }
    }

}
