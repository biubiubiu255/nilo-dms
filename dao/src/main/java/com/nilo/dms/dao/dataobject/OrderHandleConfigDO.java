package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
public class OrderHandleConfigDO extends BaseDo<Long> {
    private Long merchantId;

    private String optType;

    private String allowStatus;

    private String notAllowStatus;

    private Integer updateStatus;

    private String className;

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

    public String getAllowStatus() {
        return allowStatus;
    }

    public void setAllowStatus(String allowStatus) {
        this.allowStatus = allowStatus;
    }

    public String getNotAllowStatus() {
        return notAllowStatus;
    }

    public void setNotAllowStatus(String notAllowStatus) {
        this.notAllowStatus = notAllowStatus;
    }

    public Integer getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
