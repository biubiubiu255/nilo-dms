package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryFeeDetailsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryFeeDetailsDao extends BaseDao<Long, DeliveryFeeDetailsDO> {


    List<DeliveryFeeDetailsDO> queryBy(@Param("merchantId") Long merchantId, @Param("bizType") String bizType, @Param("orderNo") String orderNo, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime, @Param("offset") int offset, @Param("limit") int limit);

    Long queryCountBy(@Param("merchantId") Long merchantId, @Param("bizType") String bizType, @Param("orderNo") String orderNo, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime);

}
