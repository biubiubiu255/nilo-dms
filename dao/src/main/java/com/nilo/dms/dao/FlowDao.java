package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.FlowDO;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowDao extends BaseDao<Long, FlowDO> {

    FlowDO queryFlowBy(String type);
}
