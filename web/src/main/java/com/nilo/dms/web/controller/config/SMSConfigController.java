package com.nilo.dms.web.controller.config;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.SMSConfigStatusEnum;
import com.nilo.dms.service.system.config.SMSConfigService;
import com.nilo.dms.service.system.model.SMSConfig;
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
@RequestMapping("/systemConfig/sms")
public class SMSConfigController extends BaseController {

    @Autowired
    private SMSConfigService smsConfigService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/sms/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList(String smsType) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<SMSConfig> list = smsConfigService.queryAllBy(merchantId);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String msgType, Model model) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        SMSConfig smsConfig = smsConfigService.queryBy(merchantId, msgType);
        model.addAttribute("smsConfig", smsConfig);
        return "system_config/sms/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateConfig(String optTypeEdit, String content, String remark) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            SMSConfig config = new SMSConfig();
            config.setMerchantId(merchantId);
            config.setSmsType(optTypeEdit);
            config.setContent(content);
            config.setRemark(remark);

            smsConfigService.update(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(String optType, Model model) {

        return "system_config/sms/add";
    }

    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String addConfig(String optTypeAdd,  String content, String remark) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            SMSConfig config = new SMSConfig();
            config.setMerchantId(merchantId);
            config.setSmsType(optTypeAdd);
            config.setContent(content);
            config.setRemark(remark);

            smsConfigService.add(config);
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
            SMSConfig config = new SMSConfig();
            config.setMerchantId(merchantId);
            config.setStatus(SMSConfigStatusEnum.NORMAL);
            smsConfigService.update(config);
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
            SMSConfig config = new SMSConfig();
            config.setMerchantId(merchantId);
            config.setStatus(SMSConfigStatusEnum.DISABLED);
            smsConfigService.update(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
