package com.nilo.dms.web.controller.order;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.ClientTypeEnum;
import com.nilo.dms.common.enums.FetchTypeEnum;
import com.nilo.dms.common.utils.*;
import com.nilo.dms.common.utils.model.CellData;
import com.nilo.dms.common.utils.model.ExcelData;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
@RequestMapping("/order/deliveryOrder")
public class DeliveryOrderController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("#{configProperties['template_file_path']}")
    private String templatePath;

    @Value("#{configProperties['excel_save_path']}")
    private String savePath;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "delivery_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(DeliveryOrderParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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
        List<DeliveryOrder> list = orderService.queryDeliveryOrderBy(parameter, page);
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/importOrderData.html", method = RequestMethod.POST)
    @ResponseBody
    public String importOrderData(Model model, @RequestParam("file") CommonsMultipartFile file) {
        try {


            //保存上传的问题件
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            String fileSaveName = IdWorker.getInstance().nextId() + ".xlsx";
            FileUtil.saveFile(file.getInputStream(), savePath, fileSaveName);

            String fileName = file.getOriginalFilename();
            ExcelData excelData = ReadExcel.readTable(file.getInputStream(), fileName);

            Map<String, DeliveryOrder> deliveryOrderMap = new HashMap<>();

            for (Map.Entry<String, List<CellData>> entry : excelData.getData().entrySet()) {

                //订单头信息
                DeliveryOrder deliveryOrder = new DeliveryOrder();
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
            for (Map.Entry<String, DeliveryOrder> entry : deliveryOrderMap.entrySet()) {

                orderService.addCreateDeliveryOrderRequest(merchantId, JSON.toJSONString(entry.getValue()), "import");
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

    @RequestMapping(value = "/{orderNo}.html", method = RequestMethod.GET)
    public String details(Model model, @PathVariable String orderNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);
        List<DeliveryRoute> orderRouteList = deliveryRouteService.queryRoute(merchantId, orderNo);
        model.addAttribute("deliveryOrder", deliveryOrder);
        model.addAttribute("orderRouteList", orderRouteList);

        return "delivery_order/details";
    }

    @RequestMapping(value = "/edit/{orderNo}.html", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String orderNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);
        model.addAttribute("delivery", deliveryOrder);

        return "delivery_order/edit";
    }

    @RequestMapping(value = "/print/{orderNo}.html", method = RequestMethod.GET)
    public String print(Model model, @PathVariable String orderNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询订单详情
        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);
        model.addAttribute("delivery", deliveryOrder);

        return "delivery_order/print";
    }

    @RequestMapping(value = "/export.html", method = RequestMethod.GET)
    public String export(DeliveryOrderParameter parameter, @RequestParam(value = "orderTypes[]", required = false) String[] orderTypes, @RequestParam(value = "orderStatus[]", required = false) Integer[] orderStatus) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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
        List<DeliveryOrder> list = orderService.queryDeliveryOrderBy(parameter, page);

        ExportExcel exportExcel = new ExportExcel();

        List<String> header = Arrays.asList("orderNo","orderType","referenceNo","orderTime","country","orderPlatform","totalPrice","needPayAmount","weight","status");

        List<Map<String, String>> data = new ArrayList<>();
        for(DeliveryOrder order : list) {
            Map<String, String> map = new HashMap<>();
            for (String field : header) {
                String value = null;
                try {
                    value = (String) PropertyUtils.getSimpleProperty(order, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                map.put(field, value);
            }
            data.add(map);
        }
        exportExcel.fillData(data,header);
        exportExcel.export("waybill_"+DateUtil.getSysTimeStamp());

        return "delivery_order/print";
    }
}
