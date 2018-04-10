package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.AbnormalOptRequest;
import com.nilo.dms.dto.order.AbnormalOrder;
import com.nilo.dms.dto.order.QueryAbnormalOrderParameter;

import java.util.List;

/**
 * Created by admin on 2017/11/9.
 */
public interface AbnormalOrderService {

    void addAbnormalOrder(AbnormalOrder abnormalOrder);

    void delete(String abnormalNo,String merchantId);

    List<AbnormalOrder> queryAbnormalBy(QueryAbnormalOrderParameter parameter, Pagination pagination);

    AbnormalOrder queryByOrderNo(String merchantId, String orderNo);

    void handleAbnormal(AbnormalOptRequest request);
}
