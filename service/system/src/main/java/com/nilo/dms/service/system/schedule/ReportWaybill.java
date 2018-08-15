package com.nilo.dms.service.system.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.ReportWaybillDao;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.ReportWaybillDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.org.Staff;

public class ReportWaybill {

	private static final Logger logger = LoggerFactory.getLogger(ReportWaybill.class);

	/*
	 * ClassPathXmlApplicationContext context = ClassPathXmlApplicationContext.
	 * IMonMileageService monMileageService = (IMonMileageService)
	 * context.getBean("monMileageService");
	 */

	@Autowired
	private ReportWaybillDao reportWaybillDao;

	@Autowired
	private WaybillDao waybillDao;

	@Autowired
	private DistributionNetworkDao distributionNetworkDao;

	@Autowired
	private StaffDao staffDao;

	public void run() {

		Long maxUpdatedTime = reportWaybillDao.queryWaybillReportMaxUpdatedTime();
		List<WaybillDO> waybillDOs = waybillDao.queryByUpdateTime(maxUpdatedTime);
		List<DistributionNetworkDO> distributionNetworkDOs = distributionNetworkDao.findAll();

		Map<Integer, String> networkMap = new HashMap<Integer, String>();
		for (DistributionNetworkDO distributionNetworkDO : distributionNetworkDOs) {
			networkMap.put(distributionNetworkDO.getId().intValue(), distributionNetworkDO.getName());
		}
		List<StaffDO> staffs = staffDao.queryAllRider(1L, null);
		Map<Long, String> riderMap = new HashMap<Long, String>();
		Map<Long, String> outsourceMap = new HashMap<Long, String>();
		for (StaffDO staffDO : staffs) {
			riderMap.put(Long.parseLong(staffDO.getUserId()), staffDO.getStaffId() + "-" + staffDO.getRealName());
			outsourceMap.put(Long.parseLong(staffDO.getUserId()), staffDO.getOutsource());
		}

		for (WaybillDO waybillDO : waybillDOs) {
			try {
				// 查询是否已经新增过该单如果未新增，取得信息后，进行新增。如果已新增，则else更新
				ReportWaybillDO newReportWaybill = reportWaybillDao.queryWaybillReportByOrderNo(waybillDO.getOrderNo());
				boolean newFlag = true;
				if (newReportWaybill == null) {
					// 从waybill表取新增的基本信息
					newReportWaybill = reportWaybillDao.queryWaybillReportFormWaybill(waybillDO.getOrderNo());
				} else {
					newFlag = false;
				}
				
				newReportWaybill.setUpdatedTime(waybillDO.getUpdatedTime().intValue());
				// 取最早一次到件信息
				ReportWaybillDO updateArriveReportWaybill = reportWaybillDao
						.queryWaybillArriveLog(waybillDO.getOrderNo());
				if (updateArriveReportWaybill != null) {
					newReportWaybill.setFirstArriveNetworkId(updateArriveReportWaybill.getFirstArriveNetworkId());
					newReportWaybill.setFirstArriveTime(updateArriveReportWaybill.getFirstArriveTime());
				}
				// 取最后一次派给自提点的信息
				// 自提点的路由信息存在大包里，所以根据大包取派送信息
				ReportWaybillDO updateDeliverStationWaybill = reportWaybillDao
						.queryWaybillDeliverStationLog(waybillDO.getParentNo());
				if (updateDeliverStationWaybill != null) {
					WaybillDO parentWaybill = waybillDao.queryByOrderNo(1L, waybillDO.getParentNo());
					newReportWaybill.setLastDeliverNetworkId(parentWaybill.getNextNetworkId());
					newReportWaybill.setLastDeliverTime(updateDeliverStationWaybill.getLastDeliverTime());
				}



				// 取最后一次派给快递员的信息
				ReportWaybillDO updateDeliverRiderWaybill = reportWaybillDao
						.queryWaybillDeliverRiderLog(waybillDO.getOrderNo());
				if (updateDeliverRiderWaybill != null) {
					if (newReportWaybill.getLastDeliverTime() == null
							|| updateDeliverRiderWaybill.getLastDeliverTime() > newReportWaybill.getLastDeliverTime()) {
						newReportWaybill.setLastDeliverNetworkId(updateDeliverRiderWaybill.getLastDeliverNetworkId());
						newReportWaybill.setLastDeliverTime(updateDeliverRiderWaybill.getLastDeliverTime());

						newReportWaybill.setLastDeliverRiderId(updateDeliverRiderWaybill.getLastDeliverRiderId());
					}
				}
				// 取最后一次派给送快递公司信息
				ReportWaybillDO updateDeliverExpressWaybill = reportWaybillDao
						.queryWaybillDeliverExpressLog(waybillDO.getOrderNo());
				if (updateDeliverExpressWaybill != null) {
					if (newReportWaybill.getLastDeliverTime() == null || updateDeliverExpressWaybill
							.getLastDeliverTime() > newReportWaybill.getLastDeliverTime()) {
						newReportWaybill.setLastDeliverNetworkId(updateDeliverExpressWaybill.getLastDeliverNetworkId());
						newReportWaybill
								.setLastDeliverExpressCode(updateDeliverExpressWaybill.getLastDeliverExpressCode());
						newReportWaybill.setLastDeliverTime(updateDeliverExpressWaybill.getLastDeliverTime());
						newReportWaybill.setLastDeliverRiderId(null);// 如果最后一次是派给快递公司的，要清空rider
					}
				}
				// 签收信息
				ReportWaybillDO signWaybill = reportWaybillDao.queryWaybillSignLog(waybillDO.getOrderNo());
				if (signWaybill != null) {
					newReportWaybill.setSignHandleById(signWaybill.getSignHandleById());
					newReportWaybill.setSignNetworkId(signWaybill.getSignNetworkId());
					newReportWaybill.setSignTime(signWaybill.getSignTime());
				}

				// 根据对应的网点id、riderId更新对应的描述
				if (newReportWaybill.getFirstArriveNetworkId() != null) {
					newReportWaybill
							.setFirstArriveNetworkName(networkMap.get(newReportWaybill.getFirstArriveNetworkId()));
				}
				if (newReportWaybill.getLastDeliverNetworkId() != null) {
					newReportWaybill
							.setLastDeliverNetworkName(networkMap.get(newReportWaybill.getLastDeliverNetworkId()));
				}
				if (newReportWaybill.getSignNetworkId() != null) {
					newReportWaybill.setSignNetworkName(networkMap.get(newReportWaybill.getSignNetworkId()));
				}
				if (newReportWaybill.getLastDeliverRiderId() != null) {
					if (riderMap.containsKey(newReportWaybill.getLastDeliverRiderId())) {
						newReportWaybill
								.setLastDeliverRiderName(riderMap.get(newReportWaybill.getLastDeliverRiderId()));
						newReportWaybill.setLastDeliverOutsourceName(
								outsourceMap.get(newReportWaybill.getLastDeliverRiderId()));
					}
				}
				if (newReportWaybill.getSignHandleById() != null) {
					if (riderMap.containsKey(newReportWaybill.getSignHandleById())) {
						newReportWaybill.setSignHandleByName(riderMap.get(newReportWaybill.getSignHandleById()));
						newReportWaybill.setSignOutsourceCode(outsourceMap.get(newReportWaybill.getSignHandleById()));
						newReportWaybill.setSignOutsourceName(outsourceMap.get(newReportWaybill.getSignHandleById()));
					}
				}

				//判断最新的waybill状态是否已经变更
				if (newFlag){
                    ReportWaybillDO newWaybill = reportWaybillDao.queryWaybillReportFormWaybill(waybillDO.getOrderNo());
                    if (!newWaybill.getOrderStatus().equals(newReportWaybill.getOrderStatus())){
                        newReportWaybill.setOrderStatus(newWaybill.getOrderStatus());
                    }
                }


				if (newFlag) {
					// 插入到结果表中
					reportWaybillDao.insertReportWaybill(newReportWaybill);
				} else {
					// 更新
					reportWaybillDao.updateWaybillReport(newReportWaybill);
				}

			} catch (Exception e) {
				logger.error(waybillDO.getOrderNo() + ":" + e.getMessage());
			}

			// reportWaybillDao.queryWaybillReport(creatTime)
		}

	}

}
