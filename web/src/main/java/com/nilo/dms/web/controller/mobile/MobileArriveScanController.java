package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.dao.WaybillLogDao;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/mobile/arrive")
public class MobileArriveScanController extends BaseController {

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private WaybillLogDao waybillLogDao;

    @RequestMapping(value = "/scan.html")
    public String toPage() {
        return "mobile/network/arrive_scan/arriveScan";
    }

    @ResponseBody
    @RequestMapping(value = "/check.html")
    public String check(String code) {

        Long a = waybillLogDao.getStateByOrderNo(code);
        if (a == null) {
            return toJsonErrorMsg("There is no OrderNo");
        }
/*        if (a == 35 || a == 40 || a == 50 || a == 60) {
            return toJsonErrorMsg("There are restrictions on this order");
        }*/
        return toJsonTrueMsg();
    }


    @RequestMapping(value = "/submit.html")
    @ResponseBody
    public String submit(String[] scanedCodes, String logisticsNo ) {

        if (scanedCodes.length > 0) {
            waybillService.arrive(new ArrayList<>(Arrays.asList(scanedCodes)));
        }

        return toJsonTrueMsg();
    }
}
