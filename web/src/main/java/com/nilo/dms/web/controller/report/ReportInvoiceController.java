package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.WaybillCodDao;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.web.controller.BaseController;
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
@RequestMapping("/report/invoice")
public class ReportInvoiceController extends BaseController {
    @Autowired
    private WaybillCodDao waybillCodDao;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/cod/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(String orderNo, String rider, String payMethod, String orderPlatform, Integer sTime_creat, Integer eTime_creat, Integer sTime_sign,
                               Integer eTime_sign) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("merchantId", merchantId);
        map.put("orderNo", orderNo);
        //map.put("sTime_creat", sTime_creat);
        //map.put("eTime_creat", eTime_creat);
        map.put("sTime_creat", sTime_creat);
        map.put("eTime_creat", eTime_creat);
        map.put("sTime_sign", sTime_sign);
        map.put("eTime_sign", eTime_sign);
        map.put("rider", rider);
        map.put("payType", payMethod);
        map.put("orderPlatform", orderPlatform);
        map.put("offset", page.getOffset());
        map.put("limit", page.getLimit());

        List<ReportCodDO> list = waybillCodDao.queryReportCod(map);
        page.setTotalCount(waybillCodDao.queryReportCodCount(map));
        //page.setTotalCount(commonDao.lastFoundRows());
        return toPaginationLayUIData(page, list);
}
}
