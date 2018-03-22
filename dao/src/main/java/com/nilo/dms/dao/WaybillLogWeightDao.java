package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.WaybillLogArriveDO;
import com.nilo.dms.dao.dataobject.WaybillLogPackageDO;
import com.nilo.dms.service.order.model.WaybillLogWeight;

import org.springframework.stereotype.Repository;

@Repository
public interface WaybillLogWeightDao extends BaseDao<Long, WaybillLogWeight> {

}
