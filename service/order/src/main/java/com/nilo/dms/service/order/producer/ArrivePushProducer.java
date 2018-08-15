package com.nilo.dms.service.order.producer;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.nilo.dms.service.mq.model.ProducerDesc;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;

/**
 * Created by admin on 2017/11/18.
 */
@ProducerDesc(topic = "arrive_weight", group = "arrive_weight_group", tags = "arrive_weight")
public class ArrivePushProducer extends AbstractMQProducer {

    @Override
    protected void beforeSend(Object obj) {
    }
    @Override
    protected void afterSend(Object obj, SendResult result) {
    }
}
