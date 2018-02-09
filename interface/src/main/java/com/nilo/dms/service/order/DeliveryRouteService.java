package com.nilo.dms.service.order;


import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.service.order.model.DeliveryRoute;
import com.nilo.dms.service.order.model.OrderOptRequest;

import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
public interface DeliveryRouteService {

    List<DeliveryRoute> queryRoute(String merchantId, String orderNo);

    void addRoute(OrderOptRequest request);
}
