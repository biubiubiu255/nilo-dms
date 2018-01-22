package com.nilo.dms.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
}
