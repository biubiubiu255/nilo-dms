/*
 * Kilimall Inc.
 * Copyright (c) 2016. All Rights Reserved.
 */
package com.nilo.dms.common.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utils - Web
 *
 * @author deng lei
 * @version 1.0
 */
public final class WebUtil {

    private static final String COOKIE_PATH = "/";

    private static final String COOKIE_DOMAIN = "";

    public static final String SESSION_USER_KEY = "USER";

    public static final String SESSION_RLAC_KEY = "USER_RLAC";

    /**
     * 不可实例化
     */
    private WebUtil() {
    }

    /**
     * 添加cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     cookie名称
     * @param value    cookie值
     * @param maxAge   有效期(单位: 秒)
     * @param path     路径
     * @param domain   域
     * @param secure   是否启用加密
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer maxAge, String path, String domain, Boolean secure) {
        Assert.notNull(request);
        Assert.notNull(response);
        Assert.hasText(name);
        try {
            name = URLEncoder.encode(name, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, value);
            if (maxAge != null) {
                cookie.setMaxAge(maxAge);
            }
            if (StringUtils.isNotEmpty(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            if (secure != null) {
                cookie.setSecure(secure);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     cookie名称
     * @param value    cookie值
     * @param maxAge   有效期(单位: 秒)
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer maxAge) {
        addCookie(request, response, name, value, maxAge, COOKIE_PATH, COOKIE_DOMAIN, null);
    }

    /**
     * 添加cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     cookie名称
     * @param value    cookie值
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        addCookie(request, response, name, value, null, COOKIE_PATH, COOKIE_DOMAIN, null);
    }

    /**
     * 获取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie名称
     * @return 若不存在则返回null
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request);
        Assert.hasText(name);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                }
            } catch (UnsupportedEncodingException ignored) {
            }
        }
        return null;
    }

    /**
     * 移除cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     cookie名称
     * @param path     路径
     * @param domain   域
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String path, String domain) {
        Assert.notNull(request);
        Assert.notNull(response);
        Assert.hasText(name);
        try {
            name = URLEncoder.encode(name, "UTF-8");
            Cookie cookie = new Cookie(name, null);
            cookie.setMaxAge(0);
            if (StringUtils.isNotEmpty(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     cookie名称
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        removeCookie(request, response, name, COOKIE_PATH, COOKIE_DOMAIN);
    }

    /**
     * 在web线程中调用该方法，获取HttpSession
     *
     * @param required 当没有session时是否创建
     * @return
     */
    public static HttpSession getHttpSession(boolean required) {
        ServletRequestAttributes sr = getServletRequestAttributes();
        return sr.getRequest().getSession(required);
    }

    /**
     * 在web线程中调用该方法，获取HttpSession
     *
     * @param key
     */
    public static Object getHttpSessionValue(String key) {
        return RequestContextHolder.currentRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 在web线程中调用该方法，设置HttpSession
     *
     * @param key
     * @param value
     */
    public static void setHttpSessionValue(String key, Serializable value) {
        RequestContextHolder.currentRequestAttributes().setAttribute(key, value, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取客户端ip地址
     *
     * @return
     */
    public static String getClientAddress() {
        return getClientAddress(getServletRequestAttributes().getRequest());
    }

    /**
     * 获取客户端ip地址
     *
     * @param request
     * @return
     */
    public static String getClientAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return StringUtils.substringBefore(ip, ",");
        }
        return ip;
    }

    /**
     * 直接获取 ServletRequestAttributes
     *
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 直接获取contextPath
     *
     * @return
     */
    public static String getContextPath() {
        HttpServletRequest req = getServletRequestAttributes().getRequest();
        return req != null ? req.getContextPath() : null;
    }


    /**
     * 设置 no-cache
     *
     * @param response
     */
    public static void disableHttpCache(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
    }

    /**
     * 设置content-type
     *
     * @param response
     * @param contentType
     */
    public static void setContentType(HttpServletResponse response, String contentType) {
        response.setHeader("Content-Type", contentType);
    }

    public enum Browser {
        IE, FIREFOX, CHROME, SAFARI, OPERA, MOBILE, UNKNOWN
    }


    public static Browser getUserBrowser(HttpServletRequest req) {
        String ua = req.getHeader("user-agent");
        ua = ua == null ? "msie" : ua.toLowerCase();
        Browser ub = null;
        if (ua.contains("msie")) {
            ub = Browser.IE;
        } else if (ua.contains("firefox")) {
            ub = Browser.FIREFOX;
        } else if (ua.contains("chrome")) {
            ub = Browser.CHROME;
        } else if (ua.contains("safari")) {
            ub = Browser.SAFARI;
        } else if (ua.contains("opera")) {
            ub = Browser.OPERA;
        } else {
            ub = Browser.UNKNOWN;
        }
        return ub;
    }

    /**
     * 设置下载头
     *
     * @param request
     * @param response
     */
    public static void setContentDisposition(HttpServletRequest request, HttpServletResponse response) {
        setContentDisposition(request, response, RandomStringUtils.randomAlphanumeric(8));
    }

    /**
     * 设置下载头
     *
     * @param request
     * @param response
     */
    public static void setContentDisposition(HttpServletRequest request, HttpServletResponse response, String fileName) {
        Browser ub = getUserBrowser(request);
        String headerValue = null;
        try {
            switch (ub) {
                case FIREFOX:
                case OPERA:
                    headerValue = String.format("attachment; filename*=\"UTF-8''%s\"", URLEncoder.encode(fileName, "UTF-8"));
                    break;
                case CHROME:
                case SAFARI:
                    headerValue = String.format("attachment; filename=\"%s\"", new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
                    break;
                default:
                    headerValue = String.format("attachment; filename=\"%s\"", URLEncoder.encode(fileName, "UTF-8"));
                    break;
            }
        } catch (UnsupportedEncodingException ignored) {
            fileName = RandomStringUtils.randomAlphanumeric(8);
            headerValue = String.format("attachment; filename=\"%s\"", fileName);
        }
        response.setHeader("Content-Disposition", headerValue);
    }

    /**
     * 获取当前已登陆的用户
     *
     * @return
     */
    public static Object getCurrentUser() {
        return getHttpSessionValue(SESSION_USER_KEY);
    }
}