package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/SendScanController")
public class SendScanController extends BaseController {
    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String[] arr) {
        for(int i = 0; i < arr.length; i ++) {
            System.out.println(arr[i]);
        }
        return "true";
    }
}
