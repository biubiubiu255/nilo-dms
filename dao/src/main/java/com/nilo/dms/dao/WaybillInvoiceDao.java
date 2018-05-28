package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.ReportArriveDO;
import com.nilo.dms.dao.dataobject.ReportInvoiceDO;
import com.nilo.dms.dao.dataobject.ReportInvoiceQueryDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WaybillInvoiceDao {
    List<ReportInvoiceDO> queryReportInvoiceInput(ReportInvoiceQueryDO reportInvoiceQueryDO);
    Integer queryReportInvoiceCountInput(ReportInvoiceQueryDO reportInvoiceQueryDO);

    List<ReportInvoiceDO> queryReportInvoiceOut(ReportInvoiceQueryDO reportInvoiceQueryDO);
    Integer queryReportInvoiceCountOut(ReportInvoiceQueryDO reportInvoiceQueryDO);
}
