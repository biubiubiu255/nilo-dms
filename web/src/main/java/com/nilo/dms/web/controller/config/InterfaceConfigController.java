package com.nilo.dms.web.controller.config;


import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.system.config.InterfaceConfigService;
import com.nilo.dms.service.system.model.InterfaceConfig;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/systemConfig/interface")
public class InterfaceConfigController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private InterfaceConfigService interfaceConfigService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/interface/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getUserList(String operation, String operator, String parameter, String fromTime, String toTime) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<InterfaceConfig> list = interfaceConfigService.queryAll(merchantId);
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }
    @ResponseBody
    @RequestMapping(value = "/edit.html")
    public String edit(InterfaceConfig config) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {

            InterfaceConfig edit = new InterfaceConfig();
            edit.setMerchantId(merchantId);
            edit.setMethod(config.getMethod());
            edit.setOp(config.getOp());
            edit.setUrl(config.getUrl());
            edit.setRequestMethod(config.getRequestMethod());
            interfaceConfigService.update(edit);
        } catch (Exception e) {
            log.error("edit interface config failed. InterfaceConfig:{}", config, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
