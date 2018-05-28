package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.ReportService;
import com.nilo.dms.dto.order.ReportCodQuery;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/report/cod")
public class ReportCodController extends BaseController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String listPage(Model model) {
        return "report/cod/list";
    }

    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, Integer exportType, ReportCodQuery reportCodQuery, HttpServletRequest request) {
        Pagination page = getPage();
        //page.setTotalCount(commonDao.lastFoundRows());
        Principal me = SessionLocal.getPrincipal();
        reportCodQuery.setMerchantId(Long.valueOf(me.getMerchantId()));
        String fileType;
        switch (exportType){
            case 0 :
                        fileType = "pdf";
                        break;
            case 1 :
                        fileType = "xls";
                break;
            case 2 :
                fileType = "json";
                break;
            default:
                        fileType = "pdf";
        }

        System.out.println("fileTypeis = " + fileType);

        List<ReportCodDO> list = reportService.qeueryCodList(reportCodQuery, page);
        for (int i=0;i<list.size();i++){
            ReportCodDO reportCodDO = list.get(i);
            reportCodDO.setColumnStr(ReportUtil.queryDOtoStr(reportCodQuery));
            list.set(i, reportCodDO);
        }
        if (fileType.equals("json")){
            request.setAttribute("toDate", toPaginationLayUIData(page, list));
            return "common/toResponseBody";
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/cod.jasper");
        model.addAttribute("format", fileType); // 报表格式
        //model.addAttribute("net.sf.jasperreports.json.source", jrDataSource);
        //model.addAttribute("net.sf.jasperreports.json.source", input);
       // model.addAttribute("JSON_INPUT_STREAM", toJsonTrueData(list));
        model.addAttribute("jrMainDataSource", jrDataSource);
        System.out.println("本次测试 = " + ReportUtil.queryDOtoStr(reportCodQuery));
        model.addAttribute("POJO_COLMUN", ReportUtil.queryDOtoStr(reportCodQuery));
        return "iReportView"; // 对应jasper-defs.xml中的bean id
        //return toPaginationLayUIData(page, list);
    }

    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

}

