package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.RiderDeliveryDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.service.order.model.ShipParameter;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public interface RiderDeliveryService {

    void addRiderPackDetail(Long merchantId , RiderDeliveryDO riderDeliveryDO, String[] smallOrders);

    void addRiderPack(RiderDeliveryDO riderDeliveryDO);

    void addRiderPackAndDetail(Long merchantId , RiderDeliveryDO riderDeliveryDO, String[] smallOrders);

    List<RiderDeliveryDO> queryRiderDelivery(String merchantId , RiderDeliveryDO riderDeliveryDO, Pagination page);

    List<RiderDeliverySmallDO> queryRiderDeliveryDetail(String merchantId , RiderDeliverySmallDO riderDeliverySmallDO, Pagination page);
}
