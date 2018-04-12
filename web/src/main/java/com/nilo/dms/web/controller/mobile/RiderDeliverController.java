package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/mobile/rider/deliver")
public class RiderDeliverController extends BaseController {
    @RequestMapping(value = "/toScan.html")
    public String customers() {
        return "mobile/rider/delivery/riderDelivery";
    }
}
