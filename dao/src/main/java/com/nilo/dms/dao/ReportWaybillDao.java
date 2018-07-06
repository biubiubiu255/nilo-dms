package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.QO.ReportWaybillQO;
import com.nilo.dms.dao.dataobject.ReportWaybillDO;
import com.nilo.dms.dao.dataobject.SignReportDO;
import com.nilo.dms.dto.order.SignOrderParameter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface ReportWaybillDao extends BaseDao<Long,ReportWaybillDO> {

    List<ReportWaybillDO> queryWaybillReport(ReportWaybillQO reportWaybillQO);

    Long queryCountBy(ReportWaybillQO reportWaybillQO);

}
