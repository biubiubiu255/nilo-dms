package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;
import com.nilo.dms.common.enums.AbnormalOrderStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/2.
 */
@Controller
@RequestMapping("/order/abnormalOrder")
public class AbnormalOrderController extends BaseController {
    @Autowired
    private RiderOptService riderOptService;

    @Autowired
    private AbnormalOrderService abnormalOrderService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "abnormal_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(QueryAbnormalOrderParameter parameter, String type, String all) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        List<Integer> status = new ArrayList<>();
        status.add(AbnormalOrderStatusEnum.CREATE.getCode());
        if (StringUtil.isNotEmpty(all)) {
            status.add(AbnormalOrderStatusEnum.COMPLETE.getCode());
        }
        if (StringUtil.isNotEmpty(type)) {
            List<String> types = new ArrayList<>();
            types.add(type);
            parameter.setAbnormalType(types);
        }
        parameter.setStatus(status);
        Pagination page = getPage();
        List<AbnormalOrder> list = abnormalOrderService.queryAbnormalBy(parameter, page);
        return toPaginationLayUIData(page, list);
    }


    @ResponseBody
    @RequestMapping(value = "/abnormal.html")
    public String abnormal(AbnormalOrder abnormalOrder) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {

            AbnormalParam param = new AbnormalParam();
            abnormalOrder.setCreatedBy(me.getUserId());
            abnormalOrder.setMerchantId(merchantId);
            param.setAbnormalOrder(abnormalOrder);
            param.setOptBy(me.getUserId());
            riderOptService.abnormal(param);

        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/handleAbnormal.html")
    public String handleAbnormal(AbnormalOptRequest request, String returnToMerchantFlag, String handleTypeCode) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {

            AssertUtil.isNotBlank(handleTypeCode, BizErrorCode.HANDLE_TYPE_EMPTY);

            request.setMerchantId(merchantId);
            if (StringUtil.equals(returnToMerchantFlag, "Y")) {
                request.setReturnToMerchant(true);
            }
            request.setHandleType(AbnormalHandleTypeEnum.getEnum(handleTypeCode));
            request.setOptBy(me.getUserId());
            abnormalOrderService.handleAbnormal(request);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/handlePage.html")
    public String handlePage(Model model, String abnormalNo, String orderNo, String referenceNo) {
        model.addAttribute("abnormalNo", abnormalNo);
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("referenceNo", referenceNo);

        //查询上一次dispatch task
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Task task = taskService.queryTaskByTypeAndOrderNo(merchantId, TaskTypeEnum.DISPATCH.getCode(), orderNo);
        if (task != null) {
            model.addAttribute("rider", task.getHandledBy());
        }
        return "abnormal_order/handle";
    }

}
