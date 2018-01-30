package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.TaskTransferDO;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNetworkDao extends BaseDao<Long, UserNetworkDO> {

    List<UserNetworkDO> queryByUserId(Long userId);

    List<UserNetworkDO> queryByNetworkId(Long networkId);

    Integer deleteAll(Long userId);

    Integer insertAll(@Param("userId") Long userId, @Param("networks") Long[] networks);
}
