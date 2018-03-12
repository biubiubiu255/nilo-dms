package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.WaybillArriveDao;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.web.controller.BaseController;
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
@RequestMapping("/report/cod")
public class ReportCodController extends BaseController {
    @Autowired
    private WaybillArriveDao waybillArriveDao;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/cod/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(String orderNo, Integer sTime_creat, Integer eTime_creat, String scanNetwork) {
        Pagination page = getPage();
        //page.setTotalCount(commonDao.lastFoundRows());
        return toPaginationLayUIData(page, getList(orderNo,sTime_creat,eTime_creat,scanNetwork,page));
    }

    private List<ReportArriveDO> getList(String orderNo, Integer sTime_creat, Integer eTime_creat, String scanNetwork, Pagination page ){
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
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
        return list;
    }

    /**
     * 返回iReport报表视图
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/cod_report.html", method = RequestMethod.GET)
    public String report(Model model,String orderNo, Integer sTime_creat, Integer eTime_creat, String scanNetwork) {
        Pagination page = getPage();
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(getList(orderNo,sTime_creat,eTime_creat,scanNetwork,page));
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/cod.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
