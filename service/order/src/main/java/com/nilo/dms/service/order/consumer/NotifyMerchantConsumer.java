package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.enums.NotifyStatusEnum;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.order.model.NotifyRequest;
import com.nilo.dms.service.order.model.NotifyResponse;
import com.nilo.dms.service.system.SpringContext;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "merchant_notify", group = "notify_group", filterExpression = "notify")
public class NotifyMerchantConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(NotifyMerchantConsumer.class);

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        String msgId = messageExt.getMsgId();

        NotifyDao notifyDao = SpringContext.getBean("notifyDao", NotifyDao.class);
        String response = "";
        try {

            NotifyRequest request = (NotifyRequest) obj;
            logger.info("MessageExt:{},Message:{}", messageExt, request);
            Map<String, String> params = new HashMap<>();
            params.put("method", request.getMethod());
            params.put("sign", request.getSign());
            params.put("data", request.getData());
            params.put("app_key", "dms");


            //判断是否是第一次通知
            if (messageExt.getReconsumeTimes() == 0) {
                NotifyDO notifyDO = new NotifyDO();
                notifyDO.setUrl(request.getUrl());
                notifyDO.setOrderNo(request.getOrderNo());
                notifyDO.setReferenceNo(request.getReferenceNo());
                notifyDO.setStatus(NotifyStatusEnum.CREATE.getCode());
                notifyDO.setBizType(request.getBizType());
                notifyDO.setMethod(request.getMethod());
                notifyDO.setMerchantId(Long.parseLong(request.getMerchantId()));
                notifyDO.setNum(1);
                notifyDO.setNotifyId(msgId);
                notifyDO.setParam(request.getData());
                notifyDO.setSign(request.getSign());
                notifyDao.insert(notifyDO);
            }
            response = HttpUtil.post(request.getUrl(), params);
            NotifyResponse notifyResponse = JSON.parseObject(response, NotifyResponse.class);
            if (notifyResponse == null || !notifyResponse.getResult()) {
                throw new IllegalStateException("NotifyMerchant failed." + notifyResponse);
            }

            NotifyDO update = new NotifyDO();
            update.setNotifyId(msgId);
            update.setStatus(NotifyStatusEnum.SUCCESS.getCode());
            update.setNum(messageExt.getReconsumeTimes() + 1);
            update.setResult(response);
            notifyDao.update(update);

        } catch (Exception e) {
            NotifyDO update = new NotifyDO();
            update.setNotifyId(msgId);
            update.setStatus(NotifyStatusEnum.Failed.getCode());
            update.setNum(messageExt.getReconsumeTimes() + 1);
            String htmlEncode = HtmlUtils.htmlEscape(response);
            update.setResult(StringUtil.isEmpty(response) ? e.getMessage() : htmlEncode);
            notifyDao.update(update);
            if (messageExt.getReconsumeTimes() == 3) {
                return;
            }
            throw e;
        }
    }
}
