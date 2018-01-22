package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoDao extends BaseDao<Long, UserInfoDO> {

    List<UserInfoDO> findAll(String merchantId);

    UserInfoDO queryByUserId(@Param("merchantId") Long merchantId, @Param("userId")Long userId);

    List<UserInfoDO> queryByUserIds(@Param("merchantId") Long merchantId, @Param("userIds")List<Long> userIds);

    List<UserInfoDO> findUserByRoleName(@Param("merchantId") Long merchantId, @Param("roleName")String roleName);
}
