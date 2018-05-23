package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.QO.SendReportQO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendReportService;
import com.nilo.dms.dto.order.SendOrderParameter;
import com.nilo.dms.dto.order.SendReport;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.PackageController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static com.nilo.dms.common.Constant.MAX_EXPORT_COUNT;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/report/send")
public class ReportSendThirdController extends BaseController {

    @Autowired
    private SendReportService sendReportService;

    @Autowired
    private ThirdExpressDao thirdExpressDao;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {

        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());

        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();

        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(merchantId);
        List<PackageController.NextStation> list = new ArrayList<>();

        for (DistributionNetworkDO n : networkDOList) {
            PackageController.NextStation s = new PackageController.NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            list.add(s);
         }

        List<ThirdExpressDO> expressList = userService.findExpressesAll(page);

        model.addAttribute("nextStations", list);
        model.addAttribute("expressList", expressList);

        return "report/send/list";
    }


    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model,  HttpServletRequest request, SendReportQO sendReportQO) {

        Principal me = SessionLocal.getPrincipal();

        Pagination page = getPage();


        //获取merchantId
        Long merchantId = Long.parseLong(me.getMerchantId());
        sendReportQO.setMerchantId(merchantId);
        
        String fileType;
        switch (sendReportQO.getExportType()) {
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
                break;
            default:
                fileType = "pdf";
        }

        List<SendReportDO> list = sendReportService.querySendReport(sendReportQO, page);

        if (fileType.equals("json")) {
            request.setAttribute("toDate", toPaginationLayUIData(page, list));
            return "common/toResponseBody";
        }

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/sendThird.jasper");
        model.addAttribute("format", fileType); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
