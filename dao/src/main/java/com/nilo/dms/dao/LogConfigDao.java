package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.LogConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ronny on 2017/9/18.
 */
@Repository
public interface LogConfigDao extends BaseDao<Long, LogConfigDO> {

    List<LogConfigDO> queryAll();
}
