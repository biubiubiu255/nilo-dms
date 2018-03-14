package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.MethodEnum;
import com.nilo.dms.common.enums.NotifyStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.common.utils.IdWorker;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "data_bus_notify", group = "notify_group", filterExpression = "notify")
public class NotifyDataBusConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(NotifyDataBusConsumer.class);

    @Autowired
    private NotifyDao notifyDao;

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        String msgId = messageExt.getKeys();
        NotifyRequest request = null;
        String response = "";
        boolean success = false;
        try {

            request = (NotifyRequest) obj;

            Map<String, String> params = new HashMap<>();
            params.put("method", request.getMethod());
            params.put("sign", request.getSign());
            params.put("data", request.getData());
            params.put("request_id", "" + IdWorker.getInstance().nextId());
            params.put("app_key", "dms");

            response = HttpUtil.post(request.getUrl(), params);
            NotifyResponse notifyResponse = JSON.parseObject(response, NotifyResponse.class);
            if (notifyResponse != null && notifyResponse.isSuccess()) {
                success = true;
                saveNotify(request, msgId, response, success);
            } else {
                saveNotify(request, msgId, response, success);
                logger.error("Notify Failed. NotifyID:{}, Response:{} , NotifyRequest:{}", msgId, response, request);
            }
        } catch (Exception e) {
            saveNotify(request, msgId, response, false);
            logger.error("Notify Failed. NotifyID:{}, Response:{} , NotifyRequest:{}", msgId, response, request);
            if (messageExt.getReconsumeTimes() == 4) {
                return;
            }
            throw e;
        }
    }

    private void saveNotify(NotifyRequest request, String notifyId, String response, boolean success) {

        NotifyDO query = notifyDao.findByNotifyId(Long.parseLong(request.getMerchantId()), notifyId);
        if (query == null) {
            NotifyDO notifyDO = new NotifyDO();
            notifyDO.setUrl(request.getUrl());
            notifyDO.setOrderNo(request.getOrderNo());
            notifyDO.setReferenceNo(request.getReferenceNo());
            notifyDO.setStatus(success ? NotifyStatusEnum.SUCCESS.getCode() : NotifyStatusEnum.Failed.getCode());
            notifyDO.setBizType(request.getBizType());
            notifyDO.setMethod(request.getMethod());
            notifyDO.setMerchantId(Long.parseLong(request.getMerchantId()));
            notifyDO.setNum(1);
            notifyDO.setNotifyId(notifyId);
            notifyDO.setParam(request.getData());
            notifyDO.setSign(request.getSign());
            notifyDO.setResult(response);
            notifyDao.insert(notifyDO);
        } else {
            NotifyDO update = new NotifyDO();
            update.setNotifyId(notifyId);
            update.setStatus(success ? NotifyStatusEnum.SUCCESS.getCode() : NotifyStatusEnum.Failed.getCode());
            update.setNum(query.getNum() + 1);
            update.setResult(response);
            notifyDao.update(update);
        }

    }

}
