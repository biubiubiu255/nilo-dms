package com.nilo.dms.web.controller.interfaces;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.system.NotifyService;
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

import java.util.List;

@Controller
@RequestMapping("/interface/notify")
public class NotifyRecordController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotifyDao notifyDao;

    @Autowired
    private NotifyService notifyService;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "interface/notify/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getUserList(String orderNo, String bizType, Integer status) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination pagination = getPage();

        List<NotifyDO> list = notifyDao.findBy(Long.parseLong(merchantId), orderNo, bizType, status, pagination.getOffset(), pagination.getLimit());
        pagination.setTotalCount(notifyDao.findCountBy(Long.parseLong(merchantId), orderNo, bizType, status));
        return toPaginationLayUIData(pagination, list);
    }

    @ResponseBody
    @RequestMapping(value = "/retryNotify.html")
    public String retryNotify(String notifyId) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            notifyService.retryNotify(merchantId, notifyId);
        } catch (Exception e) {
            log.error("retryNotify failed. notifyId:{}", notifyId, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
