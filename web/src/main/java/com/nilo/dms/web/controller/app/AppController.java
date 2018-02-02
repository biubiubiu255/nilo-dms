package com.nilo.dms.web.controller.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nilo.dms.service.impl.LoginService;
import com.nilo.dms.web.controller.BaseController;

/**
 * Created by ronny on 2017/8/30.
 */
@Controller
@RequestMapping("/app")
public class AppController extends BaseController {

    @Autowired
    private LoginService loginService;


    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    @ResponseBody
    public String doPost(@RequestBody String userName,@RequestBody String passWord ) {

        Map<String, String> resultData = new HashMap<>();
        
        return toJsonTrueData(resultData);
    }

}
