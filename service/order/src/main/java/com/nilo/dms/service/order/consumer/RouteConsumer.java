package com.nilo.dms.service.order.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.TaskDao;
import com.nilo.dms.dao.UserInfoDao;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.TaskDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import com.nilo.dms.service.order.model.DeliveryRouteMessage;
import com.nilo.dms.service.system.SpringContext;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "delivery_order_route", group = "route_group", filterExpression = "route")
public class RouteConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(RouteConsumer.class);

    @Autowired
    private DeliveryOrderRouteDao deliveryOrderRouteDao;
    @Autowired
    private UserInfoDao userInfoDao;


    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        DeliveryRouteMessage message = null;
        try {
            message = (DeliveryRouteMessage) obj;

            OptTypeEnum optTypeEnum = OptTypeEnum.getEnum(message.getOptType());

            for (String orderNo : message.getOrderNo()) {
                DeliveryOrderRouteDO orderRouteDO = new DeliveryOrderRouteDO();
                orderRouteDO.setMerchantId(Long.parseLong(message.getMerchantId()));
                orderRouteDO.setOpt(message.getOptType());
                orderRouteDO.setOptBy(message.getOptBy());
                orderRouteDO.setOrderNo(orderNo);

                switch (optTypeEnum) {
                    case ARRIVE_SCAN: {
                        orderRouteDO.setOptNetwork(message.getNetworkId());
                        break;
                    }
                    case DELIVERY: {
                        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(message.getMerchantId()), Long.parseLong(message.getRider()));
                        orderRouteDO.setOptBy(message.getRider());
                        orderRouteDO.setPhone(userInfoDO.getPhone());
                        break;
                    }
                    case SEND: {
                        orderRouteDO.setOptBy(message.getNetworkId());
                        break;
                    }
                    case RECEIVE: {
                        break;
                    }
                    default:
                        break;
                }
                deliveryOrderRouteDao.insert(orderRouteDO);
            }
        } catch (Exception e) {
            //不进行重复消费
            logger.error("RouteConsume Failed. RouteMessage:{}", message, e);
        }
    }

}
