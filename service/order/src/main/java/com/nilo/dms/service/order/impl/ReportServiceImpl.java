package com.nilo.dms.service.order.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.ReportWaybillDao;
import com.nilo.dms.dao.WaybillArriveDao;
import com.nilo.dms.dao.WaybillCodDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.dao.dataobject.ReportWaybillDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.order.ReportCodQuery;
import com.nilo.dms.service.order.ReportService;

/**
 * Created by wenzhuo-company on 2018/3/29.
 */

@Service
public class ReportServiceImpl implements ReportService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WaybillCodDao waybillCodDao;

	@Autowired
	private ReportWaybillDao reportWaybillDao;

	@Autowired
	private WaybillDao waybillDao;

	@Autowired
	private WaybillArriveDao waybillArriveDao;

	@Override
	public List<ReportCodDO> qeueryCodList(ReportCodQuery reportCodQuery, Pagination page) {

		reportCodQuery.setLimit(page.getLimit());
		reportCodQuery.setOffset(page.getOffset());
		if (reportCodQuery.getOut_warm() == null) {
			reportCodQuery.setOut_warm(1);
			// 设置默认的预警时间
		}
		List<ReportCodDO> list = waybillCodDao.queryReportCod(reportCodQuery);
		for (int i = 0; i < list.size(); i++) {
			ReportCodDO reportCodDO = list.get(i);
			reportCodDO.setOrder_status(reportCodDO.getOrder_status_desc());
			list.set(i, reportCodDO);
		}
		page.setTotalCount(waybillCodDao.queryReportCodCount(reportCodQuery));
		return list;
	}

	@Override
	@Transactional
	public void updateReportWaybill() {
		// TODO Auto-generated method stub
		Long maxUpdatedTime = reportWaybillDao.queryWaybillReportMaxUpdatedTime();

		List<WaybillDO> waybillDOs = waybillDao.queryByUpdateTime(maxUpdatedTime);
		
		

		for (WaybillDO waybillDO : waybillDOs) {
			try {
				//查询是否已经新增过该单如果未新增，取得信息后，进行新增。如果已新增，则else更新
				ReportWaybillDO reportWaybill = reportWaybillDao.queryWaybillReportByOrderNo(waybillDO.getOrderNo());
				if (reportWaybill == null) {
					
					//从waybill表取新增的基本信息
					ReportWaybillDO newReportWaybill =reportWaybillDao.queryWaybillReportFormWaybill(waybillDO.getOrderNo());
					
					//取最早一次到件信息
					ReportWaybillDO updateArriveReportWaybill = reportWaybillDao.queryWaybillArriveLog(waybillDO.getOrderNo());
					if(updateArriveReportWaybill!=null) {
						newReportWaybill.setFirstArriveNetworkId(updateArriveReportWaybill.getFirstArriveNetworkId());
						newReportWaybill.setFirstArriveTime(updateArriveReportWaybill.getFirstArriveTime());
					}
					
					//取最后一次派给自提点的信息
					//自提点的路由信息存在大包里，所以根据大包取派送信息
					ReportWaybillDO updateDeliverStationWaybill = reportWaybillDao.queryWaybillDeliverStationLog(waybillDO.getParentNo());
					if(updateDeliverStationWaybill!=null) {
						newReportWaybill.setLastDeliverNetworkId(waybillDO.getNetworkId());
						newReportWaybill.setLastDeliverTime(updateDeliverStationWaybill.getLastDeliverTime());
					}
					
					//取最后一次派给快递员的信息
					ReportWaybillDO updateDeliverRiderWaybill = reportWaybillDao.queryWaybillDeliverRiderLog(waybillDO.getOrderNo());
					if(updateDeliverRiderWaybill!=null) {
						if(newReportWaybill.getLastDeliverNetworkId()!=null&&newReportWaybill.getLastDeliverNetworkId()!=0) {
							if(updateDeliverRiderWaybill.getLastDeliverTime()>newReportWaybill.getLastDeliverTime()) {
								newReportWaybill.setLastDeliverNetworkId(updateDeliverRiderWaybill.getLastDeliverNetworkId());
								newReportWaybill.setLastDeliverTime(updateDeliverRiderWaybill.getLastDeliverTime());
								
								newReportWaybill.setLastDeliverRiderId(updateDeliverRiderWaybill.getLastDeliverRiderId());
							}
						}
					}
					
					
					//取最后一次派给送快递公司信息
					ReportWaybillDO updateDeliverExpressWaybill = reportWaybillDao.queryWaybillDeliverExpressLog(waybillDO.getOrderNo());
					
					if(updateDeliverExpressWaybill!=null) {
						if(newReportWaybill.getLastDeliverNetworkId()!=null&&newReportWaybill.getLastDeliverNetworkId()!=0) {
							if(updateDeliverExpressWaybill.getLastDeliverTime()>newReportWaybill.getLastDeliverTime()) {
								newReportWaybill.setLastDeliverNetworkId(updateDeliverExpressWaybill.getLastDeliverNetworkId());
								newReportWaybill.setLastDeliverTime(updateDeliverStationWaybill.getLastDeliverTime());
								newReportWaybill.setLastDeliverRiderId(null);//如果最后一次是派给快递公司的，要清空rider
							}
						}
					}
					
					//根据对应的网点id、riderId更新对应的描述
					
					
					
					reportWaybillDao.insertReportWaybill(newReportWaybill);
				}
			} catch (Exception e) {
				logger.error(waybillDO.getOrderNo()+":"+e.getMessage());
			}
		
			// reportWaybillDao.queryWaybillReport(creatTime)
		}

	}

	@Override
	@Transactional
	public void insertReportWaybill() {
		Long maxCreatTime = reportWaybillDao.queryWaybillReportMaxCreatTime();
		List<ReportWaybillDO> reportWaybills = reportWaybillDao.queryWaybillReportByUpdateTime(maxCreatTime);
		for (ReportWaybillDO reportWaybillDO : reportWaybills) {
			reportWaybillDao.insertReportWaybill(reportWaybillDO);
		}
	}
}
