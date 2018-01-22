package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.NotifyDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyDao extends BaseDao<Long, NotifyDO> {

    NotifyDO findByNotifyId(@Param("merchantId") Long merchantId, @Param("notifyId") String notifyId);

    List<NotifyDO> findBy(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo, @Param("bizType") String bizType,@Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    Long findCountBy(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo, @Param("bizType") String bizType,@Param("status") Integer status);

    List<NotifyDO> queryRetryList(Integer status);
}
