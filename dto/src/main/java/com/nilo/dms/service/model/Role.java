package com.nilo.dms.service.model;

import com.nilo.dms.common.enums.RoleStatusEnum;

/**
 * 角色
 */
public class Role {

    private static final long serialVersionUID = -3378484184068204348L;

    private String roleId;

    private String merchantId;

    private String roleName;

    private String description;

    /**
     * 状态 0--正常 1--已删除 2--冻结
     */
    private RoleStatusEnum status;

    private Long createdTime;

    private Long updatedTime;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
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

    public RoleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RoleStatusEnum status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return this.status.getCode();
    }

}
