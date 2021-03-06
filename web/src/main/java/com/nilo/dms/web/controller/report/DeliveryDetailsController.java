package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.*;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/11/20.
 */
@Controller
@RequestMapping("/report")
public class DeliveryDetailsController extends BaseController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderOptLogService orderOptLogService;

    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @Autowired
    private AbnormalOrderService abnormalOrderService;


    @RequestMapping(value = "/deliveryDetailsReport.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "report/delivery_details";
    }

    @ResponseBody
    @RequestMapping(value = "/deliveryDetailsList.html")
    public String getOrderList(DeliveryOrderParameter parameter, @RequestParam(value = "orderTypes[]",required=false)String[] orderTypes, @RequestParam(value = "orderStatus[]",required=false)Integer[] orderStatus) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        if(orderTypes!= null && orderTypes.length >0){
            parameter.setOrderType(Arrays.asList(orderTypes));
        }
        if(orderStatus!= null && orderStatus.length >0){
            parameter.setStatus(Arrays.asList(orderStatus));
        }
        Pagination page = getPage();

        //订单信息
        List<DeliveryOrder> list = orderService.queryDeliveryOrderBy(parameter, page);
        //所有订单号
        List<String> orderNos = new ArrayList<String>();
        for(DeliveryOrder order : list){
            orderNos.add(order.getOrderNo());
        }
        //订单操作记录
        List<DeliveryOrderOpt> optList = orderOptLogService.queryByOrderNos(merchantId,orderNos);
        //订单轨迹信息


        return toPaginationLayUIData(page, list);
    }
}
