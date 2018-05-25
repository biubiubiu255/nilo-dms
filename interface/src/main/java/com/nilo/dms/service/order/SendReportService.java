package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.QO.SendReportQO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import com.nilo.dms.dto.order.SendOrderParameter;
import com.nilo.dms.dto.order.SendReport;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface SendReportService {

    List<SendReportDO> querySendStationReport(SendReportQO sendReportQO, Pagination pagination);

    List<SendReportDO> querySendExpressReport(SendReportQO sendReportQO, Pagination pagination);



}
