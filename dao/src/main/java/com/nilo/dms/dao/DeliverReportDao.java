package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliverReportDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliverReportDao extends BaseDao<Long,DeliverReportDO> {
    List<DeliverReportDO> queryDeliverReport(Map map);
    Long queryCountBy(Map map);
}
