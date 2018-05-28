package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.MerchantConfigDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantConfigDao extends BaseDao<Long, MerchantConfigDO> {

    List<MerchantConfigDO> findAll();

    MerchantConfigDO findBy(Long merchantId);
}
