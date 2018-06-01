package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.RiderDelivery;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.WaybillDO;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public interface RiderDeliveryService {

    void addRiderPackDetail(Long merchantId, String handleNo, String[] smallOrders);

    void addRiderPack(RiderDelivery riderDelivery);

    void addRiderPackAndDetail(Long merchantId, RiderDelivery riderDelivery, String[] smallOrders);

    List<RiderDelivery> queryRiderDelivery(String merchantId, RiderDelivery riderDelivery, Pagination page);

    List<RiderDeliverySmallDO> queryRiderDeliveryDetail(String merchantId, RiderDeliverySmallDO riderDeliverySmallDO, Pagination page);

    List<WaybillDO> queryRiderDeliveryDetailPlus(String merchantId, RiderDelivery riderDelivery, Pagination page);

    void editRiderPackAndDetail(RiderDelivery riderDelivery, String[] smallOrders);

    void insertSmalls(Long merchantId, String handleNo, String[] smallOrders);

    void ship(String handleNo);

    List<StaffDO> findUserInfoByUserIds(Long merchantId, Long[] userIDs);

    void deleteHandleAndSmalls(String handleNo);

}
