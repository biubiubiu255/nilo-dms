package com.nilo.dms.web.controller;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AreaDao;
import com.nilo.dms.dao.dataobject.AreaDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/22.
 */
@Controller
@RequestMapping("/DemoController")
public class DemoController {
//    @Autowired
//    private AreaDao areaDao;

    @RequestMapping(value = "/toLoginPage.html")
    public String list(Model model) {
        return "mobile/list";
    }
}
