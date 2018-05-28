package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.MessageDO;
import com.nilo.dms.dao.dataobject.SMSConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSConfigDao extends BaseDao<Long, SMSConfigDO> {

    List<SMSConfigDO> queryAll();

    List<SMSConfigDO> queryAllBy(@Param("merchantId") Long merchantId);

    SMSConfigDO queryBy(@Param("merchantId") Long merchantId, @Param("smsType") String smsType);

}
