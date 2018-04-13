package com.nilo.dms.common.utils;

import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.ErrorCode;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 参数检查断言工具类，可以减少一些<code>if</code>代码逻辑<br>
 * 当断言不成立时，抛出指定错误代码的异常 LdPayBaseException
 *
 * @author songze
 * @version $Id: AssertUtil.java, v 0.1 2015年7月28日 下午2:06:49 songze Exp $
 */
public final class AssertUtil {

    /**
     * 禁用构造函数
     */
    private AssertUtil() {
        // 禁用构造函数
    }

    /**
     * 期待字符串str1，str2相等
     *
     * @param str1
     * @param str2
     * @param resutlCode
     */
    public static void isNotEquals(String str1, String str2,
                                   ErrorCode resutlCode) throws DMSException {
        if (StringUtil.equals(str1, str2)) {
            throw new DMSException(resutlCode);
        }
    }

    /**
     * 期待字符串str1，str2不相等
     *
     * @param str1
     * @param str2
     * @param resutlCode
     */
    public static void isEquals(String str1, String str2,
                                ErrorCode resutlCode) throws DMSException {
        if (!StringUtil.equals(str1, str2)) {
            throw new DMSException(resutlCode);
        }
    }

    /**
     * 是否为正确的长度，不是指定长度抛异常
     *
     * @param
     */
    public static void isRightSize(String obj, int size, ErrorCode errCode) throws DMSException {
        if (!StringUtil.isEmpty(obj) && obj.length() != size) {
            throw new DMSException(errCode);
        }
    }

    /**
     * 是否大于指定长度,不大于抛异常
     *
     * @param str
     * @param size
     * @param errCode
     * @throws DMSException
     */
    public static void isGreaterSize(String str, int size, ErrorCode errCode) throws DMSException {
        if (!StringUtil.isEmpty(str) && str.length() < size) {
            throw new DMSException(errCode);
        }
    }

    /**
     * 是否小于指定长度，不小于抛异常
     *
     * @param str
     * @param size
     * @param errCode
     * @throws DMSException
     */
    public static void isLessSize(String str, int size, ErrorCode errCode) throws DMSException {
        if (!StringUtil.isEmpty(str) && str.length() > size) {
            throw new DMSException(errCode);
        }
    }


    /**
     * 都为空抛出异常
     *
     * @param errCode
     * @param objs
     * @throws DMSException
     */
    public static void isBothNull(ErrorCode errCode, Object... objs) throws DMSException {
        int num = 0;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] == null || objs[i].equals("")) {
                num++;
            }
        }
        if (num == objs.length) {
            throw new DMSException(errCode);

        }
    }


    /**
     * 期待对象为非空，如果检查的对象为<code>null</code>，抛出异常<code>LdPayBaseException</code>
     *
     * @param object
     * @param resutlCode
     * @throws DMSException
     */
    public static void isNotNull(Object object, ErrorCode resutlCode) throws DMSException {
        if (object == null) {
            throw new DMSException(resutlCode);
        }
    }

    /**
     * 期待对象是数字类型
     *
     * @param text
     * @param resutlCode
     * @throws DMSException
     */
    public static void isNotNumeric(String text, ErrorCode resutlCode) throws DMSException {
        if (!StringUtil.isNumeric(text)) {
            throw new DMSException(resutlCode);
        }
    }


    /**
     * 期待字符串为非空，如果检查字符串是空白：<code>null</code>、空字符串""或只有空白字符，抛出异常<code>LdPayBaseException</code>
     *
     * @param text       待检查的字符串
     * @param resutlCode 异常代码
     * @throws DMSException
     */
    public static void isNotBlank(String text, ErrorCode resutlCode) throws DMSException {
        if (StringUtil.isBlank(text)) {
            throw new DMSException(resutlCode);
        }
    }

    /**
     * 期待集合对象为非空，如果检查集合对象是否为null或者空数据，抛出异常<code>LdPayBaseException</code>
     *
     * @param collection 集合对象
     * @param resutlCode 异常代码
     * @throws DMSException
     */
    public static void notEmpty(Collection<?> collection,
                                ErrorCode resutlCode) throws DMSException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new DMSException(resutlCode);
        }
    }

    /**
     * 期待的正确值为true，如果实际为false，抛出异常<code>LdPayBaseException</code>
     *
     * @param expression
     * @param resutlCode 异常代码
     * @throws DMSException
     */
    public static void isTrue(boolean expression, ErrorCode resutlCode) throws DMSException {
        if (!expression) {
            throw new DMSException(resutlCode);
        }
    }

    /**
     * 期待的正确值为false，如果实际为true，抛出异常<code>LdPayBaseException</code>
     *
     * @param expression
     * @param resutlCode 异常代码
     * @throws DMSException
     */
    public static void isFalse(boolean expression, ErrorCode resutlCode) throws DMSException {
        if (expression) {
            throw new DMSException(resutlCode);
        }
    }
}
