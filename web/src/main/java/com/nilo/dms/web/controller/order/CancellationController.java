package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.PaymentService;
import com.nilo.dms.dto.order.WaybillPaymentOrder;
import com.nilo.dms.dto.order.WaybillPaymentRecord;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 手动核销快递员配送运单收款
 * Created by admin on 2018/2/26.
 */
@Controller
@RequestMapping("/order/cancellation")
public class CancellationController extends BaseController {

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/listPage.html")
    public String list(Model model) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("riderList", getRiderList(merchantId));
        return "delivery_order/cancellation/list";
    }

    @ResponseBody
    @RequestMapping("/list.html")
    public String getList(String userId) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<WaybillDO> list = waybillDao.queryAllNotCancellation(Long.parseLong(merchantId), userId, page.getOffset(), page.getLimit());
        Long count = waybillDao.queryAllNotCancellationCount(Long.parseLong(merchantId), userId);
        page.setTotalCount(count);
        return toPaginationLayUIData(page, list);
    }

    @ResponseBody
    @RequestMapping("/handle.html")
    public String handle(@RequestParam(value = "orderNos[]", required = false) String[] orderNos, String transNo, Double priceAmount) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        WaybillPaymentOrder paymentOrder = new WaybillPaymentOrder();
        String id = IdWorker.getInstance().nextId() + "";
        paymentOrder.setId(id);
        paymentOrder.setNetworkId(me.getNetworks().get(0));
        paymentOrder.setPaymentTime(DateUtil.getSysTimeStamp());
        paymentOrder.setPriceAmount(new BigDecimal(priceAmount));
        paymentOrder.setWaybillCount(orderNos.length);
        paymentOrder.setStatus(1);
        paymentService.savePaymentOrder(paymentOrder, Arrays.asList(orderNos));


        WaybillPaymentRecord record = new WaybillPaymentRecord();
        record.setStatus(1);
        record.setPaymentOrderId(id);
        record.setThirdPaySn(transNo);
        paymentService.savePaymentOrderRecord(record);

        //修改运单已经付款金额
        for (String orderNo : orderNos) {

            WaybillDO query = waybillDao.queryByOrderNo(Long.parseLong(me.getMerchantId()),orderNo);
            WaybillDO orderDO = new WaybillDO();
            orderDO.setMerchantId(Long.parseLong(me.getMerchantId()));
            orderDO.setOrderNo(orderNo);
            orderDO.setAlreadyPaid(query.getNeedPayAmount());
            waybillDao.update(orderDO);
        }
        return toJsonTrueMsg();
    }

}
