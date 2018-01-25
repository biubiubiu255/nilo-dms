package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.SMSLogDO;
import com.nilo.dms.dao.dataobject.WaybillScanDO;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillScanDao extends BaseDao<Long, WaybillScanDO> {

}
