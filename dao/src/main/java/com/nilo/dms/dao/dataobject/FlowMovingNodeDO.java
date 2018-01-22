package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * Created by admin on 2017/9/26.
 */
public class FlowMovingNodeDO extends BaseDo<Long> {

    private String nodeId;

    private String nodeName;

    private String nodeDesc;

    private String flowId;

    private String handlerRole;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getHandlerRole() {
        return handlerRole;
    }

    public void setHandlerRole(String handlerRole) {
        this.handlerRole = handlerRole;
    }
}
