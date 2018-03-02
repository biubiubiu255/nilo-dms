package com.nilo.dms.service.order;


import com.nilo.dms.service.order.model.DeliveryRoute;
import com.nilo.dms.service.order.model.OrderOptRequest;

import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
public interface NotifyMerchantService {

    void updateStatus(OrderOptRequest request);

}
