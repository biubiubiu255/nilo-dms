package com.nilo.dms.service.order;


import com.nilo.dms.dto.order.OrderOptRequest;

/**
 * Created by admin on 2017/11/15.
 */
public interface NotifyService {

    void updateStatus(OrderOptRequest request);

}
