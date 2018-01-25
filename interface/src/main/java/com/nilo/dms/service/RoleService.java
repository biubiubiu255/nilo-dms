package com.nilo.dms.service;

import com.nilo.dms.common.enums.RoleStatusEnum;
import com.nilo.dms.service.model.Role;

import java.util.List;


public interface RoleService {

    /**
     * 保存角色信息
     *
     * @param role
     * @return
     */
    void save(Role role);

    /**
     * 更新角色信息
     *
     * @param role
     * @return
     */
    void update(Role role);

    /**
     * @param id
     * @return
     */
    void updateRoleStatus(String id, RoleStatusEnum roleStatusEnum);

    /**
     * 通过用户id 查询用户角色集合
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<Role> findRolesByUserId(String userId);

    /**
     * 通过用户id 查询用户权限集合
     *
     * @param userId
     * @return
     */
    List<String> findPermissionsByUserId(String userId);

    /**
     * 通过用户id 查询用户url权限集合
     *
     * @param userId
     * @return
     */
    List<String> findUrlPermissionsByUserId(String userId);


    List<Role> findBy(String merchantId, String roleName, RoleStatusEnum status);

    /**
     * 保存角色权限项
     *
     * @param roleId
     */
    void saveRolePermission(String roleId, List<String> permission);

    Role findById(String roleId);

}
