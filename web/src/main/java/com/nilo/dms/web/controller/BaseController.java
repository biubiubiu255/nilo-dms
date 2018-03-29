/*
 * Copyright 2015-2016 kilimall.com All rights reserved.
* Support: http://www.kilimall.co.ke

 */
package com.nilo.dms.web.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.nilo.dms.common.enums.EnumMessage;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.service.org.model.Staff;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.utils.WebUtil;
import com.nilo.dms.dao.UserInfoDao;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.service.model.UserInfo;

import nl.bitwalker.useragentutils.UserAgent;

/**
 * Controller - 基类
 *
 * @author deng lei
 * @version 1.0
 */
public class BaseController {


    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StaffDao staffDao;

    protected boolean isMobile(HttpServletRequest request) {
        String userAgentStr = request.getHeader("user-agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgentStr);
        return ua.getOperatingSystem().isMobileDevice();

    }

    protected ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected Object getSessionAttr(String name) {
        return getRequest().getSession().getAttribute(name);
    }

    protected void setSessionAttr(String name, Object obj) {
        getRequest().getSession().setAttribute(name, obj);
    }

    protected String toJsonErrorMsg(String msg) {
        Map<Object, Object> map = new HashMap<>();
        map.put("result", "fail");
        map.put("msg", msg);
        return JSON.toJSONString(map);
    }

    protected String toJsonTrueMsg() {
        Map<Object, Object> map = new HashMap<>();
        map.put("result", "succ");
        return JSON.toJSONString(map);
    }

    protected String toJsonTrueData(Object obj) {
        Map<Object, Object> map = new HashMap<>();
        map.put("result", "succ");
        map.put("response", obj);
        return JSON.toJSONString(map);
    }

    protected Pagination getPage() {
        return getPage(10);
    }

    protected String toPaginationData(Pagination pagination, Object data) {
        JSONObject jo = new JSONObject();
        int pIndex = 1 + (pagination.getOffset() / pagination.getLimit());
        // total records count
        jo.put("recordsTotal", pagination.getTotalCount());
        jo.put("recordsFiltered", pagination.getTotalCount());
        //jo.put("recordsFiltered", pagination.getLimit());
        // page index
        HttpServletRequest req = WebUtil.getServletRequestAttributes().getRequest();
        String draw = req.getParameter("draw");
        jo.put("draw", draw == null ? pIndex : draw);
        jo.put("data", data);
        return jo.toJSONString();
    }

    protected String toPaginationLayUIData(Pagination pagination, Object data) {
        JSONObject jo = new JSONObject();
        jo.put("count", pagination.getTotalCount());
        jo.put("code", 0);
        jo.put("msg", 0);
        jo.put("data", data);
        jo.put("pages", pagination.getTotalCount() / pagination.getLimit());
        return jo.toJSONString();
    }

    protected Pagination getPage(int size) {
        String _offset, _limit, _page;
        int offset = 0, limit = 10;
        HttpServletRequest req = getRequest();
        if ((_limit = req.getParameter("_size")) != null || (_limit = req.getParameter("length")) != null
                || (_limit = req.getParameter("limit")) != null) {
            limit = NumberUtils.toInt(_limit, size);
        }
        if ((_offset = req.getParameter("_index")) != null || (_offset = req.getParameter("start")) != null
                || (_offset = req.getParameter("offset")) != null) {
            offset = NumberUtils.toInt(_offset);
        }
        if ((_page = req.getParameter("page")) != null) {
            int page = NumberUtils.toInt(_page);
            offset = limit * (page - 1);
        }
        return new Pagination(offset, limit);
    }

    protected List<StaffDO> getRiderList(String companyId) {
        List<StaffDO> riderList = staffDao.queryAllRider(Long.parseLong(companyId));
        return riderList;
    }

    protected void setProperty(Field field, String value, Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (field.getType() == String.class) {
            PropertyUtils.setProperty(obj, field.getName(), value);
        }
        if (field.getType() == Long.class) {
            PropertyUtils.setProperty(obj, field.getName(), Long.parseLong(value));
        }
        if (field.getType() == Double.class) {
            PropertyUtils.setProperty(obj, field.getName(), Double.parseDouble(value));
        }
        if (field.getType() == Integer.class) {
            PropertyUtils.setProperty(obj, field.getName(), Integer.parseInt(value));
        }
    }

    protected String getProperty(Object obj, String field) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Object value = PropertyUtils.getSimpleProperty(obj, field);
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return "" + (Long) value;
        }
        if (value instanceof Double) {
            return "" + (Double) value;
        }
        if (value instanceof EnumMessage) {
            EnumMessage e = (EnumMessage) value;
            return e.getDesc();
        }

        return (String) value;
    }
}