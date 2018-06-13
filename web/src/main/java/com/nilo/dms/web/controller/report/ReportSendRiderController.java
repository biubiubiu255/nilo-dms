package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.OutsourceDao;
import com.nilo.dms.dao.ReportDispatchDao;
import com.nilo.dms.dao.dataobject.OutsourceDO;
import com.nilo.dms.dao.dataobject.QO.ReportDispatchQO;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportDispatchDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/20.
 */
@Controller
@RequestMapping("/report/dispatch")
public class ReportSendRiderController extends BaseController {

    @Autowired
    private ReportDispatchDao reportDispatchDao;

    @Autowired
    private OutsourceDao outsourceDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest http) {
        model.addAttribute("list", getRiderList(null));

        List<OutsourceDO> outsourceList = outsourceDao.findAll(SessionLocal.getPrincipal().getMerchantId());
        model.addAttribute("outsourceList", outsourceList);
        return "report/dispatch";
    }

    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, ReportDispatchQO reportDispatchQO, HttpServletRequest request) {


        reportDispatchQO.setMerchantId(Long.parseLong(SessionLocal.getPrincipal().getMerchantId()));

        Pagination page = getPage();

        reportDispatchQO.setLimit(page.getLimit());
        reportDispatchQO.setOffset(page.getOffset());
        if(reportDispatchQO.getToCreatedTime()==null && reportDispatchQO.getFromCreatedTime()==null){
            return "common/toResponseBody";
        }



        String fileType;
        switch (reportDispatchQO.getExportType()) {
            case 0:
                fileType = "pdf";
                reportDispatchQO.setLimit(1000);
                reportDispatchQO.setOffset(0);
                break;
            case 1:
                fileType = "xls";
                reportDispatchQO.setLimit(1000);
                reportDispatchQO.setOffset(0);
                break;
            case 2:
                fileType = "json";
                break;
            default:
                fileType = "pdf";
        }

        List<ReportDispatchDO> list = reportDispatchDao.queryReportDispatch(reportDispatchQO);
        page.setTotalCount(reportDispatchDao.queryReportDispatchCount(reportDispatchQO));
        //page.setTotalCount(commonDao.lastFoundRows());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

        if (fileType.equals("json")) {
            request.setAttribute("toDate", toPaginationLayUIData(page, list));
            return "common/toResponseBody";
        }

        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/sendRider.jasper");
        model.addAttribute("format", fileType); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
