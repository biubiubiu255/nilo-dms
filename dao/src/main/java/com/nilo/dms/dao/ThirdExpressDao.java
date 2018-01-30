package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdExpressDao extends BaseDao<Long, ThirdExpressDO> {

    List<ThirdExpressDO> findByMerchantId(Long merchantId);
}
