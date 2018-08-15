package com.nilo.dms.web.controller.report;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.ThirdPushOrderStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.WaybillArriveDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.QO.ReportArriveQO;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dto.order.WaybillHeader;
import com.nilo.dms.dto.system.InterfaceConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.order.producer.ArrivePushProducer;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Controller
@RequestMapping("/report/arrive")
public class ReportArriveController extends BaseController {
    @Autowired
    private WaybillArriveDao waybillArriveDao;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    @Qualifier("arrivePushProducer")
    private ArrivePushProducer arrivePushProducer;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        Principal me = SessionLocal.getPrincipal();
        List<DistributionNetworkDO> list = distributionNetworkDao.findAllBy(Long.valueOf(me.getMerchantId()));
        model.addAttribute("list", list);
        return "report/arrive/list";
    }

    @RequestMapping(value = "/list.html")
    public String getOrderList(ReportArriveQO reportArriveQO, Model model, HttpServletRequest request) {
        Principal principal = SessionLocal.getPrincipal();

/*        List<String> waybillNos = new ArrayList<String>();
        waybillNos.add(reportArriveQO.getOrderNo());
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId", principal.getMerchantId());
        map.put("orderList" , waybillNos);
        try {
            arrivePushProducer.sendMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DMSException(BizErrorCode.MQ_SEND_FAIL);
        }*/

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        reportArriveQO.setLimit(page.getLimit());
        reportArriveQO.setOffset(page.getOffset());
        reportArriveQO.setMerchantId(Long.parseLong(merchantId));


        String fileType;
        switch (reportArriveQO.getExportType()) {
            case 0:
                fileType = "pdf";
                reportArriveQO.setOffset(0);
                reportArriveQO.setLimit(1000);
                break;
            case 1:
                fileType = "xls";
                reportArriveQO.setOffset(0);
                reportArriveQO.setLimit(1000);
                break;
            case 2:
                fileType = "json";
                break;
            default:
                fileType = "pdf";
        }

/*
        if(reportArriveQO.getToCreatedTime()==null && reportArriveQO.getFromCreatedTime()==null){
            reportArriveQO.setFromCreatedTime(new Long(LocalDateTime.now().withHour(0).withMinute(0).toEpochSecond(ZoneOffset.of("+8"))).intValue());
            reportArriveQO.setToCreatedTime(new Long(LocalDateTime.now().withHour(23).withMinute(59).toEpochSecond(ZoneOffset.of("+8"))).intValue());
        }
*/

        reportArriveQO.setNetworks(me.getNetworks());
        List<ReportArriveDO> list = waybillArriveDao.queryReportArrive(reportArriveQO);
        page.setTotalCount(waybillArriveDao.queryReportArriveCount(reportArriveQO));
        //page.setTotalCount(commonDao.lastFoundRows());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        System.out.println(" = " + list.size());

        if (fileType.equals("json")) {
            request.setAttribute("toDate", toPaginationLayUIData(page, list));
            return "common/toResponseBody";
        }


        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/arrive.jasper");
        model.addAttribute("format", fileType); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
