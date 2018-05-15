package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.AbnormalTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.HandleDelayDao;
import com.nilo.dms.dto.handle.HandleDelay;
import com.nilo.dms.dto.order.AbnormalOrder;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.web.controller.BaseController;
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
    private WaybillOptService waybillOptService;
    @Autowired
    private AbnormalOrderService abnormalOrderService;
    @Autowired
    private HandleDelayDao handleDelayDao;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "delay_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(String orderNo, String fromTime, String toTime) {

        Principal me = SessionLocal.getPrincipal();
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

        List<HandleDelay> delayDOList = new ArrayList<>();
        Long count = handleDelayDao.queryCountBy(Long.parseLong(merchantId), orderNo, fromTimeLong, toTimeLong);
        if (count == null || count == 0) return toPaginationLayUIData(page, delayDOList);

        delayDOList = handleDelayDao.queryBy(Long.parseLong(merchantId), orderNo, fromTimeLong, toTimeLong, page.getOffset(), page.getLimit());

        page.setTotalCount(handleDelayDao.queryCountBy(Long.parseLong(merchantId), orderNo, fromTimeLong, toTimeLong));

        return toPaginationLayUIData(page, delayDOList);
    }

    @RequestMapping(value = "/problemPage.html")
    public String problemPage(Model model, String orderNo) {
        //查询上一次dispatch task
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        List<HandleDelay> handleDelays = handleDelayDao.queryBy(Long.parseLong(merchantId), orderNo, null, null, 0, 2);
        if (handleDelays.size() > 0) {
            //查询rider列表
            model.addAttribute("delayDO", handleDelays.get(0));
        }
        return "delay_order/problem";
    }

    @ResponseBody
    @RequestMapping(value = "/problem.html")
    public String problem(DelayParam param) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        AbnormalOrder abnormalOrder = new AbnormalOrder();
        abnormalOrder.setCreatedBy(me.getUserId());
        abnormalOrder.setMerchantId(merchantId);
        abnormalOrder.setReason(param.getReason());
        abnormalOrder.setOrderNo(param.getOrderNo());
        abnormalOrder.setRemark(param.getRemark());
        abnormalOrder.setAbnormalType(AbnormalTypeEnum.PROBLEM);
        abnormalOrder.setMerchantId(me.getMerchantId());
        abnormalOrderService.addAbnormalOrder(abnormalOrder);
        waybillOptService.completeDelay(param.getOrderNo());

        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/resend.html")
    public String resend(DelayParam param) {
        waybillOptService.completeDelay(param.getOrderNo());
        return toJsonTrueMsg();
    }


    @ResponseBody
    @RequestMapping(value = "/delay.html")
    public String delay(DelayParam param) {
        waybillOptService.delay(param);
        return toJsonTrueMsg();
    }
}
