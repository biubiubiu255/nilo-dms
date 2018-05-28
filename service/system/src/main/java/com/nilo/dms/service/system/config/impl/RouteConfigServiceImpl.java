package com.nilo.dms.service.system.config.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.RouteConfigStatusEnum;
import com.nilo.dms.dao.RouteConfigDao;
import com.nilo.dms.dao.dataobject.RouteConfigDO;
import com.nilo.dms.dto.system.RouteConfig;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.config.RouteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
@Service
public class RouteConfigServiceImpl implements RouteConfigService {

    @Autowired
    private RouteConfigDao routeConfigDao;

    @Override
    public List<RouteConfig> queryAll() {

        List<RouteConfigDO> configDOList = routeConfigDao.findAll();

        List<RouteConfig> list = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return list;

        for (RouteConfigDO configDO : configDOList) {
            list.add(convert(configDO));
        }
        return list;

    }

    @Override
    public List<RouteConfig> queryAllBy(String merchantId) {
        List<RouteConfigDO> configDOList = routeConfigDao.findAllBy(merchantId);

        List<RouteConfig> list = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return list;

        for (RouteConfigDO configDO : configDOList) {
            list.add(convert(configDO));
        }
        return list;
    }

    @Override
    public void addConfig(RouteConfig config) {
        config.setStatus(RouteConfigStatusEnum.NORMAL);
        RouteConfigDO configDO = convert(config);
        routeConfigDao.insert(configDO);

        refreshCache(config);
    }

    @Override
    public void updateRouteConfig(RouteConfig config) {
        RouteConfigDO configDO = convert(config);
        routeConfigDao.update(configDO);

        refreshCache(config);
    }

    @Override
    public RouteConfig queryBy(String merchantId, String optType) {

        RouteConfigDO configDO = routeConfigDao.findByOptType(merchantId, optType);
        if (configDO == null) return null;

        return convert(configDO);
    }

    private RouteConfig convert(RouteConfigDO routeConfigDO) {

        RouteConfig config = new RouteConfig();
        config.setRemark(routeConfigDO.getRemark());
        config.setMerchantId("" + routeConfigDO.getMerchantId());
        config.setOptType(routeConfigDO.getOptType());
        config.setRouteDescC(routeConfigDO.getRouteDescC());
        config.setRouteDescE(routeConfigDO.getRouteDescE());
        config.setStatus(RouteConfigStatusEnum.getEnum(routeConfigDO.getStatus()));

        return config;
    }

    private RouteConfigDO convert(RouteConfig config) {

        RouteConfigDO routeConfigDO = new RouteConfigDO();
        if (config.getStatus() != null) {
            routeConfigDO.setStatus(config.getStatus().getCode());
        }

        routeConfigDO.setRouteDescC(config.getRouteDescC());
        routeConfigDO.setOptType(config.getOptType());
        routeConfigDO.setRouteDescE(config.getRouteDescE());
        routeConfigDO.setRemark(config.getRemark());
        routeConfigDO.setMerchantId(Long.parseLong(config.getMerchantId()));

        return routeConfigDO;
    }

    private void refreshCache(RouteConfig config) {
        RedisUtil.hset(Constant.ROUTE_CONF + config.getMerchantId(), config.getOptType(), JSON.toJSONString(config));
    }

}
