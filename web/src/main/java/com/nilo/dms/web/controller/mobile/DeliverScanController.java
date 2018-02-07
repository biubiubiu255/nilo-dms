package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.LoadingController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/mobile/deliver")
public class DeliverScanController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoadingService loadingService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/scan.html")
    public String toPage(Model model, HttpServletRequest request) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("riderList", getRiderList(merchantId));

        return "mobile/network/deliver_scan/deliverScan";
    }

    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String scaned_codes[],String rider,String logisticsNo ) {
        Loading loading = new Loading();
        loading.setRider(rider);

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            System.out.println("*--------------------------");
            if(StringUtil.isEmpty(rider)){
                throw new IllegalArgumentException("Rider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if(StringUtil.isNotEmpty(rider)) {
                loading.setRider(rider);
            }
            loadingNo = loadingService.addLoading(loading);
        } catch (Exception e) {
            log.error("addLoading failed. loading:{}", loading, e);
            return toJsonErrorMsg(e.getMessage());
        }

        DeliveryOrder order = null;
        for (int i=0; i<scaned_codes.length; i++){
            System.out.println("====================");
            try {
                loadingService.loadingScan(merchantId, loadingNo, scaned_codes[i], me.getUserId());
                //order = orderService.queryByOrderNo(merchantId, scaned_codes);
                loadingService.ship(merchantId, loadingNo, me.getUserId());

            }catch (Exception e) {
                log.error("loadingScan failed. orderNo:{}", scaned_codes[i], e);
                return toJsonErrorMsg(e.getMessage());
            }

        }
        return toJsonTrueData(loadingNo);

    }

}
