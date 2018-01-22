package com.nilo.dms.service.order.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import com.nilo.dms.service.order.model.TaskMessage;
import com.nilo.dms.service.system.model.SocketMessage;
import com.nilo.dms.service.system.model.SocketMessageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "send_message",group = "ws_message_group",filterExpression = "task")
public class WSMessageConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(WSMessageConsumer.class);


    @Override
    public void handleMessage(MessageExt messageExt,Object obj) throws Exception {

        TaskMessage message = (TaskMessage) obj;
        logger.info("Message:{}", message);
        //发送消息
        String receiverId = message.getReceiverId();
        String senderId = message.getSenderId();
        String content = message.getContent();

        SocketMessage sm = new SocketMessage();
        sm.setSenderId(senderId);
        sm.setReceiverId(receiverId);
        sm.setMessageId("" + IdWorker.getInstance().nextId());
        sm.setContent(content);
        sm.setMessageType(SocketMessageTypeEnum.MESSAGE.getCode());

        //boolean sendSuccess = websocket.sendMessageToUser(sm);

    }
}
