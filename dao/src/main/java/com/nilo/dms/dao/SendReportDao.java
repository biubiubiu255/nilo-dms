package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DeliveryEfficiencyDO;
import com.nilo.dms.dao.dataobject.QO.ReportRepeatQO;
import com.nilo.dms.dao.dataobject.QO.SendReportQO;
import com.nilo.dms.dao.dataobject.ReportRepeatDO;
import com.nilo.dms.dao.dataobject.SendReportDO;
import com.nilo.dms.dao.dataobject.SummarizeExpressDO;
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


    List<ReportRepeatDO>queryRepeatDispatch(ReportRepeatQO reportRepeatQO);

    Long queryRepeatDispatchCount(ReportRepeatQO reportRepeatQO);

    //查询按月和城市等条件
    List<SummarizeExpressDO>querySummarizeExpress(SummarizeExpressDO summarizeExpressDO);

    Integer querySummarizeExpressCount(SummarizeExpressDO summarizeExpressDO);

    //查询按月汇总头信息
    List<SummarizeExpressDO>querySummarizeExpressHeader(SummarizeExpressDO summarizeExpressDO);

    // 查询每月的派件时效
    List<DeliveryEfficiencyDO>queryExpressDeliveryEfficiency(DeliveryEfficiencyDO deliveryEfficiencyDO);

}
