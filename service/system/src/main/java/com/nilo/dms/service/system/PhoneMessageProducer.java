package com.nilo.dms.service.system;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.nilo.dms.service.mq.model.ProducerDesc;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;

/**
 * Created by admin on 2017/11/18.
 */
@ProducerDesc(group = "ug_phone_message_group",topic = "ug_sms",tags = "phone")
public class PhoneMessageProducer extends AbstractMQProducer {

    @Override
    protected void beforeSend(Object obj) {

    }
    @Override
    protected void afterSend(Object obj, SendResult result) {

    }
}
