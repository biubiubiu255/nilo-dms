package com.nilo.dms.service.mq.producer;


/**
 * Created by admin on 2017/10/19.
 */
public interface MQProducer<T> {
    void sendMessage(T var1) throws Exception;
}
