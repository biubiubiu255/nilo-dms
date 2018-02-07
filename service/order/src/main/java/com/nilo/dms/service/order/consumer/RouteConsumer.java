package com.nilo.dms.service.order.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.service.order.model.DeliveryRoute;
import com.nilo.dms.service.system.SpringContext;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "delivery_order_route", group = "route_group", filterExpression = "route")
public class RouteConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(RouteConsumer.class);

    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {

        DeliveryOrderRouteDao deliveryOrderRouteDao = SpringContext.getBean("deliveryOrderRouteDao", DeliveryOrderRouteDao.class);
        try {
            DeliveryRoute rout = (DeliveryRoute) obj;
            logger.info("MessageExt:{},DeliveryRoute:{}", messageExt, rout);

            DeliveryOrderRouteDO orderRouteDO = new DeliveryOrderRouteDO();
            orderRouteDO.setMemoEn(rout.getTraceEN());
            orderRouteDO.setOrderNo(rout.getOrderNo());
            orderRouteDO.setRemark(rout.getRemark());
            orderRouteDO.setMemoCn(rout.getTraceCN());
            orderRouteDO.setOptBy(rout.getOptBy());
            orderRouteDO.setOptTime(rout.getOptTime());
            orderRouteDO.setOptNetwork(rout.getOptNetwork());
            orderRouteDO.setMerchantId(Long.parseLong(rout.getMerchantId()));
            orderRouteDO.setOpt(rout.getOpt());

            deliveryOrderRouteDao.insert(orderRouteDO);
        } catch (Exception e) {
            //不进行重复消费
            logger.error("RouteConsume Failed. ", e);
        }
    }

}
