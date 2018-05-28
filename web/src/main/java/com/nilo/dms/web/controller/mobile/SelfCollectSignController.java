package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.web.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/rider/self")

public class SelfCollectSignController extends BaseController {

	

    @RequestMapping(value = "/self.html")
    public String customers() {
        return "mobile/rider/self/self";
    }
	
    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String logisticsNo,String signer,String idNo,String danxuan) {
        System.out.println("+++++++++++++++++++++++++++++");
        System.out.println(logisticsNo);
        System.out.println(signer);
        System.out.println(idNo);
        System.out.println(danxuan);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        return "true";
    }
    
}
