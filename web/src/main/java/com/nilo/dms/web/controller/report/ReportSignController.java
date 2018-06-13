package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.OutsourceDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.OutsourceDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SignReportService;
import com.nilo.dms.dto.order.SignOrderParameter;
import com.nilo.dms.dto.order.SignReport;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.PackageController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/report/sign")
public class ReportSignController extends BaseController {

    @Autowired
    private SignReportService signReportService;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Autowired
    private OutsourceDao outsourceDao;


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {


        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(SessionLocal.getPrincipal().getMerchantId()));
        List<PackageController.NextStation> list = new ArrayList<>();

        for (DistributionNetworkDO n : networkDOList) {
            PackageController.NextStation s = new PackageController.NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            list.add(s);
        }
        List<OutsourceDO> outsourceList = outsourceDao.findAll(SessionLocal.getPrincipal().getMerchantId());

        model.addAttribute("outsourceList", outsourceList);
        model.addAttribute("nextStations", list);
        model.addAttribute("riderList", getRiderList(null));
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
    public String getOrderList(Model model, SignOrderParameter parameter, Integer exportType, HttpServletRequest request) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        if(parameter.getToHandledTime()==null && parameter.getFromHandledTime()==null){
            return "common/toResponseBody";
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
