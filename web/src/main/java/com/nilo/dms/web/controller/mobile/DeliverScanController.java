package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.order.LoadingService;
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
    public String test(String scaned_codes,String rider,String logisticsNo ) {
        System.out.println(scaned_codes);
//        for (int i=0; i<scaned_codes.length;i++){
//            System.out.println(scaned_codes[i]);
//        }
        System.out.println(rider);
        return "true";
    }

}
