package com.nilo.dms.web.controller.config;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.RouteConfigStatusEnum;
import com.nilo.dms.service.system.config.RouteConfigService;
import com.nilo.dms.service.system.model.RouteConfig;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
@Controller
@RequestMapping("/systemConfig/route")
public class RouteConfigController extends BaseController {

    @Autowired
    private RouteConfigService routeConfigService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/route/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList() {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<RouteConfig> list = routeConfigService.queryAllBy(merchantId);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String optType, Model model) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        RouteConfig routeConfig = routeConfigService.queryBy(merchantId, optType);
        model.addAttribute("routeConfig", routeConfig);
        return "system_config/route/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateConfig(String optTypeEdit, String routeDescC, String routeDescE, String remark) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            RouteConfig config = new RouteConfig();
            config.setMerchantId(merchantId);
            config.setOptType(optTypeEdit);
            config.setRouteDescC(routeDescC);
            config.setRouteDescE(routeDescE);
            config.setRemark(remark);

            routeConfigService.updateRouteConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(String optType, Model model) {

        return "system_config/route/add";
    }

    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String addConfig(String optTypeAdd, String routeDescC, String routeDescE, String remark) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            RouteConfig config = new RouteConfig();
            config.setMerchantId(merchantId);
            config.setOptType(optTypeAdd);
            config.setRouteDescC(routeDescC);
            config.setRouteDescE(routeDescE);
            config.setRemark(remark);

            routeConfigService.addConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/active.html", method = RequestMethod.POST)
    public String active(String optType) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            RouteConfig config = new RouteConfig();
            config.setMerchantId(merchantId);
            config.setOptType(optType);
            config.setStatus(RouteConfigStatusEnum.NORMAL);
            routeConfigService.updateRouteConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/delete.html", method = RequestMethod.POST)
    public String delete(String optType) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            RouteConfig config = new RouteConfig();
            config.setMerchantId(merchantId);
            config.setOptType(optType);
            config.setStatus(RouteConfigStatusEnum.DELETE);

            routeConfigService.updateRouteConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
