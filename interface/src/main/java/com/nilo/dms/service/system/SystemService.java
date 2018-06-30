package com.nilo.dms.service.system;

/**
 * 系统服务
 * Created by ronny on 2017/7/6.
 */
public interface SystemService {

    void loadingAndRefreshCustomerConfig();

    void loadingAndRefreshNetwork();

    void loadingAndRefreshSerialNumRule();

    void loadingAndRefreshLogConfig();

    void loadingAndRefreshSystemCode();

    void loadingAndRefreshOrderHandleConfig();

    void loadingAndRefreshSMSConfig();

    void loadingAndRefreshRouteConfig();

    void loadingAndRefreshBizFeeConfig();

    void loadingAndRefreshAddressConfig();

}
