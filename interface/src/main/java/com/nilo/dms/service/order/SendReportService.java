package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.SendOrderParameter;
import com.nilo.dms.dto.order.SendReport;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface SendReportService {
    List<SendReport> querySendReport(SendOrderParameter parameter, Pagination pagination);

}
