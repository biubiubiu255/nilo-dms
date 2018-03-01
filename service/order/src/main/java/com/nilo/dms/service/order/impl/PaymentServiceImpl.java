package com.nilo.dms.service.order.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nilo.dms.dao.DeliveryOrderDao;
import com.nilo.dms.dao.WaybillPaymentOrderDao;
import com.nilo.dms.dao.WaybillPaymentOrderWaybillDao;
import com.nilo.dms.dao.WaybillPaymentRecordDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
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
	private DeliveryOrderDao deliveryOrderDao;

	@Autowired
	private WaybillPaymentOrderDao waybillPaymentOrderDao;

	@Override
	public void savePaymentOrder(WaybillPaymentOrder paymentOrder, List<String> waybillNos) {

		int paymentOrderId = paymentDao.insertSelective(paymentOrder);

		for (String waybillOrderNo : waybillNos) {
			WaybillPaymentOrderWaybill waybillPaymentOrderWaybill = new WaybillPaymentOrderWaybill();
			waybillPaymentOrderWaybill.setPaymentOrderId(String.valueOf(paymentOrderId));
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

		/*
		 * DeliveryOrderDO orderDO = new DeliveryOrderDO();
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
		List<DeliveryOrderDO> deliveryOrderDOList = deliveryOrderDao.selectByOrderNos(orderNos);
		for (DeliveryOrderDO deliveryOrderDO : deliveryOrderDOList) {
			deliveryOrderDO.setAlreadyPaid(deliveryOrderDO.getNeedPayAmount());
			deliveryOrderDao.update(deliveryOrderDO);
		}
	}
}
