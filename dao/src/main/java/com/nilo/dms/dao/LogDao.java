package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.LogDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ronny on 2017/9/18.
 */
@Repository
public interface LogDao extends BaseDao<Long, LogDO> {

    List<LogDO> queryBy(@Param(value = "merchantId") Long merchantId, @Param(value = "operation") String operation, @Param(value = "operator") String operator, @Param(value = "content") String content, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime, @Param("offset") int offset, @Param("limit") int limit);

    Long queryCountBy(@Param(value = "merchantId") Long merchantId, @Param(value = "operation") String operation, @Param(value = "operator") String operator, @Param(value = "content") String content, @Param(value = "beginTime") Long beginTime, @Param(value = "endTime") Long endTime );

}
