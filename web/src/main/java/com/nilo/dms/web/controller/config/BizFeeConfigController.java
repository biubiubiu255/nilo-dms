package com.nilo.dms.web.controller.config;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.BizFeeConfigStatusEnum;
import com.nilo.dms.dto.system.BizFeeConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.config.BizFeeConfigService;
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
@RequestMapping("/systemConfig/bizFee")
public class BizFeeConfigController extends BaseController {

    @Autowired
    private BizFeeConfigService bizFeeConfigService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/biz_fee/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList() {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<BizFeeConfig> list = bizFeeConfigService.queryAllBy(merchantId);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String optType, Model model) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        BizFeeConfig bizFeeConfig = bizFeeConfigService.queryBy(merchantId, optType);
        model.addAttribute("bizFeeConfig", bizFeeConfig);
        return "system_config/biz_fee/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateConfig(String bizTypeEdit, Double fee, String remark) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            BizFeeConfig config = new BizFeeConfig();
            config.setMerchantId(merchantId);
            config.setOptType(bizTypeEdit);
            config.setFee(fee);
            config.setRemark(remark);

            bizFeeConfigService.updateConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(String optType, Model model) {

        return "system_config/biz_fee/add";
    }

    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String addConfig(String bizTypeAdd, Double fee, String remark) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            BizFeeConfig config = new BizFeeConfig();
            config.setMerchantId(merchantId);
            config.setOptType(bizTypeAdd);
            config.setFee(fee);
            config.setRemark(remark);

            bizFeeConfigService.addConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/active.html", method = RequestMethod.POST)
    public String active(String bizType) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            BizFeeConfig config = new BizFeeConfig();
            config.setMerchantId(merchantId);
            config.setOptType(bizType);
            config.setStatus(BizFeeConfigStatusEnum.NORMAL);
            bizFeeConfigService.updateConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/delete.html", method = RequestMethod.POST)
    public String delete(String bizType) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            BizFeeConfig config = new BizFeeConfig();
            config.setMerchantId(merchantId);
            config.setOptType(bizType);
            config.setStatus(BizFeeConfigStatusEnum.DELETE);

            bizFeeConfigService.updateConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
