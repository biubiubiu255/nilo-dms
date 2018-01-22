package com.nilo.dms.service.impl;

import com.nilo.dms.dao.PermissionDao;
import com.nilo.dms.dao.RoleDao;
import com.nilo.dms.dao.dataobject.PermissionDO;
import com.nilo.dms.service.PermissionService;
import com.nilo.dms.service.model.ZTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ronny on 2017/8/24.
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 根据角色查询权限树结构
     *
     * @param roles
     * @return
     */
    @Override
    public List<ZTree> permissionTreeByRole(List<Integer> roles) {

        List<ZTree> list = new ArrayList<>();

        if (roles == null) return list;

        Set<String> permList = new HashSet<>();
        for (Integer roleId : roles) {
            List<String> tmp = roleDao.findPermissionsByRoleId(roleId);
            permList.addAll(tmp);
        }
        List<PermissionDO> permissionDOList = permissionDao.findAll();


        if (permissionDOList != null) {
            for (PermissionDO p : permissionDOList) {
                ZTree t = new ZTree();
                t.setName(p.getName());
                t.setValue(p.getValue());
                t.setId("" + p.getId());
                t.setpId("" + p.getpId());
                t.setOpen(p.getIsMenu() == 1 ? true : false);
                t.setIsParent(p.getIsMenu() == 1 ? true : false);
                t.setChecked(permList.contains(p.getValue()));
                list.add(t);
            }
        }
        return list;
    }

}
