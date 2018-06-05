package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.SendReportDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dao.dataobject.QO.ReportRepeatQO;
import com.nilo.dms.dao.dataobject.QO.SendReportQO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendReportService;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/report/repeat_send")
public class ReportRepeatSendController extends BaseController {

    @Autowired
    private SendReportDao sendReportDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

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

        List<StaffDO> riderList = getRiderList(null);


        model.addAttribute("nextStations", list);
        model.addAttribute("expressList", expressList);
        model.addAttribute("riderList", riderList);


        return "report/repeatDispatch/list";
    }


    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model,  HttpServletRequest request, ReportRepeatQO reportRepeatQO) {

        Principal me = SessionLocal.getPrincipal();

        Pagination page = getPage();

        //获取merchantId
        Long merchantId = Long.parseLong(me.getMerchantId());
        reportRepeatQO.setMerchantId(merchantId);
        
        String fileType;
        switch (reportRepeatQO.getExportType()) {
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
        if(reportRepeatQO.getToCreatedTime()==null && reportRepeatQO.getFromCreatedTime()==null){
            reportRepeatQO.setFromCreatedTime(new Long(LocalDateTime.now().withHour(0).withMinute(0).toEpochSecond(ZoneOffset.of("+8"))).intValue());
            reportRepeatQO.setToCreatedTime(new Long(LocalDateTime.now().withHour(23).withMinute(59).toEpochSecond(ZoneOffset.of("+8"))).intValue());
        }

        reportRepeatQO.setLimit(page.getLimit());
        reportRepeatQO.setOffset(page.getOffset());
        List<ReportRepeatDO> list = sendReportDao.queryRepeatDispatch(reportRepeatQO);

        page.setTotalCount(sendReportDao.queryRepeatDispatchCount(reportRepeatQO));

        for (ReportRepeatDO e : list){
            switch (e.getDispatchType()){

                case "package":
                    e.setDispatchType("Station");
                    break;
                case "waybill":
                    e.setDispatchType("Express");
                    break;
                case "riderDelivery":
                    e.setDispatchType("RiderDelivery");
                    break;
            }
        }


        if (fileType.equals("json")) {
            request.setAttribute("toDate", toPaginationLayUIData(page, list));
            return "common/toResponseBody";
        }

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/repeat.jasper");
        model.addAttribute("format", fileType); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
