package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.TaskDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskDao extends BaseDao<Long, TaskDO> {

    List<TaskDO> findBy(Map map);

    Long findCountBy(Map map);


    TaskDO findByTaskId(Long taskId);

    TaskDO queryByTypeAndOrderNo(@Param(value = "merchantId") Long merchantId, @Param(value = "taskType") String taskType, @Param(value = "orderNo") String orderNo);
    
    TaskDO queryUnFinishTaskByOrderNo(@Param(value = "merchantId") Long merchantId, @Param(value = "orderNo") String orderNo);
}
