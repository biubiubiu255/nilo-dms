package com.nilo.dms.service.order.producer;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.nilo.dms.service.mq.model.ProducerDesc;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;

/**
 * Created by admin on 2017/11/18.
 */
@ProducerDesc(group = "create_order_group",topic = "create_order",tags = "create")
public class CreateDeliveryOrderProducer extends AbstractMQProducer {

    @Override
    protected void beforeSend(Object obj) {

    }
    @Override
    protected void afterSend(Object obj, SendResult result) {

    }
}
