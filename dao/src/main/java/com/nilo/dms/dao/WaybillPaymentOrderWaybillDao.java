package com.nilo.dms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nilo.dms.dto.order.WaybillPaymentOrderWaybill;
@Repository
public interface WaybillPaymentOrderWaybillDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WaybillPaymentOrderWaybill record);

    int insertSelective(WaybillPaymentOrderWaybill record);

    WaybillPaymentOrderWaybill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WaybillPaymentOrderWaybill record);

    int updateByPrimaryKey(WaybillPaymentOrderWaybill record);
    
    List<String> queryByPaymentOrderId(String paymentOrderId);
    
}