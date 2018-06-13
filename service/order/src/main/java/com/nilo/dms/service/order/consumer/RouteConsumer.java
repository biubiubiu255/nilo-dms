package com.nilo.dms.service.order.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.HandleThirdDao;
import com.nilo.dms.dao.UserInfoDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dao.dataobject.WaybillRouteDO;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.DeliveryRouteMessage;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import com.nilo.dms.service.order.WaybillOptService;
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

    @Autowired
    private HandleThirdDao handleThirdDao;

    @Autowired
    WaybillDao waybillDao;


    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {


        DeliveryRouteMessage message = null;
        try {
            message = (DeliveryRouteMessage) obj;

            OptTypeEnum optTypeEnum = OptTypeEnum.getEnum(message.getOptType());

            for (String orderNo : message.getOrderNo()) {
                //DeliveryOrderRouteDO orderRouteDO = new DeliveryOrderRouteDO();
                WaybillRouteDO routeInfo = new WaybillRouteDO();
                routeInfo.setMerchantId(Integer.parseInt(message.getMerchantId()));
                routeInfo.setOpt(message.getOptType());
                routeInfo.setOptByName(message.getOptBy());
                routeInfo.setOrderNo(orderNo);
                routeInfo.setOptNetwork(message.getNetworkId());

                switch (optTypeEnum) {
                    case ARRIVE_SCAN: {
                        break;
                    }
                    case DELIVERY: {
                        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(message.getMerchantId()), Long.parseLong(message.getRider()));
                        routeInfo.setRider(userInfoDO.getName());
                        routeInfo.setPhone(userInfoDO.getPhone());
                        break;
                    }
                    case SEND: {
                        SendThirdHead bigDO = handleThirdDao.queryHandleBySmallNo(Long.parseLong(message.getMerchantId()), routeInfo.getOrderNo());
                        //handleThirdDao.
                        routeInfo.setNextStation(bigDO.getNextStation());
                        routeInfo.setDriver(bigDO.getDriver());
                        routeInfo.setExpressName(bigDO.getThirdExpressCode());
                        routeInfo.setOptByName(bigDO.getHandleName());

                        List<WaybillDO> smallBags = waybillDao.queryByPackageNo(routeInfo.getMerchantId().longValue(), routeInfo.getOrderNo());
                        //routeInfo.set
                        break;
                    }
                    case SIGN: {
                        break;
                    }
                    default:
                        break;
                }
                deliveryOrderRouteDao.insert(routeInfo);
            }
        } catch (Exception e) {
            //不进行重复消费
            logger.error("RouteConsume Failed. RouteMessage:{}", message, e);
        }
    }

}
