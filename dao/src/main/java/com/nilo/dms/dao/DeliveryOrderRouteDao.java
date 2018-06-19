package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderRouteDO;
import com.nilo.dms.dao.dataobject.WaybillRouteDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRouteDao extends BaseDao<Long, WaybillRouteDO> {

    List<WaybillRouteDO> findBy(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo);

    WaybillRouteDO findByType(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo,@Param("type") String type);

}
