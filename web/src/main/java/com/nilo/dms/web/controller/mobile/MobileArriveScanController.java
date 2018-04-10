package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DeliveryOrderOptDao;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
@RequestMapping("/mobile/arrive")
public class MobileArriveScanController extends BaseController {

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private DeliveryOrderOptDao deliveryOrderOptDao;

    @RequestMapping(value = "/scan.html")
    public String toPage() {
        return "mobile/network/arrive_scan/arriveScan";
    }

    @ResponseBody
    @RequestMapping(value = "/check.html")
    public String check(String code) {

        Long a = deliveryOrderOptDao.getStateByOrderNo(code);
        if (a == null) {
            return toJsonErrorMsg("There is no OrderNo");
        }
        if (a == 35 || a == 40 || a == 50 || a == 60) {
            return toJsonErrorMsg("There are restrictions on this order");
        }
        return toJsonTrueMsg();
    }


    @RequestMapping(value = "/submit.html")
    @ResponseBody
    public String submit(String scanedCodes, String logisticsNos) {
        try {
            String[] logisticsNoArray = scanedCodes.split(",");
            if (null != logisticsNoArray && logisticsNoArray.length > 0) {
                waybillService.arrive(Arrays.asList(logisticsNoArray));
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }

        return toJsonTrueMsg();
    }
}
