package com.nilo.dms.web.controller.task;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.dao.UserInfoDao;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
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
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/task/pickup")
public class PickUpTaskController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RiderOptService riderOptService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "task/pick_up/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getPickupList(String orderNo, @RequestParam(value = "taskStatus[]", required = false) Integer[] taskStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        //查询取件任务
        TaskParameter parameter = new TaskParameter();
        parameter.setOrderNo(orderNo);
        parameter.setMerchantId(merchantId);
        parameter.setTaskType(Arrays.asList(TaskTypeEnum.PICK_UP.getCode()));
        parameter.setHandledBy(me.getUserId());
        parameter.setStatus(Arrays.asList(TaskStatusEnum.CREATE.getCode()));
        Pagination page = getPage();
        List<Task> list = taskService.queryTask(parameter, page);

        return toPaginationLayUIData(page, list);
    }

    @ResponseBody
    @RequestMapping(value = "/pickup.html")
    public String pickup(String orderNo, String taskId) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            riderOptService.pickup(merchantId, orderNo, me.getUserId(), taskId);

        } catch (Exception e) {
            log.error("pickup failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/pickupFailedPage.html")
    public String pickupFailedPage(Model model, String referenceNo, String orderNo, String taskId) {
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("taskId", taskId);
        model.addAttribute("referenceNo", referenceNo);
        return "task/pick_up/pick_up_failed";
    }

    @ResponseBody
    @RequestMapping(value = "/pickupFailed.html")
    public String pickupFailed(String orderNo, String taskId, String notes, String type) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            riderOptService.pickupFailed(merchantId, orderNo, notes, me.getUserId(), taskId);
        } catch (Exception e) {
            log.error("pickup failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/goToPickup.html")
    public String goToPickup(String orderNo, String taskId) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            riderOptService.goToPickup(merchantId, orderNo, me.getUserId(), taskId);
        } catch (Exception e) {
            log.error("pickup failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, HttpServletRequest request) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String[] orderNos = request.getParameter("orderNos").split(",");
        //查询订单详情
        List<DeliveryOrder> list = new ArrayList<>();
        for (int i = 0; i < orderNos.length; i++) {
            DeliveryOrder order = orderService.queryByOrderNo(merchantId, orderNos[i]);
            list.add(order);
        }
        model.addAttribute("list", list);
        return "task/pick_up/print";
    }

    @RequestMapping(value = "/transferPage.html")
    public String transferPage(Model model, String taskId) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //快递员
        model.addAttribute("list", getRiderList(merchantId));
        model.addAttribute("taskId", taskId);
        return "task/pick_up/transfer";
    }

    @ResponseBody
    @RequestMapping(value = "/transfer.html")
    public String transfer(String userId, String orderNo, String taskId, String remark) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
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
