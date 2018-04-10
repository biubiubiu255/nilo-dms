package com.nilo.dms.dao;

import org.springframework.stereotype.Repository;

import com.nilo.dms.dto.order.WaybillPaymentRecord;
@Repository
public interface WaybillPaymentRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WaybillPaymentRecord record);

    int insertSelective(WaybillPaymentRecord record);

    WaybillPaymentRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WaybillPaymentRecord record);

    int updateByPrimaryKey(WaybillPaymentRecord record);
    
    WaybillPaymentRecord selectByPaymentOrderId(String orderId);
}