package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.SerialNumberRuleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface SerialNumberRuleDao extends BaseDao<Long, SerialNumberRuleDO> {

    List<SerialNumberRuleDO> findAll();

    List<SerialNumberRuleDO> findAllBy(Long merchantId);

    List<SerialNumberRuleDO> findBy(@Param("merchantId") Long merchantId, @Param("serialType") List<String> serialType);
}
