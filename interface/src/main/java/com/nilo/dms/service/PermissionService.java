package com.nilo.dms.service;

import com.nilo.dms.service.model.ZTree;

import java.util.List;

public interface PermissionService {
    /**
     * 根据角色查询权限树结构
     *
     * @param roles
     * @return
     */
    List<ZTree> permissionTreeByRole(List<Integer> roles);
}
