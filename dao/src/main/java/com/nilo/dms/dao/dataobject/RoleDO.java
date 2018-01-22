package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * 角色
 */
public class RoleDO extends BaseDo<Long> {

    private Long merchantId;
    private String roleName;
    private String description;

    /**
     * 状态 0--正常 1--已删除 2--冻结
     */
    private Integer status;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof RoleDO) {
            RoleDO r = (RoleDO) obj;
            return this.getId().equals(r.getId());
        }
        return false;
    }
}
