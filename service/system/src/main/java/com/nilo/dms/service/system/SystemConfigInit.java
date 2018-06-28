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
        systemService.loadingAndRefreshCustomerConfig("1");

        systemService.loadingAndRefreshNetwork();

        systemService.loadingAndRefreshSerialNumRule("1");
        // 加载日志配置
        systemService.loadingAndRefreshLogConfig();

        systemService.loadingAndRefreshSystemCode("1");

        systemService.loadingAndRefreshOrderHandleConfig("1");

        systemService.loadingAndRefreshSMSConfig("1");

        systemService.loadingAndRefreshRouteConfig("1");

        systemService.loadingAndRefreshBizFeeConfig("1");

        systemService.loadingAndRefreshAddressConfig();
    }
}
