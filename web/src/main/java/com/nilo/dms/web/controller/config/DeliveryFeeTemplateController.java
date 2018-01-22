package com.nilo.dms.web.controller.config;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.FeeTemplateStatusEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryFeeFactorDao;
import com.nilo.dms.dao.DeliveryFeeTemplateDao;
import com.nilo.dms.dao.dataobject.DeliveryFeeFactorDO;
import com.nilo.dms.dao.dataobject.DeliveryFeeTemplateDO;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/20.
 */
@Controller
@RequestMapping("/systemConfig/deliveryFeeTemplate")
public class DeliveryFeeTemplateController extends BaseController {

    @Autowired
    private DeliveryFeeTemplateDao deliveryFeeTemplateDao;
    @Autowired
    private DeliveryFeeFactorDao deliveryFeeFactorDao;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/template/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList(String country, String orderType, String orderPlatform, String goodsType, String area) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DeliveryFeeTemplateDO> list = deliveryFeeTemplateDao.findAllBy(Long.parseLong(merchantId), country, orderType, orderPlatform, goodsType, area);

        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);

    }

    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(Model model, String id) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();

        DeliveryFeeTemplateDO templateDO = deliveryFeeTemplateDao.queryById(Long.parseLong(id));

        List<DeliveryFeeFactorDO> factorDO = deliveryFeeFactorDao.queryBy(Long.parseLong(id));

        Map<String,DeliveryFeeFactorDO> factorDOMap = new HashMap<>();
        if(factorDO!=null && factorDO.size() >0){
            for (DeliveryFeeFactorDO f : factorDO){
                factorDOMap.put(f.getFactorType(),f);
            }
        }

        model.addAttribute("template", templateDO);
        model.addAttribute("map", factorDOMap);

        return "system_config/template/edit";
    }

    @RequestMapping(value = "/details.html")
    public String details(Model model, String id) {

        DeliveryFeeTemplateDO templateDO = deliveryFeeTemplateDao.queryById(Long.parseLong(id));

        List<DeliveryFeeFactorDO> factorDO = deliveryFeeFactorDao.queryBy(Long.parseLong(id));

        model.addAttribute("template", templateDO);
        model.addAttribute("factor", factorDO);
        return "system_config/template/details";
    }

    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(String optType, Model model) {

        return "system_config/template/add";
    }

    @ResponseBody
    @RequestMapping(value = "/addBasic.html", method = RequestMethod.POST)
    public String addConfig(DeliveryFeeTemplateDO templateDO, String fromTime, String toTime) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            templateDO.setMerchantId(Long.parseLong(merchantId));
            templateDO.setStatus(FeeTemplateStatusEnum.NORMAL.getCode());
            if (StringUtil.isNotEmpty(fromTime)) {
                templateDO.setExpiryFrom(DateUtil.parse(fromTime, "yyyy-MM-dd"));
            }
            if (StringUtil.isNotEmpty(toTime)) {
                templateDO.setExpiryTo(DateUtil.parse(toTime, "yyyy-MM-dd"));
            }
            deliveryFeeTemplateDao.insert(templateDO);

        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(templateDO.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/addFactor.html", method = RequestMethod.POST)
    public String addConfig(String templateId, Factors factorDOs) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            for (DeliveryFeeFactorDO factorDO : factorDOs.getFactorDOs()) {
                factorDO.setTemplateId(Long.parseLong(templateId));
                deliveryFeeFactorDao.insert(factorDO);
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    private static class Factors{
        private List<DeliveryFeeFactorDO> factorDOs;

        public List<DeliveryFeeFactorDO> getFactorDOs() {
            return factorDOs;
        }

        public void setFactorDOs(List<DeliveryFeeFactorDO> factorDOs) {
            this.factorDOs = factorDOs;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/editBasic.html", method = RequestMethod.POST)
    public String editBasic(DeliveryFeeTemplateDO templateDO, String fromTime, String toTime) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            //修改之前记录为历史状态
            DeliveryFeeTemplateDO update = new DeliveryFeeTemplateDO();
            update.setMerchantId(Long.parseLong(merchantId));
            update.setId(templateDO.getId());
            update.setStatus(FeeTemplateStatusEnum.HISTORY.getCode());
            deliveryFeeTemplateDao.update(update);

            templateDO.setMerchantId(Long.parseLong(merchantId));
            templateDO.setStatus(FeeTemplateStatusEnum.NORMAL.getCode());
            if (StringUtil.isNotEmpty(fromTime)) {
                templateDO.setExpiryFrom(DateUtil.parse(fromTime, "yyyy-MM-dd"));
            }
            if (StringUtil.isNotEmpty(toTime)) {
                templateDO.setExpiryTo(DateUtil.parse(toTime, "yyyy-MM-dd"));
            }
            deliveryFeeTemplateDao.insert(templateDO);

        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(templateDO.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/editFactor.html", method = RequestMethod.POST)
    public String editFactor(String templateId, Factors factorDOs) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();

            //修改之前记录为历史状态
            deliveryFeeFactorDao.updateStatus(Long.parseLong(templateId), FeeTemplateStatusEnum.HISTORY.getCode());

            //获取merchantId
            String merchantId = me.getMerchantId();
            for (DeliveryFeeFactorDO factorDO : factorDOs.getFactorDOs()) {
                factorDO.setTemplateId(Long.parseLong(templateId));
                deliveryFeeFactorDao.insert(factorDO);
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/active.html", method = RequestMethod.POST)
    public String active(String id) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            DeliveryFeeTemplateDO templateDO = new DeliveryFeeTemplateDO();
            templateDO.setMerchantId(Long.parseLong(merchantId));
            templateDO.setId(Long.parseLong(id));
            templateDO.setStatus(FeeTemplateStatusEnum.NORMAL.getCode());
            deliveryFeeTemplateDao.update(templateDO);

        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/delete.html", method = RequestMethod.POST)
    public String delete(String id) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            DeliveryFeeTemplateDO templateDO = new DeliveryFeeTemplateDO();
            templateDO.setMerchantId(Long.parseLong(merchantId));
            templateDO.setId(Long.parseLong(id));
            templateDO.setStatus(FeeTemplateStatusEnum.DELETE.getCode());
            deliveryFeeTemplateDao.update(templateDO);

        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
