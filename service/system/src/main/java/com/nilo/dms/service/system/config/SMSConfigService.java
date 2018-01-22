package com.nilo.dms.service.system.config;


import com.nilo.dms.service.system.model.SMSConfig;

import java.util.List;

/**
 * Created by admin on 2017/9/22.
 */
public interface SMSConfigService {

    SMSConfig queryBy(String merchantId, String smsType);

    void add(SMSConfig smsConfig);

    void update(SMSConfig smsConfig);

    List<SMSConfig> queryAllBy(String merchantId);

    List<SMSConfig> queryAll();
}
