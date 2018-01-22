package com.nilo.dms.dao.interceptor;

import com.nilo.dms.common.Principal;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;

import java.util.Properties;

/**
 * Created by admin on 2017/12/29.
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})})
public class NetworkPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation
                .getArgs()[0];
        String sqlId = mappedStatement.getId();

        if (sqlId.indexOf("com.nilo.dms.dao.DeliveryOrderDao") != -1) {
            BoundSql boundSql = mappedStatement.getBoundSql(null);
            Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
            PropertyUtils.setProperty(boundSql,"sql","");
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
