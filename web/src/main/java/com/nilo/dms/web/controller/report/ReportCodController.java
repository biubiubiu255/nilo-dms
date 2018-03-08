package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.WaybillArriveDao;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
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
@RequestMapping("/report/cod")
public class ReportCodController extends BaseController {
    @Autowired
    private WaybillArriveDao waybillArriveDao;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/arrive/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(String orderNo, Integer sTime_creat, Integer eTime_creat, String scanNetwork) {

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
        return toPaginationLayUIData(page, list);
}
}
