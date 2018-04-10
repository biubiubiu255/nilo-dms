package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.RiderDeliveryDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.WaybillDO;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public interface RiderDeliveryService {

    void addRiderPackDetail(Long merchantId, String handleNo, String[] smallOrders);

    void addRiderPack(RiderDeliveryDO riderDeliveryDO);

    void addRiderPackAndDetail(Long merchantId, RiderDeliveryDO riderDeliveryDO, String[] smallOrders);

    List<RiderDeliveryDO> queryRiderDelivery(String merchantId, RiderDeliveryDO riderDeliveryDO, Pagination page);

    List<RiderDeliverySmallDO> queryRiderDeliveryDetail(String merchantId, RiderDeliverySmallDO riderDeliverySmallDO, Pagination page);

    List<WaybillDO> queryRiderDeliveryDetailPlus(String merchantId, RiderDeliveryDO riderDeliveryDO, Pagination page);

    void editSmall(RiderDeliveryDO riderDeliveryDO, String[] smallOrders);

    void editBig(RiderDeliveryDO riderDeliveryDO);

    void insertSmalls(Long merchantId, String handleNo, String[] smallOrders);

}
