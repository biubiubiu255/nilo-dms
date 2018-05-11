package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.OutsourceDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutSourceDao extends BaseDao<Long, OutsourceDO> {

    List<OutsourceDO> findAll(String merchantId);

}
