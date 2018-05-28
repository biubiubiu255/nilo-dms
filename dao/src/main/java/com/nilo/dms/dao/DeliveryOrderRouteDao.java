package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRouteDao extends BaseDao<Long, DeliveryOrderRouteDO> {

    List<DeliveryOrderRouteDO> findBy(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo);

    DeliveryOrderRouteDO findByType(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo,@Param("type") String type);

}
