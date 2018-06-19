package com.nilo.dms.web.controller.order;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.*;
import com.nilo.dms.dao.dataobject.WaybillRouteDO;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.common.CellData;
import com.nilo.dms.common.ExcelData;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.report.model.ReportUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/waybill")
public class WaybillController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("#{configProperties['template_file_path']}")
    private String templatePath;

    @Value("#{configProperties['excel_save_path']}")
    private String savePath;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "delivery_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(WaybillParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        if (orderTypes != null && orderTypes.length > 0) {
            parameter.setOrderType(Arrays.asList(orderTypes));
        }
        if (orderStatus != null && orderStatus.length > 0) {
            parameter.setStatus(Arrays.asList(orderStatus));
        }
        parameter.setIsPackage("0");

        Pagination page = getPage();
        List<Waybill> list = waybillService.queryWaybillBy(parameter, page);
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/importOrderData.html", method = RequestMethod.POST)
    @ResponseBody
    public String importOrderData(Model model, @RequestParam("file") CommonsMultipartFile file) {
        try {


            //保存上传的问题件
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            String fileSaveName = IdWorker.getInstance().nextId() + ".xlsx";
            FileUtil.saveFile(file.getInputStream(), savePath, fileSaveName);

            String fileName = file.getOriginalFilename();
            ExcelData excelData = ReadExcel.readTable(file.getInputStream(), fileName);

            Map<String, Waybill> deliveryOrderMap = new HashMap<>();

            for (Map.Entry<String, List<CellData>> entry : excelData.getData().entrySet()) {

                //订单头信息
                Waybill deliveryOrder = new Waybill();
                deliveryOrder.setMerchantId(merchantId);
                Field[] deliveryOrderFields = deliveryOrder.getClass().getDeclaredFields();
                for (Field f1 : deliveryOrderFields) {
                    for (CellData cell1 : entry.getValue()) {
                        if (StringUtil.equals(f1.getName(), cell1.getColName())) {
                            setProperty(f1, cell1.getValue(), deliveryOrder);
                        }
                    }
                }
                //收货人信息
                ReceiverInfo receiverInfo = new ReceiverInfo();
                Field[] receiverInfoFields = receiverInfo.getClass().getDeclaredFields();
                for (Field f2 : receiverInfoFields) {
                    for (CellData cell2 : entry.getValue()) {
                        if (StringUtil.equals(f2.getName(), cell2.getColName())) {
                            setProperty(f2, cell2.getValue(), receiverInfo);
                        }
                    }
                }
                //发件人信息
                SenderInfo senderInfo = new SenderInfo();
                Field[] senderInfoFields = senderInfo.getClass().getDeclaredFields();
                for (Field f3 : senderInfoFields) {
                    for (CellData cell3 : entry.getValue()) {
                        if (StringUtil.equals(f3.getName(), cell3.getColName())) {
                            setProperty(f3, cell3.getValue(), senderInfo);
                        }
                    }
                }
                deliveryOrder.setReceiverInfo(receiverInfo);
                deliveryOrder.setSenderInfo(senderInfo);

                //商品列表信息
                GoodsInfo goodsInfo = new GoodsInfo();
                Field[] goodsInfoFields = goodsInfo.getClass().getDeclaredFields();
                for (Field f : goodsInfoFields) {
                    for (CellData cell : entry.getValue()) {
                        if (StringUtil.equals(f.getName(), cell.getColName())) {
                            setProperty(f, cell.getValue(), goodsInfo);
                        }
                    }
                }

                if (deliveryOrderMap.containsKey(deliveryOrder.getReferenceNo())) {
                    List<GoodsInfo> list = deliveryOrderMap.get(deliveryOrder.getReferenceNo()).getGoodsInfoList();
                    list.add(goodsInfo);
                } else {
                    List<GoodsInfo> list = new ArrayList<>();
                    list.add(goodsInfo);
                    deliveryOrder.setGoodsInfoList(list);
                    deliveryOrderMap.put(deliveryOrder.getReferenceNo(), deliveryOrder);
                }

            }
            for (Map.Entry<String, Waybill> entry : deliveryOrderMap.entrySet()) {

                waybillService.createWaybillRequest(merchantId, JSON.toJSONString(entry.getValue()), "import");
            }
        } catch (Exception e) {
            log.error("import OrderData failed.", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/exportTemplate.html")
    public void exportTemplate(Model model, HttpServletResponse response) {

        File file = new File(templatePath);
        if (!file.exists()) {
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] b = new byte[1024];
        int len = 0;
        try {
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();

            response.setContentType("application/force-download");
            String filename = file.getName();
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentLength((int) file.length());

            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
        } catch (Exception e) {
            log.error("download template  failed.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("download template close stream failed.", e);
                }
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("download template close stream failed.", e);
            }
        }
    }


    //获取订单详情
    @RequestMapping(value = "/{orderNo}.html", method = RequestMethod.GET)
    public String details(Model model, @PathVariable String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);
        List<WaybillRouteDO> waybillRouteDOS = deliveryRouteService.queryRoute(merchantId, orderNo);

        model.addAttribute("deliveryOrder", deliveryOrder);
        model.addAttribute("orderRouteList", waybillRouteDOS);

        return "delivery_order/details";
    }

    @RequestMapping(value = "/edit/{orderNo}.html", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);
        model.addAttribute("delivery", deliveryOrder);

        return "delivery_order/edit";
    }

    @RequestMapping(value = "/print/{orderNo}.html", method = RequestMethod.GET)
    public String print(Model model, @PathVariable String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);
        
        model.addAttribute("delivery", deliveryOrder);

        return "delivery_order/print";
    }
    
    @RequestMapping(value = "/arriveScanPrint/{orderNo}.html", method = RequestMethod.GET)
    public String arriveScanPrint(Model model, @PathVariable String orderNo) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);
        
        if(deliveryOrder.getWeight()==null||deliveryOrder.getWeight()==0) {
        	throw new DMSException(BizErrorCode.WEIGHT_MORE_THAN_0);
        };
        
        model.addAttribute("delivery", deliveryOrder);

        return "delivery_order/print";
    }

    @RequestMapping(value = "/export.html", method = RequestMethod.GET)
    public String export(HttpServletResponse response, WaybillParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus) throws Exception {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        if (orderTypes != null && orderTypes.length > 0) {
            parameter.setOrderType(Arrays.asList(orderTypes));
        }
        if (orderStatus != null && orderStatus.length > 0) {
            parameter.setStatus(Arrays.asList(orderStatus));
        }

        Pagination page = getPage();
        List<Waybill> list = waybillService.queryWaybillBy(parameter, page);

        HSSFWorkbook wb = new HSSFWorkbook();
        ExportExcel exportExcel = new ExportExcel(wb);
        List<String> header = Arrays.asList("orderNo", "orderType", "referenceNo", "orderTime", "country", "orderPlatform", "totalPrice", "needPayAmount", "weight", "status", "receiverName", "receiverPhone", "receiverAddress");

        List<Map<String, String>> data = new ArrayList<>();
        for (Waybill order : list) {
            Map<String, String> map = new HashMap<>();
            for (String field : header) {

                String value = null;
                try {
                    value = getProperty(order, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    value = getProperty(order.getReceiverInfo(), field);
                }
                map.put(field, value);
            }
            data.add(map);
        }
        exportExcel.fillData(list, Waybill.class);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(
                ("waybill(" + DateUtil.formatCurrent(DateUtil.SHORT_FORMAT) + ").xls").getBytes(), "utf-8"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return null;
    }


    @RequestMapping(value = "/exportPlus.html", method = RequestMethod.GET)
    public String exportPlus(WaybillParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus, Model model) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        if (orderTypes != null && orderTypes.length > 0) {
            parameter.setOrderType(Arrays.asList(orderTypes));
        }
        if (orderStatus != null && orderStatus.length > 0) {
            parameter.setStatus(Arrays.asList(orderStatus));
        }
        parameter.setIsPackage("0");

        Pagination page = getPage();
        List<Waybill> list = waybillService.queryWaybillBy(parameter, page);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        model.addAttribute("url", "/WEB-INF/jasper/report/waybill.jasper");
        model.addAttribute("format", "xls"); // 报表格式
        //model.addAttribute("net.sf.jasperreports.json.source", jrDataSource);
        //model.addAttribute("net.sf.jasperreports.json.source", input);
        // model.addAttribute("JSON_INPUT_STREAM", toJsonTrueData(list));
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }

}
