package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.FlowMovingNodeDO;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowMovingNodeDao extends BaseDao<Long,FlowMovingNodeDO> {

    FlowMovingNodeDO queryFlowNodeBy(String flowId);
}
