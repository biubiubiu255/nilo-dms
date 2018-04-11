package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.ReadExcel;
import com.nilo.dms.dto.util.CellData;
import com.nilo.dms.dto.util.ExcelData;
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
import java.util.HashMap;
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

    @Value("#{configProperties['excel_save_path']}")
    private String savePath;

    @RequestMapping(value = "/page.html", method = RequestMethod.GET)
    public String receivePage(Model model, String orderNo, String taskId, String referenceNo) {
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("taskId", taskId);
        model.addAttribute("referenceNo", referenceNo);
        return "task/dispatch/receive";
    }

    @RequestMapping(value = "/importSignData.html", method = RequestMethod.POST)
    @ResponseBody
    public String importSignData(Model model, @RequestParam("file") CommonsMultipartFile file) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try {
            //保存上传的excel
            String fileSaveName = IdWorker.getInstance().nextId() + ".xlsx";
            FileUtil.saveFile(file.getInputStream(), savePath, fileSaveName);

            //解析excel
            String fileName = file.getOriginalFilename();
            ExcelData excelData = ReadExcel.readTable(file.getInputStream(), fileName);
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, List<CellData>> entry : excelData.getData().entrySet()) {
                for (CellData cell1 : entry.getValue()) {
                    list.add(cell1.getValue());
                }
            }
            //批量签收
            for (String orderNo : list) {
                try {
                    waybillOptService.sign(orderNo, "Batch Sign");
                } catch (Exception e) {
                    Map<String, String> map = new HashMap<>();
                    map.put("message", orderNo + ":" + e.getMessage());
                    resultList.add(map);
                }
            }
        } catch (Exception e) {
            log.error("importSignData failed.", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(resultList);
    }


}
