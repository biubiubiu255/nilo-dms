package com.nilo.dms.service.order;

import java.util.List;

import com.nilo.dms.service.order.model.WaybillPaymentOrder;

public interface PaymentService {

	void savePaymentOrder(WaybillPaymentOrder paymentOrder,List<String> waybillNos);
    
}
