package com.nilo.dms.web.controller.mobile;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AreaDao;
import com.nilo.dms.dao.dataobject.AreaDO;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/22.
 */
@Controller
@RequestMapping("/mobile/DemoController")
public class DemoController  extends BaseController {
//    @Autowired
//    private AreaDao areaDao;
private final Logger log = LoggerFactory.getLogger(getClass());
    @RequestMapping(value = "/toIndexPage.html")
    public String toIndexPage(Model model) {
        System.out.println("-------------------------------");
        return "mobile/sindex";
    }

    @RequestMapping(value = "/test.html",method = RequestMethod.POST)
    @ResponseBody
    public String test(UsernamePasswordToken token, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        try {
            if (!subject.isAuthenticated()) {
                System.out.println("-----------------------------------------");
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

    @RequestMapping(value = "/toLoginPage.html")
    public String list(Model model) {
        return "mobile/list";
    }

}
