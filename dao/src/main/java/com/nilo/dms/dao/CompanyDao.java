package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDao extends BaseDao<Long, CompanyDO> {

    List<CompanyDO> findAll();

    CompanyDO findByMerchantId(Long merchantId);
}
