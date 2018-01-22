package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.nilo.dms.dao.DeliveryOrderRouteDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderSenderDO;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.model.DeliveryOrderRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
@Service
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    @Autowired
    private DeliveryOrderRouteDao deliveryOrderRouteDao;

    @Override
    public List<DeliveryOrderRoute> queryRoute(String merchantId, String orderNo) {

        List<DeliveryOrderRouteDO> routeDOs = deliveryOrderRouteDao.findBy(Long.parseLong(merchantId), orderNo);

        if (routeDOs == null) return null;

        List<DeliveryOrderRoute> routeList = new ArrayList<>();
        for(DeliveryOrderRouteDO routeDO: routeDOs){
            routeList.add(convert(routeDO));
        }

        return routeList;
    }

    private DeliveryOrderRoute convert(DeliveryOrderRouteDO routeDO) {
        DeliveryOrderRoute route = new DeliveryOrderRoute();
        route.setMerchantId("" + routeDO.getMerchantId());
        route.setOrderNo(routeDO.getOrderNo());

        route.setOpt(routeDO.getOpt());
        route.setRemark(routeDO.getRemark());
        route.setMemoCN(routeDO.getMemoCn());
        route.setMemoEN(routeDO.getMemoEn());

        route.setCreatedTime(routeDO.getCreatedTime());
        return route;
    }
}
