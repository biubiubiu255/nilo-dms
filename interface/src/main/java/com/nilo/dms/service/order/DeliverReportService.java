package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.DeliverOrderParameter;
import com.nilo.dms.service.order.model.DeliverReport;
import com.nilo.dms.service.order.model.SendOrderParameter;
import com.nilo.dms.service.order.model.SendReport;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface DeliverReportService {
    List<DeliverReport> queryDeliverReport(DeliverOrderParameter parameter, Pagination pagination);
}
