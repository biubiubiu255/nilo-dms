package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliverReportDO;
import com.nilo.dms.dao.dataobject.SignReportDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface SignReportDao extends BaseDao<Long,SignReportDO> {
    List<SignReportDO> querySignReport(Map map);
    Long queryCountBy(Map map);
}
