package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.BizFeeConfigDO;
import com.nilo.dms.dao.dataobject.SMSLogDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSLogDao extends BaseDao<Long, SMSLogDO> {

    SMSLogDO queryByOrderNo(String orderNo);

}
