package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.common.utils.WebUtil;
import com.nilo.dms.dao.DeliverAgendaDao;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.DeliverAgendaDO;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import com.nilo.dms.dto.common.Role;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.org.Company;
import com.nilo.dms.dto.org.Staff;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.org.CompanyService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2017/11/22.
 */
@Controller
@RequestMapping("/mobile")
public class MobileHomeController extends BaseController {
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
    @Autowired
    private DeliverAgendaDao deliverAgendaDao;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/home.html")
    public String toIndexPage(Model model) {
        Principal principal = SessionLocal.getPrincipal();
        model.addAttribute("isRider", principal.isRider());
        return "mobile/home";
    }

    @RequestMapping(value = "/login.html")
    public String index(String username, String password, String randomCode, HttpServletRequest request) {

        AssertUtil.isNotBlank(username, BizErrorCode.USER_NAME_EMPTY);
        AssertUtil.isNotBlank(password, BizErrorCode.PASSWORD_EMPTY);
        HttpSession session = request.getSession();
        try {

            /*Object sessionUser = WebUtil.getHttpSessionValue("session_user");
            if (sessionUser != null) {
            	return "redirect:/mobile/home.html";
            }*/
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("username not exist.");
            } else if (!StringUtil.equals(DigestUtils.sha1Hex(password), user.getLoginInfo().getPassword())) {
                throw new DMSException(BizErrorCode.PASSWORD_ERROR);
            }
            // check user.status
            switch (user.getLoginInfo().getStatus()) {
                case DISABLED:
                case FROZEN:
                    throw new RuntimeException("Account status not allowed");
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
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return "redirect:/mobile/home.html";
    }

    private List<Integer> getUserNetwork(List<UserNetworkDO> networkDOList) {

        if (networkDOList == null) return null;
        List<Integer> list = new ArrayList<>();
        for (UserNetworkDO networkDO : networkDOList) {
            list.add(networkDO.getDistributionNetworkId().intValue());
        }
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getTaskReport.html")
    public String getTaskReport(String riderNo) {
        Principal principal = SessionLocal.getPrincipal();

        Map<String, DeliverAgendaDO> map = new HashMap<String, DeliverAgendaDO>();

        Calendar c = Calendar.getInstance();

        //取昨天的数据，参数：1.当前快递员id 2.要查询的日期，例如：20170503
        String dateFormat = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        map.put("day", deliverAgendaDao.queryReport(principal.getUserId(), dateFormat));

        c.add(Calendar.DATE, -1);
        dateFormat = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        map.put("yesterday", deliverAgendaDao.queryReport(principal.getUserId(), dateFormat));

        c.add(Calendar.DATE, -1);
        dateFormat = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        map.put("beforeYesterday", deliverAgendaDao.queryReport(principal.getUserId(), dateFormat));

        return toJsonTrueData(map);
    }

}
