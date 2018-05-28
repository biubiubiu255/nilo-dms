package com.nilo.dms.service.system.config;


import com.nilo.dms.dto.system.RouteConfig;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
public interface RouteConfigService {

    List<RouteConfig> queryAll();

    List<RouteConfig> queryAllBy(String merchantId);

    void addConfig(RouteConfig config);

    void updateRouteConfig(RouteConfig config);

    RouteConfig queryBy(String merchantId, String optType);

}
