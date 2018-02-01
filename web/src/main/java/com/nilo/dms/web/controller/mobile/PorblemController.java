package com.nilo.dms.web.controller.mobile;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
@RequestMapping("/mobile/PorblemController")
public class PorblemController extends BaseController {
    @RequestMapping(value = "/test.html", method = RequestMethod.POST)
    @ResponseBody
    public String test(String[] arr) {
        System.out.println(".....................................");
        System.out.println(arr.length);
        System.out.println(".....................................");
        for(int i = 0; i < arr.length; i ++) {
            System.out.println(arr[i]);
        }
        System.out.println(".....................................");
        return "true";
    }
}
