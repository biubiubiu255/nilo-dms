package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.ReadExcel;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.common.CellData;
import com.nilo.dms.common.ExcelData;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/2.
 */
@Controller
@RequestMapping("/waybill/batch_sign")
public class BatchSignController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WaybillOptService waybillOptService;

    @Autowired
    private SendThirdService sendThirdService;

    @Value("#{configProperties['excel_save_path']}")
    private String savePath;

    @RequestMapping(value = "/page.html", method = RequestMethod.GET)
    public String receivePage(Model model) {
        return "waybill/batch_sign/page";
    }

    @RequestMapping(value = "/importSignData.html", method = RequestMethod.POST)
    @ResponseBody
    public String importSignDataNew(Model model, @RequestParam("file") CommonsMultipartFile file) {
        //保存上传的excel
        ExcelData excelData = null;
        try {
            String fileSaveName = file.getFileItem().getName();
            excelData = ReadExcel.readTable(file.getInputStream(), fileSaveName);
        } catch (Exception e) {
            throw new RuntimeException("Excel Parse Failed.");
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, List<CellData>> entry : excelData.getData().entrySet()) {
            for (CellData cell1 : entry.getValue()) {
                if (StringUtil.isNotBlank(cell1.getValue())) {
                    list.add(cell1.getValue());
                }
            }
        }

        if (list.size() == 0) throw new IllegalArgumentException("Excel Data Error.");

        SendThirdHead sendThirdHead = sendThirdService.queryDetailsByHandleNo(list.get(0));

        Principal principal = SessionLocal.getPrincipal();
        principal.setUserId(sendThirdHead.getThirdExpressCode());


        List<String> resultList = new ArrayList<>();
        //批量签收
        for (String orderNo : list) {
            try {
                waybillOptService.sign(orderNo, "Batch Sign");
                resultList.add(orderNo + ": success");
            } catch (Exception e) {
                resultList.add("<font color='red'>" + orderNo + "</font>:" + e.getMessage());
            }

        }
        return toJsonTrueData(resultList);
    }

    //原版
    public String importSignData(Model model, @RequestParam("file") CommonsMultipartFile file) {
        //保存上传的excel
        ExcelData excelData = null;
        try {
            String fileSaveName = IdWorker.getInstance().nextId() + ".xlsx";
            FileUtil.saveFile(file.getInputStream(), savePath, fileSaveName);
            //解析excel
            String fileName = file.getOriginalFilename();
            excelData = ReadExcel.readTable(file.getInputStream(), fileName);
        } catch (Exception e) {
            throw new RuntimeException("Excel Parse Failed.");
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, List<CellData>> entry : excelData.getData().entrySet()) {
            for (CellData cell1 : entry.getValue()) {
                if (StringUtil.isNotBlank(cell1.getValue())) {
                    list.add(cell1.getValue());
                }
            }
        }

        if (list.size() == 0) throw new IllegalArgumentException("Excel Data Error.");
        List<String> resultList = new ArrayList<>();
        //批量签收
        for (String orderNo : list) {
            try {
                waybillOptService.sign(orderNo, "Batch Sign");
                resultList.add(orderNo + ": success");
            } catch (Exception e) {
                resultList.add("<font color='red'>" + orderNo + "</font>:" + e.getMessage());
            }

        }
        return toJsonTrueData(resultList);
    }

}
