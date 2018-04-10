package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendReportService;
import com.nilo.dms.service.order.model.SendOrderParameter;
import com.nilo.dms.service.order.model.SendReport;
import com.nilo.dms.service.order.model.Waybill;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/report/send")
public class SendReportController extends BaseController {

    @Autowired
    private SendReportService sendReportService;

    @Autowired
    private ThirdExpressDao thirdExpressDao;


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        List<ThirdExpressDO> list = thirdExpressDao.findByMerchantIdAll();
//        for (ThirdExpressDO t:list) {
//            System.out.println(t);
//        }
        model.addAttribute("express",list);
        return "report/send/list";
    }


    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, SendOrderParameter parameter, @RequestParam(value = "carrierNames[]", required = false) String[] carrierNames, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus) {
        if(!(carrierNames==null)){
            System.out.println("----------------------");
        }
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        if (carrierNames != null && carrierNames.length > 0) {
            parameter.setCarrierName(Arrays.asList(carrierNames));
        }
        if (orderStatus != null && orderStatus.length > 0) {
            parameter.setStatus(Arrays.asList(orderStatus));
        }

        Pagination page = getPage();
        List<SendReport> list = sendReportService.querySendReport(parameter, page);

        List<Waybill> listF = new ArrayList<Waybill>();
        Waybill deliver = null;
        for (SendReport d: list) {
            deliver = new Waybill();
            BeanUtils.copyProperties(d, deliver);
            deliver.setNextNetworkDesc(d.getNextNetwork());
            deliver.setNetworkDesc(d.getNetwork());
            deliver.setAllocatedRider(d.getName());
            listF.add(deliver);
        }

        System.out.println(" = " + listF.size());
        System.out.println(" = " + listF.get(0).getOrderNo());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(listF);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/send.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
