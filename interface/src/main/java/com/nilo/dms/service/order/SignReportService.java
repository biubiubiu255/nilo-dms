package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.SignOrderParameter;
import com.nilo.dms.service.order.model.SignReport;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface SignReportService {
    List<SignReport> querySignReport(SignOrderParameter parameter, Pagination pagination);
}
