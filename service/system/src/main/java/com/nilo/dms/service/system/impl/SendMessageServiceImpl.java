package com.nilo.dms.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.dao.SMSConfigDao;
import com.nilo.dms.dao.dataobject.SMSConfigDO;
import com.nilo.dms.dto.order.PhoneMessage;
import com.nilo.dms.dto.system.SMSConfig;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SendMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/1.
 */
@Service
public class SendMessageServiceImpl implements SendMessageService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SMSConfigDao smsConfigDao;
    @Autowired
    @Qualifier("phoneSMSProducer")
    private AbstractMQProducer phoneSMSProducer;

    @Override
    public SMSConfig getConfigByType(String merchantId, String smsType) {
        SMSConfig config = JSON.parseObject(RedisUtil.hget(Constant.SMS_CONF + merchantId, smsType), SMSConfig.class);
        if (config == null) {
            SMSConfigDO configDO = smsConfigDao.getBy(merchantId, smsType);
            if (configDO == null) return null;
            config = convert(configDO);
        }
        return config;
    }

    @Override
    public void updateConfig(SMSConfig smsConfig) {
        SMSConfigDO configDO = convert(smsConfig);
        smsConfigDao.update(configDO);
        //更新缓存信息
        SMSConfigDO d = smsConfigDao.getBy(smsConfig.getMerchantId(), smsConfig.getSmsType());
        RedisUtil.hset(Constant.SMS_CONF + smsConfig.getMerchantId(), smsConfig.getSmsType(), JSON.toJSONString(convert(d)));

    }

    @Override
    public List<SMSConfig> listConfigBy(String merchantId) {
        List<SMSConfigDO> configDOList = smsConfigDao.listBy(merchantId);

        List<SMSConfig> configList = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return configList;

        for (SMSConfigDO configDO : configDOList) {
            configList.add(convert(configDO));
        }
        return configList;
    }

    @Override
    public void sendMessage(String waybillNum, String smsType, String phone, String... args) {
        SMSConfig config = JSON.parseObject(RedisUtil.hget(Constant.SMS_CONF + 1, smsType), SMSConfig.class);
        if (config == null) {
            log.error("send message failed. sms config is null");
        }
        if (config.getStatus() == DISABLED) {
            return;
        }

        String content = MessageFormat.format(config.getContent(), args);
        PhoneMessage m = new PhoneMessage();
        m.setPhoneNum(phone);
        m.setWaybill(waybillNum);
        m.setContent(content);
        m.setMsgType(smsType);
        try {
            phoneSMSProducer.sendMessage(m);
        } catch (Exception e) {
            log.error("Send Message Failed.{}", m, e);
        }

    }

    private SMSConfig convert(SMSConfigDO configDO) {

        SMSConfig config = new SMSConfig();
        config.setMerchantId("" + configDO.getMerchantId());
        config.setContent(configDO.getContent());
        config.setSmsType(configDO.getSmsType());
        config.setStatus(configDO.getStatus());
        config.setParam(configDO.getParam());
        config.setName(configDO.getName());
        config.setCreatedBy(configDO.getCreatedBy());
        config.setCreatedTime(configDO.getCreatedTime());
        config.setUpdatedBy(configDO.getUpdatedBy());
        config.setUpdatedTime(configDO.getUpdatedTime());

        return config;
    }

    private SMSConfigDO convert(SMSConfig config) {

        SMSConfigDO configDO = new SMSConfigDO();
        configDO.setMerchantId(config.getMerchantId());
        configDO.setSmsType(config.getSmsType());
        configDO.setContent(config.getContent());
        configDO.setParam(config.getParam());
        configDO.setName(config.getName());
        configDO.setStatus(config.getStatus());
        configDO.setUpdatedBy(config.getUpdatedBy());
        configDO.setCreatedBy(config.getCreatedBy());
        return configDO;

    }
}
