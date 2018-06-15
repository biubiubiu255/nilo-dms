package com.nilo.dms.web.controller.config;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dto.system.SMSConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.SendMessageService;
import com.nilo.dms.web.controller.BaseController;
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
    private SendMessageService sendMessageService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/sms/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList(String smsType) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<SMSConfig> list = sendMessageService.listConfigBy(merchantId);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String msgType, Model model) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        SMSConfig smsConfig = sendMessageService.getConfigByType(merchantId, msgType);
        model.addAttribute("smsConfig", smsConfig);
        return "system_config/sms/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateConfig(SMSConfig config) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            SMSConfig update = new SMSConfig();
            update.setMerchantId(merchantId);
            update.setSmsType(config.getSmsType());
            update.setContent(config.getContent());
            update.setName(config.getName());
            update.setStatus(config.getStatus());
            update.setUpdatedBy(me.getUserName());
            sendMessageService.updateConfig(update);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

}
