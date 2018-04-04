package com.nilo.dms.dao;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.dao.dataobject.ReportInvoiceQueryDO;
import com.nilo.dms.service.order.model.ReportCodQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WaybillCodDao {
    List<ReportCodDO> queryReportCod(ReportCodQuery reportCodQuery);
    Integer queryReportCodCount(ReportCodQuery reportCodQuery);
}
