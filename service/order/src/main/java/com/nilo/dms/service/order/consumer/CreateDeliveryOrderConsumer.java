package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.CreateDeliveryRequestStatusEnum;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
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
    @Autowired
    @Qualifier("notifyMerchantProducer")
    private AbstractMQProducer notifyMerchantProducer;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        CreateDeliverOrderMessage message = (CreateDeliverOrderMessage) obj;
        try {

            DeliveryOrderRequestDO request = deliveryOrderRequestDao.queryById(message.getRequestId());
            if (request == null) {
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
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                String orderNo = deliveryOrderRequestDO.getOrderNo();
                DeliveryOrder data = JSON.parseObject(deliveryOrderRequestDO.getData(), DeliveryOrder.class);
                try {

                    Long merchantId = Long.parseLong(data.getMerchantId());
                    //1、保存订单信息
                    DeliveryOrderDO orderHeader = new DeliveryOrderDO();
                    orderHeader.setOrderNo(orderNo);
                    orderHeader.setCountry(data.getCountry());
                    orderHeader.setMerchantId(merchantId);
                    orderHeader.setOrderPlatform(data.getOrderPlatform());
                    orderHeader.setOrderTime(data.getOrderTime());
                    orderHeader.setOrderType(data.getOrderType());
                    orderHeader.setReferenceNo(data.getReferenceNo());
                    orderHeader.setStatus(DeliveryOrderStatusEnum.CREATE.getCode());
                    orderHeader.setTotalPrice(data.getTotalPrice());
                    orderHeader.setWeight(data.getWeight());

                    orderHeader.setServiceType(data.getServiceType().getCode());
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
                    orderHeader.setNotes(data.getNotes());
                    orderHeader.setRemark(data.getRemark());

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
                    deliveryOrderSenderDao.insert(s);

                    //添加成功，通知商户
                    notifyMerchantCreateResult("" + data.getMerchantId(), orderNo, data.getReferenceNo(), true, "Success");

                    //更新状态
                    DeliveryOrderRequestDO update = new DeliveryOrderRequestDO();
                    update.setId(deliveryOrderRequestDO.getId());
                    update.setStatus(CreateDeliveryRequestStatusEnum.SUCCESS.getCode());
                    update.setMsg("SUCCESS");
                    deliveryOrderRequestDao.update(update);
                } catch (Exception e) {
                    notifyMerchantCreateResult("" + data.getMerchantId(), orderNo, data.getReferenceNo(), false, e.getMessage());
                    //更新状态
                    DeliveryOrderRequestDO update = new DeliveryOrderRequestDO();
                    update.setId(deliveryOrderRequestDO.getId());
                    update.setStatus(CreateDeliveryRequestStatusEnum.FAILED.getCode());
                    update.setMsg(e.getMessage());
                    deliveryOrderRequestDao.update(update);

                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });

    }

    private void notifyMerchantCreateResult(String merchantId, String orderNo, String referenceNo, boolean result, String msg) {


        NotifyRequest notify = new NotifyRequest();
        try {
            MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + merchantId), MerchantConfig.class);
            InterfaceConfig interfaceConfig = JSON.parseObject(RedisUtil.hget(Constant.INTERFACE_CONF + merchantId, "create_order_notify"), InterfaceConfig.class);
            if (interfaceConfig == null) {
                return;
            }
            notify.setReferenceNo(referenceNo);
            notify.setOrderNo(orderNo);
            notify.setMerchantId(merchantId);
            notify.setOp(interfaceConfig.getOp());
            notify.setUrl(interfaceConfig.getUrl());
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("orderNo", orderNo);
            dataMap.put("referenceNo", referenceNo);
            dataMap.put("msg", msg);
            Map<Object, Object> map = new HashMap<>();
            map.put("result", result);
            map.put("data", dataMap);
            String data = JSON.toJSONString(map);
            notify.setData(data);
            notify.setSign(createSign(merchantConfig.getKey(), data));
            notifyMerchantProducer.sendMessage(notify);
        } catch (Exception e) {

        }
    }

    private String createSign(String key, String data) {
        return new String(DigestUtils.md5Hex("key=" + key + "&data=" + data));
    }
}
