package com.nilo.dms.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.JWTUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/4/10.
 */
public class SessionFilter implements Filter {
    private String loginUrl = "/login.jsp";
    private static final String AJAX_HEADER_KEY = "X-Requested-With";

    @Autowired
    private UserService userService;

    @Autowired
    private UserNetworkDao userNetworkDao;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        //排除登录请求
        if (uri.indexOf("api") != -1 || Constant.ALLOW_URL.indexOf(uri) != -1) {
            chain.doFilter(req, res);
            return;
        }

        if (uri.indexOf("/app_api") != -1) {
            String token = request.getParameter("m_token");
            if (StringUtil.isEmpty(token)){
                throw new DMSException(BizErrorCode.TOKEN_EMPTY);
            }
            if(!JWTUtil.isValidTokenJWT(token)){
                throw new DMSException(BizErrorCode.TOKEN_ERROR);
            }
            Claims claims = JWTUtil.parseTokenJWT(token);
            String userId = claims.getSubject();

            Principal principal = new Principal();
            String app_curr_user = RedisUtil.hget("app_curr_user", userId);

            if (app_curr_user==null){
                User byUserId = userService.findByUserId(null, userId);
                List<UserNetworkDO> userNetworkDOList = userNetworkDao.queryByUserId(Long.parseLong(byUserId.getUserId()));
                if (userNetworkDOList == null || userNetworkDOList.size() == 0) {
                    throw new DMSException(BizErrorCode.NOT_FOUND_NEXTWORK);
                }
                principal.setUserId(byUserId.getUserId());
                principal.setMerchantId(byUserId.getMerchantId());
                principal.setUserName(byUserId.getUserInfo().getName());
                principal.setNetworks(getUserNetwork(userNetworkDOList));
                RedisUtil.hset("app_curr_user", userId, JSON.toJSONString(principal));
            }else {
                principal = JSON.parseObject(app_curr_user, Principal.class);
            }

            SessionLocal.setPrincipal(principal);
            return;
        }


        HttpSession session = request.getSession();
        if (session.getAttribute("session_user") != null) {
            SessionLocal.setPrincipal((Principal) session.getAttribute("session_user"));
        }else {
            boolean isAjax = request.getHeader(AJAX_HEADER_KEY) != null;
            HttpServletResponse resp = ((HttpServletResponse) res);
            if (isAjax) {
                resp.setHeader("Redirect", request.getContextPath() + loginUrl);
                resp.setStatus(403);
                return;
            } else {
                resp.sendRedirect(loginUrl);
                return;
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

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
