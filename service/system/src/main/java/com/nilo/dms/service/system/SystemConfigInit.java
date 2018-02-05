package com.nilo.dms.service.system;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ronny on 2017/6/9.
 */
@Component
public class SystemConfigInit implements InitializingBean {

    @Autowired
    private SystemService systemService;
    

    @Override
    public void afterPropertiesSet() {

        // 加载客户配置信息
        systemService.loadingAndRefreshCustomerConfig(null);
        // 加载流水号规则
        systemService.loadingAndRefreshSerialNumRule(null);
        // 加载日志配置
        systemService.loadingAndRefreshLogConfig();

        systemService.loadingAndRefreshSystemCode(null);

        systemService.loadingAndRefreshOrderHandleConfig(null);

        systemService.loadingAndRefreshSMSConfig(null);

        systemService.loadingAndRefreshRouteConfig(null);

        systemService.loadingAndRefreshBizFeeConfig(null);

        systemService.loadingAndRefreshAddressConfig();
    }
}
