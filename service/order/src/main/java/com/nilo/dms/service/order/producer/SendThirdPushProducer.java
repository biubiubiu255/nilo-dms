package com.nilo.dms.service.order.producer;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.nilo.dms.service.mq.model.ProducerDesc;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;

/**
 * Created by admin on 2017/11/18.
 */
@ProducerDesc(topic = "send_third", group = "send_third_group", tags = "notify")
public class SendThirdPushProducer extends AbstractMQProducer {

    @Override
    protected void beforeSend(Object obj) {
    }
    @Override
    protected void afterSend(Object obj, SendResult result) {
    }
}
