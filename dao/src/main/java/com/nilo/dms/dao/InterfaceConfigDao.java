package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.InterfaceConfigDO;
import com.nilo.dms.dao.dataobject.MerchantConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceConfigDao extends BaseDao<Long, InterfaceConfigDO> {

    List<InterfaceConfigDO> queryByMerchantId(Long merchantId);

    InterfaceConfigDO queryByMethod(@Param(value = "merchantId") Long merchantId, @Param(value = "method") String method);
}
