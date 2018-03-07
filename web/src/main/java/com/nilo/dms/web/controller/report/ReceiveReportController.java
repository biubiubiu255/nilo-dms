package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.SendReportDao;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.ReceiveReportDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.order.SendReportService;
import com.nilo.dms.service.order.model.SendOrderParameter;
import com.nilo.dms.service.order.model.SendReport;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report/receive")
public class ReceiveReportController extends BaseController {

    @Autowired
    private SendReportService sendReportService;

    @Autowired
    private SendReportDao sendReportDao;

    @Autowired
    private CommonDao commonDao;


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        //List<ReceiveReportDO> list = sendReportDao.queryReportReceive(null);
        //model.addAttribute("resultData",list);
        return "report/receive/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(String orderNo, Integer sTime, Integer eTime, String mother, String clientName) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("merchantId", merchantId);
        map.put("sTime", sTime);
        map.put("eTime", eTime);
        map.put("mother", mother);
        map.put("orderNo", orderNo);
        map.put("clientName", clientName);

        map.put("offset", page.getOffset());
        map.put("limit", page.getLimit());

        List<ReceiveReportDO> list = sendReportDao.queryReportReceive(map);
        page.setTotalCount(sendReportDao.queryReportReceiveCount(map));
        //page.setTotalCount(commonDao.lastFoundRows());
        return toPaginationLayUIData(page, list);
}
}
