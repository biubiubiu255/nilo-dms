package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.AbnormalTypeEnum;
import com.nilo.dms.common.enums.DelayStatusEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderDelayDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDelayDO;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.system.SystemCodeUtil;
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
@RequestMapping("/order/delay")
public class DelayOrderController extends BaseController {
    @Autowired
    private RiderOptService riderOptService;
    @Autowired
    private AbnormalOrderService abnormalOrderService;
    @Autowired
    private DeliveryOrderDelayDao deliveryOrderDelayDao;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "delay_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(String orderNo, String fromTime, String toTime) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        Long fromTimeLong = null, toTimeLong = null;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeLong = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeLong = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }
        Pagination page = getPage();

        List<DeliveryOrderDelayDO> delayDOList = new ArrayList<>();
        Long count = deliveryOrderDelayDao.queryCountBy(Long.parseLong(merchantId), orderNo, fromTimeLong, toTimeLong);
        if (count == null || count == 0) return toPaginationLayUIData(page, delayDOList);

        delayDOList = deliveryOrderDelayDao.queryBy(Long.parseLong(merchantId), orderNo, fromTimeLong, toTimeLong, page.getOffset(), page.getLimit());

        //设置类型描述
        for (DeliveryOrderDelayDO d : delayDOList) {
            String abnormalTypeDesc = SystemCodeUtil.getCodeVal("" + d.getMerchantId(), Constant.DELAY_REASON, d.getDelayReason());
            d.setDelayReason(abnormalTypeDesc);
        }

        return toPaginationLayUIData(page, delayDOList);
    }

    @RequestMapping(value = "/problemPage.html")
    public String problemPage(Model model, String orderNo) {
        //查询上一次dispatch task
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        DeliveryOrderDelayDO delayDO = deliveryOrderDelayDao.findByOrderNo(Long.parseLong(merchantId), orderNo);
        //查询rider列表
        model.addAttribute("delayDO", delayDO);
        return "delay_order/problem";
    }

    @ResponseBody
    @RequestMapping(value = "/problem.html")
    public String problem(DelayParam param) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {

            AbnormalOrder abnormalOrder = new AbnormalOrder();
            abnormalOrder.setCreatedBy(me.getUserId());
            abnormalOrder.setMerchantId(merchantId);
            abnormalOrder.setReason(param.getReason());
            abnormalOrder.setOrderNo(param.getOrderNo());
            abnormalOrder.setRemark(param.getRemark());
            abnormalOrder.setAbnormalType(AbnormalTypeEnum.PROBLEM);
            abnormalOrderService.addAbnormalOrder(abnormalOrder);

            DeliveryOrderDelayDO update = new DeliveryOrderDelayDO();
            update.setOrderNo(param.getOrderNo());
            update.setMerchantId(Long.parseLong(merchantId));
            update.setStatus(DelayStatusEnum.COMPLETE.getCode());
            deliveryOrderDelayDao.update(update);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/resend.html")
    public String resend(DelayParam param) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();

        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            param.setOptBy(me.getUserId());
            param.setMerchantId(merchantId);
            riderOptService.resend(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }


    @ResponseBody
    @RequestMapping(value = "/delay.html")
    public String delay(DelayParam param) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            param.setOptBy(me.getUserId());
            param.setMerchantId(merchantId);
            riderOptService.delay(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
