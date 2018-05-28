package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.DeliverOrderParameter;
import com.nilo.dms.dto.order.DeliverReport;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface DeliverReportService {
    List<DeliverReport> queryDeliverReport(DeliverOrderParameter parameter, Pagination pagination);
}
