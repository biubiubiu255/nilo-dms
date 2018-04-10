package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.WaybillInvoiceDao;
import com.nilo.dms.dao.dataobject.ReportInvoiceDO;
import com.nilo.dms.dao.dataobject.ReportInvoiceQueryDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.web.controller.BaseController;
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
@RequestMapping("/report/invoice")
public class ReportInvoiceController extends BaseController {
    @Autowired
    private WaybillInvoiceDao waybillInvoiceDao;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/cod/list_old";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(ReportInvoiceQueryDO param) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();

        //new java.lang.Double().doubleValue()

        Map<String, Object> map = new HashMap<String, Object>();

        // 原对象 ReportCodDO() 所需参数
        // Param: rider
        // Param: payType
        // Param: orderPlatform


        List<ReportInvoiceDO> list = waybillInvoiceDao.queryReportInvoiceInput(param);

        //List<ReportCodDO> list = waybillInvoiceDao.queryReportCod(param, page.getOffset(), page.getOffset() );
        //page.setTotalCount(waybillCodDao.queryReportCodCount(map));
        //page.setTotalCount(commonDao.lastFoundRows());
        return toPaginationLayUIData(page, list);
}
}
