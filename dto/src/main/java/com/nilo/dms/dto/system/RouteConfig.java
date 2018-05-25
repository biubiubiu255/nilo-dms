package com.nilo.dms.dto.system;

import com.nilo.dms.common.enums.RouteConfigStatusEnum;

/**
 * Created by admin on 2017/12/2.
 */
public class RouteConfig {

    private String merchantId;

    private String optType;

    private String routeDescC;

    private String routeDescE;

    private String remark;

    private RouteConfigStatusEnum status;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getRouteDescC() {
        return routeDescC;
    }

    public void setRouteDescC(String routeDescC) {
        this.routeDescC = routeDescC;
    }

    public String getRouteDescE() {
        return routeDescE;
    }

    public void setRouteDescE(String routeDescE) {
        this.routeDescE = routeDescE;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RouteConfigStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RouteConfigStatusEnum status) {
        this.status = status;
    }
}
