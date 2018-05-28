package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.NotifyDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyDao extends BaseDao<Long, NotifyDO> {

    NotifyDO queryByNotifyId(@Param("notifyId") String notifyId);

    List<NotifyDO> queryFailed();

}
