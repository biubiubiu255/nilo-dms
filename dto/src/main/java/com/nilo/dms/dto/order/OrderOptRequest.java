package com.nilo.dms.dto.order;

import com.nilo.dms.common.enums.OptTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/3.
 */
public class OrderOptRequest {

    private List<String> orderNo;

    private OptTypeEnum optType;

    private String rider;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public List<String> getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(List<String> orderNo) {
        this.orderNo = orderNo;
    }

    public OptTypeEnum getOptType() {
        return optType;
    }

    public void setOptType(OptTypeEnum optType) {
        this.optType = optType;
    }

}
