package com.nilo.dms.web.controller.mobile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/SignScanController")
public class SignScanController {
    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String waybillNumber, String recipient, String idNumber) {
        System.out.println("+++++++++++++++++++++++++++++");
        System.out.println(waybillNumber);
        System.out.println(recipient);
        System.out.println(idNumber);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

        return "true";
    }
}
