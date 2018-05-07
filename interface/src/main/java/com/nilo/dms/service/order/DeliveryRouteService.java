package com.nilo.dms.service.order;


import com.nilo.dms.dto.order.DeliveryRoute;
import com.nilo.dms.dto.order.OrderOptRequest;

import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
public interface DeliveryRouteService {

    List<DeliveryRoute> queryRoute(String merchantId, String orderNo);

    void addRoute(OrderOptRequest request);

    void addKiliRoute(List<String> orderNos,String statusId);

}
