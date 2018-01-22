package com.nilo.dms.web.interceptor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.nilo.dms.common.Principal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * AuthenticationFilter
 *
 * @author spance
 * @version 2016/8/23
 */
public class AuthenticationFilter extends FormAuthenticationFilter {

    private static final String AJAX_HEADER_KEY = "X-Requested-With";

    private static final String CLIENT_HOST_WHITE_LIST = "127.0.0.8";

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        boolean isAjax = req.getHeader(AJAX_HEADER_KEY) != null;
        if (isAjax) {
            HttpServletResponse resp = ((HttpServletResponse) response);
            resp.setHeader("Redirect", req.getContextPath() + getLoginUrl());
            resp.setStatus(403);
        } else {
            super.saveRequestAndRedirectToLogin(request, response);
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String clientHost = request.getRemoteHost();

        String uri = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //通过当前uri和Principal中授权的url比较，判断是否有权限
        
        
        
        return CLIENT_HOST_WHITE_LIST.equals(clientHost) || super.isAccessAllowed(request, response, mappedValue);
    }
}
