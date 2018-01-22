package com.nilo.dms.service.order.third;

/**
 * Created by admin on 2017/11/18.
 */

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageField {

    boolean required() default true;

}
