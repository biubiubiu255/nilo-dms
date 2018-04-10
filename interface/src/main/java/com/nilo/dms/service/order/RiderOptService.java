package com.nilo.dms.service.order;

import com.nilo.dms.dto.order.AbnormalParam;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.dto.order.SignForOrderParam;

/**
 * Created by admin on 2017/10/30.
 */
public interface RiderOptService {

    void goToPickup(String merchantId, String orderNo, String optBy, String taskId);

    void pickup(String merchantId, String orderNo, String optBy, String taskId);

    void pickupFailed(String merchantId, String orderNo, String reason, String optBy, String taskId);

    void signForOrder(SignForOrderParam param);

    void refuse(AbnormalParam param);

    void delay(DelayParam param);

    void resend(DelayParam param);


}
