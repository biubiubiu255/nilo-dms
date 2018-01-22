package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.AreaDO;
import com.nilo.dms.dao.dataobject.ScheduleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleDao extends BaseDao<Long, ScheduleDO> {

    List<ScheduleDO> findAll(@Param("merchantId") Long merchantId,@Param("status") Integer status);

    ScheduleDO queryByJobName(@Param("jobName") String jobName);

}
