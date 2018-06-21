package com.nilo.dms.service.order;


import com.nilo.dms.dao.dataobject.WaybillRouteDO;
import com.nilo.dms.dto.order.DeliveryRoute;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.Waybill;

import java.util.List;

/**
 * Created by admin on 2017/11/15.
 */
public interface DeliveryRouteService {

    List<WaybillRouteDO> queryRoute(String merchantId, String orderNo);

    void addRoute(OrderOptRequest request);

    void addKiliRoute(List<String> orderNos,String statusId,String remark);

}
