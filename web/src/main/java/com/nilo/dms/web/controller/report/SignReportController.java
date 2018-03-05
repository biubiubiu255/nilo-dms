package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.order.DeliverReportService;
import com.nilo.dms.service.order.SignReportService;
import com.nilo.dms.service.order.model.DeliverOrderParameter;
import com.nilo.dms.service.order.model.DeliverReport;
import com.nilo.dms.service.order.model.SignOrderParameter;
import com.nilo.dms.service.order.model.SignReport;
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
@RequestMapping("/report/sign")
public class SignReportController extends BaseController {
    @Autowired
    private SignReportService signReportService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/sign/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(SignOrderParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "taskStatus[]", required = false) Integer[] taskStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        if (taskStatus != null && taskStatus.length > 0) {
            parameter.setStatus(Arrays.asList(taskStatus));
        }

        Pagination page = getPage();
        List<SignReport> list = signReportService.querySignReport(parameter, page);

        return toPaginationLayUIData(page, list);
//        return "";
    }
}
