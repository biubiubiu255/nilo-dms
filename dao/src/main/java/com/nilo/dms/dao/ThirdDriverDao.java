package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdDriverDao extends BaseDao<Long, ThirdDriverDO> {

    List<ThirdDriverDO> findByExpressCode(String thirdExpressCode);
    
    List<ThirdDriverDO> findByExpressMultiple(ThirdDriverDO express);
    
    void add(ThirdDriverDO express);
    
    void up (ThirdDriverDO express);
    
    void del(ThirdDriverDO express);
    
}
