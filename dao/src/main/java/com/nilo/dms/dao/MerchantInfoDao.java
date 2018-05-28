package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.MerchantInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantInfoDao extends BaseDao<Long, MerchantInfoDO> {

    List<MerchantInfoDO> findAll();

    List<MerchantInfoDO> findBy(@Param("merchantName") String merchantName,@Param("offset") int offset, @Param("limit") int limit);

    Integer findCountBy(@Param("merchantName") String merchantName);
}
