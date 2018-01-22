package com.nilo.dms.service.system.model;

import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
public class OrderHandleConfig {

    private String merchantId;

    private String optType;

    private List<Integer> allowStatus;

    private List<Integer> notAllowStatus;


    private Integer updateStatus;

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

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

    public List<Integer> getAllowStatus() {
        return allowStatus;
    }

    public void setAllowStatus(List<Integer> allowStatus) {
        this.allowStatus = allowStatus;
    }

    public List<Integer> getNotAllowStatus() {
        return notAllowStatus;
    }

    public void setNotAllowStatus(List<Integer> notAllowStatus) {
        this.notAllowStatus = notAllowStatus;
    }

    public Integer getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getUpdateStatusDesc() {
        return updateStatus == null ? "" : DeliveryOrderStatusEnum.getEnum(updateStatus).getDesc();
    }

    public List<String> getAllowStatusDesc() {
        List<String> desc = new ArrayList<>();
        if (allowStatus != null) {
            for (Integer s : allowStatus) {
                desc.add(DeliveryOrderStatusEnum.getEnum(s).getDesc());
            }
        }
        return desc;
    }

    public List<String> getNotAllowStatusDesc() {
        List<String> desc = new ArrayList<>();
        if (notAllowStatus != null) {
            for (Integer s : notAllowStatus) {
                desc.add(DeliveryOrderStatusEnum.getEnum(s).getDesc());
            }
        }
        return desc;
    }
}
