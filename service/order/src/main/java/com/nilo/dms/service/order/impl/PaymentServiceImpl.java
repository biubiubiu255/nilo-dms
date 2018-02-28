package com.nilo.dms.service.order.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nilo.dms.dao.WaybillPaymentOrderDao;
import com.nilo.dms.dao.WaybillPaymentOrderWaybillDao;
import com.nilo.dms.service.order.PaymentService;
import com.nilo.dms.service.order.model.WaybillPaymentOrder;
import com.nilo.dms.service.order.model.WaybillPaymentOrderWaybill;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WaybillPaymentOrderDao paymentDao;

	@Autowired
	private WaybillPaymentOrderWaybillDao paymentOrderWaybillDao;

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

}
