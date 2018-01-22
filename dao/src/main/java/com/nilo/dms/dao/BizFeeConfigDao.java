package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.BizFeeConfigDO;
import com.nilo.dms.dao.dataobject.RouteConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizFeeConfigDao extends BaseDao<Long, BizFeeConfigDO> {

    List<BizFeeConfigDO> findAll();

    List<BizFeeConfigDO> findAllBy(Long merchantId);

    BizFeeConfigDO findByOptType(@Param("merchantId") Long merchantId, @Param("optType") String optType);
}
