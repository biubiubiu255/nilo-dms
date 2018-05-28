package com.nilo.dms.service;

import com.nilo.dms.common.enums.RoleStatusEnum;
import com.nilo.dms.dto.common.Role;

import java.util.List;


public interface RoleService {

    void save(Role role);

    void update(Role role);

    void updateRoleStatus(String id, RoleStatusEnum roleStatusEnum);

    List<Role> findRolesByUserId(String userId);

    List<String> findPermissionsByUserId(String userId);

    List<String> findUrlPermissionsByUserId(String userId);


    List<Role> findBy(String merchantId, String roleName, RoleStatusEnum status);

    void saveRolePermission(String roleId, List<String> permission);

    Role findById(String roleId);

}
