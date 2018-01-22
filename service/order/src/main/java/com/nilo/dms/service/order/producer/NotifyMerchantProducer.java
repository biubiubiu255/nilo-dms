package com.nilo.dms.service.order.producer;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.nilo.dms.service.mq.model.ProducerDesc;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.consumer.NotifyMerchantConsumer;

/**
 * Created by admin on 2017/11/18.
 */
@ProducerDesc(group = "notify_group",topic = "merchant_notify",tags = "notify")
public class NotifyMerchantProducer extends AbstractMQProducer {

    @Override
    protected void beforeSend(Object obj) {

    }
    @Override
    protected void afterSend(Object obj, SendResult result) {

    }
}
