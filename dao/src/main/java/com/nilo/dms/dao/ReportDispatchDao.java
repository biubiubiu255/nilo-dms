package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.ReportDispatchDO;
import com.nilo.dms.dao.dataobject.ReportReceiveDO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface ReportDispatchDao extends BaseDao<Long,SendReportDO> {
    List<ReportDispatchDO> queryReportDispatch(Map map);
    Integer queryReportDispatchCount(Map map);
}
