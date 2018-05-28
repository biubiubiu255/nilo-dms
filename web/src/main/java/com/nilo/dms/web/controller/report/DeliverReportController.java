package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.DeliverReportService;
import com.nilo.dms.dto.order.DeliverOrderParameter;
import com.nilo.dms.dto.order.DeliverReport;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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


    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, DeliverOrderParameter parameter,@RequestParam(value = "taskStatus[]", required = false) Integer[] taskStatus) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        if (taskStatus != null && taskStatus.length > 0) {
            parameter.setStatus(Arrays.asList(taskStatus));
        }
        Pagination page = getPage();
        List<DeliverReport> list = deliverReportService.queryDeliverReport(parameter, page);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        System.out.println(" = " + list.size());
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/deliver.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
