/*
 * Copyright 2015-2016 kilimall.com All rights reserved.
* Support: http://www.kilimall.co.ke

 */
package com.nilo.dms.web;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.MenuDO;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.Role;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.org.CompanyService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.service.org.model.Company;
import com.nilo.dms.service.org.model.Staff;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限认证
 *
 * @author deng lei
 * @version 1.0
 */
public class AuthenticationRealm extends AuthorizingRealm {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserNetworkDao userNetworkDao;
    @Autowired
    private StaffService staffService;

    @Override
    protected void onInit() {
        super.onInit();
        // TODO enable auth cache
        // setAuthenticationCachingEnabled(true);
        setCredentialsMatcher(new HashedCredentialsMatcher("SHA1"));
    }

    /**
     * 获取认证信息
     *
     * @param token 令牌
     * @return 认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        String username = String.valueOf(token.getPrincipal());

        if (StringUtils.isNotBlank(username)) {
            User user = userService.findByUsername(username);

            if (user != null) {
                // check user.status
                switch (user.getLoginInfo().getStatus()) {
                    case DISABLED:
                        throw new DisabledAccountException();
                    case FROZEN:
                        throw new LockedAccountException();
                }

                List<String> urlAuthorities = roleService.findUrlPermissionsByUserId(user.getUserId());
                List<String> authorities = roleService.findPermissionsByUserId(user.getUserId());
                List<String> roles = new ArrayList<>();
                List<Role> roleList = roleService.findRolesByUserId(user.getUserId());
                if (roleList != null) {
                    for (Role role : roleList) {
                        roles.add(role.getRoleName());
                    }
                }
                Company company = companyService.findByMerchantId(user.getMerchantId());

                List<UserNetworkDO> userNetworkDOList = userNetworkDao.queryByUserId(Long.parseLong(user.getUserId()));

                Principal principal = new Principal();
                principal.setUserId(user.getUserId());
                principal.setUserName(user.getLoginInfo().getUserName());
                principal.setMerchantId(user.getMerchantId());
                principal.setRoles(roles);
                principal.setAuthorities(authorities);
                principal.setCompanyId(company.getCompanyId());
                principal.setUrlAuthorities(urlAuthorities);
                principal.setNetworks(getUserNetwork(userNetworkDOList));
                Staff staff = staffService.findByStaffId(company.getCompanyId(), user.getLoginInfo().getUserName());
                if (staff != null) {
                    principal.setRider(staff.isRider());
                }
                return new SimpleAuthenticationInfo(principal, user.getLoginInfo().getPassword(), getName());
            }
        }
        throw new UnknownAccountException();
    }

    /**
     * 获取授权信息
     *
     * @param principals principals
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Principal principal = ((Principal) principals.getPrimaryPrincipal());
        if (principal != null) {
            info.addStringPermissions(principal.getAuthorities());
            info.addRoles(principal.getRoles());
            return info;
        }
        return info;
    }

    private List<Integer> getUserNetwork(List<UserNetworkDO> networkDOList) {

        if (networkDOList == null) return null;
        List<Integer> list = new ArrayList<>();
        for (UserNetworkDO networkDO : networkDOList) {
            list.add(networkDO.getDistributionNetworkId().intValue());
        }
        return list;
    }

}