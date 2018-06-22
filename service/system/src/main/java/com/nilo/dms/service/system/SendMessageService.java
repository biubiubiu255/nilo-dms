package com.nilo.dms.service.system;


import com.nilo.dms.dto.system.SMSConfig;

import java.util.List;

/**
 * Created by admin on 2017/9/22.
 */
public interface SendMessageService {

    Integer DISABLED=0;
    Integer NORMAL=1;

    String SELF_COLLECT_ARRIVED = "self_collect_arrived";
    String THIRD_SELF_COLLECT = "third_self_collect";
    String THIRD_DOORSTEP = "third_doorstep";
    String RIDER_DELIVERY = "rider_delivery";


    SMSConfig getConfigByType(String merchantId, String smsType);

    void updateConfig(SMSConfig smsConfig);

    List<SMSConfig> listConfigBy(String merchantId);

    void sendMessage(String waybill, String smsType, String phone, String... args);

}
