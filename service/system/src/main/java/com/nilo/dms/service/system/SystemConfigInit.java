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
        systemService.loadingAndRefreshCustomerConfig();

        systemService.loadingAndRefreshNetwork();

        systemService.loadingAndRefreshSerialNumRule();
        // 加载日志配置
        systemService.loadingAndRefreshLogConfig();

        systemService.loadingAndRefreshSystemCode();

        systemService.loadingAndRefreshOrderHandleConfig();

        systemService.loadingAndRefreshSMSConfig();

        systemService.loadingAndRefreshRouteConfig();

        systemService.loadingAndRefreshBizFeeConfig();

        systemService.loadingAndRefreshAddressConfig();
    }
}
