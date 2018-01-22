package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.OrderHandleConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHandleConfigDao extends BaseDao<Long, OrderHandleConfigDO> {

    List<OrderHandleConfigDO> findAll();

    List<OrderHandleConfigDO> findAllBy(@Param(value = "merchantId") Long merchantId);

    OrderHandleConfigDO findBy(@Param(value="merchantId") Long merchantId, @Param(value="optType")String optType);
}
