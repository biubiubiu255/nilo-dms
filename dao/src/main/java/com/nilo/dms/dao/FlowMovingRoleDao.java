package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.FlowMovingDO;
import com.nilo.dms.dao.dataobject.FlowMovingRoleDO;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowMovingRoleDao extends BaseDao<Long,FlowMovingRoleDO> {

    FlowMovingRoleDO queryBy(String fromNode);

}
