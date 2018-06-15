package com.nilo.dms.service.system.config;



import com.nilo.dms.dto.system.SMSConfig;

import java.util.List;

/**
 * Created by admin on 2017/9/22.
 */
public interface SMSConfigService {

    String IS_SELF_COLLECT = "1";

    String SELF_COLLECT_ARRIVED = "self_collect_arrived";

    SMSConfig queryBy(String merchantId, String smsType);

    void add(SMSConfig smsConfig);

    void update(SMSConfig smsConfig);

    List<SMSConfig> queryAllBy(String merchantId);

    List<SMSConfig> queryAll();
}
