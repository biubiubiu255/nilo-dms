package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * 用户信息
 */
public class UserNetworkDO extends BaseDo<Long> {

    private Long userId;

    private Long distributionNetworkId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDistributionNetworkId() {
        return distributionNetworkId;
    }

    public void setDistributionNetworkId(Long distributionNetworkId) {
        this.distributionNetworkId = distributionNetworkId;
    }
}
