package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.MenuDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ronny on 2017/9/14.
 */
@Repository
public interface MenuDao extends BaseDao<Long, MenuDO> {

    List<MenuDO> queryBy(Long userId);
}
