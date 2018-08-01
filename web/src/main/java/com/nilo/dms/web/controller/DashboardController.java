package com.nilo.dms.web.controller;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.WaybillPenalDao;
import com.nilo.dms.dto.common.Menu;
import com.nilo.dms.service.MenuService;
import com.nilo.dms.service.impl.SessionLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private WaybillPenalDao waybillPenalDao;

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

    @ResponseBody
    @RequestMapping("/penal_data/signedMonth.html")
    public String signedMonth(){
        String firstNetwork = SessionLocal.getPrincipal().getFirstNetwork();
        Calendar c = Calendar.getInstance();
        String dateFormat = new SimpleDateFormat("yyyyMM").format(c.getTime());
        Integer count = waybillPenalDao.querySignMonthCount(firstNetwork, dateFormat);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("count", count);
        return toJsonTrueData(map);
    }

    @ResponseBody
    @RequestMapping("/penal_data/outTimeSigned.html")
    public String outTimeSigned(Integer timeOutNumber){
        String firstNetwork = SessionLocal.getPrincipal().getFirstNetwork();
        Calendar c = Calendar.getInstance();
        String dateFormat = new SimpleDateFormat("yyyyMM").format(c.getTime());
        Integer count = waybillPenalDao.queryTimeOutSignCount(firstNetwork, dateFormat, timeOutNumber);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("count", count);
        return toJsonTrueData(map);
    }

    @ResponseBody
    @RequestMapping("/penal_data/signedMonthGroup.html")
    public String signedMonthGroup(Integer fromTime, Integer toTime){
        String firstNetwork = SessionLocal.getPrincipal().getFirstNetwork();
        Calendar c = Calendar.getInstance();
        String dateFormat = new SimpleDateFormat("yyyyMM").format(c.getTime());
        Map<String, Integer> list = waybillPenalDao.queryGroupSignMonthCount(firstNetwork, fromTime, toTime);
        return toJsonTrueData(list);
    }

}
