package com.nilo.dms.web.controller;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dto.common.Menu;
import com.nilo.dms.service.MenuService;
import com.nilo.dms.service.impl.SessionLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DashboardController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/dashboard.html")
    public String dashboard(Model model) {

        List<Menu> list = (List<Menu>) getSessionAttr("userMenu");
        Principal me = SessionLocal.getPrincipal();
        if (list == null) {
            //根据权限过滤菜单
            list = menuService.getWebMenu(me.getUserId());
            setSessionAttr("userMenu", list);
        }

        model.addAttribute("menu", JSON.toJSONString(list));
        return "dashboard";
    }

}
