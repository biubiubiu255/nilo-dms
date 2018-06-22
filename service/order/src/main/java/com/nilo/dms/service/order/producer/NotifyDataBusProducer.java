package com.nilo.dms.service.order.producer;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.nilo.dms.service.mq.model.ProducerDesc;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;

/**
 * Created by admin on 2017/11/18.
 */
@ProducerDesc(topic = "ug_data_bus_notify", group = "ug_notify_group", tags = "notify")
public class NotifyDataBusProducer extends AbstractMQProducer {

    @Override
    protected void beforeSend(Object obj) {
    }
    @Override
    protected void afterSend(Object obj, SendResult result) {
    }
}
