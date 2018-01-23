package com.nilo.dms.web.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;


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
        uri = uri.substring(1, uri.length());//去掉uri前面的‘/’
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        
        //通过当前uri和Principal中授权的url比较，判断是否有权限
        if(me!=null) {
        	List<String> urlAuthorities = me.getUrlAuthorities();
            if(Constant.ALLOW_URL.indexOf(uri)==-1&&urlAuthorities.indexOf(uri)==-1) {
            	//ErrorCode resutlCode = DEFAULT_ERROR_KEY_ATTRIBUTE_NAME
            	throw new DMSException(BizErrorCode.USER_NAME_EXIST);
            }
        }
        
        return CLIENT_HOST_WHITE_LIST.equals(clientHost) || super.isAccessAllowed(request, response, mappedValue);
    }
}
