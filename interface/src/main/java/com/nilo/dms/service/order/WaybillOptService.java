package com.nilo.dms.service.order;

import com.nilo.dms.dto.order.AbnormalParam;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.dto.order.SignForOrderParam;

import java.util.List;

/**
 * Created by admin on 2017/10/30.
 */
public interface WaybillOptService {

    void goToPickup(String merchantId, String orderNo, String optBy, String taskId);

    void pickup(String merchantId, String orderNo, String optBy, String taskId);

    void pickupFailed(String merchantId, String orderNo, String reason, String optBy, String taskId);

    void sign(String orderNo, String remark);

    void refuse(AbnormalParam param);

    void delay(DelayParam param);

    void completeDelay(String orderNo);


}
