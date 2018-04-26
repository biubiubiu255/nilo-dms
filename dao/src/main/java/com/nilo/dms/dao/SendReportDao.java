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
    List<SendReportDO> querySendReport(SendReportQO sendReportQO);
    Long queryCountBy(SendReportQO sendReportQO);
}
