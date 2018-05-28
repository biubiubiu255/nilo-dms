package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderRequestDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRequestDao extends BaseDao<Long, DeliveryOrderRequestDO> {

    List<DeliveryOrderRequestDO> queryBy(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo,@Param(value = "data") String data, @Param(value = "sign") String sign, @Param("offset") int offset, @Param("limit") int limit);

    DeliveryOrderRequestDO queryByOrderNo(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo);

}
