package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.SendReportDO;
import com.nilo.dms.service.order.model.SendOrderParameter;
import com.nilo.dms.service.order.model.SendReport;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface SendReportService {
    List<SendReport> querySendReport(SendOrderParameter parameter, Pagination pagination);
}
