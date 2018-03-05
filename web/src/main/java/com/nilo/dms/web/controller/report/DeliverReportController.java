package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.order.DeliverReportService;
import com.nilo.dms.service.order.model.DeliverOrderParameter;
import com.nilo.dms.service.order.model.DeliverReport;
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
@RequestMapping("/report/deliver")
public class DeliverReportController extends BaseController {
    @Autowired
    private DeliverReportService deliverReportService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/deliver/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(DeliverOrderParameter parameter,@RequestParam(value = "taskStatus[]", required = false) Integer[] taskStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        if (taskStatus != null && taskStatus.length > 0) {
            parameter.setStatus(Arrays.asList(taskStatus));
        }

        Pagination page = getPage();
        List<DeliverReport> list = deliverReportService.queryDeliverReport(parameter, page);

        return toPaginationLayUIData(page, list);
    }
}
