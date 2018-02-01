package com.nilo.dms.web.controller.system;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.RoleStatusEnum;
import com.nilo.dms.common.enums.UserStatusEnum;
import com.nilo.dms.common.enums.UserTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.model.Role;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.model.test.Express;
import com.nilo.dms.service.system.DistributionNetworkService;
import com.nilo.dms.service.system.model.DistributionNetwork;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserNetworkDao userNetworkDao;

    @Autowired
    private DistributionNetworkService distributionNetworkService;
    
    
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "user/list";
    }

    
    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getUserList(String username) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<User> list = userService.findUserPageBy(merchantId, username, page);
        return toPaginationLayUIData(page, list);
    }

    
    
    //修改密码
    @RequestMapping("/passwordView.html")
    public String passwordView(Model model) {
        return "user/passwordView";
    }
    
    
    //添加字段页面
    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(Model model) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<Role> roleList = roleService.findBy(merchantId,"",RoleStatusEnum.NORMAL);


        Pagination page = new Pagination(0, 100);
        List<DistributionNetwork> distributionList = distributionNetworkService.queryBy(merchantId, null, page);
        model.addAttribute("distributionList", distributionList);
        model.addAttribute("roleList", roleList);
        return "user/add";
    }

    //添加用户参数，以及写入
    @ResponseBody
    @RequestMapping(value = "/addUserInfo.html", method = RequestMethod.POST)
    public String addUserInfo(UserInfo userInfo, String userName, Long[] roleIds, Long[] networks) {

        try {
            AssertUtil.isNotNull(userInfo, SysErrorCode.REQUEST_IS_NULL);

            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            userInfo.setMerchantId(merchantId);

            User user = new User();
            user.setMerchantId(merchantId);
            user.setUserInfo(userInfo);

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setMerchantId(merchantId);
            loginInfo.setUserType(UserTypeEnum.NORMAL);
            loginInfo.setUserName(userName);
            loginInfo.setPassword(DigestUtils.sha1Hex("12345678"));
            loginInfo.setStatus(UserStatusEnum.NORMAL);
            user.setLoginInfo(loginInfo);

            userService.addUser(user);
            userService.updateUserRoles(merchantId, user.getUserId(), roleIds);
            userService.updateUserNetwork(merchantId, user.getUserId(), networks);
        } catch (Exception e) {
            logger.error("updateUser failed.", e);
            return toJsonErrorMsg("Failed." + e.getMessage());
        }
        return toJsonTrueMsg();
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String editPage(Model model, String userId) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        User user = userService.findByUserId(merchantId, userId);
        model.addAttribute("user", user);
        model.addAttribute("userRoles", roleService.findRolesByUserId(userId));
        model.addAttribute("userNetworks", userNetworkDao.queryByUserId(Long.parseLong(userId)));
        Pagination page = new Pagination(0, 100);
        List<DistributionNetwork> distributionList = distributionNetworkService.queryBy(user.getMerchantId(),null, page);
        model.addAttribute("distributionList", distributionList);

        List<Role> roleList = roleService.findBy(merchantId,"",RoleStatusEnum.NORMAL);
        //去掉不可用的角色
        Iterator<Role> it = roleList.iterator();
        while (it.hasNext()) {
            Role x = it.next();
            if (RoleStatusEnum.DISABLED.getCode() == x.getStatus().getCode()) {
                it.remove();
            }
        }
        model.addAttribute("roleList", roleList);
        return "user/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserInfo.html", method = RequestMethod.POST)
    public String updateUserInfo(UserInfo userInfo, Long[] roleIds, Long[] networks) {

        try {
            AssertUtil.isNotNull(userInfo, SysErrorCode.REQUEST_IS_NULL);

            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            userInfo.setMerchantId(merchantId);
            //更新用户基本信息
            userService.updateUserInfo(userInfo);
            //更新用户角色
            userService.updateUserRoles(merchantId, userInfo.getUserId(), roleIds);
            userService.updateUserNetwork(merchantId, userInfo.getUserId(), networks);


        } catch (Exception e) {
            logger.error("updateUser failed.", e);
            return toJsonErrorMsg("Failed." + e.getMessage());
        }

        return toJsonTrueMsg();

    }

    @ResponseBody
    @RequestMapping(value = "/changePassword.html", method = RequestMethod.POST)
    public String changePassword(String oldPassword, String newPassword) {
        try {
            AssertUtil.isNotNull(user, SysErrorCode.REQUEST_IS_NULL);
            AssertUtil.isNotBlank(oldPassword, SysErrorCode.REQUEST_IS_NULL);
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            String userId = me.getUserId();
            //校验旧密码
            User user = userService.findByUserId(me.getMerchantId(), userId);
            if (!StringUtils.equals(DigestUtils.sha1Hex(oldPassword), user.getLoginInfo().getPassword())) {
                throw new DMSException(BizErrorCode.OLD_PASSWORD_ERROR);
            }

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(userId);
            loginInfo.setMerchantId(me.getMerchantId());
            loginInfo.setPassword(DigestUtils.sha1Hex(newPassword));
            userService.updateLoginInfo(loginInfo);
        } catch (Exception e) {
            logger.error("changePassword failed. ", e);
            return toJsonErrorMsg("Failed:" + e.getMessage());
        }
        return toJsonTrueMsg();

    }

    @ResponseBody
    @RequestMapping(value = "/resetPassword.html", method = RequestMethod.POST)
    public String resetPassword(String userId) {
        try {
            AssertUtil.isNotNull(userId, BizErrorCode.USER_ID_ILLEGAL);
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            String merchantId = me.getMerchantId();
            //更新密码
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(userId);
            loginInfo.setMerchantId(merchantId);
            loginInfo.setPassword(DigestUtils.sha1Hex("12345678"));
            userService.updateLoginInfo(loginInfo);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();

    }

    @ResponseBody
    @RequestMapping(value = "/changeProfile.html", method = RequestMethod.POST)
    public String changeProfile(User user) {
        /*User me = WebUtils.getCurrentUser();
        user.setId(me.getId());
        user.setEmail(user.getEmail());
        return toJsonMsg("success", userService.update(user));*/
        return "";
    }


    @ResponseBody
    @RequestMapping("/delUser.html")
    public String delUser(String userId) {
        try {
            AssertUtil.isNotNull(userId, BizErrorCode.USER_ID_ILLEGAL);
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            String merchantId = me.getMerchantId();
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(userId);
            loginInfo.setMerchantId(merchantId);
            loginInfo.setStatus(UserStatusEnum.DISABLED);
            userService.updateLoginInfo(loginInfo);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping("/activeUser.html")
    public String activeUser(String userId) {
        try {
            AssertUtil.isNotNull(userId, BizErrorCode.USER_ID_ILLEGAL);
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            String merchantId = me.getMerchantId();
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(userId);
            loginInfo.setMerchantId(merchantId);
            loginInfo.setStatus(UserStatusEnum.NORMAL);
            userService.updateLoginInfo(loginInfo);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }


    @ResponseBody
    @RequestMapping("/usernameExists.html")
    public String usernameExists(String username) {
        boolean exist = userService.usernameExists(username);
        if (exist) {
            return toJsonErrorMsg("");
        }
        return toJsonTrueMsg();
    }

}
