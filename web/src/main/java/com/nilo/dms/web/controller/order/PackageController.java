package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.UserServiceImpl;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.nilo.dms.common.Constant.IS_PACKAGE;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/package")
public class PackageController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private WaybillScanDao waybillScanDao;
    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list() {
        return "package/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String list(String packageNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        DeliveryOrderParameter parameter = new DeliveryOrderParameter();
        parameter.setMerchantId(merchantId);
        parameter.setOrderNo(packageNo);
        parameter.setIsPackage(IS_PACKAGE);
        List<Integer> status = new ArrayList<>();
        status.add(DeliveryOrderStatusEnum.ARRIVED.getCode());
/*
        status.add(DeliveryOrderStatusEnum.SEND.getCode());
*/
        parameter.setStatus(status);
        List<DeliveryOrder> list = orderService.queryDeliveryOrderBy(parameter, page);
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/packagePage.html", method = RequestMethod.GET)
    public String packagePage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
        List<NextStation> list = new ArrayList<>();

        for (DistributionNetworkDO n : networkDOList) {
            NextStation s = new NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            list.add(s);
        }


        WaybillScanDO scanDO = new WaybillScanDO();
        scanDO.setMerchantId(Long.parseLong(merchantId));
        scanDO.setScanBy(me.getUserId());
        scanDO.setScanNo("" + IdWorker.getInstance().nextId());
        waybillScanDao.insert(scanDO);

        model.addAttribute("scanNo", scanDO.getScanNo());

        model.addAttribute("nextStation", list);
        return "package/package_scan";
    }

    @ResponseBody
    @RequestMapping(value = "/packageScan.html")
    public String packageScan(String orderNo, String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        DeliveryOrder order = null;
        try {
            WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
            scanDetailsDO.setScanNo(scanNo);
            scanDetailsDO.setOrderNo(orderNo);
            waybillScanDetailsDao.insert(scanDetailsDO);
            order = orderService.queryByOrderNo(merchantId, orderNo);
        } catch (Exception e) {
            log.error("loadingScan failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(order);
    }

    @ResponseBody
    @RequestMapping(value = "/addPackage.html")
    public String addLoading(PackageRequest packageRequest, String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String orderNo = "";
        try {

            List<WaybillScanDetailsDO> scanDetailList = waybillScanDetailsDao.queryByScanNo(scanNo);
            if(scanDetailList==null ||scanDetailList.size()==0){
                throw new DMSException(BizErrorCode.PACKAGE_EMPTY);
            }
            List<String> orderNos = new ArrayList<>();
            for (WaybillScanDetailsDO d : scanDetailList) {
                orderNos.add(d.getOrderNo());
            }
            packageRequest.setOrderNos(orderNos);
            packageRequest.setMerchantId(merchantId);
            packageRequest.setOptBy(me.getUserId());
            if (me.getNetworks() != null && me.getNetworks().size() != 0) {
                packageRequest.setNetworkId(me.getNetworks().get(0));
            }

            orderNo = orderService.addPackage(packageRequest);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(orderNo);
    }


    @RequestMapping(value = "/{orderNo}.html", method = RequestMethod.GET)
    public String details(Model model, @PathVariable String orderNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        DeliveryOrder packageInfo = orderService.queryByOrderNo(merchantId, orderNo);
        UserInfo userInfo = userService.findUserInfoByUserId(merchantId, packageInfo.getCreatedBy());
        List<DeliveryOrder> list = orderService.queryByPackageNo(merchantId, orderNo);
        model.addAttribute("list", list);
        model.addAttribute("packageInfo", packageInfo);
        model.addAttribute("packageBy", userInfo.getName());
        return "package/details";
    }

    @RequestMapping(value = "/print/{orderNo}.html", method = RequestMethod.GET)
    public String print(Model model, @PathVariable String orderNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        DeliveryOrder packageInfo = orderService.queryByOrderNo(merchantId, orderNo);
        List<DeliveryOrder> list = orderService.queryByPackageNo(merchantId, orderNo);
        model.addAttribute("list", list);
        model.addAttribute("packageInfo", packageInfo);
        return "package/print";
    }


    public static class NextStation {
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
