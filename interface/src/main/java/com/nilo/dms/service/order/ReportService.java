package com.nilo.dms.service.order;

import java.util.List;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.dao.dataobject.ReportWaybillDO;
import com.nilo.dms.dto.order.ReportCodQuery;

/**
 * Created by wenzhuo-company on 2018/3/29.
 */
public interface ReportService {
	
    List<ReportCodDO> qeueryCodList(ReportCodQuery reportCodQuery, Pagination pagination);
    
    void updateReportWaybill();
    
    void insertReportWaybill();
    
    
}
