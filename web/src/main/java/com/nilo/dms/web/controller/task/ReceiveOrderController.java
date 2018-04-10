package com.nilo.dms.web.controller.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.model.AbnormalParam;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
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

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.ReadExcel;
import com.nilo.dms.common.utils.model.CellData;
import com.nilo.dms.common.utils.model.ExcelData;
import com.nilo.dms.service.model.ReceiverData;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.SignForOrderParam;
import com.nilo.dms.service.order.model.Task;
import com.nilo.dms.web.controller.BaseController;

/**
 * Created by admin on 2017/11/2.
 */
@Controller
@RequestMapping("/task/dispatch")
public class ReceiveOrderController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RiderOptService riderOptService;

    @Autowired
    private TaskService taskService;

    @Value("#{configProperties['excel_save_path']}")
    private String savePath;

    @RequestMapping(value = "/receivePage.html", method = RequestMethod.GET)
    public String receivePage(Model model, String orderNo, String taskId, String referenceNo) {
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("taskId", taskId);
        model.addAttribute("referenceNo", referenceNo);
        return "task/dispatch/receive";
    }

    @ResponseBody
    @RequestMapping(value = "/receive.html")
    public String receive(String orderNo, String signBy, String remark) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            SignForOrderParam param = new SignForOrderParam();
            param.setMerchantId(merchantId);
            param.setOptBy(me.getUserId());
            param.setOrderNo(orderNo);
            param.setRemark(remark);
            param.setSignBy(signBy);
            riderOptService.signForOrder(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
    @ResponseBody
    @RequestMapping(value = "/refuse.html")
    public String refuse(String orderNo, String reason, String remark) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            AbnormalParam param = new AbnormalParam();
            param.setMerchantId(merchantId);
            param.setOptBy(me.getUserId());
            param.setOrderNo(orderNo);
            param.setRemark(remark);
            param.setReason(reason);
            riderOptService.refuse(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
    @RequestMapping(value = "/importSignData.html", method = RequestMethod.POST)
    @ResponseBody
    public String importSignData(Model model, @RequestParam("file") CommonsMultipartFile file) {
    	List<Map<String, String>> resultList = new ArrayList<>();
        try {
            //保存上传的问题件
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            String fileSaveName = IdWorker.getInstance().nextId() + ".xlsx";
            FileUtil.saveFile(file.getInputStream(), savePath, fileSaveName);

            String fileName = file.getOriginalFilename();
            ExcelData excelData = ReadExcel.readTable(file.getInputStream(), fileName);

            List<ReceiverData> list = new ArrayList<>();
            for (Map.Entry<String, List<CellData>> entry : excelData.getData().entrySet()) {
                ReceiverData receiverData = new ReceiverData();
                for (CellData cell1 : entry.getValue()) {
                    PropertyUtils.setProperty(receiverData, cell1.getColName(), cell1.getValue());
                }
                //查询TaskId
                Task task = taskService.queryTaskByTypeAndOrderNo(merchantId, TaskTypeEnum.DELIVERY.getCode(), receiverData.getOrderNo());
                if (task == null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("message", receiverData.getOrderNo()+":No Dispatch Task for this orderNo" );
                    resultList.add(map);
                    continue;
                }
                receiverData.setTaskId(task.getTaskId());
                list.add(receiverData);
            }

            //批量签收
            for (ReceiverData data : list) {
                SignForOrderParam param = new SignForOrderParam();
                param.setMerchantId(merchantId);
                param.setOptBy(me.getUserId());
                param.setOrderNo(data.getOrderNo());
                param.setRemark("Batch Sign");
                param.setSignBy("1");//本人
                try {
                    riderOptService.signForOrder(param);
                } catch (Exception e) {
                    Map<String, String> map = new HashMap<>();
                    map.put("message", data.getOrderNo()+":"+e.getMessage());
                    resultList.add(map);
                }
            }

        } catch (Exception e) {
            log.error("importSignData failed.", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(resultList);
    }

    /*private static class ReceiverData {
        private String orderNo;
        private String carrier;
        private String taskId;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }*/
}
