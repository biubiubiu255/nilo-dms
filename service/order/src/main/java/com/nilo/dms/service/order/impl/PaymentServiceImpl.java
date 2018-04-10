package com.nilo.dms.service.order.impl;

import java.math.BigDecimal;
import java.util.List;

import com.nilo.dms.dao.dataobject.WaybillDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.WaybillPaymentOrderDao;
import com.nilo.dms.dao.WaybillPaymentOrderWaybillDao;
import com.nilo.dms.dao.WaybillPaymentRecordDao;
import com.nilo.dms.dao.WaybillTaskDao;
import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import com.nilo.dms.service.order.PaymentService;
import com.nilo.dms.service.order.model.WaybillPaymentOrder;
import com.nilo.dms.service.order.model.WaybillPaymentOrderWaybill;
import com.nilo.dms.service.order.model.WaybillPaymentRecord;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WaybillPaymentOrderDao paymentDao;

	@Autowired
	private WaybillPaymentOrderWaybillDao paymentOrderWaybillDao;

	@Autowired
	private WaybillPaymentRecordDao waybillPaymentRecordDao;

	@Autowired
	private WaybillDao waybillDao;

	@Autowired
	private WaybillPaymentOrderDao waybillPaymentOrderDao;
	
	@Autowired
	private WaybillTaskDao waybillTaskDao;

	@Override
	public void savePaymentOrder(WaybillPaymentOrder paymentOrder, List<String> waybillNos) {

		paymentDao.insertSelective(paymentOrder);

		for (String waybillOrderNo : waybillNos) {
			WaybillPaymentOrderWaybill waybillPaymentOrderWaybill = new WaybillPaymentOrderWaybill();
			waybillPaymentOrderWaybill.setPaymentOrderId(paymentOrder.getId());
			waybillPaymentOrderWaybill.setWaybillOrderNo(waybillOrderNo);
			paymentOrderWaybillDao.insert(waybillPaymentOrderWaybill);
		}
	}

	@Override
	public void savePaymentOrderRecord(WaybillPaymentRecord waybillPaymentRecord) {
		waybillPaymentRecordDao.insert(waybillPaymentRecord);
	}

	@Override
	public void payRerun(WaybillPaymentRecord waybillPaymentRecord) {

		WaybillPaymentRecord oldWaybillPaymentRecord = waybillPaymentRecordDao
				.selectByPaymentOrderId(waybillPaymentRecord.getPaymentOrderId());
		if (oldWaybillPaymentRecord == null) {
			this.savePaymentOrderRecord(waybillPaymentRecord);
		}
		
		//支付成功
		if(waybillPaymentRecord.getStatus()==1) {
			this.paySucess(waybillPaymentRecord);
		}

		/*
		 * WaybillDO orderDO = new WaybillDO();
		 * 
		 * orderDO.setOrderNo(waybillPaymentRecord.getPaymentOrderId());
		 * //orderDO.setAlreadyPaid(amount);
		 * orderDO.setMerchantId(Long.parseLong(merchantId));
		 * 
		 * deliveryOrderDao.update(orderDO);
		 */

	}

	@Override
	public void paySucess(WaybillPaymentRecord waybillPaymentRecord) {

		// 更新订单状态
		WaybillPaymentOrder waybillPaymentOrder = new WaybillPaymentOrder();
		waybillPaymentOrder.setId(waybillPaymentRecord.getPaymentOrderId());
		waybillPaymentOrder.setStatus(1);
		waybillPaymentOrder.setUpdatedTime(System.currentTimeMillis());
		waybillPaymentOrderDao.updateByPrimaryKeySelective(waybillPaymentOrder);

		// 更新支付记录或者新增支付记录
		WaybillPaymentRecord oldWaybillPaymentRecord = waybillPaymentRecordDao
				.selectByPaymentOrderId(waybillPaymentRecord.getPaymentOrderId());
		if (oldWaybillPaymentRecord == null) {
			this.savePaymentOrderRecord(waybillPaymentRecord);
		} else {
			waybillPaymentRecord.setId(oldWaybillPaymentRecord.getId());
			waybillPaymentRecordDao.updateByPrimaryKeySelective(waybillPaymentRecord);
		}

		// 更新订单已付金额
		List<String> orderNos = paymentOrderWaybillDao.queryByPaymentOrderId(waybillPaymentRecord.getPaymentOrderId());
		if(null==orderNos||orderNos.size()==0) {
			return;
		}
		List<WaybillDO> waybillDOList = waybillDao.selectByOrderNos(orderNos);
		for (WaybillDO waybillDO : waybillDOList) {
			waybillDO.setAlreadyPaid(waybillDO.getNeedPayAmount());
			waybillDao.update(waybillDO);
		}
	}
	
	@Override
	public List<String> getOrderNosByPayOrderId(String paymentOrderId){
		List<String> orderNos = paymentOrderWaybillDao.queryByPaymentOrderId(paymentOrderId);
		return orderNos;
	}

	@Override
	public WaybillPaymentOrder savePaymentOrderByWaybill(Long merchantId ,Integer networks,String createdBy, List<String> waybillNos) {
		
		List<WaybillDO> deliveryOrders = waybillDao.queryByOrderNos(merchantId, waybillNos);
		double totalNeedPayAmount = 0;
		for (WaybillDO waybillDO : deliveryOrders) {
			totalNeedPayAmount = totalNeedPayAmount + waybillDO.getNeedPayAmount();
		}
		totalNeedPayAmount = totalNeedPayAmount*100;
		
		WaybillPaymentOrder paymentOrder = new WaybillPaymentOrder();
		paymentOrder.setId(IdWorker.getInstance().nextId() + "");
		paymentOrder.setNetworkId(networks);
		paymentOrder.setPriceAmount(new BigDecimal(totalNeedPayAmount));
		paymentOrder.setWaybillCount(waybillNos.size());
		paymentOrder.setPaymentTime(System.currentTimeMillis());
		paymentOrder.setCreatedTime(System.currentTimeMillis());
		paymentOrder.setCreatedBy(createdBy);
		this.savePaymentOrder(paymentOrder, waybillNos);
		
		return paymentOrder;
	}

	@Override
	public List<WaybillTaskDo> queryNeedPayOrderByRider(String userId) {
		List<WaybillTaskDo> waybillTaskDos = waybillTaskDao.queryNeedPayOrderByRider(userId);
		return waybillTaskDos;
	}
}
