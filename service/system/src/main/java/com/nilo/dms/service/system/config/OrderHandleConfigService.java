package com.nilo.dms.service.system.config;

import com.nilo.dms.service.system.model.OrderHandleConfig;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
public interface OrderHandleConfigService {

    List<OrderHandleConfig> queryAll();

    List<OrderHandleConfig> queryAllBy(String merchantId);

    void addConfig(OrderHandleConfig config);

    void updateHandleConfig(OrderHandleConfig config);

    OrderHandleConfig queryBy(String merchantId, String optType);

}
