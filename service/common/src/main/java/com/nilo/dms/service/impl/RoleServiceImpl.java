package com.nilo.dms.service.impl;

import com.nilo.dms.common.enums.RoleStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.RoleDao;
import com.nilo.dms.dao.RolePermissionDao;
import com.nilo.dms.dao.dataobject.RoleDO;
import com.nilo.dms.dao.dataobject.RolePermissionDO;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Override
    @Transactional
    public void save(Role role) {
        role.setStatus(RoleStatusEnum.NORMAL);
        RoleDO roleDO = converToDO(role);
        roleDao.insert(roleDO);
        role.setRoleId("" + roleDO.getId());
    }

    @Override
    public void update(Role role) {
        roleDao.update(converToDO(role));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRoleStatus(String roleId, RoleStatusEnum statusEnum) {
        RoleDO roleDO = new RoleDO();
        roleDO.setId(Long.parseLong(roleId));
        roleDO.setStatus(statusEnum.getCode());
        roleDao.update(roleDO);
    }

    @Override
    public List<Role> findRolesByUserId(String userId) {
        List<Role> roleList = new ArrayList<>();
        List<RoleDO> list = roleDao.findRolesByUserId(Long.parseLong(userId));
        for (RoleDO r : list) {
            Role role = convertToRole(r);
            roleList.add(role);
        }
        return roleList;
    }

    @Override
    public List<String> findPermissionsByUserId(String userId) {
        return roleDao.findPermissionsByUserId(Long.parseLong(userId));
    }

    @Override
    public List<Role> findAllRoles(String merchantId) {
        List<RoleDO> roleDOList = roleDao.findAllRoles(Long.parseLong(merchantId));
        List<Role> list = new ArrayList<>();
        for (RoleDO r : roleDOList) {
            list.add(convertToRole(r));
        }
        return list;
    }

    /**
     * 保存角色权限项
     *
     * @param roleId
     */
    @Override
    public void saveRolePermission(String roleId, List<String> permission) {

        AssertUtil.isNotNull(roleId, BizErrorCode.ROLE_ID_EMPTY);

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
                    //先删后增
                    rolePermissionDao.deleteAllPermissionsByRoleId(Long.parseLong(roleId));
                    List<RolePermissionDO> list = new ArrayList<>();
                    for (String id : permission) {
                        RolePermissionDO rolePermissionDO = new RolePermissionDO();
                        rolePermissionDO.setRoleId(Long.parseLong(roleId));
                        rolePermissionDO.setPermission(id);
                        list.add(rolePermissionDO);
                    }
                    rolePermissionDao.saveRolePermission(list);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });


    }

    @Override
    public Role findById(String roleId) {
        RoleDO roleDO = roleDao.queryById(Long.parseLong(roleId));
        if (roleDO == null) {
            return null;
        }
        return convertToRole(roleDO);
    }

    private Role convertToRole(RoleDO roleDO) {
        Role role = new Role();
        role.setRoleName(roleDO.getRoleName());
        role.setDescription(roleDO.getDescription());
        role.setStatus(RoleStatusEnum.getEnum(roleDO.getStatus()));
        role.setRoleId("" + roleDO.getId());
        role.setCreatedTime(roleDO.getCreatedTime());
        return role;
    }


    private RoleDO converToDO(Role role) {
        RoleDO roleDO = new RoleDO();
        if (StringUtil.isNotEmpty(role.getRoleId())) {
            roleDO.setId(Long.parseLong(role.getRoleId()));
        }
        roleDO.setDescription(role.getDescription());
        roleDO.setRoleName(role.getRoleName());
        if (StringUtil.isNotEmpty(role.getMerchantId())) {
            roleDO.setMerchantId(Long.parseLong(role.getMerchantId()));
        }
        if (role.getStatus() != null) {
            roleDO.setStatus(role.getStatus().getCode());
        }
        return roleDO;
    }

}
