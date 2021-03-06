package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.PermissionDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends BaseDao<Long, PermissionDO> {

    /**
     * @return
     */
    List<PermissionDO> findAll();

}
