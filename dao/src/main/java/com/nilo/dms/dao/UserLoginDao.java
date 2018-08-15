package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.UserLoginDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginDao extends BaseDao<Long, UserLoginDO> {

    List<UserLoginDO> findAll();

    List<UserLoginDO> findBy(@Param(value = "merchantId") Long merchantId, @Param(value = "userName")String userName,@Param("offset") int offset, @Param("limit") int limit);

    Long findCountBy(@Param(value = "merchantId") Long merchantId, @Param(value = "userName")String userName);

    UserLoginDO findByUserName(String username);

    UserLoginDO findByUserId(Long userId);

    List<UserLoginDO> findByUserIds(@Param(value = "merchantId") Long merchantId, @Param(value = "userIds")List<Long> userIds);

}
