package com.nilo.dms.common.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class TBean {

    @SuppressWarnings("all")
    public static <T> T copy(T obj) {
        try {
            return (T) BeanUtils.cloneBean(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    public static <T> T copy(Class<T> toClass, Object from) {
        try {
            T obj = toClass.newInstance();
            BeanUtils.copyProperties(obj, from);
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("all")
    public static void copy(Object to, Object from) {
        try {
            BeanUtils.copyProperties(to, from);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    public static <T> List<T> copyList(Class<T> dest, List from) {
        List<T> list = new ArrayList<>();
        try {
            for (Object item : from) {
                T obj = dest.newInstance();
                list.add(obj);
                BeanUtils.copyProperties(obj, item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
