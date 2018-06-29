package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliverAgendaDO;
import com.nilo.dms.dao.dataobject.DeliverReportDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
@Repository
public interface DeliverAgendaDao extends BaseDao<Long,DeliverAgendaDO> {
    DeliverAgendaDO queryReport(@Param("riderNo") String riderNo, @Param("dateFormat") String dateFormat);
    DeliverAgendaDO querySumSignReport(@Param("riderNo") String riderNo);
}
