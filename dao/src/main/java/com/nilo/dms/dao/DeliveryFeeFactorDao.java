package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.BizFeeConfigDO;
import com.nilo.dms.dao.dataobject.DeliveryFeeFactorDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryFeeFactorDao extends BaseDao<Long, DeliveryFeeFactorDO> {

    List<DeliveryFeeFactorDO> queryBy(Long templateId);

    void updateStatus(@Param("templateId") Long templateId,@Param("status") Integer status);
}
