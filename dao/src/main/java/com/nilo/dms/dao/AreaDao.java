package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.AreaDO;
import com.nilo.dms.dao.dataobject.CompanyDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaDao extends BaseDao<Long, AreaDO> {

    List<AreaDO> findAll(String countryCode);

}
