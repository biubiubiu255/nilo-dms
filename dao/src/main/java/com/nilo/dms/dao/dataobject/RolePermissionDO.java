package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * 角色权限
 */
public class RolePermissionDO extends BaseDo<Integer> {

    private Long roleId;

    private String permission;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
