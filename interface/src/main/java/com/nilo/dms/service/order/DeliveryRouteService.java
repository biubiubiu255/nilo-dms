package com.nilo.dms.service.order;

import com.nilo.dms.service.order.model.DeliveryOrderRoute;

import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
public interface DeliveryRouteService {

    List<DeliveryOrderRoute> queryRoute(String merchantId, String orderNo);
}
