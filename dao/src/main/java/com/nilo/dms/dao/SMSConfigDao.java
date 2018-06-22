package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.MessageDO;
import com.nilo.dms.dao.dataobject.SMSConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSConfigDao extends BaseDao<Long, SMSConfigDO> {


    List<SMSConfigDO> listBy(@Param("merchantId") String merchantId);

    SMSConfigDO getBy(@Param("merchantId") String merchantId, @Param("smsType") String smsType);

}
