package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.QO.SendReportQO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
         */
@Repository
public interface SendReportDao extends BaseDao<Long,SendReportDO> {
    List<SendReportDO> querySendStationReport(SendReportQO sendReportQO);
    Long querySendStationCount(SendReportQO sendReportQO);

    List<SendReportDO> querySendExpressReport(SendReportQO sendReportQO);
    Long querySendExpressCount(SendReportQO sendReportQO);
}
