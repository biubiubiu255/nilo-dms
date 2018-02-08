package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.model.User;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Controller
@RequestMapping("/mobile/password")
public class ModifyPasswordController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toPage.html")
    public String toPage() { return "mobile/basic/modifypassword"; }

    @RequestMapping(value = "/submit.html")
    @ResponseBody
    public String submit(String oldPassword,String newPassword,String againPassword ) {
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
//        System.out.println(oldPassword);
//        System.out.println(newPassword);
//        System.out.println(againPassword);
//        return "true";
    }
}
