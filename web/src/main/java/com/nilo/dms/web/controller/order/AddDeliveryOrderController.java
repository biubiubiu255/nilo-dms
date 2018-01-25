package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.ClientTypeEnum;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderDao;
import com.nilo.dms.dao.DeliveryOrderGoodsDao;
import com.nilo.dms.dao.DeliveryOrderReceiverDao;
import com.nilo.dms.dao.DeliveryOrderSenderDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderGoodsDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderSenderDO;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.service.system.SystemConfig;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/21.
 */
@Controller
@RequestMapping("/order/deliveryOrder")
public class AddDeliveryOrderController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;

    @Autowired
    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;

    @Autowired
    private DeliveryOrderSenderDao deliveryOrderSenderDao;

    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(Model model) {
        return "delivery_order/add";
    }

    @RequestMapping(value = "/addGoodsPage.html", method = RequestMethod.GET)
    public String addGoodsPage(Model model, String orderNo) {
        model.addAttribute("orderNo", orderNo);
        return "delivery_order/add_goods";
    }

    @RequestMapping(value = "/addHeader.html", method = RequestMethod.POST)
    @ResponseBody
    public String addDeliveryOrderHeader(Model model, DeliveryOrderDO orderHeader, String orderDateTime) {

        String orderNo = "";
        try {

            //校验数据
            AssertUtil.isNotBlank(orderHeader.getReferenceNo(), BizErrorCode.REFERENCE_NO_EMPTY);
            AssertUtil.isNotBlank(orderHeader.getOrderType(), BizErrorCode.ORDER_TYPE_EMPTY);

            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            orderHeader.setMerchantId(Long.parseLong(merchantId));
            orderHeader.setOrderTime(DateUtil.parse(orderDateTime, DateUtil.LONG_WEB_FORMAT));
            orderHeader.setStatus(DeliveryOrderStatusEnum.CREATE.getCode());
            if (StringUtil.isEmpty(orderHeader.getOrderNo())) {
                orderNo = SystemConfig.getNextSerialNo(merchantId, SerialTypeEnum.DELIVERY_ORDER_NO.getCode());
                orderHeader.setOrderNo(orderNo);
                deliveryOrderDao.insert(orderHeader);
            } else {
                deliveryOrderDao.update(orderHeader);
            }
        } catch (Exception e) {
            log.error("add delivery order failed. order:{}", orderHeader, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(orderNo);
    }

    @RequestMapping(value = "/editHeader.html", method = RequestMethod.POST)
    @ResponseBody
    public String editDeliveryOrderHeader(Model model, DeliveryOrder order) {

        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            order.setMerchantId(merchantId);
            //1、保存订单信息
            DeliveryOrderDO orderHeader = new DeliveryOrderDO();
            orderHeader.setCountry(order.getCountry());
            orderHeader.setMerchantId(Long.parseLong(merchantId));
            orderHeader.setOrderPlatform(order.getOrderPlatform());
            orderHeader.setOrderTime(order.getOrderTime());
            orderHeader.setOrderType(order.getOrderType());
            orderHeader.setReferenceNo(order.getReferenceNo());
            orderHeader.setStatus(DeliveryOrderStatusEnum.CREATE.getCode());
            orderHeader.setTotalPrice(order.getTotalPrice());

            //获取订单号
            orderHeader.setOrderNo(order.getOrderNo());
            deliveryOrderDao.update(orderHeader);
        } catch (Exception e) {
            log.error("Edit delivery order failed. order:{}", order, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addSender.html", method = RequestMethod.POST)
    @ResponseBody
    public String addDeliveryOrderSender(Model model, SenderInfo senderInfo, String orderNo) {

        try {

            AssertUtil.isNotBlank(orderNo, BizErrorCode.ORDER_NO_EMPTY);

            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            DeliveryOrderSenderDO s = new DeliveryOrderSenderDO();
            s.setMerchantId(Long.parseLong(merchantId));
            s.setOrderNo(orderNo);
            s.setAddress(senderInfo.getSenderAddress());
            s.setArea(senderInfo.getSenderArea());
            s.setCity(senderInfo.getSenderCity());
            s.setContactNumber(senderInfo.getSenderPhone());
            s.setCountry(senderInfo.getSenderCountry());
            s.setName(senderInfo.getSenderName());
            s.setProvince(senderInfo.getSenderProvince());
            deliveryOrderSenderDao.insert(s);
        } catch (Exception e) {
            log.error("addDeliveryOrderSender failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(orderNo);
    }

    @RequestMapping(value = "/editSender.html", method = RequestMethod.POST)
    @ResponseBody
    public String editDeliveryOrderSender(Model model, SenderInfo senderInfo, String orderNo) {

        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            DeliveryOrderSenderDO s = new DeliveryOrderSenderDO();
            s.setMerchantId(Long.parseLong(merchantId));
            s.setOrderNo(orderNo);
            s.setAddress(senderInfo.getSenderAddress());
            s.setArea(senderInfo.getSenderArea());
            s.setCity(senderInfo.getSenderCity());
            s.setContactNumber(senderInfo.getSenderPhone());
            s.setCountry(senderInfo.getSenderCountry());
            s.setName(senderInfo.getSenderName());
            s.setProvince(senderInfo.getSenderProvince());
            deliveryOrderSenderDao.update(s);
        } catch (Exception e) {
            log.error("addDeliveryOrderSender failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(orderNo);
    }

    @RequestMapping(value = "/addReceiver.html", method = RequestMethod.POST)
    @ResponseBody
    public String addDeliveryOrderReceiver(Model model, ReceiverInfo receiverInfo, String orderNo) {

        try {

            AssertUtil.isNotBlank(orderNo, BizErrorCode.ORDER_NO_EMPTY);

            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            DeliveryOrderReceiverDO r = new DeliveryOrderReceiverDO();
            r.setMerchantId(Long.parseLong(merchantId));
            r.setOrderNo(orderNo);
            r.setAddress(receiverInfo.getReceiverAddress());
            r.setArea(receiverInfo.getReceiverArea());
            r.setCity(receiverInfo.getReceiverCity());
            r.setContactNumber(receiverInfo.getReceiverPhone());
            r.setCountry(receiverInfo.getReceiverCountry());
            r.setName(receiverInfo.getReceiverName());
            r.setProvince(receiverInfo.getReceiverProvince());
            deliveryOrderReceiverDao.insert(r);
        } catch (Exception e) {
            log.error("addDeliveryOrderReceiver failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/editReceiver.html", method = RequestMethod.POST)
    @ResponseBody
    public String editDeliveryOrderReceiver(Model model, ReceiverInfo receiverInfo, String orderNo) {

        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            DeliveryOrderReceiverDO r = new DeliveryOrderReceiverDO();
            r.setMerchantId(Long.parseLong(merchantId));
            r.setOrderNo(orderNo);
            r.setAddress(receiverInfo.getReceiverAddress());
            r.setArea(receiverInfo.getReceiverArea());
            r.setCity(receiverInfo.getReceiverCity());
            r.setContactNumber(receiverInfo.getReceiverPhone());
            r.setCountry(receiverInfo.getReceiverCountry());
            r.setName(receiverInfo.getReceiverName());
            r.setProvince(receiverInfo.getReceiverProvince());
            deliveryOrderReceiverDao.update(r);
        } catch (Exception e) {
            log.error("addDeliveryOrderReceiver failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addGoods.html", method = RequestMethod.POST)
    @ResponseBody
    public String addGoods(Model model, GoodsInfo goods, String orderNo) {

        try {

            AssertUtil.isNotBlank(orderNo, BizErrorCode.ORDER_NO_EMPTY);

            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            DeliveryOrderGoodsDO goodsDO = new DeliveryOrderGoodsDO();
            goodsDO.setMerchantId(Long.parseLong(merchantId));
            goodsDO.setOrderNo(orderNo);
            goodsDO.setUnitPrice(goods.getUnitPrice());
            goodsDO.setQuality(goods.getQuality());
            goodsDO.setQty(goods.getQty());
            goodsDO.setGoodsDesc(goods.getGoodsDesc());
            goodsDO.setGoodsId(goods.getGoodsId());

            deliveryOrderGoodsDao.insert(goodsDO);
        } catch (Exception e) {
            log.error("addGoods failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/delGoods.html", method = RequestMethod.POST)
    @ResponseBody
    public String delGoods(Model model, String goodsId, String orderNo, String id) {

        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            deliveryOrderGoodsDao.deleteBy(Long.parseLong(merchantId), orderNo, goodsId);
        } catch (Exception e) {
            log.error("delGoods failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/editGoods.html", method = RequestMethod.POST)
    @ResponseBody
    public String editGoods(Model model, GoodsInfo goods, String orderNo, String id) {

        try {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();

            DeliveryOrderGoodsDO goodsDO = new DeliveryOrderGoodsDO();
            goodsDO.setMerchantId(Long.parseLong(merchantId));
            goodsDO.setOrderNo(orderNo);
            goodsDO.setUnitPrice(goods.getUnitPrice());
            goodsDO.setQuality(goods.getQuality());
            goodsDO.setQty(goods.getQty());
            goodsDO.setGoodsDesc(goods.getGoodsDesc());
            goodsDO.setGoodsId(goods.getGoodsId());
            deliveryOrderGoodsDao.update(goodsDO);
        } catch (Exception e) {
            log.error("addGoods failed. orderNo:{}", orderNo, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getGoodsList.html")
    public String getGoodsList(String orderNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = new Pagination();
        List<DeliveryOrderGoodsDO> list = new ArrayList<>();
        if (StringUtil.isNotEmpty(orderNo)) {
            list = deliveryOrderGoodsDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
            page.setTotalCount(list.size());
        }
        return toPaginationLayUIData(page, list);

    }

}
