package com.nilo.dms.service.system;

/**
 * 系统服务
 * Created by ronny on 2017/7/6.
 */
public interface SystemService {

    void loadingAndRefreshCustomerConfig(String merchantId);

    void loadingAndRefreshNetwork();

    void loadingAndRefreshSerialNumRule(String merchantId);

    void loadingAndRefreshLogConfig();

    void loadingAndRefreshSystemCode(String merchantId);

    void loadingAndRefreshOrderHandleConfig(String merchantId);

    void loadingAndRefreshSMSConfig(String merchantId);

    void loadingAndRefreshRouteConfig(String merchantId);

    void loadingAndRefreshBizFeeConfig(String merchantId);

    void loadingAndRefreshAddressConfig();

}
