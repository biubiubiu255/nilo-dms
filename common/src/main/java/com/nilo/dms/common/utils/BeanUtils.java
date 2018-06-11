package com.nilo.dms.common.utils;

import com.nilo.dms.common.enums.EnumMessage;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by admin on 2017/11/18.
 */
public class BeanUtils {
    public BeanUtils() {
    }

    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass, boolean must) {
        Annotation anno = clazz.getAnnotation(annotationClass);
        if (must && anno == null) {
            throw new IllegalArgumentException("missing " + annotationClass + " on class " + clazz);
        } else {
            return (T) anno;
        }
    }


    public static String getProperty(Object obj, String field) {

        Object value = null;
        try {
            value = PropertyUtils.getSimpleProperty(obj, field);
        } catch (Exception e) {
        }
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

    public static <T> T readField(Object owner, String name, Class<T> type) {
        Class clz = owner.getClass();
        Field field = null;

        try {
            field = clz.getField(name);
        } catch (NoSuchFieldException var8) {
            ;
        }

        while (field == null && clz != Object.class) {
            try {
                field = clz.getDeclaredField(name);
            } catch (NoSuchFieldException var7) {
                clz = clz.getSuperclass();
            }
        }

        if (field == null) {
            throw new IllegalArgumentException("No such field \'" + name + "\' in the object " + owner);
        } else {
            try {
                field.setAccessible(true);
                return type.cast(field.get(owner));
            } catch (IllegalAccessException var6) {
                throw new RuntimeException(var6);
            }
        }
    }

    public static void setNotNullValue(Object oriVal, Object valueA, Object valueB){
        oriVal = valueA==null ? valueB : valueA;
    }

    public static Object getNotNullValue(Object... vals){
        for (int i=0;i<vals.length;i++){
            if (vals[i]!=null){
                return vals[i];
            }
        }
        return null;
    }
}
