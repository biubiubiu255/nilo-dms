package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.RiderDeliveryDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.WaybillOrderDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleRiderDao extends BaseDao<Long, RiderDeliveryDO> {

    List<RiderDeliveryDO> queryRiderDeliveryBig(RiderDeliveryDO riderDeliveryDO);

    List<RiderDeliverySmallDO> queryDeliverySmall(RiderDeliverySmallDO riderDeliverySmallDO);

    void insertBig(RiderDeliveryDO riderDeliveryDO);

    void insertSmall(RiderDeliverySmallDO riderDeliverySmallDO);

}
