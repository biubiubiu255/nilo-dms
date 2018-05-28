package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.SMSLogDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaybillScanDetailsDao extends BaseDao<Long, WaybillScanDetailsDO> {

    List<WaybillScanDetailsDO> queryByScanNo(@Param("scanNo") String scanNo);

    WaybillScanDetailsDO queryBy(@Param("orderNo") String orderNo, @Param("scanNo") String scanNo);

    void deleteBy(@Param("orderNo") String orderNo, @Param("scanNo") String scanNo);
}
