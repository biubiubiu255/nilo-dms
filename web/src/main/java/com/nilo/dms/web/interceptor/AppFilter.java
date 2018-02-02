package com.nilo.dms.web.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;


/**
 * AppFilter
 *
 */
public class AppFilter extends AuthenticatingFilter {

	public static final String TOKEN = "token";

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;

	    String token = req.getParameter(TOKEN);
	    if (isAccess(token)) {
	      return onAccessSuccess(req, (HttpServletResponse) response);
	    }

	    return onAccessFail(req, (HttpServletResponse) response);
	}
	
	 /**
	   * 判断token的合法性
	   * 
	   * @param token
	   * @return
	   */
	  public  boolean isAccess(String token) {
		  return true;
	  }

	  /**
	   * 认证成功进行的操作处理
	   * 
	   * @param request
	   * @param response
	   * @return true 继续后续处理，false 不需要后续处理
	   */
	  public  boolean onAccessSuccess(HttpServletRequest request,
	      HttpServletResponse response) {
		  return true;
	  }

	  /**
	   * 认证失败时处理结果
	   * 
	   * @param request
	   * @param response
	   * @return true 继续后续处理，false 不需要后续处理
	   */
	  public  boolean onAccessFail(HttpServletRequest request,
	      HttpServletResponse response) {
		  return false;
	  }
}
