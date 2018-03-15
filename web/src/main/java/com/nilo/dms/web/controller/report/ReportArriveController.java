package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportReceiveDO;
import com.nilo.dms.web.controller.BaseController;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report/arrive")
public class ReportArriveController extends BaseController {
    @Autowired
    private WaybillArriveDao waybillArriveDao;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        List<DistributionNetworkDO> list = distributionNetworkDao.findAllBy(Long.valueOf(me.getMerchantId()));
        model.addAttribute("list", list);
        return "report/arrive/list";
    }

    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, String orderNo, Integer sTime_creat, Integer eTime_creat, String scanNetwork) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("merchantId", merchantId);
        map.put("sTime_creat", sTime_creat);
        map.put("eTime_creat", eTime_creat);
        map.put("orderNo", orderNo);
        map.put("scanNetwork", scanNetwork);
        map.put("offset", page.getOffset());
        map.put("limit", page.getLimit());

        List<ReportArriveDO> list = waybillArriveDao.queryReportArrive(map);
        page.setTotalCount(waybillArriveDao.queryReportArriveCount(map));
        //page.setTotalCount(commonDao.lastFoundRows());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        System.out.println(" = " + list.size());
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/arrive.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
}
}
