package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.DeliveryFeeDetails;

import java.util.List;

/**
 * Created by admin on 2017/12/5.
 */
public interface DeliveryFeeDetailsService {

    void buildDeliveryFee(String merchantId, String orderNo, String bizType);

    void update(DeliveryFeeDetails network);

    List<DeliveryFeeDetails> queryBy(String merchantId, String optType, Long beginTime, Long endTime, Pagination pagination);

    DeliveryFeeDetails queryByCode(String merchantId, String code);

}
