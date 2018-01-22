package com.nilo.dms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by  on 2017/9/13.
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/home.html")
    public String home() {

        return "home";
    }
}
