package com.nilo.dms.web.interceptor;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.impl.SessionLocal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by admin on 2018/4/10.
 */
public class SessionFilter implements Filter {
    private String loginUrl = "/login.jsp";
    private static final String AJAX_HEADER_KEY = "X-Requested-With";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        //排除登录请求
        if (uri.indexOf("api") != -1 || Constant.ALLOW_URL.indexOf(uri) != -1) {
            chain.doFilter(req, res);
            return;
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("session_user") != null) {
            SessionLocal.setPrincipal((Principal) session.getAttribute("session_user"));
        } else {
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

}
