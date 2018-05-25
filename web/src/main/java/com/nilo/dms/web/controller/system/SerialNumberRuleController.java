package com.nilo.dms.web.controller.system;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dto.system.SerialNumberRule;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.SerialNumberService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
@Controller
@RequestMapping("/admin/serialNumRule")
public class SerialNumberRuleController extends BaseController {

    @Autowired
    private SerialNumberService serialNumberService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "serial_num_rule/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getSerialNumRuleList(@RequestParam(value = "serialTypes[]", required = false) String[] serialTypes) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<String> typeList = null;
        if (serialTypes != null && serialTypes.length > 0) {
            typeList = Arrays.asList(serialTypes);
        }
        List<SerialNumberRule> list = serialNumberService.findBy(merchantId, typeList);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String serialType, Model model) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<String> typeList = new ArrayList<>();
        typeList.add(serialType);
        List<SerialNumberRule> ruleList = serialNumberService.findBy(merchantId, typeList);
        model.addAttribute("rule", ruleList !=null?ruleList.get(0):null);
        return "serial_num_rule/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateRule(String serialType, String name, String prefix, String format, Integer serialLength) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            SerialNumberRule rule = new SerialNumberRule();
            rule.setMerchantId(merchantId);
            rule.setSerialType(serialType);
            rule.setSerialLength(serialLength);
            rule.setPrefix(prefix);
            rule.setFormat(format);
            rule.setName(name);
            serialNumberService.updateSerialNumberRule(rule);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
