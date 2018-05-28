package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.AreaDO;
import com.nilo.dms.dao.dataobject.RouteConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteConfigDao extends BaseDao<Long, RouteConfigDO> {

    List<RouteConfigDO> findAll();

    List<RouteConfigDO> findAllBy(String merchantId);

    RouteConfigDO findByOptType(@Param("merchantId") String merchantId,@Param("optType") String optType);
}
