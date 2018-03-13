package com.nilo.dms.service.order.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.SMSSendStatusEnum;
import com.nilo.dms.dao.SMSLogDao;
import com.nilo.dms.dao.dataobject.SMSLogDO;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import com.nilo.dms.service.order.model.PhoneMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "sms", group = "phone_message_group", filterExpression = "phone")
public class PhoneMessageConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(PhoneMessageConsumer.class);

    @Autowired
    private SMSLogDao smsLogDao;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        PhoneMessage phoneMessage = null;
        try {
            phoneMessage = (PhoneMessage) obj;
            SMSLogDO logDO = new SMSLogDO();
            logDO.setMerchantId(Long.parseLong(phoneMessage.getMerchantId()));
            logDO.setMsgType(phoneMessage.getMsgType());
            logDO.setContent(phoneMessage.getContent());
            logDO.setReceiver(phoneMessage.getPhoneNum());
            logDO.setStatus(SMSSendStatusEnum.SUCCESS.getCode());
            smsLogDao.insert(logDO);
        } catch (Exception e) {

            logger.error("PhoneMessageConsumer Failed. Message:{} ", phoneMessage, e);
            if (messageExt.getReconsumeTimes() == 4) {
                return;
            }
            throw e;
        }
    }
}
