/*
 * Copyright 2005-2013 xxcb.cn. All rights reserved.
* Support: http://www.xxcb.cn

 */
package com.nilo.dms.web.interceptor;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.common.utils.WebUtil;
import com.nilo.dms.dto.common.Log;
import com.nilo.dms.dto.system.LogConfig;
import com.nilo.dms.service.LogService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.Map.Entry;

public class LogInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    /**
     * 忽略参数表
     */
    private Set<String> ignoredParametersTable;

    private String[] ignoredParameters = {"password", "passwd"};

    @Resource
    private LogService logService;

    private List<LogConfig> configList;

    public LogInterceptor() {
        ignoredParametersTable = new HashSet<>();
        setupIgnoredParameters();
        ignoredParameters = null;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String path = request.getRequestURI(); // request.getRequestURI();
        for (LogConfig logConfig : configList) {
            if (StringUtil.equalsIgnoreCase(logConfig.getUrl(), path)) {
                Log sysLogDO = extractLog(logConfig, request);
                StringBuilder parameter = new StringBuilder();
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (parameterMap != null) {
                    for (Entry<String, String[]> entry : parameterMap.entrySet()) {
                        String parameterName = entry.getKey();
                        if (!ignoredParametersTable.contains(parameterName)) {
                            String[] parameterValues = entry.getValue();
                            if (parameterValues != null) {
                                for (String parameterValue : parameterValues) {
                                    parameter.append(parameterName)
                                            .append(" = ")
                                            .append(parameterValue)
                                            .append('\n');
                                }
                            } else {
                                parameter.append(parameterName).append(" = <NULL>\n");
                            }
                        }
                    }
                }
                if (ex != null) {
                    String exception = StringUtils.abbreviate(
                            ex.getClass() + ": " + ex.getMessage(), 1024);
                    sysLogDO.setException(exception);
                }
                sysLogDO.setParameter(parameter.toString());
                logService.saveLog(sysLogDO);
                return;
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        configList = SystemConfig.getLogConfig();
        if (configList == null) {
            configList = Collections.emptyList();
        }
        setupIgnoredParameters();
    }


    private void setupIgnoredParameters() {
        if (ignoredParameters != null) {
            Collections.addAll(ignoredParametersTable, ignoredParameters);
        }
    }

    private Log extractLog(LogConfig logConfig, HttpServletRequest request) {
        Log log = new Log();


        Principal principal = SessionLocal.getPrincipal();
        String userName = principal.getUserName();
        String merchantId = principal.getMerchantId();

        log.setMerchantId(merchantId);
        log.setOperator(userName);
        log.setOperation(logConfig.getOperation());

        String content = (String) request.getAttribute(log.LOG_CONTENT_ATTRIBUTE_NAME);
        log.setContent(content);
        request.removeAttribute(log.LOG_CONTENT_ATTRIBUTE_NAME);

        String ip = WebUtil.getClientAddress(request);
        log.setIp(ip);

        return log;
    }
}