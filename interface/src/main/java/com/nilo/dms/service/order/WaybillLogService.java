package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.WaybillLog;
import com.nilo.dms.dto.order.OrderOptRequest;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */
public interface WaybillLogService {

    List<WaybillLog> queryBy(String merchantId, String optType, String orderNo, String optBy, Long fromtTime, Long toTime, Pagination pagination);

    List<WaybillLog> queryByOrderNos(String merchantId, List<String> orderNo);

    void addOptLog(OrderOptRequest request);
}
