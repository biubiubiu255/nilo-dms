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
public interface ReceiveReportDao extends BaseDao<Long,SendReportDO> {
    List<ReceiveReportDO> queryReportReceive(Map map);
    Integer queryReportReceiveCount(Map map);
}
