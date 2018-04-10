package com.nilo.dms.dao;

import org.springframework.stereotype.Repository;

import com.nilo.dms.dto.order.WaybillPaymentOrder;

@Repository
public interface WaybillPaymentOrderDao {
    int deleteByPrimaryKey(String id);

    int insert(WaybillPaymentOrder record);

    int insertSelective(WaybillPaymentOrder record);

    WaybillPaymentOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WaybillPaymentOrder record);

    int updateByPrimaryKey(WaybillPaymentOrder record);
}