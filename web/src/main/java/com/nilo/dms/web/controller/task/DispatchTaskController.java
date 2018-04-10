package com.nilo.dms.web.controller.task;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.order.model.Task;
import com.nilo.dms.service.order.model.TaskParameter;
import com.nilo.dms.service.order.model.Waybill;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/task/dispatch")
public class DispatchTaskController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskService taskService;

    @Autowired
    private WaybillService waybillService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return isMobile(request) ? "task/dispatch/mobile/list" : "task/dispatch/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getDispatchList(String orderNo, @RequestParam(value = "taskStatus[]", required = false) Integer[] taskStatus) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询配送任务
        TaskParameter parameter = new TaskParameter();
        parameter.setOrderNo(orderNo);
        parameter.setMerchantId(merchantId);
        //如果当前用户不是快递员查询条件
        if (me.isRider()) {
            parameter.setHandledBy(me.getUserId());
            parameter.setTaskType(Arrays.asList(new String[]{TaskTypeEnum.DELIVERY.getCode()})); //快递员类型权限
        } else {
            parameter.setTaskType(Arrays.asList(TaskTypeEnum.SELF_DELIVERY.getCode(), TaskTypeEnum.SEND.getCode()));  //管理员，自提、第三方权限
            parameter.setHandledBy("" + me.getNetworks().get(0));
        }
        parameter.setStatus(Arrays.asList(new Integer[]{TaskStatusEnum.CREATE.getCode()}));

        Pagination page = getPage();
        List<Task> list = taskService.queryTask(parameter, page);
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, HttpServletRequest request) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String[] orderNos = request.getParameter("orderNos").split(",");
        //查询订单详情
        List<Waybill> list = new ArrayList<>();
        for (int i = 0; i < orderNos.length; i++) {
            Waybill order = waybillService.queryByOrderNo(merchantId, orderNos[i]);
            list.add(order);
        }
        model.addAttribute("list", list);
        return "task/dispatch/print";
    }

    @RequestMapping(value = "/transferPage.html")
    public String transferPage(Model model, String taskId) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        //获取快递员列表，排除自己
        List<StaffDO> list = getRiderList(me.getCompanyId());
        Iterator<StaffDO> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (StringUtil.equals(me.getUserId(), "" + iterator.next().getUserId())) {
                iterator.remove();
            }
        }

        model.addAttribute("list", list);
        model.addAttribute("taskId", taskId);
        return "task/dispatch/transfer";
    }

    @RequestMapping(value = "/abnormalPage.html", method = RequestMethod.GET)
    public String abnormalPage(Model model, String orderNo, String taskId, String referenceNo) {
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("taskId", taskId);
        model.addAttribute("referenceNo", referenceNo);
        return "task/dispatch/refuse";
    }

    @RequestMapping(value = "/delayPage.html", method = RequestMethod.GET)
    public String delayPage(Model model, String orderNo, String taskId, String referenceNo) {
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("taskId", taskId);
        model.addAttribute("referenceNo", referenceNo);
        return "task/dispatch/delay";
    }

    @ResponseBody
    @RequestMapping(value = "/transfer.html")
    public String transfer(String userId, String orderNo, String taskId, String remark) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            taskService.transferTask(taskId, me.getUserId(), userId, remark);
        } catch (Exception e) {
            log.error("pickup failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
