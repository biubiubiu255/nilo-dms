package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.AbnormalOptRequest;
import com.nilo.dms.service.order.model.AbnormalOrder;
import com.nilo.dms.service.order.model.QueryAbnormalOrderParameter;

import java.util.List;

/**
 * Created by admin on 2017/11/9.
 */
public interface AbnormalOrderService {

    void addAbnormalOrder(AbnormalOrder abnormalOrder);

    List<AbnormalOrder> queryAbnormalBy(QueryAbnormalOrderParameter parameter, Pagination pagination);

    AbnormalOrder queryByOrderNo(String merchantId, String orderNo);

    void handleAbnormal(AbnormalOptRequest request);
}
