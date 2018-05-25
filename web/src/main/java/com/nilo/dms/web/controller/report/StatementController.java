package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.WaybillStatementDao;
import com.nilo.dms.dao.dataobject.WaybillStatementDo;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/2.
 */
@Controller
@RequestMapping("/report/statement")
public class StatementController extends BaseController {
    @Autowired
    private WaybillStatementDao waybillStatementDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "report/statement";
    }

    @RequestMapping(value = "/list.html")
    public String getList(Model model, Integer sTime, Integer eTime) {
        if (sTime == null || eTime == null) {
            sTime = 0;
            eTime = (int) (System.currentTimeMillis() / 1000);
            System.out.println("时间：" + eTime);
        }
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        List<WaybillStatementDo> list = new ArrayList<WaybillStatementDo>();

        list = waybillStatementDao.queryAllWaybillStatement(sTime, eTime);
        //list = waybillStatementDao.queryAllWaybillStatement( 1423094400, 1519689600);

        Pagination page = getPage();

        if (list == null) {
            page.setTotalCount(0);
            return toPaginationLayUIData(page, null);
        }
        page.setTotalCount(list.size());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/statement.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }

}
