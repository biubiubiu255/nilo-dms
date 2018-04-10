package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderOptDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliveryOrderOptDao extends BaseDao<Long, DeliveryOrderOptDO> {

    List<DeliveryOrderOptDO> queryBy(@Param(value = "merchantId") Long merchantId, @Param(value = "optType") String optType, @Param(value = "orderNo") String orderNo, @Param(value = "optBy") String optBy, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime, @Param("offset") int offset, @Param("limit") int limit);

    Long queryCountBy(@Param(value = "merchantId") Long merchantId, @Param(value = "optType") String optType, @Param(value = "orderNo") String orderNo, @Param(value = "optBy") String optBy, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime);

    List<DeliveryOrderOptDO> queryByOrderNos(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNos") List<String> orderNos);

    Long getStateByOrderNo(@Param(value = "orderNo") String orderNo);
}
