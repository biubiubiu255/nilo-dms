package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/26.
 */
public class FlowMovingDO extends BaseDo<Long> {

    private String node;

    private String applyType;

    private String apply;

    private String transition;

    private Long handlerUser;

    private Integer status;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public Long getHandlerUser() {
        return handlerUser;
    }

    public void setHandlerUser(Long handlerUser) {
        this.handlerUser = handlerUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
