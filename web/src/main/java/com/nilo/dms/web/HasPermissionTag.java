package com.nilo.dms.web;

import com.nilo.dms.service.impl.SessionLocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.List;

/**
 * Created by admin on 2018/4/10.
 */
public class HasPermissionTag extends BodyTagSupport {

    private String name;

    @Override
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //获取session中存放的权限
        List<String> list = SessionLocal.getPrincipal().getAuthorities();
        //判断是否有权限访问
        if (list.contains(name)) {
            //允许访问标签body
            return BodyTagSupport.EVAL_BODY_INCLUDE;
        } else {
            return BodyTagSupport.SKIP_BODY;
        }

    }

    @Override
    public int doEndTag() throws JspException {
        return BodyTagSupport.EVAL_BODY_INCLUDE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
