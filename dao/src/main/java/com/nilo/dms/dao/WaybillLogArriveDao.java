package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.WaybillLogArriveDO;
import com.nilo.dms.dao.dataobject.WaybillLogPackageDO;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillLogArriveDao extends BaseDao<Long, WaybillLogArriveDO> {

}
