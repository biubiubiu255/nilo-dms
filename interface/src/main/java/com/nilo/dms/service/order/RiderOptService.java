package com.nilo.dms.service.order;

import com.nilo.dms.service.order.model.AbnormalParam;
import com.nilo.dms.service.order.model.DelayParam;
import com.nilo.dms.service.order.model.SignForOrderParam;

/**
 * Created by admin on 2017/10/30.
 */
public interface RiderOptService {

    void goToPickup(String merchantId, String orderNo, String optBy, String taskId);

    void pickup(String merchantId, String orderNo, String optBy, String taskId);

    void pickupFailed(String merchantId, String orderNo, String reason, String optBy, String taskId);

    void signForOrder(SignForOrderParam param);

    void abnormal(AbnormalParam param);

    void delay(DelayParam param);

    void detain(DelayParam param);

    void resend(DelayParam param);


}
