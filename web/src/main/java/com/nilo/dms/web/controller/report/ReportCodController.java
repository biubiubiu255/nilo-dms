package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.WaybillArriveDao;
import com.nilo.dms.dao.WaybillCodDao;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.service.order.ReportService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.ReportCodQuery;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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
        //InputStream input = new ByteArrayInputStream("{\"country\": \"aaabbb\"}".getBytes());
        System.out.println("本次测试 = " + toJsonTrueData(list));
        //InputStream input = new ByteArrayInputStream(toJsonTrueData(list).getBytes());
        //InputStream input = new ByteArrayInputStream("{\"country\": \"aaa\" , \"dataa\": [{ \"qq\":\"ok\" },{ \"qq\":\"no\" }] }".getBytes());
        //InputStream input = new ByteArrayInputStream("{\"sales\": {\"item\": [{\"price\": \"2500\"}]}}".getBytes());
        //File file = new File("C:\\Users\\wenzhuo-company\\Desktop\\test.json");

        InputStream input = new ByteArrayInputStream(readToString("C:\\Users\\wenzhuo-company\\Desktop\\test.json").getBytes());
        JRDataSource jrDataSource1 = null;
        try {
            jrDataSource1 = new JsonDataSource(input);
        } catch (JRException e) {
            e.printStackTrace();
        }

        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/test2.jasper");
        model.addAttribute("format", fileType); // 报表格式
        //model.addAttribute("net.sf.jasperreports.json.source", jrDataSource);
        //model.addAttribute("net.sf.jasperreports.json.source", input);
       // model.addAttribute("JSON_INPUT_STREAM", toJsonTrueData(list));
        model.addAttribute("jrMainDataSource", jrDataSource1);
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

