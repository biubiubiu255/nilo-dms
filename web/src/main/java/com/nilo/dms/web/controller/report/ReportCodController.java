package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.WaybillArriveDao;
import com.nilo.dms.dao.WaybillCodDao;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.service.order.ReportService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.ReportCodQuery;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report/cod")
public class ReportCodController extends BaseController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/cod/list";
    }

    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, Integer exportType, ReportCodQuery reportCodQuery, HttpServletRequest request) {
        Pagination page = getPage();
        //page.setTotalCount(commonDao.lastFoundRows());
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        reportCodQuery.setMerchantId(Long.valueOf(me.getMerchantId()));
        String fileType;
        switch (exportType){
            case 0 :
                        fileType = "pdf";
                        break;
            case 1 :
                        fileType = "xls";
                break;
            case 2 :
                fileType = "json";
                break;
            default:
                        fileType = "pdf";
        }

        System.out.println("fileTypeis = " + fileType);

        List<ReportCodDO> list = reportService.qeueryCodList(reportCodQuery, page);

        if (fileType.equals("json")){
            request.setAttribute("toDate", toPaginationLayUIData(getPage(), list));
            return "common/toResponseBody";
        }


        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/cod.jasper");
        model.addAttribute("format", fileType); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
        //return toPaginationLayUIData(page, list);
    }

}

