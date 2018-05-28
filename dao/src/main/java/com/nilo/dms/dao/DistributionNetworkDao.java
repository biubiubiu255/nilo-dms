package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionNetworkDao extends BaseDao<Long, DistributionNetworkDO> {

    List<DistributionNetworkDO> findAll();

    List<DistributionNetworkDO> findAllBy(@Param("merchantId") Long merchantId);

    List<DistributionNetworkDO> findBy(@Param("merchantId") Long merchantId,@Param("name") String name, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Long findCountBy(@Param("merchantId") Long merchantId,@Param("name") String name);

    DistributionNetworkDO findByCode(@Param("merchantId") Long merchantId,@Param("code")String code);


}
