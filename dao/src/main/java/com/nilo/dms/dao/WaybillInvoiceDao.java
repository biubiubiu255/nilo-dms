package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportInvoiceDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WaybillInvoiceDao {
    List<ReportInvoiceDO> queryReportInvoiceInput(Map map);
    Integer queryReportInvoiceCountInput(Map map);

    List<ReportInvoiceDO> queryReportInvoiceOut(Map map);
    Integer queryReportInvoiceCountOut(Map map);
}
