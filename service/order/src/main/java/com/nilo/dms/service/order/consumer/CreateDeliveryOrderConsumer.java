package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.CreateDeliveryRequestStatusEnum;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.*;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.system.model.InterfaceConfig;
import com.nilo.dms.service.system.model.MerchantConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "create_order", group = "create_order_group", filterExpression = "create")
public class CreateDeliveryOrderConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(CreateDeliveryOrderConsumer.class);
    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;

    @Autowired
    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;

    @Autowired
    private DeliveryOrderSenderDao deliveryOrderSenderDao;
    @Autowired
    private DeliveryOrderRequestDao deliveryOrderRequestDao;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        CreateDeliverOrderMessage message = (CreateDeliverOrderMessage) obj;
        try {

            DeliveryOrderRequestDO request = deliveryOrderRequestDao.queryById(message.getRequestId());
            if (request == null) {
                logger.error("CreateDeliveryOrderConsumer failed.request is empty");
                return;
            }
            createDeliveryOrder(request, deliveryOrderRequestDao);

        } catch (Exception e) {
            logger.error("CreateDeliveryOrderConsumer failed.CreateDeliverOrderMessage:{} ", message, e);
        }
        return;
    }

    /**
     * 新增发运订单
     *
     * @param deliveryOrderRequestDO
     * @return
     */
    public void createDeliveryOrder(DeliveryOrderRequestDO deliveryOrderRequestDO, DeliveryOrderRequestDao deliveryOrderRequestDao) {


        //判断是否已存在
        DeliveryOrderDO orderDO = deliveryOrderDao.queryByOrderNo(deliveryOrderRequestDO.getMerchantId(), deliveryOrderRequestDO.getOrderNo());
        if (orderDO != null) {
            return;
        }
        Long merchantId = deliveryOrderRequestDO.getMerchantId();
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                String orderNo = deliveryOrderRequestDO.getOrderNo();
                DeliveryOrder data = JSON.parseObject(deliveryOrderRequestDO.getData(), DeliveryOrder.class);
                try {

                    //1、保存订单信息
                    DeliveryOrderDO orderHeader = new DeliveryOrderDO();
                    orderHeader.setOrderNo(orderNo);
                    orderHeader.setCountry(data.getReceiverInfo().getReceiverCountry());
                    orderHeader.setMerchantId(merchantId);
                    orderHeader.setOrderPlatform(data.getOrderPlatform());
                    if (data.getOrderTime() != null) {
                        orderHeader.setOrderTime(data.getOrderTime());
                    } else {
                        orderHeader.setOrderTime(DateUtil.getSysTimeStamp());
                    }

                    if (StringUtil.equals(data.getOrderType(), "1")) {
                        orderHeader.setOrderType("FBK");
                    }
                    if (StringUtil.equals(data.getOrderType(), "2")) {
                        orderHeader.setOrderType("GS");
                    }
                    if (StringUtil.equals(data.getOrderType(), "0")) {
                        orderHeader.setOrderType("DS");
                    }

                    orderHeader.setReferenceNo(data.getReferenceNo());
                    orderHeader.setStatus(DeliveryOrderStatusEnum.CREATE.getCode());
                    orderHeader.setTotalPrice(data.getTotalPrice());
                    orderHeader.setWeight(data.getWeight());
                    orderHeader.setGoodsType(data.getGoodsType());


                    orderHeader.setWarehouseId(data.getWarehouseId());
                    orderHeader.setStop(data.getStop());
                    orderHeader.setStopId(data.getStopId());
                    orderHeader.setChannel(data.getChannel());
                    orderHeader.setChannelStation(data.getChannelStation());
                    orderHeader.setOrderCategory(data.getOrderCategory());
                    orderHeader.setCarrierId(data.getCarrierId());
                    orderHeader.setCarrierName(data.getCarrierName());
                    orderHeader.setRelationOrderNo(data.getRelationOrderNo());
                    orderHeader.setDeliveryFee(data.getDeliveryFee());
                    orderHeader.setIsCod(data.getIsCod());
                    if (data.getNeedPayAmount() != null) {
                        data.setIsCod("1");
                    }
                    orderHeader.setNotes(data.getNotes());
                    orderHeader.setRemark(data.getRemark());

                    orderHeader.setNeedPayAmount(data.getNeedPayAmount());
                    orderHeader.setAlreadyPaid(data.getAlreadyPaid());

                    deliveryOrderDao.insert(orderHeader);

                    //2、保存订单商品明细信息
                    if (data.getGoodsInfoList() != null) {
                        for (GoodsInfo g : data.getGoodsInfoList()) {
                            DeliveryOrderGoodsDO goods = new DeliveryOrderGoodsDO();
                            goods.setMerchantId(merchantId);
                            goods.setOrderNo(orderNo);
                            goods.setGoodsDesc(g.getGoodsDesc());
                            goods.setQty(g.getQty());
                            goods.setUnitPrice(g.getUnitPrice());
                            goods.setGoodsId(g.getGoodsId());
                            goods.setQuality(g.getQuality());
                            deliveryOrderGoodsDao.insert(goods);
                        }
                    }

                    //3、保存收货人信息
                    ReceiverInfo receiverInfo = data.getReceiverInfo();
                    DeliveryOrderReceiverDO r = new DeliveryOrderReceiverDO();
                    r.setMerchantId(merchantId);
                    r.setOrderNo(orderNo);
                    r.setAddress(receiverInfo.getReceiverAddress());
                    r.setArea(receiverInfo.getReceiverArea());
                    r.setCity(receiverInfo.getReceiverCity());
                    r.setContactNumber(receiverInfo.getReceiverPhone());
                    r.setCountry(receiverInfo.getReceiverCountry());
                    r.setName(receiverInfo.getReceiverName());
                    r.setProvince(receiverInfo.getReceiverProvince());
                    r.setCountryId(receiverInfo.getCountryId());
                    r.setProvinceId(receiverInfo.getProvinceId());
                    r.setCityId(receiverInfo.getCityId());
                    r.setAreaId(receiverInfo.getAreaId());
                    deliveryOrderReceiverDao.insert(r);

                    //3、保存寄件人信息
                    SenderInfo senderInfo = data.getSenderInfo();
                    DeliveryOrderSenderDO s = new DeliveryOrderSenderDO();
                    s.setMerchantId(merchantId);
                    s.setOrderNo(orderNo);
                    s.setAddress(senderInfo.getSenderAddress());
                    s.setArea(senderInfo.getSenderArea());
                    s.setCity(senderInfo.getSenderCity());
                    s.setContactNumber(senderInfo.getSenderPhone());
                    s.setCountry(senderInfo.getSenderCountry());
                    s.setName(senderInfo.getSenderName());
                    s.setProvince(senderInfo.getSenderProvince());
                    s.setCountryId(senderInfo.getCountryId());
                    s.setProvinceId(senderInfo.getProvinceId());
                    s.setCityId(senderInfo.getCityId());
                    s.setAreaId(senderInfo.getAreaId());
                    deliveryOrderSenderDao.insert(s);

                    //更新状态
                    DeliveryOrderRequestDO update = new DeliveryOrderRequestDO();
                    update.setId(deliveryOrderRequestDO.getId());
                    update.setStatus(CreateDeliveryRequestStatusEnum.SUCCESS.getCode());
                    update.setMsg("SUCCESS");
                    deliveryOrderRequestDao.update(update);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    //更新状态
                    DeliveryOrderRequestDO update = new DeliveryOrderRequestDO();
                    update.setId(deliveryOrderRequestDO.getId());
                    update.setStatus(CreateDeliveryRequestStatusEnum.FAILED.getCode());
                    update.setMsg(e.getMessage());
                    deliveryOrderRequestDao.update(update);
                    throw e;
                }
                return null;
            }
        });

    }

    private String createSign(String key, String data) {
        return new String(DigestUtils.md5Hex("key=" + key + "&data=" + data));
    }
}
