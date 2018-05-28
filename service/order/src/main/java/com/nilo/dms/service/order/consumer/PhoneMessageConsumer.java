package com.nilo.dms.service.order.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.SMSSendStatusEnum;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.dao.SMSLogDao;
import com.nilo.dms.dao.dataobject.SMSLogDO;
import com.nilo.dms.dto.order.PhoneMessage;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "sms", group = "phone_message_group", filterExpression = "phone")
public class PhoneMessageConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(PhoneMessageConsumer.class);

    @Autowired
    private SMSLogDao smsLogDao;
    @Value("#{configProperties['sms_url']}")
    private String sms_url;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        PhoneMessage phoneMessage = null;
        try {
            phoneMessage = (PhoneMessage) obj;
            phoneMessage.setPhoneNum(phoneMessage.getPhoneNum().replace("+", ""));
            //发送短信
            Map<String, String> param = new HashMap<>();
            param.put("mobile", phoneMessage.getPhoneNum());
            param.put("msg", phoneMessage.getContent());
            param.put("sign", sign(phoneMessage.getContent(), phoneMessage.getPhoneNum()));
            logger.info("SMS URL:{},Param:{}", sms_url, param);
            String response = HttpUtil.post(sms_url, param);
            logger.info("SMS Response:{}", response);
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

    public static String sign(String msg, String mobile) {
        StringBuffer str = new StringBuffer();
        str = str.append("mobile=").append(mobile).append("&").append("msg=").append(msg).append("weghjzxfjcgjhdf");
        return DigestUtils.md5Hex(str.toString());
    }

    public static void main(String[] args) {

        String sms_url = "https://mobile.kilimall.co.ke/index.php?act=send_short";
        String phone = "254729038955";
        String msg = "Dear customer, your order 10000000572843 has been dispatched today. Your total order amount is Ksh.0 .Kindly call Joshua Matianyi Shatimba 254718138094 to notify you the time of delivery. Please keep your phone on. Thank you.";
        //发送短信
        Map<String, String> param = new HashMap<>();
        param.put("mobile", phone);
        param.put("msg", msg);
        param.put("sign", sign(msg, phone));
        logger.info("SMS URL:{},Param:{}", sms_url, param);
        String response = null;
        try {
            response = HttpUtil.post(sms_url, param);
            System.out.println(111 + response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
