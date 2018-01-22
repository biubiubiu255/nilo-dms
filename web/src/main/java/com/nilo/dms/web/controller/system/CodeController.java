package com.nilo.dms.web.controller.system;


import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DictionaryTypeDao;
import com.nilo.dms.dao.dataobject.DictionaryTypeDO;
import com.nilo.dms.service.system.DictionaryService;
import com.nilo.dms.service.system.model.Dictionary;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/code")
public class CodeController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictionaryTypeDao dictionaryTypeDao;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "code/list";
    }

    @ResponseBody
    @RequestMapping(value = "/getList.html", method = RequestMethod.POST)
    public String getList(String type, String desc) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<String> typeList = new ArrayList<>();
        if (StringUtil.isNotEmpty(type)) {
            typeList.add(type);
        }
        List<DictionaryTypeDO> list = dictionaryTypeDao.queryBy(typeList, desc);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/editCodePage.html", method = RequestMethod.GET)
    public String editCodePage(Model model, String type) {
        model.addAttribute("type", type);
        return "code/edit";
    }

    @RequestMapping(value = "/getCodeList.html")
    @ResponseBody
    public String editCodeGet(String type) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<Dictionary> list = dictionaryService.queryBy(merchantId, type);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/editCode.html", method = RequestMethod.POST)
    @ResponseBody
    public String editCode(Dictionary dictionary) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            dictionary.setMerchantId(merchantId);
            dictionaryService.update(dictionary);
        } catch (Exception e) {
            logger.error("editCode Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addCodePage.html")
    public String addCodePage(Model model, String type) {
        model.addAttribute("type", type);
        return "code/addCodePage";
    }

    @RequestMapping(value = "/addCode.html", method = RequestMethod.POST)
    @ResponseBody
    public String addCode(Model model, Dictionary dictionary) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            dictionary.setMerchantId(merchantId);
            dictionaryService.add(dictionary);
        } catch (Exception e) {
            logger.error("Add Code Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/delCode.html", method = RequestMethod.POST)
    @ResponseBody
    public String delCode(String type, String code) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            Dictionary dictionary = new Dictionary();
            dictionary.setMerchantId(merchantId);
            dictionary.setType(type);
            dictionary.setCode(code);
            dictionaryService.delete(dictionary);
        } catch (Exception e) {
            logger.error("Add Code Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/updateCode.html", method = RequestMethod.POST)
    @ResponseBody
    public String updateCode(Model model, Dictionary dictionary) {
        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            dictionary.setMerchantId(merchantId);
            dictionaryService.update(dictionary);
        } catch (Exception e) {
            logger.error("Add Code Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
