package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.dto.order.ReportCodQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaybillCodDao {
    List<ReportCodDO> queryReportCod(ReportCodQuery reportCodQuery);
    Integer queryReportCodCount(ReportCodQuery reportCodQuery);
}
