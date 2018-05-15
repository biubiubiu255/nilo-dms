package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.order.PackageRequest;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.dto.order.WaybillParameter;
import com.nilo.dms.web.controller.BaseController;
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
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nilo.dms.common.Constant.IS_PACKAGE;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/package")
public class PackageController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WaybillService waybillService;
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

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        WaybillParameter parameter = new WaybillParameter();
        parameter.setMerchantId(merchantId);
        parameter.setOrderNo(packageNo);
        parameter.setIsPackage(IS_PACKAGE);
        List<Integer> status = new ArrayList<>();
        status.add(DeliveryOrderStatusEnum.ARRIVED.getCode());
/*
        status.add(DeliveryOrderStatusEnum.SEND.getCode());
*/
        //parameter.setStatus(status);
        List<Waybill> list = waybillService.queryWaybillBy(parameter, page);
        //下面都是list中的createBy转对应的名字
        Set<String> userList = new HashSet();
        for (Waybill e : list){
            userList.add(e.getCreatedBy());
        }
        List<User> userInfoList = userService.findByUserIds(merchantId, new ArrayList<>(userList));
        for (Waybill e : list){
            for (User u : userInfoList){
                if(e.getCreatedBy().equals(u.getUserId())){
                    e.setCreatedBy(u.getUserInfo().getName());
                }
            }
        }
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/packagePage.html", method = RequestMethod.GET)
    public String packagePage(Model model) {
        Principal me = SessionLocal.getPrincipal();
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

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Waybill order = null;
        try {
            order = waybillService.queryByOrderNo(merchantId, orderNo);

            if (order == null) throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, orderNo);

            WaybillScanDetailsDO scanDetailsDO = new WaybillScanDetailsDO();
            scanDetailsDO.setScanNo(scanNo);
            scanDetailsDO.setOrderNo(orderNo);
            waybillScanDetailsDao.insert(scanDetailsDO);

        } catch (Exception e) {
            log.error("loadingScan failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(order);
    }

    @ResponseBody
    @RequestMapping(value = "/addPackage.html")
    public String addLoading(PackageRequest packageRequest, String scanNo, HttpServletRequest request) {

        HttpSession session = request.getSession();

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String orderNo = "";
        try {

            List<WaybillScanDetailsDO> scanDetailList = waybillScanDetailsDao.queryByScanNo(scanNo);
            if (scanDetailList == null || scanDetailList.size() == 0) {
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

            orderNo = waybillService.addPackage(packageRequest);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }

        session.setAttribute("packageNo", orderNo);

        return toJsonTrueData(orderNo);
    }


    @RequestMapping(value = "/{orderNo}.html", method = RequestMethod.GET)
    public String details(Model model, @PathVariable String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        Waybill packageInfo = waybillService.queryByOrderNo(merchantId, orderNo);
        UserInfo userInfo = userService.findUserInfoByUserId(merchantId, packageInfo.getCreatedBy());
        List<Waybill> list = waybillService.queryByPackageNo(merchantId, orderNo);
        model.addAttribute("list", list);
        model.addAttribute("packageInfo", packageInfo);
        model.addAttribute("packageBy", userInfo.getName());
        return "package/details";
    }

    @RequestMapping(value = "/print/{orderNo}.html", method = RequestMethod.GET)
    public String print(Model model, @PathVariable String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Waybill packageInfo = waybillService.queryByOrderNo(merchantId, orderNo);
        List<Waybill> list = waybillService.queryByPackageNo(merchantId, orderNo);
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
