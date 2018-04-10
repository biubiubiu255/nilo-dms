package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.ReportReceiveDao;
import com.nilo.dms.dao.SendReportDao;
import com.nilo.dms.dao.dataobject.ReportReceiveDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report/receive")
public class ReportReceiveController extends BaseController {

    @Autowired
    private ReportReceiveDao reportReceiveDao;

    @Autowired
    private SendReportDao sendReportDao;

    @Autowired
    private CommonDao commonDao;


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        //List<ReportReceiveDO> list = sendReportDao.queryReportReceive(null);
        //model.addAttribute("resultData",list);
        return "report/receive/list";
    }

    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, String orderNo, Integer sTime_creat, Integer eTime_creat, Integer sTime_receive,
                               Integer eTime_receive, String mother, String clientName) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("merchantId", merchantId);
        map.put("sTime_creat", sTime_creat);
        map.put("eTime_creat", eTime_creat);
        map.put("sTime_receive", sTime_receive);
        map.put("eTime_receive", eTime_receive);
        map.put("mother", mother);
        map.put("orderNo", orderNo);
        map.put("clientName", clientName);

        map.put("offset", page.getOffset());
        map.put("limit", page.getLimit());

        List<ReportReceiveDO> list = reportReceiveDao.queryReportReceive(map);
        page.setTotalCount(reportReceiveDao.queryReportReceiveCount(map));
        //page.setTotalCount(commonDao.lastFoundRows());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/receive.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
}
}
