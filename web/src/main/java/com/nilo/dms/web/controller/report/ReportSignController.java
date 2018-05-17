package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SignReportService;
import com.nilo.dms.dto.order.SignOrderParameter;
import com.nilo.dms.dto.order.SignReport;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/report/sign")
public class ReportSignController extends BaseController {
    @Autowired
    private SignReportService signReportService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/sign/list";
    }

    //@RequestMapping(value = "/list.html")
    /*public String getOrderList(Model model, SignOrderParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "taskStatus[]", required = false) Integer[] taskStatus) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        if (taskStatus != null && taskStatus.length > 0) {
            parameter.setStatus(Arrays.asList(taskStatus));
        }

        Pagination page = getPage();
        List<SignReport> list = signReportService.querySignReport(parameter, page);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/sign.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id

        //return toPaginationLayUIData(page, list);

    }
*/
    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, SignOrderParameter parameter, Integer sTime_creat, Integer eTime_creat,
                                Integer exportType, HttpServletRequest request) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        if(sTime_creat!=null){
            parameter.setFromHandledTime(sTime_creat.toString());
        }
        if(eTime_creat!=null){
            parameter.setToHandledTime(eTime_creat.toString());
        }

        Pagination page = null;

        String fileType;
        switch (exportType) {
            case 0:
                fileType = "pdf";
                page = new Pagination(0, 1000);
                break;
            case 1:
                fileType = "xls";
                page = new Pagination(0, 1000);
                break;
            case 2:
                fileType = "json";
                page = getPage();
                break;
            default:
                fileType = "pdf";
                page = getPage();
        }

        List<SignReport> list = signReportService.querySignReport(parameter, page);
        //page.setTotalCount(commonDao.lastFoundRows());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        System.out.println(" = " + list.size());

        if (fileType.equals("json")) {
            request.setAttribute("toDate", toPaginationLayUIData(page, list));
            return "common/toResponseBody";
        }

        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/sign.jasper");
        model.addAttribute("format", fileType); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
