package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportReceiveDO;
import com.nilo.dms.dao.dataobject.WaybillExternalDo;
import com.nilo.dms.service.order.model.ReportCodQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WaybillArriveDao {
    List<ReportArriveDO> queryReportArrive(Map map);
    Integer queryReportArriveCount(Map map);
}
