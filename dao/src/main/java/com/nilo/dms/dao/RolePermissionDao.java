package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.RolePermissionDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionDao extends BaseDao<Long, RolePermissionDO> {

    /**
     * 删除角色关联权限
     *
     * @param roleId
     */
    void deleteAllPermissionsByRoleId(Long roleId);


    /**
     * 保存角色关联权限
     */
    void saveRolePermission(List<RolePermissionDO> list);
}
