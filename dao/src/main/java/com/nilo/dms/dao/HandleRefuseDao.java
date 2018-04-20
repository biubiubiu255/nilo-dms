package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dto.handle.HandleDelay;
import com.nilo.dms.dto.handle.HandleRefuse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandleRefuseDao extends BaseDao<Long, HandleRefuse> {

    HandleRefuse findByOrderNo(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo);

    List<HandleRefuse> queryByDO(HandleRefuse handleRefuse);

    List<HandleRefuse> queryBy(@Param(value = "now") HandleRefuse handleRefuse, @Param(value = "beginTime") Integer beginTime, @Param(value = "endTime") Integer endTime, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Long queryCountBy(@Param(value = "now") HandleRefuse handleRefuse, @Param(value = "beginTime") Integer beginTime, @Param(value = "endTime") Integer endTime);

}
