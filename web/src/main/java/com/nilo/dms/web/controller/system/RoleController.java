/*
 * Copyright 2015-2016 kilimall.com All rights reserved.
* Support: http://www.kilimall.co.ke

 */
package com.nilo.dms.web.controller.system;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.RoleStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.PermissionService;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.model.Role;
import com.nilo.dms.service.model.ZTree;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;


    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "role/list";
    }


    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getRoleList(String roleName) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<Role> list = roleService.findBy(merchantId,roleName,null);
        Pagination page = new Pagination(0, list.size());
        return toPaginationLayUIData(page, list);
    }


    @ResponseBody
    @RequestMapping("/delRole.html")
    public String delete(String roleId) {
        try {
            AssertUtil.isNotBlank(roleId, BizErrorCode.ROLE_ID_EMPTY);
            //设置为不可用
            roleService.updateRoleStatus(roleId, RoleStatusEnum.DISABLED);
        } catch (Exception e) {
            log.error("Del Role failed. roleId:{}", roleId, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping("/activeRole.html")
    public String activeRole(String roleId) {
        try {
            AssertUtil.isNotBlank(roleId, BizErrorCode.ROLE_ID_EMPTY);
            //设置为可用
            roleService.updateRoleStatus(roleId, RoleStatusEnum.NORMAL);
        } catch (Exception e) {
            log.error("Del Role failed. roleId:{}", roleId, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }


    @RequestMapping("/editRolePage.html")
    public String edit(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtil.isNotEmpty(id)) {
            Role role = roleService.findById(id);
            model.addAttribute("id", role.getRoleId());
            model.addAttribute("roleName", role.getRoleName());
            model.addAttribute("description", role.getDescription());
        }
        return "role/edit";
    }

    @ResponseBody
    @RequestMapping("/permissionTree.html")
    public String permissionTree(Integer id) {

        List<Integer> list = new ArrayList<>();
        list.add(id);
        List<ZTree> treeList = permissionService.permissionTreeByRole(list);
        return toJsonTrueData(treeList);
    }

    @ResponseBody
    @RequestMapping("/editRole.html")
    public String editRole(String id, String roleName, String description, @RequestParam(value = "permissions[]") String[] permissions) {

        Role role = new Role();
        role.setRoleId(id);
        role.setRoleName(roleName);
        role.setDescription(description);
        try {
            if (StringUtil.isNotEmpty(id)) {
                //更新角色信息
                roleService.update(role);
            } else {
                Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
                //获取merchantId
                String merchantId = me.getMerchantId();
                role.setMerchantId(merchantId);
                roleService.save(role);
                id = role.getRoleId();
            }
            roleService.saveRolePermission(id, Arrays.asList(permissions));
        } catch (Exception e) {
            log.error("Edit Role failed. roleId:{}", id, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

}