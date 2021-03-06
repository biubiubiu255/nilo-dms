package com.nilo.dms.service.order;

import java.util.List;

import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import com.nilo.dms.service.order.model.WaybillPaymentOrder;
import com.nilo.dms.service.order.model.WaybillPaymentRecord;

public interface PaymentService {

	void savePaymentOrder(WaybillPaymentOrder paymentOrder,List<String> waybillNos);
	
	void savePaymentOrderRecord(WaybillPaymentRecord waybillPaymentRecord);
	
	void paySucess(WaybillPaymentRecord waybillPaymentRecord);
    
	void payRerun(WaybillPaymentRecord waybillPaymentRecord);
	
	List<String> getOrderNosByPayOrderId(String paymentOrderId);
	
	WaybillPaymentOrder savePaymentOrderByWaybill(Long merchantId ,Integer networks,String userId,  List<String> waybillNos);
	
	List<WaybillTaskDo> queryNeedPayOrderByRider(String userId);
	
}
