package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.DeliveryOrderOpt;
import com.nilo.dms.service.order.model.OrderOptRequest;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
public interface OrderOptLogService {

    List<DeliveryOrderOpt> queryBy(String merchantId, String optType, String orderNo, String optBy, Long fromtTime, Long toTime, Pagination pagination);

    List<DeliveryOrderOpt> queryByOrderNos(String merchantId, List<String> orderNo);

    void addOptLog(OrderOptRequest request);
}
