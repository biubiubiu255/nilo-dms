package com.nilo.dms.web.controller.mobile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nilo.dms.web.controller.BaseController;

@Controller
@RequestMapping("/mobile/CODSignController")
public class CODSignController extends BaseController {
    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String logisticsNo, String serialNumber, String signer, String idNo,String danxuan) {
        System.out.println("+++++++++++++++++++++++++++++");
        System.out.println(logisticsNo);
        System.out.println(serialNumber);
        System.out.println(signer);
        System.out.println(idNo);
        System.out.println(danxuan);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        return "true";
    }
}
