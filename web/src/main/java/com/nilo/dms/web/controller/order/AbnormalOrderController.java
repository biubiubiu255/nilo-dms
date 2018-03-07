package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.AbnormalHandleTypeEnum;
import com.nilo.dms.common.enums.AbnormalOrderStatusEnum;
import com.nilo.dms.common.enums.AbnormalTypeEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.dataobject.DeliveryOrderDelayDO;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/11/2.
 */
@Controller
@RequestMapping("/order/abnormalOrder")
public class AbnormalOrderController extends BaseController {

    @Autowired
    private AbnormalOrderService abnormalOrderService;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "abnormal_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(QueryAbnormalOrderParameter parameter, String all) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);

        List<Integer> status = new ArrayList<>();
        status.add(AbnormalOrderStatusEnum.CREATE.getCode());
        if (StringUtil.isNotEmpty(all)) {
            status.add(AbnormalOrderStatusEnum.COMPLETE.getCode());
        }

        parameter.setAbnormalType(Arrays.asList(AbnormalTypeEnum.PROBLEM.getCode()));
        parameter.setStatus(status);
        Pagination page = getPage();
        List<AbnormalOrder> list = abnormalOrderService.queryAbnormalBy(parameter, page);
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/addPage.html")
    public String addPage(Model model, String orderNo) {
        return "abnormal_order/add";
    }

    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String addAbnormal(String orderNo, String reason, String remark) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            AbnormalOrder abnormalOrder = new AbnormalOrder();
            abnormalOrder.setMerchantId(merchantId);
            abnormalOrder.setAbnormalType(AbnormalTypeEnum.PROBLEM);
            abnormalOrder.setCreatedBy(me.getUserId());
            abnormalOrder.setRemark(remark);
            abnormalOrder.setOrderNo(orderNo);
            abnormalOrder.setReason(reason);
            abnormalOrderService.addAbnormalOrder(abnormalOrder);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/delete.html", method = RequestMethod.POST)
    public String delete(String abnormalNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            abnormalOrderService.delete(abnormalNo, merchantId);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
