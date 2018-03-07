package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.ReceiveReportDO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface SendReportDao extends BaseDao<Long,SendReportDO> {
    List<SendReportDO> querySendReport(Map map);
    Long queryCountBy(Map map);

    List<ReceiveReportDO> queryReportReceive(Map map);
    Integer queryReportReceiveCount(Map map);
}
