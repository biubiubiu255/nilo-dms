package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdDriverDao extends BaseDao<Long, ThirdDriverDO> {

    List<ThirdDriverDO> findByExpressCode(String expressCode);
}
