package com.nilo.dms.service.system.config;

import com.nilo.dms.service.system.model.InterfaceConfig;

import java.util.List;

/**
 * Created by admin on 2017/11/9.
 */
public interface InterfaceConfigService {

    List<InterfaceConfig> queryAll(String merchantId);

    void update(InterfaceConfig interfaceConfig);
}
