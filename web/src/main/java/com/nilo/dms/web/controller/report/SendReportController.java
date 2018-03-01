package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.dataobject.SendReportDO;
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
import java.util.List;

@Controller
@RequestMapping("/report/send")
public class SendReportController extends BaseController {

    @Autowired
    private SendReportService sendReportService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/send/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(SendOrderParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        if (orderStatus != null && orderStatus.length > 0) {
            parameter.setStatus(Arrays.asList(orderStatus));
        }

        Pagination page = getPage();
        List<SendReport> list = sendReportService.querySendReport(parameter, page);

        return toPaginationLayUIData(page, list);
    }
}
