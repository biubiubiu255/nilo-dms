package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderDelayDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderOptDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderDelayDao extends BaseDao<Long, DeliveryOrderDelayDO> {

    DeliveryOrderDelayDO findByOrderNo(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo);

    List<DeliveryOrderDelayDO> queryBy(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime, @Param("offset") int offset, @Param("limit") int limit);

    Long queryCountBy(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime);

}
