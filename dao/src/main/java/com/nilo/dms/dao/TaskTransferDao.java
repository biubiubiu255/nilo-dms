package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.TaskDO;
import com.nilo.dms.dao.dataobject.TaskTransferDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskTransferDao extends BaseDao<Long, TaskTransferDO> {

}
