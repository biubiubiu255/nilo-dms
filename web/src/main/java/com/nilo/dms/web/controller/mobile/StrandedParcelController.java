package com.nilo.dms.web.controller.mobile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nilo.dms.web.controller.BaseController;

@Controller
@RequestMapping("/mobile/StrandedParcelController")
public class StrandedParcelController extends BaseController {

    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String waybillNumber, String carrierSite, String detainedType, String remarks) {
        System.out.println("=============================");
        System.out.println(waybillNumber);
        System.out.println(carrierSite);
        System.out.println(detainedType);
        System.out.println(remarks);
        System.out.println("=========================================");
        return "true";
    }
}
