package com.nilo.dms.web.controller;

import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.UserLoginDao;
import com.nilo.dms.common.Principal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLoginDao userLoginDao;

    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    @ResponseBody
    public String index(UsernamePasswordToken token, String randomCode, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        try {
            if (!subject.isAuthenticated()) { // 未登陆的
            /*    // 校验验证码
                String codeValidate = CaptchaUtil.getCode(request);
                if (!StringUtil.equalsIgnoreCase(randomCode, codeValidate)) {
                    throw new DMSException(BizErrorCode.VERIFY_CODE_ERROR);
                }*/
                subject.login(token);
                // 登陆成功,保存用户信息到Session
                Principal principal = (Principal) subject.getPrincipal();

                request.getSession().setAttribute("userId", principal.getUserId());
                request.getSession().setAttribute("userName", principal.getUserName());
                request.getSession().setAttribute("name", principal.getUserName());
                request.getSession().setAttribute("merchantId", principal.getMerchantId());

            }
        } catch (DMSException e1) {
            log.error("Login Failed.account={}", token.getUsername());
            return toJsonErrorMsg(e1.getMessage());
        }
        catch (DisabledAccountException e2) {
            log.error("Login Failed.account={}", token.getUsername());
            return toJsonErrorMsg(BizErrorCode.DISABLED_ACCOUNT.getDescription());
        }
        catch (Exception e) {
            log.error("Login Failed.account={}", token.getUsername());
            return toJsonErrorMsg(BizErrorCode.LOGIN_FAILED.getDescription());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/logout.html", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout(); // already call session.invalidate();
        return "redirect:/";
    }
}
