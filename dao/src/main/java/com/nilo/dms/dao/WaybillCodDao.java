package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WaybillCodDao {
    List<ReportCodDO> queryReportCod(Map map);
    Integer queryReportCodCount(Map map);
}
