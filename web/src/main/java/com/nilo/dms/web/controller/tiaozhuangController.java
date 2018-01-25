package com.nilo.dms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tiaozhuangController")
public class tiaozhuangController {
    @RequestMapping(value = "/customers.html")
    public String customers() {
        return "mobile/customers";
    }

    @RequestMapping(value = "/dshkqs.html")
    public String dshkqs() {
        return "mobile/dshkqs";
    }

    @RequestMapping(value = "/wtjlr.html")
    public String wtjlr() {
        return "mobile/wtjlr";
    }

    @RequestMapping(value = "/zlj.html")
    public String zlj() {
        return "mobile/zlj";
    }

    @RequestMapping(value = "/pjqr.html")
    public String pjqr() {
        return "mobile/pjqr";
    }

    @RequestMapping(value = "/plzz.html")
    public String plzz() {
        return "mobile/plzz";
    }

    @RequestMapping(value = "/djsm.html")
    public String djsm() {
        return "mobile/djsm";
    }

    @RequestMapping(value = "/pjsm.html")
    public String pjsm() {
        return "mobile/pjsm";
    }

    @RequestMapping(value = "/fjsm.html")
    public String fjsm() {
        return "mobile/fjsm";
    }

    @RequestMapping(value = "/wtjlr2.html")
    public String wtjlr2() {
        return "mobile/wtjlr2";
    }

    @RequestMapping(value = "/pc.html")
    public String pc() {
        return "mobile/pc";
    }

    @RequestMapping(value = "/zljpc.html")
    public String zljpc() {
        return "mobile/zljpc";
    }

    @RequestMapping(value = "/zlj2.html")
    public String zlj2() {
        return "mobile/zlj2";
    }

    @RequestMapping(value = "/xgmm.html")
    public String xgmm() {
        return "mobile/xgmm";
    }
}
