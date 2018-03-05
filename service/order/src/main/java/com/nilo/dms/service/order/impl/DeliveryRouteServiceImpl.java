package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderSenderDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.model.DeliveryRoute;
import com.nilo.dms.service.order.model.DeliveryRouteMessage;
import com.nilo.dms.service.order.model.OrderOptRequest;
import com.nilo.dms.service.system.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
@Service
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryOrderRouteDao deliveryOrderRouteDao;
    @Autowired
    @Qualifier("routeProducer")
    private AbstractMQProducer routeProducer;

    @Override
    public List<DeliveryRoute> queryRoute(String merchantId, String orderNo) {

        List<DeliveryOrderRouteDO> routeDOs = deliveryOrderRouteDao.findBy(Long.parseLong(merchantId), orderNo);
        if (routeDOs == null) return null;

        List<DeliveryRoute> routeList = new ArrayList<>();
        for (DeliveryOrderRouteDO routeDO : routeDOs) {
            routeList.add(convert(routeDO));
        }

        return routeList;
    }

    @Override
    public void addRoute(OrderOptRequest request) {

        DeliveryRouteMessage message = new DeliveryRouteMessage();
        message.setMerchantId(request.getMerchantId());
        message.setOrderNo(request.getOrderNo().toArray(new String[request.getOrderNo().size()]));
        message.setOptType(request.getOptType().getCode());
        message.setOptBy(request.getOptBy());
        message.setRider(request.getRider());
        message.setNetworkId(request.getNetworkId());
        try {
            routeProducer.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private DeliveryRoute convert(DeliveryOrderRouteDO routeDO) {
        DeliveryRoute route = new DeliveryRoute();
        route.setMerchantId("" + routeDO.getMerchantId());
        route.setOrderNo(routeDO.getOrderNo());
        route.setOpt(routeDO.getOpt());
        route.setOptNetwork(routeDO.getOptNetwork());
        route.setPhone(routeDO.getPhone());
        route.setOptBy(routeDO.getOptBy());
        route.setOptTime(routeDO.getCreatedTime());
        if (StringUtil.isNotEmpty(routeDO.getOptNetwork())) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + routeDO.getMerchantId(), "" + routeDO.getOptNetwork()), DistributionNetworkDO.class);
            route.setNetworkDesc(networkDO.getName());
        }
        if (StringUtil.isNotEmpty(routeDO.getOptBy())) {
            UserInfo userInfo = userService.findUserInfoByUserId("" + routeDO.getMerchantId(), routeDO.getOptBy());
            if (userInfo != null) route.setOptByName(userInfo.getName());
        }
        route.setCreatedTime(routeDO.getCreatedTime());
        return route;
    }
}
