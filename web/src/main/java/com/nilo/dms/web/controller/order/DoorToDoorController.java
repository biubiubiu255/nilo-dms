package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.UserInfoDao;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.OrderService;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/doorToDoor")
public class DoorToDoorController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "door_to_door/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(DeliveryOrderParameter parameter) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        parameter.setOrderType(Arrays.asList(new String[]{"DS"}));
/*
        parameter.setStatus(Arrays.asList(new Integer[]{DeliveryOrderStatusEnum.CREATE.getCode(), DeliveryOrderStatusEnum.ALLOCATED.getCode()}));
*/
        Pagination page = getPage();
        List<DeliveryOrder> list = orderService.queryDeliveryOrderBy(parameter, page);

        for (DeliveryOrder o : list) {
            if (o.getStatus() != DeliveryOrderStatusEnum.CREATE) {
                Task task = taskService.queryTaskByTypeAndOrderNo(o.getMerchantId(), TaskTypeEnum.PICK_UP.getCode(), o.getOrderNo());
                if (task != null) {
                    UserInfo riderInfo = userService.findUserInfoByUserId(o.getMerchantId(), task.getHandledBy());
                    if (riderInfo != null) {
                        o.setAllocatedRider(riderInfo.getName());
                    }
                }
            }
        }

        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/allocatePage.html")
    public String allocatePage(Model model, HttpServletRequest request, @RequestParam(value = "orderNos[]") String[] orderNos) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        request.getSession().setAttribute("orderNos", orderNos);
        model.addAttribute("list", getRiderList(merchantId));
        return "door_to_door/allocatePage";
    }

    @ResponseBody
    @RequestMapping(value = "/allocate.html")
    public String allocate(String userId, String remark, HttpServletRequest request) {

        String[] orderNos = (String[]) request.getSession().getAttribute("orderNos");
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        String merchantId = me.getMerchantId();
        try {
            if (orderNos == null) {
                throw new DMSException(BizErrorCode.ORDER_NO_EMPTY);
            }
            //操作订单
            OrderOptRequest optRequest = new OrderOptRequest();
            optRequest.setMerchantId(merchantId);
            optRequest.setOptBy(me.getUserId());
            optRequest.setOptType(OptTypeEnum.ALLOCATE);
            optRequest.setOrderNo(Arrays.asList(orderNos));
            orderService.handleOpt(optRequest);

            //添加上门揽件任务
            for (String orderNo : orderNos) {
                Task task = new Task();
                task.setMerchantId(merchantId);
                task.setStatus(TaskStatusEnum.CREATE);
                task.setCreatedBy(me.getUserId());
                task.setOrderNo(orderNo);
                task.setHandledBy(userId);
                task.setTaskType(TaskTypeEnum.PICK_UP);
                taskService.addTask(task);
            }
        } catch (Exception e) {
            log.error("allocate failed. orderNo:{}", orderNos, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();

    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, HttpServletRequest request, String orderNos) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DeliveryOrder> list = orderService.queryByOrderNos(merchantId, Arrays.asList(orderNos.split(",")));
        for (DeliveryOrder o : list) {
            if (o.isPrinted()) {
                throw new IllegalArgumentException("Delivery Order :" + o.getOrderNo() + " is already printed.");
            }
        }
        orderService.print(merchantId, Arrays.asList(orderNos.split(",")));
        model.addAttribute("list", list);
        return "door_to_door/print";
    }

    @RequestMapping(value = "/printAgain.html")
    public String printAgain(Model model, HttpServletRequest request, String orderNos) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DeliveryOrder> list = orderService.queryByOrderNos(merchantId, Arrays.asList(orderNos.split(",")));

        orderService.print(merchantId, Arrays.asList(orderNos.split(",")));

        model.addAttribute("list", list);
        return "door_to_door/print";
    }

}
