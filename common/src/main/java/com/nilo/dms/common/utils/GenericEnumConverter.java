package com.nilo.dms.common.utils;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.reflect.MethodUtils;

/**
 * GenericEnumConverter
 *
 * @author spance
 * @version 2017/6/29
 */
public class GenericEnumConverter implements Converter {

    @Override
    @SuppressWarnings("all")
    public <T> T convert(Class<T> aClass, Object o) {
        try {
            Object name = MethodUtils.invokeMethod(o, "name", null);
            return (T) MethodUtils.invokeStaticMethod(aClass, "valueOf", name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}