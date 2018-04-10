package com.nilo.dms.dto.util;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {
    String name() default "";

    int order() default 0;

    boolean subType() default false;

}
