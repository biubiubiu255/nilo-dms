package com.nilo.dms.web.controller;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.common.utils.WebUtil;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.Role;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.org.CompanyService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.service.org.model.Company;
import com.nilo.dms.service.org.model.Staff;
import io.leopard.web.captcha.CaptchaUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

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

    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    @ResponseBody
    public String index(String username, String password, String randomCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {

            AssertUtil.isNotBlank(username, BizErrorCode.USER_NAME_EMPTY);
            AssertUtil.isNotBlank(password, BizErrorCode.PASSWORD_EMPTY);

            Object sessionUser = WebUtil.getHttpSessionValue("session_user");
            if (sessionUser != null) {
                return toJsonTrueMsg();
            }

            //登录次数超过3次，即开始验证码
            if (session.getAttribute("login_number") == null) session.setAttribute("login_number", 1);
            int login_number = (int) session.getAttribute("login_number");
            if (login_number++ > 3) {
                // 校验验证码
                String codeValidate = CaptchaUtil.getCode(request);
                if (!StringUtil.equalsIgnoreCase(randomCode, codeValidate)) {
                    throw new DMSException(BizErrorCode.VERIFY_CODE_ERROR);
                }
            }
            session.setAttribute("login_number", login_number);
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("username not exist.");
            } else if (!StringUtil.equals(DigestUtils.sha1Hex(password), user.getLoginInfo().getPassword())) {
                throw new DMSException(BizErrorCode.PASSWORD_ERROR);
            }
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
            WebUtil.setHttpSessionValue("session_user", principal);

            // 登陆成功,保存用户信息到Session
            session.setAttribute("userId", principal.getUserId());
            session.setAttribute("userName", principal.getUserName());
            session.setAttribute("name", principal.getUserName());
            session.setAttribute("merchantId", principal.getMerchantId());
        } catch (DMSException e1) {
            return toJsonErrorMsg(e1.getMessage());
        } catch (Exception e) {
            return toJsonErrorMsg(BizErrorCode.LOGIN_FAILED.getDescription());
        }

        return toJsonTrueMsg();

    }

    @RequestMapping(value = "/logout.html", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        // 登出操作
        WebUtil.setHttpSessionValue("session_user", null);
        return "redirect:/";
    }

    @RequestMapping(value = "/verify.html", method = RequestMethod.GET)
    @ResponseBody
    public String verify(HttpSession session) {
        if (session.getAttribute("login_number") == null || (int) session.getAttribute("login_number") < 4) {
            return toJsonErrorMsg("");
        }
        return toJsonTrueMsg();
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
