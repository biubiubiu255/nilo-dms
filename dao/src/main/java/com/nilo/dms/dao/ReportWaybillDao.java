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
public interface ReportWaybillDao extends BaseDao<Long, ReportWaybillDO> {

	List<ReportWaybillDO> queryWaybillReport(ReportWaybillQO reportWaybillQO);

	Long queryCountBy(ReportWaybillQO reportWaybillQO);

	// 如果没取到时间，会取1526743852，上线时的时间作为默认
	Long queryWaybillReportMaxUpdatedTime();

	// 如果没取到时间，会取1526743852，上线时的时间作为默认
	Long queryWaybillReportMaxCreatTime();

	Long insertReportWaybill(ReportWaybillDO reportWaybill);

	Long updateWaybillReport(ReportWaybillDO reportWaybill);

	List<ReportWaybillDO> queryWaybillReportByUpdateTime(Long updateTime);

	ReportWaybillDO queryWaybillReportByOrderNo(String orderNo);

	ReportWaybillDO queryWaybillReportFormWaybill(String orderNo);

	ReportWaybillDO queryWaybillDeliverExpressLog(String orderNo);

	ReportWaybillDO queryWaybillDeliverRiderLog(String orderNo);

	ReportWaybillDO queryWaybillDeliverStationLog(String parentNo);
	
	ReportWaybillDO queryWaybillArriveLog(String orderNo);
	
	ReportWaybillDO queryWaybillSignLog(String orderNo);
}
