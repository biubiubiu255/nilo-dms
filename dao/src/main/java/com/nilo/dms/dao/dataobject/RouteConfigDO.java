package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/12/2.
 */
public class RouteConfigDO extends BaseDo<Long> {

    private Long merchantId;

    private String optType;

    private String routeDescC;

    private String routeDescE;

    private String remark;

    private Integer status;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
