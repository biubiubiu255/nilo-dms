package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.RoleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据库访问层接口
 *
 * @author cukor
 */

@Repository
public interface RoleDao extends BaseDao<Long, RoleDO> {

    /**
     * 查询所有角色信息
     *
     * @return
     */
    List<RoleDO> findBy(@Param("merchantId")Long merchantId,@Param("roleName")String roleName,@Param("status") Integer status);


    /**
     * 删除用户所有关联角色
     *
     * @param userId
     */
    void deleteAll(long userId);



    /**
     * 保存角色关联权限
     *
     * @param userId
     */
    void insertAll(@Param("userId") Long userId,@Param("roles") Long[] roles);

    /**
     * 通过用户id 查询用户拥有的角色
     *
     * @param userId
     * @return List<RoleDO>
     */
    List<RoleDO> findRolesByUserId(Long userId);

    /**
     * 通过用户id 查询用户权限集合
     *
     * @param userId
     * @return
     */
    List<String> findPermissionsByUserId(Long userId);
    
    /**
     * 通过用户id 查询用户Url权限集合
     *
     * @param userId
     * @return
     */
    List<String> findUrlPermissionsByUserId(Long userId);

    /**
     * 通过角色id 查询角色权限集合
     *
     * @param roleId
     * @return
     */
    List<String> findPermissionsByRoleId(Integer roleId);


    /**
     * 根据角色id 获取该角色下的权限个数
     *
     * @param roleId
     * @return
     */
    Integer findPermissionCountByRole(Integer roleId);
}
